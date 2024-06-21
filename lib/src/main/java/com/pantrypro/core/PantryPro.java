package com.pantrypro.core;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.oaigptconnector.model.*;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.generation.OpenAIGPTModels;
import com.oaigptconnector.model.request.chat.completion.OAIChatCompletionRequestMessage;
import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;
import com.pantrypro.Constants;
import com.pantrypro.core.generation.IdeaRecipeExpandIngredients;
import com.pantrypro.core.generation.tagging.TagFetcher;
import com.pantrypro.database.calculators.RecipeRemainingCalculator;
import com.pantrypro.database.compoundobjects.IngredientAndCategory;
import com.pantrypro.database.compoundobjects.RecipeWithIngredients;
import com.pantrypro.database.dao.factory.RecipeFactoryDAO;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.database.objects.recipe.RecipeTag;
import com.pantrypro.exceptions.CapReachedException;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.networking.client.oaifunctioncall.categorizeingredients.CategorizeIngredientsFC;
import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaFC;
import com.pantrypro.networking.client.oaifunctioncall.finalizerecipe.FinalizeRecipeFC;
import com.pantrypro.networking.client.oaifunctioncall.generatedirections.GenerateMeasuredIngredientsAndDirectionsFC;
import com.pantrypro.networking.client.oaifunctioncall.tagrecipe.TagRecipeFC;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class PantryPro {

    // Create HttpClient
    private static final HttpClient httpClient = HttpClient.newBuilder().version(HttpClient.Version.HTTP_2).connectTimeout(Duration.ofMinutes(com.oaigptconnector.Constants.AI_TIMEOUT_MINUTES)).build();

    public static List<IngredientAndCategory> categorizeIngredients(List<String> ingredients, String store) throws OAISerializerException, OpenAIGPTException, IOException, InterruptedException, OAIDeserializerException {
        // Create input from ingredients
        String input = parseCategorizeIngredientsGPTInput(ingredients, store);

        // Create user message
        OAIChatCompletionRequestMessage userMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
                .addText(input)
                .build();

//        // Create messages list from input
//        List<OAIChatCompletionRequestMessage> messages = new OAIChatCompletionRequestMessagesBuilder()
//                .addUser(input)
//                .build();

        // Generate fcResponse from CategorizeIngredientsFC
        OAIGPTChatCompletionResponse fcResponse = FCClient.serializedChatCompletion(
                CategorizeIngredientsFC.class,
                Constants.DEFAULT_MODEL_NAME,
                Constants.Response_Token_Limit_Categorize_Ingredients,
                Constants.DEFAULT_TEMPERATURE,
                Keys.openAiAPI,
                httpClient,
                userMessage
        );

        // Deserialize fcResponse to CategorizeIngredientsFC
        CategorizeIngredientsFC categorizeIngredientsFC = OAIFunctionCallDeserializer.deserialize(fcResponse.getChoices()[0].getMessage().getTool_calls().get(0).getFunction().getArguments(), CategorizeIngredientsFC.class);

        // Adapt to IngredientAndCategory list and return
        List<IngredientAndCategory> ingredientsAndCategories = new ArrayList<>();
        for (CategorizeIngredientsFC.IngredientsWithCategories fcIngredientsWithCategories: categorizeIngredientsFC.getIngredientsWithCategories()) {
            ingredientsAndCategories.add(new IngredientAndCategory(
                    fcIngredientsWithCategories.getIngredient(),
                    fcIngredientsWithCategories.getCategory()
            ));
        }

        return ingredientsAndCategories;
    }

//    public static BodyResponse generatePackSaveCategorizeIngredientsFunction(CategorizeIngredientsRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
//        // Get userID from PPUserAuthenticator
//        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());
//
//        // Create input from ingredients
//        String input = parseCategorizeIngredientsGPTInput(request.getIngredients(), request.getStore());
//
//        // Generate categorize ingredients function call response
//        OAIGPTFunctionCallResponseCategorizeIngredients categorizeIngredientsFunctionCallResponse = OAICategorizeIngredientsGenerator.generateCategorizeIngredientsFunctionCall(
//                input,
//                Constants.Context_Character_Limit_Categorize_Ingredients,
//                Constants.Response_Token_Limit_Categorize_Ingredients
//        );
//
//        // Adapt to CategorizeIngredientsResponse
//        CategorizeIngredientsResponse ciResponse = CategorizeIngredientsResponseFactory.from(categorizeIngredientsFunctionCallResponse);
//
//        // Return in success body response
//        return BodyResponseFactory.createSuccessBodyResponse(ciResponse);
//    }

    public static Long countTodaysRecipes(String authToken) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get userID from authToken with UserAuthenticator
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        // Return count of today's recipes for user
        return RecipeDAOPooled.countWithInstructionsForToday(userID);
    }

    /* Recipe Creation */

    public static RecipeWithIngredients createSaveRecipeIdea(String authToken, String ingredientsString, String modifiersString, Integer expandIngredientsMagnitude) throws SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException, OAISerializerException, OAIDeserializerException, DBSerializerPrimaryKeyMissingException, DBSerializerException, AppStoreErrorResponseException {
        /* Validation */

        // Get userID from authToken using UserAuthenticator
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        // Get remaining Recipe Ideas for user for userID, and if not null (infinite) and less than or equal to 0 throw CapReachedException
        Long remainingIdeaRecipes = new RecipeRemainingCalculator().calculateRemaining(authToken);
        if (remainingIdeaRecipes != null && remainingIdeaRecipes <= 0)
            throw new CapReachedException("CreateRecipeIdea cap reached for user " + userID + "!");

        /* Generation */

        // Get IdeaRecipeExpandIngredients, an enum that stores the system message, function description (unused/deprecated), and Funtion Call class for the expand ingredients magnitude integer provided in the request
        IdeaRecipeExpandIngredients ideaRecipeExpandIngredients = IdeaRecipeExpandIngredients.from(expandIngredientsMagnitude);

        // Create userInput from ingredientsString and modifiersString
        String userInput = "Ingredients: " + ingredientsString + "\n" + "Modifiers: " + modifiersString;

        // Create system and user messages
        OAIChatCompletionRequestMessage systemMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.SYSTEM)
                .addText(ideaRecipeExpandIngredients.getSystemMessage())
                .build();

        OAIChatCompletionRequestMessage userMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
                .addText(userInput)
                .build();

//        // Create messages array from ingredientsString and modifiersString
//        List<OAIChatCompletionRequestMessage> messages = new OAIChatCompletionRequestMessagesBuilder()
//                .addSystem(ideaRecipeExpandIngredients.getSystemMessage())
//                .addUser(userInput)
//                .build();

        // Get fcResponse from FCClient serializedChatCompletion
        OAIGPTChatCompletionResponse fcResponse = FCClient.serializedChatCompletion(
                ideaRecipeExpandIngredients.getFcClass(),
                OpenAIGPTModels.GPT_4.getName(),
                Constants.Response_Token_Limit_Create_Recipe,
                Constants.DEFAULT_TEMPERATURE,
                Keys.openAiAPI,
                httpClient,
                systemMessage,
                userMessage
        );

        // Deserialize fcResponse to CreateRecipeIdeaFC
        CreateRecipeIdeaFC createRecipeIdeaFC = OAIFunctionCallDeserializer.deserialize(fcResponse.getChoices()[0].getMessage().getTool_calls().get(0).getFunction().getArguments(), ideaRecipeExpandIngredients.getFcClass());

        // Get and save Recipe and RecipeMeasuredIngredients in RecpieWithIngredients object using RecipeFactoryDAO and return
        RecipeWithIngredients recipeWithIngredients = RecipeFactoryDAO.createAndSaveRecipe(
                createRecipeIdeaFC,
                userID,
                userInput,
                createRecipeIdeaFC.getCuisineType(),
                expandIngredientsMagnitude
        );

        return recipeWithIngredients;
    }

    public static void finalizeSaveRecipe(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, OAISerializerException, OpenAIGPTException, IOException, OAIDeserializerException, DBSerializerPrimaryKeyMissingException {
        /* Generation */

        // Get Recipe
        Recipe recipe = RecipeDAOPooled.get(recipeID);

        // Get RecipeMeasuredIngredients
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeDAOPooled.getMeasuredIngredients(recipeID);

        // Create input from recipe and measuredIngredients
        String input = parseFinalizeRecipeGPTInput(recipe, recipeMeasuredIngredients);

        // Create user message
        OAIChatCompletionRequestMessage userMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
                .addText(input)
                .build();

//        // Create messages array from input
//        List<OAIChatCompletionRequestMessage> messages = new OAIChatCompletionRequestMessagesBuilder()
//                .addUser(input)
//                .build();

        // Get fcResponse from FCClient serializedChatCompletion
        OAIGPTChatCompletionResponse fcResponse = FCClient.serializedChatCompletion(
                FinalizeRecipeFC.class,
                OpenAIGPTModels.GPT_4.getName(),
                Constants.Response_Token_Limit_Finalize_Recipe,
                Constants.DEFAULT_TEMPERATURE,
                Keys.openAiAPI,
                httpClient,
                userMessage
        );

        // Deserialize fcResponse to FinalizeRecipeFC
        FinalizeRecipeFC finalizeRecipeFC = OAIFunctionCallDeserializer.deserialize(fcResponse.getChoices()[0].getMessage().getTool_calls().get(0).getFunction().getArguments(), FinalizeRecipeFC.class);

        // Update and save Recipe, RecipeMeasuredIngredients, and RecipeDirections in RecipeWithIngredientsAndDirections using RecipeFactoryDAO
        RecipeFactoryDAO.updateAndSaveRecipe(
                finalizeRecipeFC,
                recipeID
        );
    }

    /* Recipe Getters */

    public static Recipe getRecipe(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Return Recipe
        return RecipeDAOPooled.get(recipeID);
    }

    public static List<RecipeMeasuredIngredient> getRecipeMeasuredIngredients(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Return RecipeMeasuredIngredient list
        return RecipeDAOPooled.getMeasuredIngredients(recipeID);
    }

    public static List<RecipeInstruction> getRecipeDirections(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Get RecipeDirection list
        return RecipeDAOPooled.getDirections(recipeID);
    }

    /* Recipe Tagging */

    public static List<RecipeTag> tagReicpe(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, OAISerializerException, OpenAIGPTException, IOException, OAIDeserializerException, DBSerializerPrimaryKeyMissingException {
        // Get Recipe
        Recipe recipe = RecipeDAOPooled.get(recipeID);

        // Get RecipeMeasuredIngredients
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeDAOPooled.getMeasuredIngredients(recipeID);

        // Create input from recipe
        String input = parseTagRecipeInput(recipe, recipeMeasuredIngredients);

        // Create system message containing tag list
        OAIChatCompletionRequestMessage systemMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.SYSTEM)
                .addText("Tags:\n" + TagFetcher.getFetchedTagsLowercaseAsString())
                .build();

        // Create user message
        OAIChatCompletionRequestMessage userMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
                .addText(input)
                .build();

        // Create message array with systemMessage and userMessage
        List<OAIChatCompletionRequestMessage> messages = List.of(
                systemMessage,
                userMessage
        );

        // Get fcResponse from FCClient for TagRecipeFC
        OAIGPTChatCompletionResponse fcResponse = FCClient.serializedChatCompletion(
                TagRecipeFC.class,
                OpenAIGPTModels.GPT_4.getName(),
                Constants.Response_Token_Limit_Tag_Recipe,
                Constants.DEFAULT_TEMPERATURE,
                Keys.openAiAPI,
                httpClient,
                messages
        );

        // Deserialize fcResponse to TagRecipeFC
        TagRecipeFC tagRecipeFC = OAIFunctionCallDeserializer.deserialize(fcResponse.getChoices()[0].getMessage().getTool_calls().get(0).getFunction().getArguments(), TagRecipeFC.class);

        // Get and save RecipeTag list with RecipeFactoryDAO and return
        List<RecipeTag> recipeTags = RecipeFactoryDAO.createAndSaveRecipeTags(
                tagRecipeFC,
                recipeID
        );

        return recipeTags;
    }

    /*** TODO: Removed because this is the same as finalizeSaveRecipe
     * Regenerates Recipe Directions from..
     *  - Recipe Measured Ingredients
     *  - Recipe name
     *  - Recipe summary
     *
     *  Basically, this function takes a recipeID and gets the Recipe and RecipeMeasuredIngredients (presumably after updating values), regenerates the directions, and updates the recipe directions in the database.
     *  It's different than finalizeRecipe because it does not generate measuredIngredients, it instead uses them to create the directions. The current use case for this is to regenerate directions if the user changes the ingredients.
     *
     * @param recipeID The Recipe's ID
     */
    public static void regenerateMeasuredIngredientsAndDirections(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, OAISerializerException, OpenAIGPTException, IOException, OAIDeserializerException, DBSerializerPrimaryKeyMissingException {
        // Get Recipe
        Recipe recipe = RecipeDAOPooled.get(recipeID);

        // Get RecipeMeasuredIngredients list
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeDAOPooled.getMeasuredIngredients(recipeID);

        // Create input from recipe and recipeMeasuredIngredients
        String input = parseRegenerateDirectionsInput(recipe, recipeMeasuredIngredients);

        // Create user message
        OAIChatCompletionRequestMessage userMessage = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
                .addText(input)
                .build();

//        // Create messages array from input
//        List<OAIChatCompletionRequestMessage> messages = new OAIChatCompletionRequestMessagesBuilder()
//                .addUser(input)
//                .build();

        // Get fcResponse from FCClient serializedChatCompletion
        OAIGPTChatCompletionResponse fcResponse = FCClient.serializedChatCompletion(
                GenerateMeasuredIngredientsAndDirectionsFC.class,
                OpenAIGPTModels.GPT_4.getName(),
                Constants.Response_Token_Limit_Generate_Directions,
                Constants.DEFAULT_TEMPERATURE,
                Keys.openAiAPI,
                httpClient,
                userMessage
        );

        // Deserialize fcResponse to GenerateDirectionsFC
        GenerateMeasuredIngredientsAndDirectionsFC generateMeasuredIngredientsAndDirectionsFC = OAIFunctionCallDeserializer.deserialize(fcResponse.getChoices()[0].getMessage().getTool_calls().get(0).getFunction().getArguments(), GenerateMeasuredIngredientsAndDirectionsFC.class);

        // Update and save directions and feasibility with RecipeFactoryDAO
        RecipeFactoryDAO.updateAndSaveRecipe(generateMeasuredIngredientsAndDirectionsFC, recipeID);
    }

    public static void updateIngredientsAndMeasurements(Integer recipeID, List<String> measuredIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Adapt ingredientsAndMeasurements list to RecipeMeasuredIngredients list
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = new ArrayList<>();
        for(String measuredIngredient: measuredIngredients) {
            recipeMeasuredIngredients.add(new RecipeMeasuredIngredient(
                    null,
                    recipeID,
                    measuredIngredient
            ));
        }

        // Update ingredients
        RecipeDAOPooled.updateMeasuredIngredients(recipeID, recipeMeasuredIngredients);
    }

    public static void updateName(Integer recipeID, String name) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, IllegalAccessException {
        // TODO: I should do some error handling around here and stuff lol
        RecipeDAOPooled.updateName(recipeID, name);
    }

    public static void updateEstimatedServings(Integer recipeID, Integer estimatedServings) throws DBSerializerException, SQLException, InterruptedException {
        RecipeDAOPooled.updateEstimatedServings(recipeID, estimatedServings);
    }

    public static void updateSummary(Integer recipeID, String summary) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, IllegalAccessException {
        // TODO: I should do some error handling around here and stuff lol
        RecipeDAOPooled.updateSummary(recipeID, summary);
    }

    public static void validateUserRecipeAssociation(String authToken, Integer recipeID) throws InvalidAssociatedIdentifierException, DBSerializerException, SQLException, InterruptedException, DBObjectNotFoundFromQueryException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get userID
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        // Check if user is associated with recipe
        if(!RecipeDAOPooled.isUserAssociatedWithRecipe(userID, recipeID))
            throw new InvalidAssociatedIdentifierException("User not associated with Recipe!");
    }

    public static String parseCategorizeIngredientsGPTInput(List<String> ingredients, String store) {
        // If there is a store..
            // Ingredients: ingredient1, ingredient2, ingredient3,..., ingredient4\nFor the store: store
        // If there is no store..
            // ingredient1, ingredient2, ingredient3,..., ingredient4
        final String INGREDIENTS_STRING = "Ingredients:";
        final String STORE_STRING = "For the store:";
        final String COMMA_SEPARATOR_STRING = ", ";
        final String NEW_LINE_STRING = "\n";
        final String SPACE_STRING = " ";

        StringBuilder sb = new StringBuilder();

        // If store is not null or empty, add the INGREDIENTS_STRING and SPACE_STRING
        if (store != null && !store.equals(""))
            sb.append(INGREDIENTS_STRING + SPACE_STRING);

        // Append ingredients with COMMA_SEPARATOR_STRING
        for (int i = 0; i < ingredients.size(); i++) {
            String ingredient = ingredients.get(i);

            // Append ingredient
            sb.append(ingredient);

            // Append COMMA_SEPARATOR_STRING unless it's the last element
            if (i < ingredients.size() - 1) {
                sb.append(COMMA_SEPARATOR_STRING);
            }
        }

        // If store is not null or empty, add the NEW_LINE_STRING and STORE_STRING and SPACE_STRING and store
        if (store != null && !store.equals(""))
            sb.append(NEW_LINE_STRING + STORE_STRING + SPACE_STRING + store);

        return sb.toString();
    }

    private static String parseFinalizeRecipeGPTInput(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients) {
        // Make Peach Cobbler with base ingredients peaches flour eggs with the desciption \"A sweet and tangy dessert perfect for summer\" expanding ingredients if necessary
        final String makeString = "Make";
        final String withTheBaseIngredientsString = "with the base ingredients";
        final String withTheDescriptionString = "with the description";
        final String quoteString = "\"";
        final String spaceString = " ";

        StringBuilder sb = new StringBuilder();

        // Make a Peach Cobbler
        sb.append(makeString);
        sb.append(spaceString);
        sb.append(recipe.getName());

        // with the base ingredients a, b, c,..,n
        sb.append(withTheBaseIngredientsString);
        measuredIngredients.forEach(measuredIngredient -> {
            sb.append(spaceString);
            sb.append(measuredIngredient.getMeasuredIngredient());
        });

        // with the description "description"
        sb.append(spaceString);
        sb.append(withTheDescriptionString);
        sb.append(spaceString);
        sb.append(quoteString);
        sb.append(recipe.getSummary());
        sb.append(quoteString);

        // expanding ingredients text
        if (recipe.getExpandIngredientsMagnitude() != null) {
            sb.append(spaceString);
            sb.append(IdeaRecipeExpandIngredients.from(recipe.getExpandIngredientsMagnitude()).getSystemMessage());
        }

        return sb.toString();
    }

    private static String parseRegenerateDirectionsInput(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients) {
        // Make Peach Cobbler
        // Servings: 3
        // With ingredients: peaches flour eggs
        // With the description A sweet and tangy dessert perfect for summer
        final String makeString = "Make";
        final String emptyTitleRecipeString = "recipe";
        final String servingsString = "Servings";
        final String withIngredientsString = "With ingredients:";
        final String withTheDescriptionString = "With the description:";
        final String commaSpaceDelimiterString = ", ";
        final String newLineString = "\n";
        final String spaceString = " ";

        StringBuilder sb = new StringBuilder();

        // "Make Peach Cobbler" or if title is null or empty, "Make Recipe"
        sb.append(makeString);
        sb.append(spaceString);
        sb.append(recipe.getName().isEmpty() ? emptyTitleRecipeString : recipe.getName());

        // "Servings: 3"
        sb.append(newLineString);
        sb.append(servingsString);
        sb.append(spaceString);
        sb.append(recipe.getEstimatedServings());

        // "With ingredients: peach, flour, eggs"
        if (measuredIngredients.size() > 0) {
            sb.append(newLineString);

            sb.append(withIngredientsString);
            sb.append(spaceString);

            measuredIngredients.forEach(ingredient -> {
                sb.append(ingredient.getMeasuredIngredient());
                sb.append(commaSpaceDelimiterString);
            });

            // If the end of sb is commaSpaceDelimiterString, remove it
            if (sb.substring(sb.length() - commaSpaceDelimiterString.length(), sb.length()).equals(commaSpaceDelimiterString)) {
                sb.delete(sb.length() - commaSpaceDelimiterString.length(), sb.length());
            }
        }

        // "With the description: A sweet and tangy dessert perfect for summer"
        if (!recipe.getSummary().isEmpty()) {
            sb.append(newLineString);

            sb.append(withTheDescriptionString);
            sb.append(spaceString);

            sb.append(recipe.getSummary());
        }

        return sb.toString();
    }

    private static String parseTagRecipeInput(Recipe ideaRecipe, List<RecipeMeasuredIngredient> measuredIngredients) {
        // Generate tags for recipe named: RecipeName\nWith ingredients: ingredient, ingredient,..., ingredient
        final String generateTagsString = "Generate tags for recipe named:";
        final String withIngredientsString = "With ingredients:";
        final String spaceString = " ";
        final String newLineString = "\n";

        StringBuilder sb = new StringBuilder();

        // Generate tags for recipe named: RecipeName
        sb.append(generateTagsString);
        sb.append(spaceString);
        sb.append(ideaRecipe.getName());

        sb.append(newLineString);

        // With ingredients: ingredient, ingredient,..., ingredient
        sb.append(withIngredientsString);
        measuredIngredients.forEach(measuredIngredient -> {
            sb.append(spaceString);
            sb.append(measuredIngredient.getMeasuredIngredient());
        });

        System.out.println(sb.toString());

        return sb.toString();
    }

}

package com.pantrypro.core;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.Constants;
import com.pantrypro.core.database.adapters.*;
import com.pantrypro.core.generation.openai.*;
import com.pantrypro.model.exceptions.CapReachedException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import com.pantrypro.core.database.managers.RecipeDBManager;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.core.generation.calculators.CreateRecipeIdeaRemainingCalculator;
import com.pantrypro.core.generation.tagging.TagFilterer;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.core.service.PPPremiumValidator;
import com.pantrypro.core.service.adapters.ResponseFromDBObjectAdapter;
import com.pantrypro.core.service.adapters.ResponseFromOpenAIResponseAdapter;
import com.pantrypro.model.database.objects.*;
import com.pantrypro.model.exceptions.GenerationException;
import com.pantrypro.model.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.model.exceptions.InvalidRequestJSONException;
import com.pantrypro.model.generation.IdeaRecipeExpandIngredients;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.*;
import com.pantrypro.model.http.client.apple.itunes.exception.AppStoreStatusResponseException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.request.*;
import com.pantrypro.model.http.server.response.*;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class PPGPTGenerator {

    public static BodyResponse generatePackSaveCategorizeIngredientsFunction(CategorizeIngredientsRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        // Create input from ingredients
        String input = parseCategorizeIngredientsGPTInput(request.getIngredients(), request.getStore());

        // Generate categorize ingredients function call response
        OAIGPTFunctionCallResponseCategorizeIngredients categorizeIngredientsFunctionCallResponse = CategorizeIngredientsGenerator.generateCategorizeIngredientsFunctionCall(
                input,
                Constants.Context_Character_Limit_Categorize_Ingredients,
                Constants.Response_Token_Limit_Categorize_Ingredients
        );

        // Adapt to CategorizeIngredientsResponse
        CategorizeIngredientsResponse ciResponse = ResponseFromOpenAIResponseAdapter.from(categorizeIngredientsFunctionCallResponse);

        // Return in success body response
        return BodyResponseFactory.createSuccessBodyResponse(ciResponse);
    }

    public static BodyResponse generatePackSaveCreateRecipeIdeaFunction(CreateRecipeIdeaRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, DBSerializerPrimaryKeyMissingException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException {
        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        // Get isPremium
        boolean isPremium = PPPremiumValidator.getIsPremium(u_aT.getUserID());

        // Get remaining
        Long remaining = new CreateRecipeIdeaRemainingCalculator().calculateRemaining(u_aT.getUserID(), isPremium);

        // If remaining is not null (infinite) and less than 0, throw CapReachedException
        if (remaining != null && remaining <= 0) throw new CapReachedException("Cap reached for user when generating recipe ideas");

        // Create input atomic reference to get the input from CreateRecipeIdeaGenerator for creating IdeaRecipe object
        AtomicReference<String> input = new AtomicReference<>();

        // Generate if validated
        OAIGPTFunctionCallResponseCreateRecipeIdea createRecipeIdeaFunctionCallResponse = CreateRecipeIdeaGenerator.generateCreateRecipeIdeaFunctionCall(request.getIngredients(), request.getModifiers(), request.getExpandIngredientsMagnitude(), Constants.Context_Character_Limit_Create_Recipe_Idea, Constants.Response_Token_Limit_Create_Recipe_Idea, input);

        // Adapt to IdeaRecipe, IdeaRecipeIngredients, and IdeaRecipeEquipment
        IdeaRecipe ideaRecipe = IdeaRecipeFromOpenAIAdapter.getIdeaRecipe(u_aT.getUserID(), input.get(), request.getExpandIngredientsMagnitude(), createRecipeIdeaFunctionCallResponse);
        List<IdeaRecipeIngredient> ideaRecipeIngredients = IdeaRecipeFromOpenAIAdapter.getIdeaRecipeIngredients(createRecipeIdeaFunctionCallResponse);
//        List<IdeaRecipeEquipment> ideaRecipeEquipment = IdeaRecipeFromOpenAIAdapter.getIdeaRecipeEquipment(createRecipeIdeaFunctionCallResponse);

        // Insert into database, creating object to use to parse to return
        IdeaRecipeDBManager.insertIdeaRecipe(ideaRecipe, ideaRecipeIngredients);

        // Adapt to CreateRecipeIdeaResponse and return in success body response
        CreateRecipeIdeaResponse ideaResponse = ResponseFromDBObjectAdapter.from(
                ideaRecipe,
                ideaRecipeIngredients,
//                ideaRecipeEquipment,
                remaining == null ? null : remaining - 1
        );

        return BodyResponseFactory.createSuccessBodyResponse(ideaResponse);

    }

    public static BodyResponse generatePackSaveRegenerateRecipeDirectionsAndIdeaRecipeIngredients(RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException, InvalidRequestJSONException, OpenAIGPTException, GenerationException {
        // Require ideaID and at least one of newName with text, newSummary with text, or measuredIngredients with at least one
        if (request.getIdeaID() == null || ((request.getNewName() == null || request.getNewName().isEmpty()) && (request.getNewSummary() == null || request.getNewSummary().isEmpty()) && (request.getMeasuredIngredients() == null || request.getMeasuredIngredients().size() == 0)))
            throw new InvalidRequestJSONException("Missing required object - Please ensure your JSON contains authToken, ideaID, and optionally with at least one required newTitle, newSummary, and/or measuredIngredients.");

        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        // Get isPremium
        boolean isPremium = PPPremiumValidator.getIsPremium(u_aT.getUserID());

        // Calculate remaining TODO:
//        Long remaining = new RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRemainingCalculator().calculateRemaining(u_aT.getUserID(), isPremium);

        // If remaining is not null (infinite) and less than 0, throw CapReachedException
//        if (remaining != null && remaining <= 0) throw new CapReachedException("Cap reached for user when regenerating recipe directions and idea recipe ingredients");

        // If newTitle is not null, update in DB
        if (request.getNewName() != null)
            IdeaRecipeDBManager.updateName(request.getIdeaID(), request.getNewName());

        // If newSummary is not null, update in DB
        if (request.getNewSummary() != null)
            IdeaRecipeDBManager.updateSummary(request.getIdeaID(), request.getNewSummary());

        // Get recipe ID
        final Integer recipeID = RecipeDBManager.getFromIdeaID(request.getIdeaID()).getId();

        // If recipe is null, throw GenerationException
        if (recipeID == null)
            throw new GenerationException("Could not find Recipe. Please make sure it's generated before trying to update!");

        // Create a variable for idea recipe ingredients set as null which will be assigned in the getMeasuredIngredients not null or empty conditional, so it can be added to the response if it is not null
        List<IdeaRecipeIngredient> ideaRecipeIngredients = null;

        // If measuredIngredients is not null, update in DB and get ingredients without measurements
        if (request.getMeasuredIngredients() != null && !request.getMeasuredIngredients().isEmpty()) {
            // Delete all measured ingredients for recipe
            RecipeDBManager.deleteAllMeasuredIngredients(recipeID);

            // Create recipe measured ingredients from request
            List<RecipeMeasuredIngredient> recipeMeasuredIngredients = new ArrayList<>();
            request.getMeasuredIngredients().forEach(ingredientString -> {
                recipeMeasuredIngredients.add(new RecipeMeasuredIngredient(recipeID, ingredientString));
            });

            // Insert all measured ingredients for recipe
            RecipeDBManager.insertRecipeMeasuredIngredients(recipeMeasuredIngredients);

            // Generate new idea recipe ingredients from get ingredients without measurements
            OAIGPTFunctionCallResponseParseIngredientNames parseIngredientNamesResponse = ParseIngredientNamesGenerator.getParsedIngredientNames(
                    request.getMeasuredIngredients(),
                    Constants.Context_Character_Limit_Get_Ingredients_Without_Measurements,
                    Constants.Response_Token_Limit_Get_Ingredients_Without_Measurements
            );

            // Get ideaRecipeIngredients with IdeaRecipeIngredientFromOpenAIAdapter
            ideaRecipeIngredients = IdeaRecipeIngredientFromOpenAIAdapter.getIdeaRecipeIngredients(request.getIdeaID(), parseIngredientNamesResponse);

            // Delete all ingredients from idea recipe
            IdeaRecipeDBManager.deleteAllIngredients(request.getIdeaID());

            // Insert all ingredients for idea recipe
            IdeaRecipeDBManager.insertIdeaRecipeIngredients(ideaRecipeIngredients);
        }

        // Get updated ideaRecipe and recipeMeasuredIngredients TODO: Optimize this!
        IdeaRecipe ideaRecipe = IdeaRecipeDBManager.get(request.getIdeaID());
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeDBManager.getMeasuredIngredients(recipeID);

        // Convert recipeMeasuredIngredients into a string array using their strings
        List<String> recipeMeasuredIngredientsStrings = new ArrayList<>();
        recipeMeasuredIngredients.forEach(measuredIngredient -> {
            recipeMeasuredIngredientsStrings.add(measuredIngredient.getString());
        });

        // Generate new directions
        OAIGPTFunctionCallResponseGenerateDirections generateDirectionsResponse = GenerateDirectionsGenerator.generateDirections(
                ideaRecipe.getName(),
                ideaRecipe.getSummary(),
                recipeMeasuredIngredientsStrings,
                Constants.Context_Character_Limit_Generate_Directions,
                Constants.Response_Token_Limit_Generate_Directions
        );

        // Get recipeInstructions from RecipeInstructionFromOpenAIAdapter TODO: Rename to recipeDiretions when I rename the DB object and everything else ree lol
        List<RecipeInstruction> recipeInstructions = RecipeInstructionFromOpenAIAdapter.getRecipeInstructions(recipeID, generateDirectionsResponse);

        // Delete all recipe directions
        RecipeDBManager.deleteAllInstructions(recipeID);

        // Insert generated directions
        RecipeDBManager.insertRecipeInstructions(recipeInstructions);

        // Get feasibility and update in DB
        Integer feasibility = generateDirectionsResponse.getFeasibility();

        // Adapt to RegenerateRecipeDirectionAndIdeaRecipeIngredientsResponse
        RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse response = ResponseFromDBObjectAdapter.from(recipeInstructions, ideaRecipeIngredients, feasibility);

        return BodyResponseFactory.createSuccessBodyResponse(response);
    }

    public static BodyResponse generatePackSaveMakeRecipe(MakeRecipeRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, DBSerializerPrimaryKeyMissingException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException, InvalidAssociatedIdentifierException {
        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        // Get isPremium
        boolean isPremium = PPPremiumValidator.getIsPremium(u_aT.getUserID());

        // Get remaining
//        Long remaining = MakeRecipeRemainingCalculator.calculateRemaining(u_aT.getUserID(), isPremium);

        // If remaining is not null (infinite) and less than 0, throw CapReachedException
//        if (remaining != null && remaining <= 0) throw new CapReachedException("Cap reached for user when generating recipe ideas");

        // Get idea and ingredients from ideaID
        IdeaRecipe idea = IdeaRecipeDBManager.get(request.getIdeaID());
        List<IdeaRecipeIngredient> ideaIngredients = IdeaRecipeDBManager.getIngredients(request.getIdeaID());

        // Throw exception if idea is null
        if (idea == null)
            throw new InvalidAssociatedIdentifierException("IdeaRecipe was null!");

        // Create input from ingredients and modifiers
        String input = parseMakeRecipeGPTInput(idea, ideaIngredients);

        // Generate if validated
        OAIGPTFunctionCallResponseMakeRecipe makeRecipeFunctionCallResponse = MakeRecipeGenerator.generateMakeRecipeFunctionCall(input, Constants.Context_Character_Limit_Make_Recipe, Constants.Response_Token_Limit_Make_Recipe);

        // Adapt to IdeaRecipe, IdeaRecipeIngredients, and IdeaRecipeEquipment
        Recipe recipe = RecipeFromOpenAIAdapter.getRecipe(request.getIdeaID(), makeRecipeFunctionCallResponse);
        List<RecipeInstruction> recipeInstructions = RecipeFromOpenAIAdapter.getRecipeInstructions(makeRecipeFunctionCallResponse);
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeFromOpenAIAdapter.getRecipeMeasuredIngredients(makeRecipeFunctionCallResponse);

        // Insert into database, creating object to use to parse to return
        RecipeDBManager.insertRecipe(recipe, recipeInstructions, recipeMeasuredIngredients);

        // Adapt to CreateRecipeIdeaResponse and return in success body response
        MakeRecipeResponse recipeResponse = ResponseFromDBObjectAdapter.from(
                recipe,
                recipeInstructions,
                recipeMeasuredIngredients
        );

        return BodyResponseFactory.createSuccessBodyResponse(recipeResponse);
    }

    public static BodyResponse generatePackIdeaRecipeTags(TagRecipeIdeaRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, InvalidAssociatedIdentifierException, OpenAIGPTException, IOException {
        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(request.getAuthToken());

        //Get idea and ingredients from ideaID
        IdeaRecipe idea = IdeaRecipeDBManager.get(request.getIdeaID());
        List<IdeaRecipeIngredient> ideaIngredients = IdeaRecipeDBManager.getIngredients(request.getIdeaID());

        // Throw exception if idea is null
        if (idea == null)
            throw new InvalidAssociatedIdentifierException("IdeaRecipe was null!");

        // Create input from ingredients and modifiers
        String input = parseTagRecipeIdeaGPTInput(idea, ideaIngredients);

        // Generate if validated
        OAIGPTFunctionCallResponseTagRecipeIdea tagRecipeIdeaFunctionCallResponse = TagRecipeIdeaGenerator.generateTagRecipeIdea(
                input,
                Constants.Context_Character_Limit_Tag_Recipe_Idea,
                Constants.Response_Token_Limit_Tag_Recipe_Idea
        );

        // Adapt to IdeaRecipeTag list
        List<IdeaRecipeTag> ideaRecipeTags = IdeaRecipeTagFromOpenAIAdapter.getIdeaRecipeTags(tagRecipeIdeaFunctionCallResponse, request.getIdeaID());

        // Remove invalid tags
        TagFilterer.removeInvalidTags(ideaRecipeTags);

        // Insert into database
        IdeaRecipeDBManager.insertIdeaRecipeTags(ideaRecipeTags);

        // Adapt to TagIdeaRecipeResponse and return in success body response
        TagRecipeIdeaResponse tagRecipeIdeaResponse = ResponseFromDBObjectAdapter.from(ideaRecipeTags);

        return BodyResponseFactory.createSuccessBodyResponse(tagRecipeIdeaResponse);
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

    private static String parseMakeRecipeGPTInput(IdeaRecipe ideaRecipe, List<IdeaRecipeIngredient> ingredients) {
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
        sb.append(ideaRecipe.getName());

        // with the base ingredients a, b, c,..,n
        sb.append(withTheBaseIngredientsString);
        ingredients.forEach(ingredient -> {
            sb.append(spaceString);
            sb.append(ingredient.getName());
        });

        // with the description "description"
        sb.append(spaceString);
        sb.append(withTheDescriptionString);
        sb.append(spaceString);
        sb.append(quoteString);
        sb.append(ideaRecipe.getSummary());
        sb.append(quoteString);

        // expanding ingredients text
        if (ideaRecipe.getExpandIngredientsMagnitude() != null) {
            sb.append(spaceString);
            sb.append(IdeaRecipeExpandIngredients.from(ideaRecipe.getExpandIngredientsMagnitude()).getSystemMessageString());
        }

        return sb.toString();
    }

    private static String parseTagRecipeIdeaGPTInput(IdeaRecipe ideaRecipe, List<IdeaRecipeIngredient> ingredients) {
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
        ingredients.forEach(ingredient -> {
            sb.append(spaceString);
            sb.append(ingredient.getName());
        });

        return sb.toString();
    }

}

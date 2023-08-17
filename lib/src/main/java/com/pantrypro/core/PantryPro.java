package com.pantrypro.core;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.dbclient.DBManager;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.Constants;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.database.adapters.oai.IdeaRecipeTagFromOpenAIAdapter;
import com.pantrypro.core.database.adapters.oai.RecipeFromOpenAIAdapter;
import com.pantrypro.core.generation.RecipeDirectionsGenerator;
import com.pantrypro.core.generation.openai.*;
import com.pantrypro.core.generation.IdeaRecipeGenerator;
import com.pantrypro.core.generation.IngredientNamesFromMeasuredIngredientsGenerator;
import com.pantrypro.core.service.responsefactories.CategorizeIngredientsResponseFactory;
import com.pantrypro.core.service.responsefactories.MakeRecipeResponseFactory;
import com.pantrypro.model.exceptions.CapReachedException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import com.pantrypro.core.database.managers.RecipeDBManager;
import com.pantrypro.core.generation.tagging.TagFilterer;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.model.database.objects.*;
import com.pantrypro.model.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.model.generation.IdeaRecipeExpandIngredients;
import com.pantrypro.model.http.client.openaigpt.response.functioncall.*;
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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PantryPro {

    public static BodyResponse generatePackSaveCategorizeIngredientsFunction(CategorizeIngredientsRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        // Get userID from PPUserAuthenticator
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());

        // Create input from ingredients
        String input = parseCategorizeIngredientsGPTInput(request.getIngredients(), request.getStore());

        // Generate categorize ingredients function call response
        OAIGPTFunctionCallResponseCategorizeIngredients categorizeIngredientsFunctionCallResponse = OAICategorizeIngredientsGenerator.generateCategorizeIngredientsFunctionCall(
                input,
                Constants.Context_Character_Limit_Categorize_Ingredients,
                Constants.Response_Token_Limit_Categorize_Ingredients
        );

        // Adapt to CategorizeIngredientsResponse
        CategorizeIngredientsResponse ciResponse = CategorizeIngredientsResponseFactory.from(categorizeIngredientsFunctionCallResponse);

        // Return in success body response
        return BodyResponseFactory.createSuccessBodyResponse(ciResponse);
    }

    /* Create idea recipe */

    public static IdeaRecipe createSaveIdeaRecipe(String authToken, String ingredientsString, String modifiersString, Integer expandIngredientsMagnitude) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, DBSerializerPrimaryKeyMissingException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException {
        // Generate idea recipe with IdeaRecipeGenerator, which validates the user's count as well
        IdeaRecipe ideaRecipe = IdeaRecipeGenerator.generateForUser(
                authToken,
                ingredientsString,
                modifiersString,
                expandIngredientsMagnitude
        );

        // Deep insert into DB
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            DBManager.deepInsert(conn, ideaRecipe);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }

        // Return IdeaRecipe
        return ideaRecipe;

//        List<IdeaRecipeIngredient> ideaRecipeIngredients = IdeaRecipeFromOpenAIAdapter.getIdeaRecipeIngredients(createRecipeIdeaFunctionCallResponse);
//        List<IdeaRecipeEquipment> ideaRecipeEquipment = IdeaRecipeFromOpenAIAdapter.getIdeaRecipeEquipment(createRecipeIdeaFunctionCallResponse);

        // Insert with PPPersistinator
//        PPPersistinator.insertIdeaRecipe(ideaRecipe, ideaRecipeIngredients);

        // Adapt to CreateRecipeIdeaResponse and return in success body response
//        CreateRecipeIdeaResponse ideaResponse = CreateRecipeIdeaResponseFactory.from(
//                ideaRecipe,
//                ideaRecipeIngredients,
//                ideaRecipeEquipment,
//                remaining == null ? null : remaining - 1
//        );

//        return BodyResponseFactory.createSuccessBodyResponse(ideaResponse);
    }

    /* Regenerate recipe directions and idea recipe ingredients */

    private static IdeaRecipe updateSaveIdeaRecipe(Integer ideaID, String newName, String newSummary, List<String> measuredIngredientStrings) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, IOException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        /* Update title, summary, and ingredients in DB */

        Connection tsmiUpdateConn = SQLConnectionPoolInstance.getConnection();
        try {
            // If newTitle is not null, update in DB
            if (newName != null)
                IdeaRecipeDBManager.updateName(tsmiUpdateConn, ideaID, newName);

            // If newSummary is not null, update in DB
            if (newSummary != null)
                IdeaRecipeDBManager.updateName(tsmiUpdateConn, ideaID, newSummary);

            // If measuredIngredients is not null, update in DB and get ingredients without measurements
            if (measuredIngredientStrings != null && !measuredIngredientStrings.isEmpty()) {
                makeSaveIdeaRecipeNames(ideaID, measuredIngredientStrings);
            }
        } finally {
            SQLConnectionPoolInstance.releaseConnection(tsmiUpdateConn);
        }
    }

    private static IdeaRecipe makeSaveIdeaRecipeNames(Integer ideaID, List<String> measuredIngredientStrings) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, DBSerializerPrimaryKeyMissingException, OpenAIGPTException, IOException {
        Connection tsmiUpdateConn = SQLConnectionPoolInstance.getConnection();
        try {
            // Get recipe ID from ideaID
            Recipe recipe = RecipeDBManager.getFromIdeaID(tsmiUpdateConn, ideaID);

//            // Update measured ingredients
//            RecipeDBManager.updateMeasuredIngredients(tsmiUpdateConn, recipe.getId(), );

            // Get generate ingredient names and assign to ingredientNames
            List<IdeaRecipeIngredient> ingredientNames = IngredientNamesFromMeasuredIngredientsGenerator.generateIngredientNames(measuredIngredientStrings);

            // Update idea recipe ingredients in DB
            IdeaRecipeDBManager.updateIngredients(tsmiUpdateConn, recipe.getIdea_id(), ingredientNames);

            // Get and return ideaRecipe after updates TODO: Is this good practice?
            return IdeaRecipeDBManager.get(tsmiUpdateConn, ideaID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(tsmiUpdateConn);
        }
    }

    public static Recipe remakeSaveRecipeIngredients(Integer ideaID, List<String> measuredIngredientStrings) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException {
        /* Generate new directions */

        Connection dUpdateConn = SQLConnectionPoolInstance.getConnection();
        try {
            // Get IdeaRecipe for name, summary, and ideaID and Recipe for IdeaRecipe ideaID
            IdeaRecipe ideaRecipe = IdeaRecipeDBManager.get(dUpdateConn, ideaID);
            Recipe recipe = RecipeDBManager.getFromIdeaID(dUpdateConn, ideaRecipe.getId());

            // Adapt form string list to RecipeMeasuredIngredient list
            List<RecipeMeasuredIngredient> measuredIngredients =

            // Update measured ingredients
            RecipeDBManager.updateMeasuredIngredients(dUpdateConn, recipe.getId(), measuredIngredientStrings);

            // Get list of RecipeInstructions from name and summary in ideaRecipe and measuredIngredients in request
            List<RecipeInstruction> directions = RecipeDirectionsGenerator.generateDirections(
                    ideaRecipe.getName(),
                    ideaRecipe.getSummary(),
                    measuredIngredientStrings
            );

            // Update recipe directions in DB
            RecipeDBManager.updateDirections(
                    dUpdateConn,
                    recipe.getId(),
                    directions
            );
        } finally {
            SQLConnectionPoolInstance.releaseConnection(dUpdateConn);
        }
    }



//    public static BodyResponse generatePackSaveRegenerateRecipeDirectionsAndIdeaRecipeIngredients(RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest request) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException, InvalidRequestJSONException, OpenAIGPTException, GenerationException {
//        // Require ideaID and at least one of newName with text, newSummary with text, or measuredIngredients with at least one
//        if (request.getIdeaID() == null || ((request.getNewName() == null || request.getNewName().isEmpty()) && (request.getNewSummary() == null || request.getNewSummary().isEmpty()) && (request.getMeasuredIngredients() == null || request.getMeasuredIngredients().size() == 0)))
//            throw new InvalidRequestJSONException("Missing required object - Please ensure your JSON contains authToken, ideaID, and optionally with at least one required newTitle, newSummary, and/or measuredIngredients.");



//        // Get recipe ID
//        final Integer recipeID = PPPersistinator.getRecipeID(request.getIdeaID());

//        // If recipe is null, throw GenerationException
//        if (recipeID == null)
//            throw new GenerationException("Could not find Recipe. Please make sure it's generated before trying to update!");

//        // Create a variable for idea recipe ingredients set as null which will be assigned in the getMeasuredIngredients not null or empty conditional, so it can be added to the response if it is not null
//        List<IdeaRecipeIngredient> ideaRecipeIngredients = null;
//
//        // If measuredIngredients is not null, update in DB and get ingredients without measurements
//        if (request.getMeasuredIngredients() != null && !request.getMeasuredIngredients().isEmpty()) {
//            // Create recipe measured ingredients from request
//            List<RecipeMeasuredIngredient> recipeMeasuredIngredients = new ArrayList<>();
//            request.getMeasuredIngredients().forEach(ingredientString -> {
//                recipeMeasuredIngredients.add(new RecipeMeasuredIngredient(recipeID, ingredientString));
//            });
//
//            // Generate new idea recipe ingredients from get ingredients without measurements
//            OAIGPTFunctionCallResponseParseIngredientNames parseIngredientNamesResponse = ParseIngredientNamesGenerator.getParsedIngredientNames(
//                    request.getMeasuredIngredients(),
//                    Constants.Context_Character_Limit_Get_Ingredients_Without_Measurements,
//                    Constants.Response_Token_Limit_Get_Ingredients_Without_Measurements
//            );
//
//            // Replace recipe measured ingredients
//            PPPersistinator.replaceRecipeMeasuredIngredients(recipeID, recipeMeasuredIngredients);
//
//            // Get ideaRecipeIngredients with IdeaRecipeIngredientFromOpenAIAdapter
//            ideaRecipeIngredients = IdeaRecipeIngredientFromOpenAIAdapter.getIdeaRecipeIngredients(request.getIdeaID(), parseIngredientNamesResponse);
//
//        }
//
//        // Get updated ideaRecipe and recipeMeasuredIngredients TODO: Optimize this!
//        IdeaRecipe ideaRecipe = IdeaRecipeDBManager.get(conn, request.getIdeaID());
//        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeDBManager.getMeasuredIngredients(conn, recipeID);
//
//        // Convert recipeMeasuredIngredients into a string array using their strings
//        List<String> recipeMeasuredIngredientsStrings = new ArrayList<>();
//        recipeMeasuredIngredients.forEach(measuredIngredient -> {
//            recipeMeasuredIngredientsStrings.add(measuredIngredient.getString());
//        });
//
//        // Generate new directions
//        OAIGPTFunctionCallResponseGenerateDirections generateDirectionsResponse = OAIGenerateDirectionsGenerator.generateDirections(
//                ideaRecipe.getName(),
//                ideaRecipe.getSummary(),
//                recipeMeasuredIngredientsStrings,
//                Constants.Context_Character_Limit_Generate_Directions,
//                Constants.Response_Token_Limit_Generate_Directions
//        );
//
//        // Get recipeInstructions from RecipeInstructionFromOpenAIAdapter TODO: Rename to recipeDiretions when I rename the DB object and everything else ree lol
//        List<RecipeInstruction> recipeInstructions = RecipeInstructionFromOpenAIAdapter.getRecipeInstructions(recipeID, generateDirectionsResponse);
//
//        // Delete all recipe directions
//        RecipeDBManager.deleteAllInstructions(conn, recipeID);
//
//        // Insert generated directions
//        RecipeDBManager.insertRecipeInstructions(conn, recipeInstructions);
//
//        // Get feasibility and update in DB
//        Integer feasibility = generateDirectionsResponse.getFeasibility();
//        RecipeDBManager.updateFeasibility(conn, recipeID, feasibility);
//
//        // Adapt to RegenerateRecipeDirectionAndIdeaRecipeIngredientsResponse
//        RegenerateRecipeDirectionsAndIdeaRecipeIngredientsResponse response = ResponseFromDBObjectAdapter.from(recipeInstructions, ideaRecipeIngredients, feasibility);
//
//        return BodyResponseFactory.createSuccessBodyResponse(response);
//    }

    public static BodyResponse generatePackSaveMakeRecipe(MakeRecipeRequest request) throws AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InvalidAssociatedIdentifierException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return generatePackSaveMakeRecipe(request, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static BodyResponse generatePackSaveMakeRecipe(MakeRecipeRequest request, Connection conn) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, DBSerializerPrimaryKeyMissingException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, CapReachedException, InvalidAssociatedIdentifierException {
        // Get isPremium
        boolean isPremium = PPRemainingCalculator.getIsPremium(request.getAuthToken());

        // Get remaining
//        Long remaining = MakeRecipeRemainingCalculator.calculateRemaining(u_aT.getUserID(), isPremium);

        // If remaining is not null (infinite) and less than 0, throw CapReachedException
//        if (remaining != null && remaining <= 0) throw new CapReachedException("Cap reached for user when generating recipe ideas");

        // Get idea and ingredients from ideaID
        IdeaRecipe idea = IdeaRecipeDBManager.get(conn, request.getIdeaID());
        List<IdeaRecipeIngredient> ideaIngredients = IdeaRecipeDBManager.getIngredients(conn, request.getIdeaID());

        // Throw exception if idea is null
        if (idea == null)
            throw new InvalidAssociatedIdentifierException("IdeaRecipe was null!");

        // Create input from ingredients and modifiers
        String input = parseMakeRecipeGPTInput(idea, ideaIngredients);

        // Generate if validated
        OAIGPTFunctionCallResponseMakeRecipe makeRecipeFunctionCallResponse = OAIRecipeGenerator.generateMakeRecipeFunctionCall(input, Constants.Context_Character_Limit_Make_Recipe, Constants.Response_Token_Limit_Make_Recipe);

        // Adapt to IdeaRecipe, IdeaRecipeIngredients, and IdeaRecipeEquipment
        Recipe recipe = RecipeFromOpenAIAdapter.getRecipe(request.getIdeaID(), makeRecipeFunctionCallResponse);
        List<RecipeInstruction> recipeInstructions = RecipeFromOpenAIAdapter.getRecipeInstructions(makeRecipeFunctionCallResponse);
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = RecipeFromOpenAIAdapter.getRecipeMeasuredIngredients(makeRecipeFunctionCallResponse);

        // Insert into database, creating object to use to parse to return
        RecipeDBManager.insertRecipe(conn, recipe, recipeInstructions, recipeMeasuredIngredients);

        // Adapt to CreateRecipeIdeaResponse and return in success body response
        MakeRecipeResponse recipeResponse = MakeRecipeResponseFactory.from(
                recipe,
                recipeInstructions,
                recipeMeasuredIngredients
        );

        return BodyResponseFactory.createSuccessBodyResponse(recipeResponse);
    }

    public static BodyResponse generatePackSaveIdeaRecipeTags(TagRecipeIdeaRequest request) throws InterruptedException, InvalidAssociatedIdentifierException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, DBObjectNotFoundFromQueryException, IOException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return generatePackSaveIdeaRecipeTags(request, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static BodyResponse generatePackSaveIdeaRecipeTags(TagRecipeIdeaRequest request, Connection conn) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, InvalidAssociatedIdentifierException, OpenAIGPTException, IOException {
        //Get idea and ingredients from ideaID
        IdeaRecipe idea = IdeaRecipeDBManager.get(conn, request.getIdeaID());
        List<IdeaRecipeIngredient> ideaIngredients = IdeaRecipeDBManager.getIngredients(conn, request.getIdeaID());

        // Throw exception if idea is null
        if (idea == null)
            throw new InvalidAssociatedIdentifierException("IdeaRecipe was null!");

        // Create input from ingredients and modifiers
        String input = parseTagRecipeIdeaGPTInput(idea, ideaIngredients);

        // Generate if validated
        OAIGPTFunctionCallResponseTagRecipeIdea tagRecipeIdeaFunctionCallResponse = OAITagRecipeIdeaGenerator.generateTagRecipeIdea(
                input,
                Constants.Context_Character_Limit_Tag_Recipe_Idea,
                Constants.Response_Token_Limit_Tag_Recipe_Idea
        );

        // Adapt to IdeaRecipeTag list
        List<IdeaRecipeTag> ideaRecipeTags = IdeaRecipeTagFromOpenAIAdapter.getIdeaRecipeTags(tagRecipeIdeaFunctionCallResponse, request.getIdeaID());

        // Remove invalid tags
        TagFilterer.removeInvalidTags(ideaRecipeTags);

        // Insert into database
        IdeaRecipeDBManager.insertIdeaRecipeTags(conn, ideaRecipeTags);

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

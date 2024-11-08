package com.pantrypro.core;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.model.JSONSchemaDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.exceptions.*;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.networking.endpoints.*;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.ResponseStatus;
import com.pantrypro.networking.server.request.*;
import com.pantrypro.networking.server.response.*;
import spark.Request;
import spark.Response;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {

    public class Func {

        /***
         * Categorize Ingredients
         *
         * Categorizes ingredients for creating a shopping list
         *
         * Request: {
         *     authToken: String - Authentication token for the user
         *     ingredients: String[] - Array of ingredients to categorize
         * }
         *
         * Response: {
         *     Body: {
         *         ingredientCategories: [
         *                 {
         *                     ingredient: String - The ingredient
         *                     category: String - The category for the ingredient
         *                 }
         *         ]
         *     }
         *     Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String categorizeIngredients(Request request, Response response) throws MalformedJSONException, IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException, OAISerializerException, JSONSchemaDeserializerException {
            // Try to parse CategorizeIngredientsRequest
            CategorizeIngredientsRequest ciRequest;

            try {
                ciRequest = new ObjectMapper().readValue(request.body(), CategorizeIngredientsRequest.class);
                CategorizeIngredientsResponse cir = CategorizeIngredientsEndpoint.categorizeIngredients(ciRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(cir);

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Categorizing Ingredients.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Create Recipe Idea
         *
         * Generates a recipe idea from the given ingredients and modifier text
         *
         * Request: {
         *      authToken: String - Authentication token for the user
         *      expandIngredients: Integer - Value of 0-4 denoting how much to expand ingredients
         *      ingredients: String - String of ingredients to use
         *      modifiers: String - Additional modifiers for the generation query
         * }
         *
         * Response: {
         *      Body: {
         *          name: String - Name of the recipe idea
         *          summary: String - A summary of the recipe idea
//         *          cuisineType: String - A cuisine type for the recipe idea
         *          ingredients: String[] - List of ingredients needed for the recipe idea
//         *          equipment: String[] - List of equipment needed for the recipe idea
         *          recipeID: Integer - The ID of the recipe
         *          remaining: Integer - The amount of recipe generations the user has remaining for the current period and their tier
         *      }
         *      Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String createRecipeIdea(Request request, Response response) throws IOException, MalformedJSONException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, OAISerializerException, JSONSchemaDeserializerException, AppStoreErrorResponseException {
            // Try to parse GetRecipeIdeaRequest
            CreateIdeaRecipeRequest criRequest;

            try {
                criRequest = new ObjectMapper().readValue(request.body(), CreateIdeaRecipeRequest.class);
                CreateIdeaRecipeResponse cirr = CreateRecipeIdeaEndpoint.createRecipeIdea(criRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(cirr);

                printTimestamped("User with AuthToken " + criRequest.getAuthToken().substring(0, Math.min(criRequest.getAuthToken().length(), 7)) + " generated Recipe Idea for Ingredients - " + criRequest.getIngredients().substring(0, Math.min(criRequest.getIngredients().length(), 39)));

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Getting Recipe Idea.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Finalize Recipe
         *
         * Generates a recipe from the given text, should be an idea
         *
         * Request: {
         *      authToken: String - Authentication token for the user
         *      recipeID: Integer - The id for the recipe to finalize
         * }
         *
         * Response: {
         *      Body: {
         *          estimatedTotalCalories: Integer - The total estimated calories for the recipe
         *          estimatedTotalMinutes: Integer - The total estimated minutes to make the recipe
         *          estimatedServings: Integer - The estimated amount of servings for the recipe
         *          feasibility: Integer - On a scale of 1-10, how feasible the recipe is to make
         *          ingredientsAndMeasurements: [
         *              {
         *                  ingredient: String - The ingredient
         *                  measurement: String - The measurement or amount
         *              }
         *          ]
         *          directions: [Integer: String] - Directions represented as String (value), index represented as Integer (key)
         *      }
         *      Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String finalizeRecipe(Request request, Response response) throws MalformedJSONException, IOException, DBSerializerPrimaryKeyMissingException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, DBSerializerException, OpenAIGPTException, InstantiationException, InvalidAssociatedIdentifierException, OAISerializerException, JSONSchemaDeserializerException {
            // Try to parse MakeRecipeRequest
            MakeRecipeRequest mrRequest;

            try {
                mrRequest = new ObjectMapper().readValue(request.body(), MakeRecipeRequest.class);
                MakeRecipeResponse mrr = MakeRecipeEndpoint.makeRecipe(mrRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(mrr);

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Making Recipe.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Parse Pantry Items
         *
         * Parses pantry items from a string to dynamically group them, also categorizes them
         *
         * Request: {
         *     authToken: String - Authentication token for the user
         *     input: String - The input to get the pantry items for
         * }
         *
         * Response: {
         *     Body {
         *          barItems: [
         *              {
         *                  item: String - The parsed pantry item
         *                  category: String - The category for the pantry item
         *              }
         *          ]
         *     }
         * }
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String parsePantryItems(Request request, Response response) throws IOException, MissingRequiredRequestObjectException, DBSerializerException, SQLException, OAISerializerException, OpenAIGPTException, JSONSchemaDeserializerException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MalformedJSONException {
            // Try to parse ParsePantryItemsRequest
            ParsePantryItemsRequest ppiRequest;

            try {
                ppiRequest = new ObjectMapper().readValue(request.body(), ParsePantryItemsRequest.class);
                ParsePantryItemsResponse ppiResponse = ParsePantryItemsEndpoint.parsePantryItems(ppiRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(ppiResponse);

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Making Recipe.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Regenerate Recipe Directions and Update Measured Ingredients
         *
         * Regenerates recipe directions and idea recipe ingredients given new name, summary, and/or measuredIngredients
         *
         * Request: {
         *      authToken: String - Authentication token for the user
         *      ideaID: Integer - The ideaID received from generating an idea recipe
         *      ? newName: String - Optional new name to set for the idea recipe, to be used when generating recipe directions, replaces name in DB
         *      ? newSummary: String - Optional new summary to set for the idea recipe, to be used when generating recipe directions, replaces summary in DB
         *      ? ingredientsAndMeasurements: [
         *          {
         *              ingredient: String - The ingredient
         *              measurement: String - The measurement or amount
         *          }
         *      ] - Optional array of all measuredIngredients for the recipe, including new ones and existing ones, to be used when generating recipe directions, replaces measured ingredients for recipe in DB, removes measurement and saves to idea recipe ingredients in DB and sends these back in ideaRecipeIngredients in the response
         * }
         *
         * Response: {
         *     Body: {
         *         recipeDirections: String[] - List of new recipe directions in order
         *         feasibility: Integer - On a scale of 1-10, how feasible the recipe is to make
         *     }
         *     Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         * @param request
         * @param response
         *
         *
         */
        public static String regenerateRecipeDirectionsAndUpdateMeasuredIngredients(Request request, Response response) throws IOException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, InvalidRequestJSONException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, MalformedJSONException, GenerationException, InvalidAssociatedIdentifierException, OAISerializerException, JSONSchemaDeserializerException {
            // Try to parse RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest
            RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest rrdairiRequest;

            try {
                rrdairiRequest = new ObjectMapper().readValue(request.body(), RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest.class);
                RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse rrdaumiResponse = RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsEndpoint.regenerateRecipeDirectionsAndUpdateMeasuredIngredients(rrdairiRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(rrdaumiResponse);

                return new ObjectMapper().writeValueAsString(br);
            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when regenerating recipe directions and idea recipe ingredients.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Tag Recipe
         *
         * Tags a recipe idea from the given ideaID
         *
         * Request: {
         *     authToken: String - Authentication token for the user
         *     ideaID: Integer - The id for the idea to base the tags on
         * }
         *
         * Response: {
         *     Body: {
         *         tags: String[] - List of relevant tags for the recipe idea
         *     }
         *     Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String tagRecipeIdea(Request request, Response response) throws IOException, InvalidAssociatedIdentifierException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MalformedJSONException, OAISerializerException, JSONSchemaDeserializerException {
            // Try to parse TagRecipeRequest
            TagRecipeRequest triRequest;

            try {
                triRequest = new ObjectMapper().readValue(request.body(), TagRecipeRequest.class);
                TagRecipeResponse trr = TagRecipeEndpoint.tagRecipe(triRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(trr);

                return new ObjectMapper().writeValueAsString(br);
            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Tagging Recipe.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

    }

    public class Pinterest {

        /***
         * Log Pinterest Conversion
         *
         * Logs a pinterest conversion with Pinterest's Conversions API.
         *
         * Request: {
         *     authToken: String - Authentication token received from the server
         *     idfa: String - iOS advertising identifier
         *     eventName: String - Pinterest event name to log
         * }
         *
         * Response: {
         *     Body: {
         *          didLog: Boolean - If the logging was successful or not
         *     }
         *     Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         * @param req Request object given by Spark
         * @param res Response object given by Spark
         * @return Value of JSON response as String
         */
        public static Object logPinterestConversion(Request req, Response res) throws IOException, InvalidAssociatedIdentifierException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MalformedJSONException {
            // Try to parse LogPinterestConversionRequest
            LogPinterestConversionRequest lpcRequest;

            try {
                lpcRequest = new ObjectMapper().readValue(req.body(), LogPinterestConversionRequest.class);
                LogPinterestConversionResponse lpcResponse = LogPinterestConversionEndpoint.logPinterestConversion(lpcRequest);
                BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(lpcResponse);

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Making Recipe.. The request: " + req.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

    }

    /***
     * Get All Tags
     *
     * Gets all the tags that are available in PantryPro. This is the list that tagRecipeIdea selects from, and the list that can be provided to users to select from when generating a recipe idea for convenience
     *
     * Request: {
     *     authToken: String - Authentication token, generated from registerUser
     * }
     *
     * Response: {
     *     Body: {
     *         tags: String[] - Array of tags available
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON represented as String
     */
    public static Object getAllTags(Request request, Response response) throws IOException {
        // Process the request
        AuthRequest authRequest = new ObjectMapper().readValue(request.body(), AuthRequest.class);

        // Get get all tags response in body response and return as string
        BodyResponse bodyResponse = GetAllTagsEndpoint.getAllTags();

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }

    /***
     * Get Is Premium
     *
     * Gets the isPremium value for the user using latest receipt or transaction, updating with Apple if necessary
     *
     * Request: {
     *     authToken: String - Authentication token, generated from registerUser
     * }
     *
     * Response: {
     *     Body: {
     *         isPremium: Boolean - True if user is premium, false if not
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON represented as String
     */
    public static Object getIsPremium(Request request, Response response) throws IOException, DBSerializerPrimaryKeyMissingException, SQLException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, AppStoreErrorResponseException {
        // Process the request
        AuthRequest authRequest = new ObjectMapper().readValue(request.body(), AuthRequest.class);

        // Get IsPremiumResponse response, success body response, and return as string
        IsPremiumResponse ipResponse = GetIsPremiumEndpoint.getIsPremium(authRequest);
        BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(ipResponse);

        return new ObjectMapper().writeValueAsString(br);
    }

    public static Object registerTransaction(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, UnrecoverableKeyException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerPrimaryKeyMissingException, AppStoreErrorResponseException {
        // Parse the request
        RegisterTransactionRequest rtr = new ObjectMapper().readValue(request.body(), RegisterTransactionRequest.class);

        BodyResponse bodyResponse = RegisterTransactionEndpoint.registerTransaction(rtr);

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }

    /***
     * Register User
     *
     * Registers a user to the database. This is a blank POST request and may be changed to a GET in the future.
     *
     * Request: {
     *
     * }
     *
     * Response: {
     *     Body: {
     *         authToken: String - Authentication token generated by the server
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON response as String
     */
    public static String registerUser(Request request, Response response) throws SQLException, SQLGeneratedKeyException, PreparedStatementMissingArgumentException, IOException, DBSerializerPrimaryKeyMissingException, DBSerializerException, AutoIncrementingDBObjectExistsException, IllegalAccessException, InterruptedException, InvocationTargetException {
        BodyResponse bodyResponse = RegisterUserEndpoint.registerUser();

        printTimestamped("Registered new user!");

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }

    public static String validateAuthToken(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        AuthRequest authRequest = new ObjectMapper().readValue(request.body(), AuthRequest.class);

        ValidateAuthTokenResponse vatr = ValidateAuthTokenEndpoint.validateAuthToken(authRequest);
        BodyResponse br = BodyResponseFactory.createSuccessBodyResponse(vatr);

        return new ObjectMapper().writeValueAsString(br);
    }


    /***
     * Get Remaining Idea Recipes
     *
     * Gets the amount of idea recipes remaining in the day for the user for their tier.
     *
     * Request: {
     *     authToken: String - Authentication token, generated from registerUser
     * }
     *
     * Response: {
     *     Body: {
     *         remaining: Integer - The amount of idea recipes remaining for the user for their tier
     *     }
     *     Success: Integer - Integer denoting success, 1 if successful
     * }
     *
     * @param request Request object given by Spark
     * @param response Response object given by Spark
     * @return Value of JSON represented as String
     */
    public static Object getRemainingIdeaRecipes(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, AppStoreErrorResponseException {
        // Process the request
        AuthRequest authRequest = new ObjectMapper().readValue(request.body(), AuthRequest.class);

        // Get remaining response in body response and return as string
        BodyResponse bodyResponse = GetRemainingIdeaRecipesEndpoint.getRemaining(authRequest);

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }


    // --------------- //

    public static String getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus status, String description) {
        // Create error response
        ErrorResponse errorResponse = new ErrorResponse(description);
        BodyResponse bodyResponse = BodyResponseFactory.createBodyResponse(status, errorResponse);

        try {
            return new ObjectMapper().writeValueAsString(bodyResponse);
        } catch (IOException e) {
            return null;
        }
//        return "{\"Success\":" + ResponseStatus.EXCEPTION_MAP_ERROR.Success + "}";
    }

    // -------------- //

    public static void printTimestamped(String string) {
        // TODO: Better logging
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();

        System.out.println(sdf.format(date) + " - " + string);
    }

}

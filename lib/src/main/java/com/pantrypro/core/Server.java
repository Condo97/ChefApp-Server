package com.pantrypro.core;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.common.exceptions.*;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.core.service.endpoints.*;
import com.pantrypro.model.exceptions.CapReachedException;
import com.pantrypro.model.exceptions.GenerationException;
import com.pantrypro.model.exceptions.InvalidAssociatedIdentifierException;
import com.pantrypro.model.exceptions.InvalidRequestJSONException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.ResponseStatus;
import com.pantrypro.model.http.server.request.*;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.ErrorResponse;
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
        public static String categorizeIngredients(Request request, Response response) throws MalformedJSONException, IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, OpenAIGPTException {
            // Try to parse CategorizeIngredientsRequest
            CategorizeIngredientsRequest ciRequest;

            try {
                ciRequest = new ObjectMapper().readValue(request.body(), CategorizeIngredientsRequest.class);
                BodyResponse br = CategorizeIngredientsEndpoint.categorizeIngredients(ciRequest);

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
         *      ingredients: String - String of ingredients to use
         *      expandIngredients: Integer - Value of 0-4 denoting how much to expand ingredients
         *      modifiers: String - Additional modifiers for the generation query
         * }
         *
         * Response: {
         *      Body: {
         *          name: String - Name of the recipe idea
         *          summary: String - A summary of the recipe idea
         *          cuisineType: String - A cuisine type for the recipe idea
         *          ingredients: String[] - List of ingredients needed for the recipe idea
         *          equipment: String[] - List of equipment needed for the recipe idea
         *          ideaID: Integer - The ID of the idea
         *      }
         *      Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String createRecipeIdea(Request request, Response response) throws IOException, MalformedJSONException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
            // Try to parse GetRecipeIdeaRequest
            CreateIdeaRecipeRequest criRequest;

            try {
                criRequest = new ObjectMapper().readValue(request.body(), CreateIdeaRecipeRequest.class);
                BodyResponse br = CreateRecipeIdeaEndpoint.createRecipeIdea(criRequest);

                printTimestamped("User with AuthToken " + criRequest.getAuthToken().substring(0, Math.min(criRequest.getAuthToken().length(), 7)) + " generated Recipe Idea for Ingredients - " + criRequest.getIngredients().substring(0, Math.min(criRequest.getIngredients().length(), 39)));

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Getting Recipe Idea.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Make Recipe
         *
         * Generates a recipe from the given text, should be an idea
         *
         * Request: {
         *      authToken: String - Authentication token for the user
         *      ideaID: Integer - The id for the idea to base the recipe on
         * }
         *
         * Response: {
         *      Body: {
         *          instructions: String[] - List of instructions for the recipe
         *          allIngredientsAndMeasurements: String[] - Ingredients and measurements as strings for the recipe
         *      }
         *      Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         *
         * @param request Request object given by Spark
         * @param response Response object given by Spark
         * @return Value of JSON response as String
         */
        public static String makeRecipe(Request request, Response response) throws MalformedJSONException, IOException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, InvalidAssociatedIdentifierException {
            // Try to parse MakeRecipeRequest
            MakeRecipeRequest mrRequest;

            try {
                mrRequest = new ObjectMapper().readValue(request.body(), MakeRecipeRequest.class);
                BodyResponse br = MakeRecipeEndpoint.makeRecipe(mrRequest);

                return new ObjectMapper().writeValueAsString(br);

            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Making Recipe.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Regenerate Recipe Directions and Idea Recipe Ingredients
         *
         * Regenerates recipe directions and idea recipe ingredients given new name, summary, and/or measuredIngredients
         *
         * Request: {
         *     authToken: String - Authentication token for the user
         *     ideaID: Integer - The ideaID received from generating an idea recipe
         *     ? newName: String - Optional new name to set for the idea recipe, to be used when generating recipe directions, replaces name in DB
         *     ? newSummary: String - Optional new summary to set for the idea recipe, to be used when generating recipe directions, replaces summary in DB
         *     ? measuredIngredients: String[] - Optional array of all measuredIngredients for the recipe, including new ones and existing ones, to be used when generating recipe directions, replaces measured ingredients for recipe in DB, removes measurement and saves to idea recipe ingredients in DB and sends these back in ideaRecipeIngredients in the response
         * }
         *
         * Response: {
         *     Body: {
         *         recipeDirections: String[] - List of new recipe directions in order
         *         ? ideaRecipeIngredients: String[] - Optional, included if the request included measuredIngredients that needed to be parsed to idea recipe ingredients by removing the measurement
         *     }
         *     Success: Integer - Integer denoting success, 1 if successful
         * }
         *
         * @param request
         * @param response
         *
         *
         */
        public static String regenerateRecipeDirectionsAndIdeaRecipeIngredients(Request request, Response response) throws IOException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, InvalidRequestJSONException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, MalformedJSONException, GenerationException {
            // Try to parse RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest
            RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest rrdairiRequest;

            try {
                rrdairiRequest = new ObjectMapper().readValue(request.body(), RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest.class);
                BodyResponse br = RegenerateRecipeDirectionsAndIdeaRecipeIngredientsEndpoint.regenerateRecipeDirectionsAndIdeaRecipeIngredients(rrdairiRequest);

                return new ObjectMapper().writeValueAsString(br);
            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when regenerating recipe directions and idea recipe ingredients.. The request: " + request.body());
                e.printStackTrace();
                throw new MalformedJSONException("Malformed JSON - " + e.getMessage());
            }
        }

        /***
         * Tag Recipe Idea
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
        public static String tagRecipeIdea(Request request, Response response) throws IOException, InvalidAssociatedIdentifierException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, MalformedJSONException {
            // Try to parse TagRecipeRequest
            TagRecipeIdeaRequest triRequest;

            try {
                triRequest = new ObjectMapper().readValue(request.body(), TagRecipeIdeaRequest.class);
                BodyResponse br = GetIdeaRecipeTagsEndpoint.tagRecipeIdea(triRequest);

                return new ObjectMapper().writeValueAsString(br);
            } catch (JsonMappingException | JsonParseException e) {
                System.out.println("Error when Tagging Recipe.. The request: " + request.body());
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
    public static Object getIsPremium(Request request, Response response) throws IOException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, DBObjectNotFoundFromQueryException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
        // Process the request
        AuthRequest authRequest = new ObjectMapper().readValue(request.body(), AuthRequest.class);

        // Get get is premium response in body response and return as string
        BodyResponse bodyResponse = GetIsPremiumEndpoint.getIsPremium(authRequest);

        return new ObjectMapper().writeValueAsString(bodyResponse);
    }

    public static Object registerTransaction(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, UnrecoverableKeyException, CertificateException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerPrimaryKeyMissingException {
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

        BodyResponse br = ValidateAuthTokenEndpoint.validateAuthToken(authRequest);

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
    public static Object getRemainingIdeaRecipes(Request request, Response response) throws IOException, DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
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

//    public static String getSimpleExceptionHandlerResponseStatusJSON(ResponseStatus status) {
//
//        //TODO: - This is the default implementation that goes along with the app... This needs to be put as legacy and a new way of handling errors needs to be developed!
//        ObjectMapper mapper = new ObjectMapper();
//        ObjectNode bodyNode = mapper.createObjectNode();
//        bodyNode.put("output", "There was an issue getting your chat. Please try again..."); // Move this!
//        bodyNode.put("remaining", -1);
//        bodyNode.put("finishReason", "");
//
//        ObjectNode baseNode = mapper.createObjectNode();
//        baseNode.put("Success", ResponseStatus.SUCCESS.Success);
//        baseNode.put("Body", bodyNode);
//
//        return baseNode.toString();
////        return "{\"Success\":" + ResponseStatus.EXCEPTION_MAP_ERROR.Success + "}";
//    }

}

package pantrypro;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.oaigptconnector.model.OAIDeserializerException;
import com.oaigptconnector.model.OAISerializerException;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.Constants;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.exceptions.*;
import com.pantrypro.keys.EncryptionManager;
import com.pantrypro.keys.Keys;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.networking.endpoints.*;
import com.pantrypro.networking.server.request.*;
import com.pantrypro.networking.server.response.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import java.util.List;

public class Tests {

    @BeforeAll
    static void setUp() throws SQLException {
        SQLConnectionPoolInstance.create(Constants.MYSQL_URL, Keys.MYSQL_USER, Keys.MYSQL_PASS, 10);
    }

//    @Test
//    @DisplayName("Try creating a SELECT Prepared Statement")
//    void testSelectPreparedStatement() throws InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//
//        try {
//            // Try complete Select PS
//            ComponentizedPreparedStatement cps = SelectComponentizedPreparedStatementBuilder.forTable("Recipe").select("recipe_id").select("user_id").where("user_text", SQLOperators.EQUAL, 5).limit(5).orderBy(OrderByComponent.Direction.DESC, "date").build();
//
//            PreparedStatement cpsPS = cps.connect(conn);
//            System.out.println(cpsPS.toString());
//            cpsPS.close();
//
//            // Try minimal Select PS
//            ComponentizedPreparedStatement selectCPSMinimal = SelectComponentizedPreparedStatementBuilder.forTable("Recipe").build();
//
//            PreparedStatement selectCPSMinimalPS = selectCPSMinimal.connect(conn);
//            System.out.println(selectCPSMinimalPS.toString());
//            selectCPSMinimalPS.close();
//
//            // Try partial Select PS
//            ComponentizedPreparedStatement selectCPSPartial = SelectComponentizedPreparedStatementBuilder.forTable("IdeaRecipe").select("idea_id").where("user_id", SQLOperators.EQUAL, false).build();
//
//            PreparedStatement selectCPSPartialPS = selectCPSPartial.connect(conn);
//            System.out.println(selectCPSPartialPS.toString());
//            selectCPSPartialPS.close();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    @Test
//    @DisplayName("Try creating an INSERT INTO Prepared Statement")
//    void testInsertIntoPreparedStatement() throws InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//
//        try {
//            // Build the insert componentized statement
//            ComponentizedPreparedStatement insertCPSComplete = InsertIntoComponentizedPreparedStatementBuilder.forTable("IdeaRecipe").addColAndVal("idea_id", Types.NULL).addColAndVal("user_id", 5).addColAndVal("date", LocalDateTime.now()).build(true);
//
//            System.out.println(insertCPSComplete);
//
//            // Do update and get generated keys
//            List<Map<String, Object>> generatedKeys = DBClient.updateReturnGeneratedKeys(conn, insertCPSComplete);
//
//            System.out.println(generatedKeys);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    @Test
//    @DisplayName("Try creating an UPDATE Prepared Statement")
//    void testUpdatePreparedStatement() throws InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//
//        try {
//            ComponentizedPreparedStatement updatePSComplete = UpdateComponentizedPreparedStatementBuilder.forTable("IdeaRecipe").set("date", LocalDateTime.now()).where("user_id", SQLOperators.EQUAL, 5).build();
//
//            DBClient.update(conn, updatePSComplete);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }

//    @Test
//    @DisplayName("HttpHelper Testing")
//    void testBasicHttpRequest() {
//        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofMinutes(Constants.AI_TIMEOUT_MINUTES)).build();
//        OAIGPTChatCompletionRequestMessage promptMessageRequest = new OAIGPTChatCompletionRequestMessage(Role.USER, "write me a short joke");
//        OAIGPTChatCompletionRequest promptRequest = new OAIGPTChatCompletionRequest("gpt-3.5-turbo", 100, 0.7, Arrays.asList(promptMessageRequest));
//        Consumer<HttpRequest.Builder> c = requestBuilder -> {
//            requestBuilder.setHeader("Authorization", "Bearer " + Keys.openAiAPI);
//        };
//
//        OpenAIGPTHttpsHandler httpHelper = new OpenAIGPTHttpsHandler();
//
//        try {
//            OAIGPTChatCompletionResponse response = httpHelper.postChatCompletion(promptRequest, Keys.openAiAPI);
//            System.out.println(response.getChoices()[0].getMessage().getContent());
//
//        } catch (OpenAIGPTException e) {
//            System.out.println(e.getErrorObject().getError().getMessage());
//        } catch (IOException e) {
//             throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//
//    }

//    @Test
//    @DisplayName("Test Registering Transaction")
//    void testTransactionValidation() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, AppStoreStatusResponseException, UnrecoverableKeyException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchMethodException, InstantiationException, PreparedStatementMissingArgumentException, AppleItunesResponseException {
//        /* REGISTER TRANSACTION ENDPOINT */
//        // Input
//        final Long sampleTransactionId = 2000000351816446l;
//        // Expected Results
//        final AppStoreSubscriptionStatus expectedStatus = AppStoreSubscriptionStatus.EXPIRED;
//        final Boolean expectedIsPremiumValue1 = false;
//
//        // Register user
//        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
//        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();
//
//        // Get authToken
//        String authToken = aResponse.getAuthToken();
//
//        // Create register transaction request
//        RegisterTransactionRequest rtr = new RegisterTransactionRequest(authToken, sampleTransactionId, null);
//
//        // Register transaction
//        BodyResponse registerTransactionBR = RegisterTransactionEndpoint.registerTransaction(rtr);
//        IsPremiumResponse ipr1 = (IsPremiumResponse)registerTransactionBR.getBody();
//
//        // Get User_AuthToken
//        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(authToken);
//
//        // Verify transaction registered successfully
//        Transaction transaction = TransactionDBManager.getMostRecent(u_aT.getUserID());
//        assert(transaction != null);
//        System.out.println(transaction.getAppstoreTransactionID() + " " + sampleTransactionId);
//        assert(transaction.getAppstoreTransactionID().equals(sampleTransactionId));
////        assert(transaction.getStatus() == expectedStatus);
//
//        // Verify registered transaction successfully got isPremium value
////        assert(ipr1.getIsPremium() == expectedIsPremiumValue1);
//
//        /* IS PREMIUM ENDPOINT */
//        // Expected Results
//        final Boolean expectedIsPremiumValue2 = false;
//
//        // Create authRequest
//        AuthRequest aRequest = new AuthRequest(authToken);
//
//        // Get Is Premium from endpoint
//        BodyResponse isPremiumBR = GetIsPremiumEndpoint.getIsPremium(aRequest);
//        IsPremiumResponse ipr2 = (IsPremiumResponse)isPremiumBR.getBody();
//
//        // Verify results
////        assert(ipr2.getIsPremium() == expectedIsPremiumValue2);
//    }

//    @Test
//    @DisplayName("Test Create Recipe Idea Endpoint")
//    void testCreateRecipeIdeaEndpoint() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, OpenAIGPTException, DBObjectNotFoundFromQueryException, IOException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, AutoIncrementingDBObjectExistsException {
//        // Register user
//        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
//        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();
//
//        // Get authToken
//        String authToken = aResponse.getAuthToken();
//
//        // Create create recipe idea request
//        CreateRecipeIdeaRequest request = new CreateRecipeIdeaRequest(
//                authToken,
//                List.of("onions, potatoes, peas"),
//                List.of("salad"),
//                0
//        );
//
//        System.out.println("Request:\n" + new ObjectMapper().writeValueAsString(request));
//
//        BodyResponse br = CreateRecipeIdeaEndpoint.createRecipeIdea(request);
//
//        System.out.println("Response:\n" + new ObjectMapper().writeValueAsString(br));
//    }

//    @Test
//    @DisplayName("Test Create Recipe Idea Endpoint")
//    void testCreateRecipeIdeaEndpoint() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, AppStoreStatusResponseException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, NoSuchMethodException, UnrecoverableKeyException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, OAISerializerException, OAIDeserializerException {
//        // Register user
//        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
//        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();
//
//        // Get authToken
//        String authToken = aResponse.getAuthToken();
//
//        // Build CreateRecipeIdeaRequest
//        CreateIdeaRecipeRequest request = new CreateIdeaRecipeRequest(
//                authToken,
//                "peaches, flour, eggs, oatmeal, chocolate chips",
//                "make souffle",
//                0
//        );
//
//        // Test Create Recipe Idea Endpoint
//        CreateIdeaRecipeResponse criResponse = CreateRecipeIdeaEndpoint.createRecipeIdea(request);
//
//        // Ensure criResponse is not null and no fields in criResponse are null nor empty nor blank depending on the type
//        assert(criResponse != null);
//        assert(criResponse.getRecipeID() != null);
//        assert(criResponse.getName() != null && !criResponse.getName().isBlank());
//        assert(criResponse.getSummary() != null && !criResponse.getSummary().isBlank());
//        assert(criResponse.getIngredients() != null && !criResponse.getIngredients().isEmpty());
//        assert(criResponse.getRemaining() != null);
//    }

    @Test
    @DisplayName("Test Full Recipe Creation Flow - Create Recipe and Finalize Recipe")
    void testMakeRecipeEndpiont() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, NoSuchMethodException, UnrecoverableKeyException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, InvalidAssociatedIdentifierException, OAISerializerException, OAIDeserializerException, AppStoreErrorResponseException {
        // Register user
        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();

        // Get authToken
        String authToken = aResponse.getAuthToken();

        // Build CreateRecipeIdeaRequest
        CreateIdeaRecipeRequest criRequest = new CreateIdeaRecipeRequest(
                authToken,
                "peaches, flour, eggs, oatmeal, chocolate chips",
                "make souffle",
                0
        );

        // Test Create Recipe Idea Endpoint
        CreateIdeaRecipeResponse criResponse = CreateRecipeIdeaEndpoint.createRecipeIdea(criRequest);

        // Ensure criResponse is not null and no fields in criResponse are null nor empty nor blank depending on the type
        assert(criResponse != null);
        assert(criResponse.getRecipeID() != null);
        assert(criResponse.getName() != null && !criResponse.getName().isBlank());
        assert(criResponse.getSummary() != null && !criResponse.getSummary().isBlank());
        assert(criResponse.getIngredients() != null && !criResponse.getIngredients().isEmpty());
        assert(criResponse.getRemaining() != null);

        // Register user
//        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
//        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();
//
//        // Get authToken
//        String authToken = aResponse.getAuthToken();

        // Build MakeRecipeRequest
        MakeRecipeRequest mrRequest = new MakeRecipeRequest(
                authToken,
                criResponse.getRecipeID(),
                criResponse.getRecipeID()
        );

        // Test Make Recipe Endpoint
        MakeRecipeResponse mrResponse = MakeRecipeEndpoint.makeRecipe(mrRequest);

        // Ensure mrResponse is not null and no fields in mrResponse are null nor empty nor blank depending on the type
        assert(mrResponse != null);
        assert(mrResponse.getEstimatedTotalCalories() != null);
        assert(mrResponse.getEstimatedTotalMinutes() != null);
        assert(mrResponse.getEstimatedServings() != null);
        assert(mrResponse.getFeasibility() != null);
        assert(mrResponse.getAllIngredientsAndMeasurements() != null && !mrResponse.getAllIngredientsAndMeasurements().isEmpty());
        assert(mrResponse.getInstructions() != null && !mrResponse.getInstructions().isEmpty());
    }

    @Test
    @DisplayName("Test Tag Recipe Endpoint")
    void testTagRecipeIdea() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, OpenAIGPTException, DBObjectNotFoundFromQueryException, IOException, NoSuchMethodException, InstantiationException, UnrecoverableKeyException, CapReachedException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, OAISerializerException, OAIDeserializerException, InvalidAssociatedIdentifierException, AppStoreErrorResponseException {
        final String ingredients = "peaches, flour, eggs";
        final String modifiers = null;

        // Register user
        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();

        // Create recipe idea request
        CreateIdeaRecipeRequest criRequest = new CreateIdeaRecipeRequest(
                aResponse.getAuthToken(),
                ingredients,
                modifiers,
                1
        );
        CreateIdeaRecipeResponse criResponse = CreateRecipeIdeaEndpoint.createRecipeIdea(criRequest);

        // Parse recipeID out of recipeIdeaBR
        Integer recipeID = criResponse.getRecipeID();

        // Build TagRecipeRequest
        TagRecipeRequest trRequest = new TagRecipeRequest(
                aResponse.getAuthToken(),
                recipeID,
                null
        );

        // Test Tag Recipe Endpoint
        TagRecipeResponse trResponse = TagRecipeEndpoint.tagRecipe(trRequest);

        // Ensure trResponse is not null and no fields in trResponse are null nor empty nor blank depending on the type
        assert(trResponse != null);
//        assert(trResponse.getTags() != null && !trResponse.getTags().isEmpty());
    }

    @Test
    @DisplayName("Test Categorize Ingredients Endpoint")
    void testCategorizeIngredients() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, DBObjectNotFoundFromQueryException, NoSuchMethodException, InstantiationException, OpenAIGPTException, IOException, OAISerializerException, OAIDeserializerException {
        final List<String> ingredients = List.of(
                "peaches",
                "flour",
                "eggs",
                "bread",
                "paper towels",
                "chef boy r dee",
                "pasta"
        );

        // Register user
        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();

        // Create categorize ingredients request
        CategorizeIngredientsRequest ciRequest = new CategorizeIngredientsRequest(
                aResponse.getAuthToken(),
                ingredients,
                null
        );

        // Test Categorize Ingredients Endpoint
        CategorizeIngredientsResponse ciResponse = CategorizeIngredientsEndpoint.categorizeIngredients(ciRequest);

        // Ensure ciResponse is not null and no fields in ciResponse are null nor empty nor blank depending on the type
        assert(ciResponse != null);
        assert(ciResponse.getIngredientCategories() != null && !ciResponse.getIngredientCategories().isEmpty());
    }

    @Test
    @DisplayName("Test Regenerate Recipe Directions And Idea Recipe Ingredients")
    void testRegenerateRecipeDirectionsAndIdeaRecipeIngredients() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, NoSuchMethodException, UnrecoverableKeyException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, InvalidAssociatedIdentifierException, InvalidRequestJSONException, GenerationException, OAISerializerException, OAIDeserializerException, AppStoreErrorResponseException {
        /* Create Idea Recipe and Recipe */
        // Register user
        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();

        // Get authToken
        String authToken = aResponse.getAuthToken();

        // Build CreateRecipeIdeaRequest
        CreateIdeaRecipeRequest criRequest = new CreateIdeaRecipeRequest(
                authToken,
                "peaches, flour, eggs, oatmeal, chocolate chips",
                "make souffle",
                0
        );

        // Get Create Idea Recipe Response
        CreateIdeaRecipeResponse criResponse = CreateRecipeIdeaEndpoint.createRecipeIdea(criRequest);

        // Get recipeID, name, and summary from criResponse
        Integer recipeID = criResponse.getRecipeID();
        String name = criResponse.getName();
        String summary = criResponse.getSummary();

        // Build MakeRecipeRequest
        MakeRecipeRequest mrRequest = new MakeRecipeRequest(
                authToken,
                recipeID,
                recipeID
        );

        // Generate pack save make recipe as body response
        MakeRecipeResponse mrResponse = MakeRecipeEndpoint.makeRecipe(mrRequest);

        // Get measuredIngredients from mrBResponse
        List<String> measuredIngredients = mrResponse.getAllIngredientsAndMeasurements();

        /* Regenerate Recipe Directions and Idea Recipe Ingredients */
        // Create new name, summary, and measuredIngredients
        String newName = "chicken alfredo";
        String newSummary = "a delicious chicken alfredo dish that uses a lot of cream";
        Integer newServings = 5;
        List<String> newMeasuredIngredients = List.of(
                "1 lb chicken",
                "2 oz heavy cream",
                "1 lb pasta",
                "8 oz butter"
        );

        // Build RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest
        RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest rrdaumiRequest = new RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsRequest(
                authToken,
                recipeID,
                newName,
                newSummary,
                newServings,
                newMeasuredIngredients
        );

        // Generate pack save regenerate recipe directions and idea recipe ingredients as body response
        RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsResponse rrdaumiResponse = RegenerateRecipeDirectionsAndUpdateMeasuredIngredientsEndpoint.regenerateRecipeDirectionsAndUpdateMeasuredIngredients(rrdaumiRequest);

        // Get recipeDirections and feasibility from rrdaumiResponse
        List<String> recipeDirections = rrdaumiResponse.getRecipeDirections();
        Integer feasibility = rrdaumiResponse.getFeasibility();
//        List<String> ideaRecipeIngredients = rrdaumiResponse.getIdeaRecipeIngredients();

        // Assert neither are null and recipeDirections is not empty, and print their values
        assert(recipeDirections != null && !recipeDirections.isEmpty());
        assert(feasibility != null);
//        assert(ideaRecipeIngredients != null && !ideaRecipeIngredients.isEmpty());

        System.out.println(recipeDirections);
        System.out.println(feasibility);
//        System.out.println(ideaRecipeIngredients);
    }

    @Test
    @DisplayName("Test Get Encrypted Bing API Key")
    void testGetEncryptedBingAPIKey() {
        String encryptedBingAPIKey = EncryptionManager.getEncryptedBingAPIKey();

        assert(encryptedBingAPIKey != null);
    }

    @Test
    @DisplayName("Misc Modifyable")
    void misc() throws IOException, DBSerializerException, SQLException, OpenAIGPTException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        System.out.println("Here it is: " + Table.USER_AUTHTOKEN);
//        System.out.println("OAIGPTChatCompletionRequestFunctionCategorizeIngredients:\n" + new ObjectMapper().writeValueAsString(OAIGPTChatCompletionRequestFunctionCategorizeIngredientsBuilder.build()));
//
//        OAIGPTFunctionCallResponseCreateFinishedRecipe oaiResponse = OAIRecipeGenerator.generateMakeRecipeFunctionCall("chocolate, flour, milk", 800, 800);
//        Recipe r = RecipeFromOpenAIAdapter.getRecipe(123, oaiResponse);
//        System.out.println(r.getEstimated_total_minutes());
//        System.out.println(r.getFeasibility());
    }
}

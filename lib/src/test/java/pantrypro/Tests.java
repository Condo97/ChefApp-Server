package pantrypro;

import com.oaigptconnector.core.OpenAIGPTHttpsClientHelper;
import com.oaigptconnector.model.Role;
import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequest;
import com.oaigptconnector.model.request.chat.completion.OAIGPTChatCompletionRequestMessage;
import com.oaigptconnector.model.response.chat.completion.http.OAIGPTChatCompletionResponse;
import com.pantrypro.Constants;
import com.pantrypro.common.exceptions.AutoIncrementingDBObjectExistsException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.database.managers.TransactionDBManager;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.core.service.endpoints.GetIsPremiumEndpoint;
import com.pantrypro.core.service.endpoints.RegisterTransactionEndpoint;
import com.pantrypro.core.service.endpoints.RegisterUserEndpoint;
import com.pantrypro.keys.Keys;
import com.pantrypro.model.database.AppStoreSubscriptionStatus;
import com.pantrypro.model.database.objects.Transaction;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.client.apple.itunes.exception.AppStoreStatusResponseException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.request.AuthRequest;
import com.pantrypro.model.http.server.request.RegisterTransactionRequest;
import com.pantrypro.model.http.server.response.AuthResponse;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.IsPremiumResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import sqlcomponentizer.DBClient;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
import sqlcomponentizer.preparedstatement.ComponentizedPreparedStatement;
import sqlcomponentizer.preparedstatement.component.OrderByComponent;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;
import sqlcomponentizer.preparedstatement.statement.InsertIntoComponentizedPreparedStatementBuilder;
import sqlcomponentizer.preparedstatement.statement.SelectComponentizedPreparedStatementBuilder;
import sqlcomponentizer.preparedstatement.statement.UpdateComponentizedPreparedStatementBuilder;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Tests {

    @BeforeAll
    static void setUp() throws SQLException {
        SQLConnectionPoolInstance.create(Constants.MYSQL_URL, Keys.MYSQL_USER, Keys.MYSQL_PASS, 10);
    }

    @Test
    @DisplayName("Try creating a SELECT Prepared Statement")
    void testSelectPreparedStatement() throws InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();

        try {
            // Try complete Select PS
            ComponentizedPreparedStatement cps = SelectComponentizedPreparedStatementBuilder.forTable("Recipe").select("recipe_id").select("user_id").where("user_text", SQLOperators.EQUAL, 5).limit(5).orderBy(OrderByComponent.Direction.DESC, "date").build();

            PreparedStatement cpsPS = cps.connect(conn);
            System.out.println(cpsPS.toString());
            cpsPS.close();

            // Try minimal Select PS
            ComponentizedPreparedStatement selectCPSMinimal = SelectComponentizedPreparedStatementBuilder.forTable("Recipe").build();

            PreparedStatement selectCPSMinimalPS = selectCPSMinimal.connect(conn);
            System.out.println(selectCPSMinimalPS.toString());
            selectCPSMinimalPS.close();

            // Try partial Select PS
            ComponentizedPreparedStatement selectCPSPartial = SelectComponentizedPreparedStatementBuilder.forTable("IdeaRecipe").select("idea_id").where("user_id", SQLOperators.EQUAL, false).build();

            PreparedStatement selectCPSPartialPS = selectCPSPartial.connect(conn);
            System.out.println(selectCPSPartialPS.toString());
            selectCPSPartialPS.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    @Test
    @DisplayName("Try creating an INSERT INTO Prepared Statement")
    void testInsertIntoPreparedStatement() throws InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();

        try {
            // Build the insert componentized statement
            ComponentizedPreparedStatement insertCPSComplete = InsertIntoComponentizedPreparedStatementBuilder.forTable("IdeaRecipe").addColAndVal("idea_id", Types.NULL).addColAndVal("user_id", 5).addColAndVal("date", LocalDateTime.now()).build(true);

            System.out.println(insertCPSComplete);

            // Do update and get generated keys
            List<Map<String, Object>> generatedKeys = DBClient.updateReturnGeneratedKeys(conn, insertCPSComplete);

            System.out.println(generatedKeys);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    @Test
    @DisplayName("Try creating an UPDATE Prepared Statement")
    void testUpdatePreparedStatement() throws InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();

        try {
            ComponentizedPreparedStatement updatePSComplete = UpdateComponentizedPreparedStatementBuilder.forTable("IdeaRecipe").set("date", LocalDateTime.now()).where("user_id", SQLOperators.EQUAL, 5).build();

            DBClient.update(conn, updatePSComplete);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    @Test
    @DisplayName("HttpHelper Testing")
    void testBasicHttpRequest() {
        HttpClient client = HttpClient.newBuilder().version(HttpClient.Version.HTTP_1_1).connectTimeout(Duration.ofMinutes(Constants.AI_TIMEOUT_MINUTES)).build();
        OAIGPTChatCompletionRequestMessage promptMessageRequest = new OAIGPTChatCompletionRequestMessage(Role.USER, "write me a short joke");
        OAIGPTChatCompletionRequest promptRequest = new OAIGPTChatCompletionRequest("gpt-3.5-turbo", 100, 0.7, Arrays.asList(promptMessageRequest));
        Consumer<HttpRequest.Builder> c = requestBuilder -> {
            requestBuilder.setHeader("Authorization", "Bearer " + Keys.openAiAPI);
        };

        OpenAIGPTHttpsClientHelper httpHelper = new OpenAIGPTHttpsClientHelper();

        try {
            OAIGPTChatCompletionResponse response = httpHelper.postChatCompletion(promptRequest, Keys.openAiAPI);
            System.out.println(response.getChoices()[0].getMessage().getContent());

        } catch (OpenAIGPTException e) {
            System.out.println(e.getErrorObject().getError().getMessage());
        } catch (IOException e) {
             throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @DisplayName("Test Registering Transaction")
    void testTransactionValidation() throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, AutoIncrementingDBObjectExistsException, InterruptedException, InvocationTargetException, IllegalAccessException, AppStoreStatusResponseException, UnrecoverableKeyException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchMethodException, InstantiationException, PreparedStatementMissingArgumentException, AppleItunesResponseException {
        /* REGISTER TRANSACTION ENDPOINT */
        // Input
        final Long sampleTransactionId = 2000000351816446l;
        // Expected Results
        final AppStoreSubscriptionStatus expectedStatus = AppStoreSubscriptionStatus.EXPIRED;
        final Boolean expectedIsPremiumValue1 = false;

        // Register user
        BodyResponse registerUserBR = RegisterUserEndpoint.registerUser();
        AuthResponse aResponse = (AuthResponse)registerUserBR.getBody();

        // Get authToken
        String authToken = aResponse.getAuthToken();

        // Create register transaction request
        RegisterTransactionRequest rtr = new RegisterTransactionRequest(authToken, sampleTransactionId, null);

        // Register transaction
        BodyResponse registerTransactionBR = RegisterTransactionEndpoint.registerTransaction(rtr);
        IsPremiumResponse ipr1 = (IsPremiumResponse)registerTransactionBR.getBody();

        // Get User_AuthToken
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(authToken);

        // Verify transaction registered successfully
        Transaction transaction = TransactionDBManager.getMostRecent(u_aT.getUserID());
        assert(transaction != null);
        System.out.println(transaction.getAppstoreTransactionID() + " " + sampleTransactionId);
        assert(transaction.getAppstoreTransactionID().equals(sampleTransactionId));
//        assert(transaction.getStatus() == expectedStatus);

        // Verify registered transaction successfully got isPremium value
//        assert(ipr1.getIsPremium() == expectedIsPremiumValue1);

        /* IS PREMIUM ENDPOINT */
        // Expected Results
        final Boolean expectedIsPremiumValue2 = false;

        // Create authRequest
        AuthRequest aRequest = new AuthRequest(authToken);

        // Get Is Premium from endpoint
        BodyResponse isPremiumBR = GetIsPremiumEndpoint.getIsPremium(aRequest);
        IsPremiumResponse ipr2 = (IsPremiumResponse)isPremiumBR.getBody();

        // Verify results
//        assert(ipr2.getIsPremium() == expectedIsPremiumValue2);
    }

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

    @Test
    @DisplayName("Misc Modifyable")
    void misc() {
//        System.out.println("Here it is: " + Table.USER_AUTHTOKEN);
    }
}

package com.pantrypro.core.service.endpoints;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.oaigptconnector.model.request.chat.completion.function.OAIGPTChatCompletionRequestFunction;
import com.pantrypro.common.exceptions.CapReachedException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.PPGPTGenerator;
import com.pantrypro.model.http.client.apple.itunes.exception.AppStoreStatusResponseException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.client.openaigpt.request.builder.OAIGPTChatCompletionRequestFunctionCreateRecipeIdeaBuilder;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.server.request.CreateRecipeIdeaRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
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

public class CreateRecipeIdeaEndpoint {

    public static BodyResponse createRecipeIdea(CreateRecipeIdeaRequest createRecipeIdeaRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, OpenAIGPTException, IOException, AppStoreStatusResponseException, UnrecoverableKeyException, CapReachedException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
        // Generate from request
        return PPGPTGenerator.generatePackSaveCreateRecipeIdeaFunction(createRecipeIdeaRequest);
    }

}

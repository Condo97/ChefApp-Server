package com.pantrypro.core.service.endpoints;

import com.oaigptconnector.model.exception.OpenAIGPTException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.core.PPGPTGenerator;
import com.pantrypro.model.exceptions.CapReachedException;
import com.pantrypro.model.exceptions.GenerationException;
import com.pantrypro.model.exceptions.InvalidRequestJSONException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppStoreStatusResponseException;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
import com.pantrypro.model.http.server.request.RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest;
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

public class RegenerateRecipeDirectionsAndIdeaRecipeIngredientsEndpoint {

    public static BodyResponse regenerateRecipeDirectionsAndIdeaRecipeIngredients(RegenerateRecipeDirectionsAndIdeaRecipeIngredientsRequest request) throws AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CapReachedException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, InvalidRequestJSONException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, OpenAIGPTException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, GenerationException {
        return PPGPTGenerator.generatePackSaveRegenerateRecipeDirectionsAndIdeaRecipeIngredients(request);
    }

}

package com.pantrypro.database.calculators;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.pantrypro.Constants;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.PPPremiumValidator;
import com.pantrypro.core.RecipeCounter;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
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

public class RecipeRemainingCalculator extends RemainingCalculator {

    @Override
    protected Integer getCapFromPremium(boolean isPremium) {
        return isPremium ? Constants.Create_Recipe_Idea_Cap_Daily_Paid : Constants.Create_Recipe_Idea_Cap_Daily_Free;
    }

    @Override
    public Long calculateRemaining(String authToken) throws DBSerializerException, SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, DBObjectNotFoundFromQueryException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, AppStoreErrorResponseException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return calculateRemaining(authToken, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public Long calculateRemaining(String authToken, Connection conn) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, AppStoreErrorResponseException {
        // Get count of today's ideaRecipes
        Long count = RecipeCounter.countRecipes(authToken);

        // Get isPremium
        boolean isPremium = PPPremiumValidator.getIsPremium(authToken);

        // Get cap TODO: Creating a new object here just to access this value, is this okay?
        Integer cap = getCapFromPremium(isPremium);

        // Return null if cap is null
        if (cap == null)
            return null;

        return cap - count;
    }
}

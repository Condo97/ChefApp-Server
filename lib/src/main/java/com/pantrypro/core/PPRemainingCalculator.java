package com.pantrypro.core;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.common.exceptions.PreparedStatementMissingArgumentException;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import com.pantrypro.core.database.calculators.IdeaRecipeRemainingCalculator;
import com.pantrypro.core.purchase.iapvalidation.TransactionPersistentAppleUpdater;
import com.pantrypro.model.database.AppStoreSubscriptionStatusToIsPremiumAdapter;
import com.pantrypro.model.database.objects.Transaction;
import com.pantrypro.model.http.client.apple.itunes.exception.AppleItunesResponseException;
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

public class PPRemainingCalculator {

    public static Long getRemainingIdeaRecipes(Integer userID) throws AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CertificateException, DBObjectNotFoundFromQueryException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return getRemainingIdeaRecipes(userID, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static Long getRemainingIdeaRecipes(Integer userID, Connection conn) throws AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CertificateException, DBObjectNotFoundFromQueryException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
        // Get isPremium
        boolean isPremium = getIsPremium(userID, conn);

        // Count today's IdeaRecipes
        Long todaysIdeaRecipeCount = IdeaRecipeDBManager.countForToday(conn, userID);

        // Get remaining
        return new IdeaRecipeRemainingCalculator().calculateRemaining(todaysIdeaRecipeCount, isPremium);
    }

}

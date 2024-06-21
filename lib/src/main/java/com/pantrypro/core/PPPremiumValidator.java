package com.pantrypro.core;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.purchase.iapvalidation.TransactionPersistentAppleUpdater;
import com.pantrypro.database.adapters.IsPremiumFromAppStoreSubscriptionStatus;
import com.pantrypro.database.objects.transaction.Transaction;
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

public class PPPremiumValidator {

    public static boolean getIsPremium(String authToken) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, AppStoreErrorResponseException {
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(authToken);

        return getIsPremium(userID);
    }

    public static boolean getIsPremium(Integer userID) throws DBSerializerPrimaryKeyMissingException, SQLException, CertificateException, DBObjectNotFoundFromQueryException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException, AppStoreErrorResponseException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return getIsPremium(userID, conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    private static boolean getIsPremium(Integer userID, Connection conn) throws DBSerializerPrimaryKeyMissingException, SQLException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, InvalidKeySpecException, InstantiationException, PreparedStatementMissingArgumentException, AppleItunesResponseException, DBObjectNotFoundFromQueryException, AppStoreErrorResponseException {
        // Get most recent Apple updated and saved transaction with cooldown control
        Transaction transaction = TransactionPersistentAppleUpdater.getCooldownControlledAppleUpdatedMostRecentTransaction(userID);

        // Return isPremium using transaction
        return transaction != null && IsPremiumFromAppStoreSubscriptionStatus.getIsPremium(transaction.getStatus());
    }

}

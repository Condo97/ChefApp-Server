//package com.pantrypro.core;
//
//import appletransactionclient.exception.AppStoreStatusResponseException;
//import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
//import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
//import com.pantrypro.database.calculators.IdeaRecipeRemainingCalculator;
//import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
//import com.pantrypro.exceptions.PreparedStatementMissingArgumentException;
//import com.pantrypro.networking.client.apple.itunes.exception.AppleItunesResponseException;
//import sqlcomponentizer.dbserializer.DBSerializerException;
//import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
//
//import java.io.IOException;
//import java.lang.reflect.InvocationTargetException;
//import java.net.URISyntaxException;
//import java.security.KeyStoreException;
//import java.security.NoSuchAlgorithmException;
//import java.security.UnrecoverableKeyException;
//import java.security.cert.CertificateException;
//import java.security.spec.InvalidKeySpecException;
//import java.sql.Connection;
//import java.sql.SQLException;
//
//public class PPRemainingCalculator {
//
//    public static Long getRemainingIdeaRecipes(Integer userID) throws AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CertificateException, DBObjectNotFoundFromQueryException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            return getRemainingIdeaRecipes(userID, conn);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static Long getRemainingIdeaRecipes(Integer userID, Connection conn) throws AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, SQLException, CertificateException, DBObjectNotFoundFromQueryException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, UnrecoverableKeyException, DBSerializerException, PreparedStatementMissingArgumentException, AppleItunesResponseException, InvalidKeySpecException, InstantiationException {
//        // Get isPremium
//        boolean isPremium = PPPremiumValidator.getIsPremium(userID);
//
//        // Count today's IdeaRecipes
//        Long todaysIdeaRecipeCount = IdeaRecipeDBManager.countForToday(conn, userID);
//
//        // Get remaining
//        return new IdeaRecipeRemainingCalculator().calculateRemaining(todaysIdeaRecipeCount, isPremium);
//    }
//
//}

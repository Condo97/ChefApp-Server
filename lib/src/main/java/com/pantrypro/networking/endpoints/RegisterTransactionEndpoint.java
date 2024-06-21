package com.pantrypro.networking.endpoints;

import appletransactionclient.exception.AppStoreErrorResponseException;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.core.purchase.iapvalidation.TransactionPersistentAppleUpdater;
import com.pantrypro.database.adapters.IsPremiumFromAppStoreSubscriptionStatus;
import com.pantrypro.database.objects.transaction.Transaction;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.networking.responsefactories.BodyResponseFactory;
import com.pantrypro.networking.server.request.RegisterTransactionRequest;
import com.pantrypro.networking.server.response.BodyResponse;
import com.pantrypro.networking.server.response.IsPremiumResponse;
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

public class RegisterTransactionEndpoint {

    public static BodyResponse registerTransaction(RegisterTransactionRequest registerTransactionRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, UnrecoverableKeyException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerPrimaryKeyMissingException, AppStoreErrorResponseException {
        // Get the userID from authToken using UserAuthenticator
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(registerTransactionRequest.getAuthToken());
//        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(registerTransactionRequest.getAuthToken());

        // Create transaction with now record date
        Transaction transaction = Transaction.withNowRecordDate(userID, registerTransactionRequest.getTransactionId());
        
        // Update transaction with Apple status
        TransactionPersistentAppleUpdater.updateAndSaveAppleTransactionStatus(transaction);

        // Get isPremium
        boolean isPremium = IsPremiumFromAppStoreSubscriptionStatus.getIsPremium(transaction.getStatus());

                // TODO: Just logging to see things, remove and make a better logging system!
                if (isPremium == true)
                    System.out.println("User " + userID + " just registered a transaction at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));

        // Create full validate premium response
        IsPremiumResponse fvpr = new IsPremiumResponse(isPremium);

        // Create and return successful body response with full validate premium response
        return BodyResponseFactory.createSuccessBodyResponse(fvpr);
    }

}

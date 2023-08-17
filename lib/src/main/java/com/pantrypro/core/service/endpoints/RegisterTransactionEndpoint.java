package com.pantrypro.core.service.endpoints;

import appletransactionclient.exception.AppStoreStatusResponseException;
import com.pantrypro.common.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.core.purchase.iapvalidation.TransactionPersistentAppleUpdater;
import com.pantrypro.core.database.managers.User_AuthTokenDBManager;
import com.pantrypro.core.service.BodyResponseFactory;
import com.pantrypro.model.database.AppStoreSubscriptionStatusToIsPremiumAdapter;
import com.pantrypro.model.database.objects.Transaction;
import com.pantrypro.model.database.objects.User_AuthToken;
import com.pantrypro.model.http.server.request.RegisterTransactionRequest;
import com.pantrypro.model.http.server.response.BodyResponse;
import com.pantrypro.model.http.server.response.IsPremiumResponse;
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

    public static BodyResponse registerTransaction(RegisterTransactionRequest registerTransactionRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, UnrecoverableKeyException, CertificateException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException, DBSerializerPrimaryKeyMissingException, AppStoreStatusResponseException {
        // Get the user_authToken object to get the user id
        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(registerTransactionRequest.getAuthToken());

        // Create transaction with now record date
        Transaction transaction = Transaction.withNowRecordDate(u_aT.getUserID(), registerTransactionRequest.getTransactionId());
        
        // Update transaction with Apple status
        TransactionPersistentAppleUpdater.updateAndSaveAppleTransactionStatus(transaction);

        // Get isPremium
        boolean isPremium = AppStoreSubscriptionStatusToIsPremiumAdapter.getIsPremium(transaction.getStatus());

                // TODO: Just logging to see things, remove and make a better logging system!
                if (isPremium == true)
                    System.out.println("User " + u_aT.getUserID() + " just registered a transaction at " + new SimpleDateFormat("HH:mm:ss").format(new Date()));

        // Create full validate premium response
        IsPremiumResponse fvpr = new IsPremiumResponse(isPremium);

        // Create and return successful body response with full validate premium response
        return BodyResponseFactory.createSuccessBodyResponse(fvpr);
    }

}

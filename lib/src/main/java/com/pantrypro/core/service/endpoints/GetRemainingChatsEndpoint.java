//package core.service.endpoints;
//
//import com.writesmith.common.exceptions.DBObjectNotFoundFromQueryException;
//import com.writesmith.common.exceptions.PreparedStatementMissingArgumentException;
//import com.writesmith.core.WSPremiumValidator;
//import com.writesmith.core.generation.calculators.ChatRemainingCalculator;
//import com.writesmith.core.service.BodyResponseFactory;
//import com.writesmith.database.ws.managers.User_AuthTokenDBManager;
//import com.writesmith.model.database.objects.User_AuthToken;
//import com.writesmith.model.http.client.apple.itunes.exception.AppStoreStatusResponseException;
//import com.writesmith.model.http.client.apple.itunes.exception.AppleItunesResponseException;
//import com.writesmith.model.http.server.request.AuthRequest;
//import com.writesmith.model.http.server.response.BodyResponse;
//import com.writesmith.model.http.server.response.GetRemainingResponse;
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
//import java.sql.SQLException;
//
//public class GetRemainingChatsEndpoint {
//
//    public static BodyResponse getRemaining(AuthRequest authRequest) throws DBSerializerException, SQLException, DBObjectNotFoundFromQueryException, InterruptedException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, InstantiationException, AppStoreStatusResponseException, DBSerializerPrimaryKeyMissingException, UnrecoverableKeyException, CertificateException, PreparedStatementMissingArgumentException, AppleItunesResponseException, IOException, URISyntaxException, KeyStoreException, NoSuchAlgorithmException, InvalidKeySpecException {
//        // Get u_aT from authRequest
//        User_AuthToken u_aT = User_AuthTokenDBManager.getFromDB(authRequest.getAuthToken());
//
//        // Get isPremium
//        boolean isPremium = WSPremiumValidator.getIsPremium(u_aT.getUserID());
//
//        // Get remaining from userID and if null set to -1
//        Long remaining = ChatRemainingCalculator.calculateRemaining(u_aT.getUserID(), isPremium);
//        if (remaining == null)
//            remaining = -1l;
//
//        // Build getRemainingResponse
//        GetRemainingResponse getRemainingResponse = new GetRemainingResponse(remaining);
//
//        // Build and return success body response with getRemainingResponse
//        return BodyResponseFactory.createSuccessBodyResponse(getRemainingResponse);
//
//    }
//
//}

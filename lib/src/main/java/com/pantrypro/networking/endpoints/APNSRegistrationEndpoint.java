package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.core.UserAuthenticator;
import com.pantrypro.database.dao.pooled.APNSRegistrationDAOPooled;
import com.pantrypro.database.dao.pooled.User_AuthTokenDAOPooled;
import com.pantrypro.database.objects.APNSRegistration;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.exceptions.DBObjectNotFoundFromQueryException;
import com.pantrypro.networking.responsefactories.StatusResponseFactory;
import com.pantrypro.networking.server.request.APNSRegistrationRequest;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class APNSRegistrationEndpoint implements Endpoint<APNSRegistrationRequest> {

    @Override
    public Object getResponse(APNSRegistrationRequest request) throws SQLException, DBSerializerException, DBObjectNotFoundFromQueryException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException, DBSerializerPrimaryKeyMissingException {
        // Get u_aT from request
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(request.getAuthToken());

        // Update APNS TODO: Move to a different class lol
        updateOrInsertAPNSRegistration(u_aT.getUserID(), request.getDeviceID());

        // Return Success StatusResponse
        return StatusResponseFactory.createSuccessStatusResponse();
    }

    private void updateOrInsertAPNSRegistration(Integer userID, String deviceID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, DBSerializerPrimaryKeyMissingException {
        // Get latest APNS Registration for userID.. if it exists update it, otherwise create one
        APNSRegistration currentRegistration = APNSRegistrationDAOPooled.getLatestUpdateDateByUserID(userID);

        if (currentRegistration != null) {
            // Update deviceID and updateDate locally and update in DB
            currentRegistration.setDeviceID(deviceID);
            currentRegistration.setUpdateDate(LocalDateTime.now());

            APNSRegistrationDAOPooled.updateDeviceID(currentRegistration);
            APNSRegistrationDAOPooled.updateUpdateDate(currentRegistration);
        } else {
            // Create locally and in DB TODO: This should be moved to a factory method I think lol
            APNSRegistration registration = new APNSRegistration(
                    null,
                    userID,
                    deviceID,
                    LocalDateTime.now(),
                    LocalDateTime.now()
            );

            APNSRegistrationDAOPooled.insert(registration);
        }
    }

}

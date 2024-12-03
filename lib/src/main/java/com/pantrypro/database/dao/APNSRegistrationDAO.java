package com.pantrypro.database.dao;

import com.dbclient.DBManager;
import com.pantrypro.DBRegistry;
import com.pantrypro.database.objects.APNSRegistration;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
import sqlcomponentizer.preparedstatement.component.PSComponent;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperatorCondition;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class APNSRegistrationDAO {

    public static void delete(Connection conn, Integer id) throws DBSerializerException, SQLException, InterruptedException {
        DBManager.deleteWhere(
                conn,
                APNSRegistration.class,
                DBRegistry.Table.APNSRegistration.id,
                SQLOperators.EQUAL,
                id
        );
    }

    public static List<APNSRegistration> getAll(Connection conn) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return DBManager.selectAll(conn, APNSRegistration.class);
    }

    public static APNSRegistration getLatestUpdateDateByUserID(Connection conn, Integer userID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Create SQL Conditions for latest update date by user ID
        List<PSComponent> sqlConditions = List.of(
                new SQLOperatorCondition(
                        DBRegistry.Table.APNSRegistration.user_id,
                        SQLOperators.EQUAL,
                        userID
                ),
                new SQLOperatorCondition(
                        DBRegistry.Table.APNSRegistration.update_date,
                        SQLOperators.LESS_THAN,
                        LocalDateTime.now()
                )
        );

        // Get all latest update date by user ID APNSRegistrations
        List<APNSRegistration> allLatestUpdateDateByUserID = DBManager.selectAllWhere(
                conn,
                APNSRegistration.class,
                sqlConditions
        );

        // If there is at least one object, return the first
        if(allLatestUpdateDateByUserID.size() > 0)
            return allLatestUpdateDateByUserID.get(0);

        // If there are no objects, return null
        return null;
    }

    public static void insert(Connection conn, APNSRegistration apnsRegistration) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        DBManager.insert(conn, apnsRegistration);
    }

    public static void updateDeviceID(Connection conn, APNSRegistration apnsRegistration) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, IllegalAccessException {
        // TODO: Just fyi, this can be simplified one more time by getting the value for the field for toUpdateCol as newVal
        DBManager.updateWhereByPrimaryKey(
                conn,
                apnsRegistration,
                DBRegistry.Table.APNSRegistration.device_id,
                apnsRegistration.getDeviceID()
        );
    }

    public static void updateUpdateDate(Connection conn, APNSRegistration apnsRegistration) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, IllegalAccessException {
        // TODO: Just fyi, this can be simplified one more time by getting the value for the field for toUpdateCol as newVal
        DBManager.updateWhereByPrimaryKey(
                conn,
                apnsRegistration,
                DBRegistry.Table.APNSRegistration.update_date,
                apnsRegistration.getUpdateDate()
        );
    }

}

package com.pantrypro.database.dao.pooled;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.database.dao.APNSRegistrationDAO;
import com.pantrypro.database.objects.APNSRegistration;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class APNSRegistrationDAOPooled {

    public static void delete(Integer id) throws InterruptedException, DBSerializerException, SQLException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            APNSRegistrationDAO.delete(conn, id);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static List<APNSRegistration> getAll() throws SQLException, InterruptedException, DBSerializerException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return APNSRegistrationDAO.getAll(conn);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static APNSRegistration getLatestUpdateDateByUserID(Integer userID) throws SQLException, InterruptedException, DBSerializerException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return APNSRegistrationDAO.getLatestUpdateDateByUserID(conn, userID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void insert(APNSRegistration apnsRegistration) throws InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InvocationTargetException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            APNSRegistrationDAO.insert(conn, apnsRegistration);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateDeviceID(APNSRegistration apnsRegistration) throws InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            APNSRegistrationDAO.updateDeviceID(conn, apnsRegistration);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateUpdateDate(APNSRegistration apnsRegistration) throws InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            APNSRegistrationDAO.updateUpdateDate(conn, apnsRegistration);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}

package com.pantrypro.database.objects;

import com.pantrypro.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

import java.time.LocalDateTime;

@DBSerializable(tableName = DBRegistry.Table.User_AuthToken.TABLE_NAME)
public class User_AuthToken {

    @DBColumn(name = DBRegistry.Table.User_AuthToken.user_id, primaryKey = true)
    private Integer userID;

    @DBColumn(name = DBRegistry.Table.User_AuthToken.auth_token)
    private String authToken;

    @DBColumn(name = DBRegistry.Table.User_AuthToken.expiry_date)
    private LocalDateTime expiryDate;

    @DBColumn(name = DBRegistry.Table.User_AuthToken.hashed_token)
    private String hashedToken;

    public User_AuthToken() {

    }

    public User_AuthToken(Integer userID, String authToken) {
        this.userID = userID;
        this.authToken = authToken;
    }

    public User_AuthToken(Integer userID, String authToken, LocalDateTime expiryDate, String hashedToken) {
        this.userID = userID;
        this.authToken = authToken;
        this.expiryDate = expiryDate;
        this.hashedToken = hashedToken;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer id) {
        this.userID = id;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getHashedToken() {
        return hashedToken;
    }

    public void setHashedToken(String hashedToken) {
        this.hashedToken = hashedToken;
    }
}

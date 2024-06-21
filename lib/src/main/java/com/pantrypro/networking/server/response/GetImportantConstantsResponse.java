package com.pantrypro.networking.server.response;


import com.pantrypro.Constants;
import com.pantrypro.keys.EncryptionManager;

public class GetImportantConstantsResponse {

    private final String bingAPIKey = EncryptionManager.getEncryptedBingAPIKey();

    private final String weeklyProductID = Constants.WEEKLY_NAME;
    private final String monthlyProductID = Constants.MONTHLY_NAME;

    private final String shareURL = Constants.SHARE_URL;


    // DEPRECATED
    private final String weeklyDisplayPrice = Constants.WEEKLY_PRICE;
    private final String monthlyDisplayPrice = Constants.MONTHLY_PRICE;
    private final String annualDisplayPrice = Constants.YEARLY_PRICE;


    public GetImportantConstantsResponse() {

    }


    public String getBingAPIKey() {
        return bingAPIKey;
    }

    public String getWeeklyProductID() {
        return weeklyProductID;
    }

    public String getMonthlyProductID() {
        return monthlyProductID;
    }

    public String getShareURL() {
        return shareURL;
    }

    // DEPRECATED

    public String getWeeklyDisplayPrice() {
        return weeklyDisplayPrice;
    }

    public String getMonthlyDisplayPrice() {
        return monthlyDisplayPrice;
    }

    public String getAnnualDisplayPrice() {
        return annualDisplayPrice;
    }

}

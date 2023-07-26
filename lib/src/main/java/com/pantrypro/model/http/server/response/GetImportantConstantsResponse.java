package com.pantrypro.model.http.server.response;


import com.pantrypro.Constants;
import com.pantrypro.keys.EncryptionManager;

public class GetImportantConstantsResponse {

    private final String bingAPIKey = EncryptionManager.getEncryptedBingAPIKey();
    private final String weeklyDisplayPrice = Constants.WEEKLY_PRICE;
    private final String monthlyDisplayPrice = Constants.MONTHLY_PRICE;
    private final String annualDisplayPrice = Constants.YEARLY_PRICE;
    private final String shareURL = Constants.SHARE_URL;


    public GetImportantConstantsResponse() {

    }


    public String getBingAPIKey() {
        return bingAPIKey;
    }

    public String getWeeklyDisplayPrice() {
        return weeklyDisplayPrice;
    }

    public String getMonthlyDisplayPrice() {
        return monthlyDisplayPrice;
    }

    public String getAnnualDisplayPrice() {
        return annualDisplayPrice;
    }

    public String getShareURL() {
        return shareURL;
    }

}

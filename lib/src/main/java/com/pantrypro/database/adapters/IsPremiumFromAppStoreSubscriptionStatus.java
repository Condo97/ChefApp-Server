package com.pantrypro.database.adapters;

import com.pantrypro.database.objects.transaction.AppStoreSubscriptionStatus;

import java.util.List;

public class IsPremiumFromAppStoreSubscriptionStatus {

    private static List<AppStoreSubscriptionStatus> premiumStatuses = List.of(
            AppStoreSubscriptionStatus.ACTIVE,
            AppStoreSubscriptionStatus.BILLING_GRACE
    );

    public static Boolean getIsPremium(AppStoreSubscriptionStatus subscriptionStatus) {
        // If premiumStatuses contains subscriptionStatus, return true because user is premium otherwise return false
        if (premiumStatuses.contains(subscriptionStatus))
            return true;

        return false;
    }

}

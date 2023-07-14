//package com.pantrypro.core.generation.calculators;
//
//import com.pantrypro.Constants;
//import com.pantrypro.common.IntegerFromBoolean;
//import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
//import sqlcomponentizer.dbserializer.DBSerializerException;
//
//import java.sql.SQLException;
//
//public class MakeRecipeRemainingCalculator {
//
//    public static IntegerFromBoolean getCapFromPremium = t -> t ? Constants.Make_Recipe_Cap_Daily_Paid : Constants.Make_Recipe_Cap_Daily_Free;
//
//    public static Long calculateRemaining(Integer userID, boolean isPremium) throws DBSerializerException, SQLException, InterruptedException {
//        // Get count of today's ideaRecipe TODO: Should this return the count of the recipes? Should there even be a check for recipes?
//        Long count = IdeaRecipeDBManager.countForToday(userID);
//
//        // Get cap
//        Integer cap = getCapFromPremium.getInt(isPremium);
//
//        // Return null if cap is null
//        if (cap == null)
//            return null;
//
//        return cap - count;
//    }
//
//}

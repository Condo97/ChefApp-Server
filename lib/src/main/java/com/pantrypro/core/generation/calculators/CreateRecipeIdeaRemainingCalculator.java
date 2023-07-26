package com.pantrypro.core.generation.calculators;

import com.pantrypro.Constants;
import com.pantrypro.common.IntegerFromBoolean;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.sql.SQLException;

public class CreateRecipeIdeaRemainingCalculator extends RemainingCalculator {

    @Override
    protected Integer getCapFromPremium(boolean isPremium) {
        return isPremium ? Constants.Create_Recipe_Idea_Cap_Daily_Paid : Constants.Create_Recipe_Idea_Cap_Daily_Free;
    }

    @Override
    public Long calculateRemaining(Integer userID, boolean isPremium) throws DBSerializerException, SQLException, InterruptedException {
        // Get count of today's ideaRecipe
        Long count = IdeaRecipeDBManager.countForToday(userID);

        // Get cap TODO: Creating a new object here just to access this value, is this okay?
        Integer cap = getCapFromPremium(isPremium);

        // Return null if cap is null
        if (cap == null)
            return null;

        return cap - count;
    }
}

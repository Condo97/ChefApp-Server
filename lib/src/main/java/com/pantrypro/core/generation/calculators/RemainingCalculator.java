package com.pantrypro.core.generation.calculators;

import com.pantrypro.common.IntegerFromBoolean;
import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
import sqlcomponentizer.dbserializer.DBSerializerException;

import java.sql.SQLException;

public abstract class RemainingCalculator {

    protected abstract Integer getCapFromPremium(boolean isPremium);

    public abstract Long calculateRemaining(Integer userID, boolean isPremium) throws DBSerializerException, SQLException, InterruptedException;

}

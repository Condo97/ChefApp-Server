package com.pantrypro.core.database.managers;

import com.pantrypro.core.database.DBManager;
import com.pantrypro.model.database.DBRegistry;
import com.pantrypro.model.database.objects.IdeaRecipe;
import com.pantrypro.model.database.objects.IdeaRecipeEquipment;
import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
import com.pantrypro.model.database.objects.IdeaRecipeTag;
import sqlcomponentizer.DBClient;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
import sqlcomponentizer.preparedstatement.component.PSComponent;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperatorCondition;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class IdeaRecipeDBManager extends DBManager {

    public static void insertIdeaRecipe(IdeaRecipe ideaRecipe, List<IdeaRecipeIngredient> ingredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        insert(ideaRecipe);

        for (IdeaRecipeIngredient ingredient: ingredients) {
            ingredient.setIdeaID(ideaRecipe.getId());
            insert(ingredient);
        }

//        for (IdeaRecipeEquipment equipmentObject: equipment) {
//            equipmentObject.setIdeaID(ideaRecipe.getId());
//            insert(equipmentObject);
//        }
    }

    public static void insertIdeaRecipeTags(List<IdeaRecipeTag> tags) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        for (IdeaRecipeTag tag: tags) {
            insert(tag);
        }
    }

    public static Long countForToday(Integer userID) throws DBSerializerException, SQLException, InterruptedException {
        // Get from date as yesterday's date to count all ideaRecipes after it
        LocalDateTime fromDate = LocalDateTime.now().minus(Duration.ofDays(1));

        // Build SQL Conditions
        SQLOperatorCondition userIDCondition = new SQLOperatorCondition(
                DBRegistry.Table.IdeaRecipe.user_id,
                SQLOperators.EQUAL,
                userID
        );
        SQLOperatorCondition dateCondition = new SQLOperatorCondition(
                DBRegistry.Table.IdeaRecipe.date,
                SQLOperators.GREATER_THAN,
                fromDate
        );
        List<PSComponent> sqlConditions = List.of(
                userIDCondition,
                dateCondition
        );

        // Count where userID
        return countObjectWhere(
                IdeaRecipe.class,
                sqlConditions
        );
    }

    public static IdeaRecipe get(Integer ideaID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get all recipe ideas for idea ID
        List<IdeaRecipe> allRecipesForID = selectAllByPrimaryKey(IdeaRecipe.class, ideaID);

        // if there is at least one object, return the first
        if (allRecipesForID.size() > 0)
            return allRecipesForID.get(0);

        // If there are no objects, return null
        return null;
    }

    public static List<IdeaRecipeIngredient> getIngredients(Integer ideaID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        // Get all recipe idea ingredients for idea ID
        List<IdeaRecipeIngredient> allIngredientsForID = selectAllWhere(
                IdeaRecipeIngredient.class,
                DBRegistry.Table.IdeaRecipeIngredient.idea_id,
                SQLOperators.EQUAL,
                ideaID
        );

        return allIngredientsForID;
    }



}

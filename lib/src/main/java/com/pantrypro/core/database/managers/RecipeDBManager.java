package com.pantrypro.core.database.managers;

import com.pantrypro.core.database.DBManager;
import com.pantrypro.model.database.DBRegistry;
import com.pantrypro.model.database.objects.IdeaRecipe;
import com.pantrypro.model.database.objects.Recipe;
import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.database.objects.RecipeMeasuredIngredient;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class RecipeDBManager extends DBManager {

    public static void insertRecipe(Recipe recipe, List<RecipeInstruction> instructions, List<RecipeMeasuredIngredient> measuredIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        insert(recipe);

        for (RecipeInstruction instruction: instructions) {
            instruction.setRecipeID(recipe.getId());
            insert(instruction);
        }

        for (RecipeMeasuredIngredient measuredIngredient: measuredIngredients) {
            measuredIngredient.setRecipeID(recipe.getId());
            insert(measuredIngredient);
        }
    }

//    public static Integer countForToday(Integer userID) throws DBSerializerException, SQLException, InterruptedException {
//        // Get from date as yesterday's date to count all ideaRecipes after it
//        LocalDateTime fromDate = LocalDateTime.now().minus(Duration.ofDays(1));
//
//        // Count where userID
//        return countObjectByColumnWhere(
//                Recipe.class,
//                DBRegistry.Table.Recipe.recipe_id,
//                DBRegistry.Table.Recipe.date,
//                SQLOperators.GREATER_THAN,
//                fromDate
//        );
//    }

}

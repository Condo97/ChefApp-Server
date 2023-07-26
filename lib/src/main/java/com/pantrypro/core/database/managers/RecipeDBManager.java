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

    public static void insertRecipeInstructions(List<RecipeInstruction> instructions) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        for (RecipeInstruction instruction: instructions)
            insert(instruction);
    }

    public static void insertRecipeMeasuredIngredients(List<RecipeMeasuredIngredient> measuredIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        for (RecipeMeasuredIngredient measuredIngredient: measuredIngredients)
            insert(measuredIngredient);
    }

    public static void deleteAllInstructions(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException {
        deleteWhere(
                RecipeInstruction.class,
                DBRegistry.Table.RecipeInstruction.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );
    }

    public static void deleteAllMeasuredIngredients(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException {
        deleteWhere(
                RecipeMeasuredIngredient.class,
                DBRegistry.Table.RecipeMeasuredIngredient.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );
    }

    public static Recipe getFromIdeaID(Integer ideaID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Get all recipes for ideaID, and if there is at least one, return the first
        List<Recipe> allRecipesForID = selectAllWhere(
                Recipe.class,
                DBRegistry.Table.Recipe.idea_id,
                SQLOperators.EQUAL,
                ideaID
        );

        if (allRecipesForID.size() > 0)
            return allRecipesForID.get(0);

        return null;
    }

    public static List<RecipeMeasuredIngredient> getMeasuredIngredients(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // TODO: This can be an inner join from ideaID - SELECT * FROM pantrypro_schema.Recipe INNER JOIN pantrypro_schema.RecipeMeasuredIngredient ON pantrypro_schema.Recipe.recipe_id = pantrypro_schema.RecipeMeasuredIngredient.recipe_id WHERE idea_id = 72226; Maybe how this will work is that instead of SELECT * it uses SELECT fromObjectField1, fromObjectField2,...,fromObjectFieldn
        // Get all recipe measured ingredients for recipeID
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = selectAllWhere(
                RecipeMeasuredIngredient.class,
                DBRegistry.Table.RecipeMeasuredIngredient.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );

        return recipeMeasuredIngredients;
    }

    public static void updateFeasibility(Integer recipeID, Integer feasibility) throws DBSerializerException, SQLException, InterruptedException {
        updateWhere(
                Recipe.class,
                DBRegistry.Table.Recipe.feasibility,
                feasibility,
                DBRegistry.Table.Recipe.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );
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

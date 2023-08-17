package com.pantrypro.core.database.managers;

import com.dbclient.DBManager;
import com.pantrypro.model.database.DBRegistry;
import com.pantrypro.model.database.objects.Recipe;
import com.pantrypro.model.database.objects.RecipeInstruction;
import com.pantrypro.model.database.objects.RecipeMeasuredIngredient;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class RecipeDBManager {

    public static void insertRecipe(Connection conn, Recipe recipe, List<RecipeInstruction> instructions, List<RecipeMeasuredIngredient> measuredIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        DBManager.insert(conn, recipe);

        for (RecipeInstruction instruction: instructions) {
            instruction.setRecipeID(recipe.getId());
            DBManager.insert(conn, instruction);
        }

        for (RecipeMeasuredIngredient measuredIngredient: measuredIngredients) {
            measuredIngredient.setRecipeID(recipe.getId());
            DBManager.insert(conn, measuredIngredient);
        }
    }

//    public static void insertRecipeInstructions(Connection conn, List<RecipeInstruction> instructions) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        for (RecipeInstruction instruction: instructions)
//            DBManager.insert(conn, instruction);
//    }
//
//    public static void insertRecipeMeasuredIngredients(Connection conn, List<RecipeMeasuredIngredient> measuredIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        for (RecipeMeasuredIngredient measuredIngredient: measuredIngredients)
//            DBManager.insert(conn, measuredIngredient);
//    }

    public static Recipe getFromIdeaID(Connection conn, Integer ideaID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // Get all recipes for ideaID, and if there is at least one, return the first
        List<Recipe> allRecipesForID = DBManager.selectAllWhere(
                conn,
                Recipe.class,
                DBRegistry.Table.Recipe.idea_id,
                SQLOperators.EQUAL,
                ideaID
        );

        if (allRecipesForID.size() > 0)
            return allRecipesForID.get(0);

        return null;
    }

    public static List<RecipeMeasuredIngredient> getMeasuredIngredients(Connection conn, Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        // TODO: This can be an inner join from ideaID - SELECT * FROM pantrypro_schema.Recipe INNER JOIN pantrypro_schema.RecipeMeasuredIngredient ON pantrypro_schema.Recipe.recipe_id = pantrypro_schema.RecipeMeasuredIngredient.recipe_id WHERE idea_id = 72226; Maybe how this will work is that instead of SELECT * it uses SELECT fromObjectField1, fromObjectField2,...,fromObjectFieldn
        // Get all recipe measured ingredients for recipeID
        List<RecipeMeasuredIngredient> recipeMeasuredIngredients = DBManager.selectAllWhere(
                conn,
                RecipeMeasuredIngredient.class,
                DBRegistry.Table.RecipeMeasuredIngredient.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );

        return recipeMeasuredIngredients;
    }

    public static void updateFeasibility(Connection conn, Integer recipeID, Integer feasibility) throws DBSerializerException, SQLException, InterruptedException {
        DBManager.updateWhere(
                conn,
                Recipe.class,
                DBRegistry.Table.Recipe.feasibility,
                feasibility,
                DBRegistry.Table.Recipe.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );
    }

    public static void updateDirections(Connection conn, Integer recipeID, List<RecipeInstruction> recipeDirections) throws DBSerializerException, SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, InvocationTargetException, IllegalAccessException {
        // Delete all instructions for recipeID
        deleteAllInstructions(conn, recipeID);

        // Insert new instructions using recipeID
        for (RecipeInstruction recipeDirection: recipeDirections) {
            // Set recipeID as the one in the function parameter as the expected functionality is to associate it with this recipeDirection and in the current use case the recipeID should be null at this point anyways
            recipeDirection.setRecipeID(recipeID);
            DBManager.insert(conn, recipeDirection);
        }
    }

    public static void updateMeasuredIngredients(Connection conn, Integer recipeID, List<RecipeMeasuredIngredient> measuredIngredients) throws DBSerializerException, SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, InvocationTargetException, IllegalAccessException {
        // Delete all measured ingredients for recipeID
        deleteAllMeasuredIngredients(conn, recipeID);

        // Insert new measured ingredients using recipeID
        for (RecipeMeasuredIngredient measuredIngredient: measuredIngredients) {
            // Set recipeID as the one in the function parameter as the expected functionality is to associate it with this measuredIngredient and in the current use case the recipeID should be null at this point anyways
            measuredIngredient.setRecipeID(recipeID);
            DBManager.insert(conn, measuredIngredient);
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

    private static void deleteAllInstructions(Connection conn, Integer recipeID) throws DBSerializerException, SQLException, InterruptedException {
        DBManager.deleteWhere(
                conn,
                RecipeInstruction.class,
                DBRegistry.Table.RecipeInstruction.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );
    }

    private static void deleteAllMeasuredIngredients(Connection conn, Integer recipeID) throws DBSerializerException, SQLException, InterruptedException {
        DBManager.deleteWhere(
                conn,
                RecipeMeasuredIngredient.class,
                DBRegistry.Table.RecipeMeasuredIngredient.recipe_id,
                SQLOperators.EQUAL,
                recipeID
        );
    }

}

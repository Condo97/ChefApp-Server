package com.pantrypro.database.dao.factory;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.generation.tagging.TagFilterer;
import com.pantrypro.database.compoundobjects.RecipeWithIngredientsAndDirections;
import com.pantrypro.database.dao.RecipeDAO;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.database.objects.recipe.RecipeTag;
import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaSO;
import com.pantrypro.networking.client.oaifunctioncall.finalizerecipe.FinalizeRecipeSO;
import com.pantrypro.networking.client.oaifunctioncall.generatedirections.GenerateMeasuredIngredientsAndDirectionsSO;
import com.pantrypro.networking.client.oaifunctioncall.tagrecipe.TagRecipeSO;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecipeFactoryDAO {

    public static RecipeWithIngredientsAndDirections createAndSaveRecipe(CreateRecipeIdeaSO createRecipeIdeaSO, Integer userID, String input, String cuisineType, Integer expandIngredientsMagnitude) throws InterruptedException, SQLException, InvocationTargetException, IllegalAccessException, DBSerializerPrimaryKeyMissingException, DBSerializerException {
        // Create Recipe
        Recipe recipe = new Recipe(
                null,
                userID,
                input,
                createRecipeIdeaSO.getName(),
                createRecipeIdeaSO.getSummary(),
                cuisineType,
                expandIngredientsMagnitude,
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                0,
                0
        );

        Connection c = SQLConnectionPoolInstance.getConnection();
        try {
            // Save Recipe
            RecipeDAO.insert(c, recipe);
//            DBManager.insert(c, recipe);

            List<RecipeMeasuredIngredient> recipeMeasuredIngredients = new ArrayList<>();

            // Loop through ingredients from createRecipeIdeaFC
            for (String ingredientName: createRecipeIdeaSO.getIngredients()) {
                // Get RecipeMeasuredIngredient
                RecipeMeasuredIngredient measuredIngredient = new RecipeMeasuredIngredient(
                        null,
                        recipe.getRecipe_id(),
                        ingredientName
                );

                // Save Recipe Measured Ingredient
                RecipeDAO.insertMeasuredIngredient(c, measuredIngredient);

                // Add measuredIngredient to recipeMeasuredIngredients
                recipeMeasuredIngredients.add(measuredIngredient);
            }

            return new RecipeWithIngredientsAndDirections(recipe, recipeMeasuredIngredients);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(c);
        }
    }

    public static RecipeWithIngredientsAndDirections createDuplicatedRecipe(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients, List<RecipeInstruction> instructions, Integer userID) throws SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, InvocationTargetException, IllegalAccessException {
        // Set recipeID to null
        recipe.setRecipe_id(null);

        // Set recipe userID to userID
        recipe.setUser_id(userID);

        // Get connection
        Connection c = SQLConnectionPoolInstance.getConnection();

        // Save recipe
        RecipeDAO.insert(c, recipe);

        // Set measuredIngredients and directions recipeID and save
        for (RecipeMeasuredIngredient measuredIngredient: measuredIngredients) {
            measuredIngredient.setIngredientID(null);
            measuredIngredient.setRecipeID(recipe.getRecipe_id());
            RecipeDAO.insertMeasuredIngredient(c, measuredIngredient);
        }
        for (RecipeInstruction instruction: instructions) {
            instruction.setID(null);
            instruction.setRecipeID(recipe.getRecipe_id());
            RecipeDAO.insertDirection(c, instruction);
        }

        return new RecipeWithIngredientsAndDirections(
                recipe,
                measuredIngredients,
                instructions
        );
    }

    public static List<RecipeTag> createAndSaveRecipeTags(TagRecipeSO tagRecipeSO, Integer recipeID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        Connection c = SQLConnectionPoolInstance.getConnection();
        try {
            // Create, adapt to, and save RecipeTag list
            List<RecipeTag> recipeTags = new ArrayList<>();
            for (String tag : tagRecipeSO.getTags()) {
                // Create RecipeTag with lowercase tag
                RecipeTag recipeTag = RecipeTag.withLowercaseTag(
                        null,
                        recipeID,
                        tag
                );

                // Use TagFilterer to ensure tag is valid, otherwise continue
                if (!TagFilterer.isTagValid(recipeTag))
                    continue;

                // Save RecipeTag
                RecipeDAO.insertTag(c, recipeTag);

                // Add recipeTag to recipeTags
                recipeTags.add(recipeTag);
            }

            // Return recipeTags
            return recipeTags;
        } finally {
            SQLConnectionPoolInstance.releaseConnection(c);
        }
    }

    public static void updateAndSaveRecipe(FinalizeRecipeSO finalizeRecipeSO, Integer recipeID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Create RecipeMeasuredIngredients list and update in DB
        List<RecipeMeasuredIngredient> measuredIngredients = new ArrayList<>();
        for (String ingredientAndMeasurement: finalizeRecipeSO.getAllIngredientsAndMeasurements()) {
            measuredIngredients.add(new RecipeMeasuredIngredient(
                    null,
                    recipeID,
                    ingredientAndMeasurement
            ));
        }
        RecipeDAOPooled.updateMeasuredIngredients(recipeID, measuredIngredients);

        // Create RecipeDirections list and update in DB
        List<RecipeInstruction> directions = new ArrayList<>();
        for (int i = 0; i < finalizeRecipeSO.getInstructions().size(); i++) {
            directions.add(new RecipeInstruction(
                    null,
                    recipeID,
                    finalizeRecipeSO.getInstructions().get(i)
            ));
        }
        RecipeDAOPooled.updateDirections(recipeID, directions);

        // Update feasibility
        RecipeDAOPooled.updateFeasibility(recipeID, finalizeRecipeSO.getFeasibility());

        // Update estimated total minutes
        RecipeDAOPooled.updateEstimatedTotalMinutes(recipeID, finalizeRecipeSO.getEstimatedTotalMinutes());

        // Update estimated servings
        RecipeDAOPooled.updateEstimatedServings(recipeID, finalizeRecipeSO.getEstimatedServings());

        // Update estimated total calories
        RecipeDAOPooled.updateEstimatedTotalCalories(recipeID, finalizeRecipeSO.getEstimatedTotalCalories());

        // Update modification date
        RecipeDAOPooled.updateModificationDate(recipeID);
    }

    public static void updateAndSaveRecipe(GenerateMeasuredIngredientsAndDirectionsSO generateMeasuredIngredientsAndDirectionsSO, Integer recipeID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Create RecipeMeasuredIngredients list and update in DB
        List<RecipeMeasuredIngredient> measuredIngredients = new ArrayList<>();
        for (String ingredientAndMeasurement: generateMeasuredIngredientsAndDirectionsSO.getAllIngredientsAndMeasurements()) {
            measuredIngredients.add(new RecipeMeasuredIngredient(
                    null,
                    recipeID,
                    ingredientAndMeasurement
            ));
        }
        RecipeDAOPooled.updateMeasuredIngredients(recipeID, measuredIngredients);

        // Create RecipeDirections list and update in DB
        List<RecipeInstruction> directions = new ArrayList<>();
//        for (String direction: generateMeasuredIngredientsAndDirectionsSO.getDirections()) {
        for (int i = 0; i < generateMeasuredIngredientsAndDirectionsSO.getDirections().size(); i++) {
            directions.add(new RecipeInstruction(
                    null,
                    recipeID,
                    generateMeasuredIngredientsAndDirectionsSO.getDirections().get(i)
            ));
        }
        RecipeDAOPooled.updateDirections(recipeID, directions);

        // Update estimatedServings
        RecipeDAOPooled.updateEstimatedServings(recipeID, generateMeasuredIngredientsAndDirectionsSO.getEstimatedServings());

        // Update feasibility
        RecipeDAOPooled.updateFeasibility(recipeID, generateMeasuredIngredientsAndDirectionsSO.getFeasibility());

        // Update modification date
        RecipeDAOPooled.updateModificationDate(recipeID);
    }

}

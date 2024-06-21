package com.pantrypro.database.dao.factory;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.core.generation.tagging.TagFilterer;
import com.pantrypro.database.compoundobjects.RecipeWithIngredients;
import com.pantrypro.database.dao.RecipeDAO;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.database.objects.recipe.RecipeTag;
import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaFC;
import com.pantrypro.networking.client.oaifunctioncall.finalizerecipe.FinalizeRecipeFC;
import com.pantrypro.networking.client.oaifunctioncall.generatedirections.GenerateDirectionsFC;
import com.pantrypro.networking.client.oaifunctioncall.tagrecipe.TagRecipeFC;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RecipeFactoryDAO {

    public static RecipeWithIngredients createAndSaveRecipe(CreateRecipeIdeaFC createRecipeIdeaFC, Integer userID, String input, String cuisineType, Integer expandIngredientsMagnitude) throws InterruptedException, SQLException, InvocationTargetException, IllegalAccessException, DBSerializerPrimaryKeyMissingException, DBSerializerException {
        // Create Recipe
        Recipe recipe = new Recipe(
                null,
                userID,
                input,
                createRecipeIdeaFC.getName(),
                createRecipeIdeaFC.getSummary(),
                cuisineType,
                expandIngredientsMagnitude,
                null,
                null,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        Connection c = SQLConnectionPoolInstance.getConnection();
        try {
            // Save Recipe
            RecipeDAO.insert(c, recipe);
//            DBManager.insert(c, recipe);

            List<RecipeMeasuredIngredient> recipeMeasuredIngredients = new ArrayList<>();

            // Loop through ingredients from createRecipeIdeaFC
            for (String ingredientName: createRecipeIdeaFC.getIngredients()) {
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

            return new RecipeWithIngredients(recipe, recipeMeasuredIngredients);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(c);
        }
    }

    public static List<RecipeTag> createAndSaveRecipeTags(TagRecipeFC tagRecipeFC, Integer recipeID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        Connection c = SQLConnectionPoolInstance.getConnection();
        try {
            // Create, adapt to, and save RecipeTag list
            List<RecipeTag> recipeTags = new ArrayList<>();
            for (String tag : tagRecipeFC.getTags()) {
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

    public static void updateAndSaveRecipe(FinalizeRecipeFC finalizeRecipeFC, Integer recipeID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Create RecipeMeasuredIngredients list and update in DB
        List<RecipeMeasuredIngredient> measuredIngredients = new ArrayList<>();
        for (String ingredientAndMeasurement: finalizeRecipeFC.getAllIngredientsAndMeasurements()) {
            measuredIngredients.add(new RecipeMeasuredIngredient(
                    null,
                    recipeID,
                    ingredientAndMeasurement
            ));
        }
        RecipeDAOPooled.updateMeasuredIngredients(recipeID, measuredIngredients);

        // Create RecipeDirections list and update in DB
        List<RecipeInstruction> directions = new ArrayList<>();
        for (String direction: finalizeRecipeFC.getInstructions()) {
            directions.add(new RecipeInstruction(
                    null,
                    recipeID,
                    direction
            ));
        }
        RecipeDAOPooled.updateDirections(recipeID, directions);

        // Update feasibility
        RecipeDAOPooled.updateFeasibility(recipeID, finalizeRecipeFC.getFeasibility());

        // Update estimated total minutes
        RecipeDAOPooled.updateEstimatedTotalMinutes(recipeID, finalizeRecipeFC.getEstimatedTotalMinutes());

        // Update estimated servings
        RecipeDAOPooled.updateEstimatedServings(recipeID, finalizeRecipeFC.getEstimatedServings());

        // Update estimated total calories
        RecipeDAOPooled.updateEstimatedTotalCalories(recipeID, finalizeRecipeFC.getEstimatedTotalCalories());

        // Update modification date
        RecipeDAOPooled.updateModificationDate(recipeID);
    }

    public static void updateAndSaveRecipe(GenerateDirectionsFC generateDirectionsFC, Integer recipeID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        // Create RecipeDirections list and update in DB
        List<RecipeInstruction> directions = new ArrayList<>();
        for (String direction: generateDirectionsFC.getDirections()) {
            directions.add(new RecipeInstruction(
                    null,
                    recipeID,
                    direction
            ));
        }
        RecipeDAOPooled.updateDirections(recipeID, directions);

        // Update feasibility
        RecipeDAOPooled.updateFeasibility(recipeID, generateDirectionsFC.getFeasibility());

        // Update modification date
        RecipeDAOPooled.updateModificationDate(recipeID);
    }

}

package com.pantrypro.database.compoundobjects;

import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;

import java.util.List;

public class RecipeWithIngredientsAndDirections {

    private Recipe recipe;
    private List<RecipeMeasuredIngredient> measuredIngredients;
    private List<RecipeInstruction> instructions;

    public RecipeWithIngredientsAndDirections(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients) {
        this.recipe = recipe;
        this.measuredIngredients = measuredIngredients;
    }

    public RecipeWithIngredientsAndDirections(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients, List<RecipeInstruction> instructions) {
        this.recipe = recipe;
        this.measuredIngredients = measuredIngredients;
        this.instructions = instructions;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<RecipeMeasuredIngredient> getMeasuredIngredients() {
        return measuredIngredients;
    }

    public List<RecipeInstruction> getInstructions() {
        return instructions;
    }

}

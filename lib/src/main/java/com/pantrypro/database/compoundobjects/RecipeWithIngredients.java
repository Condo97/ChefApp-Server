package com.pantrypro.database.compoundobjects;

import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;

import java.util.List;

public class RecipeWithIngredients {

    private Recipe recipe;
    private List<RecipeMeasuredIngredient> measuredIngredients;

    public RecipeWithIngredients(Recipe recipe, List<RecipeMeasuredIngredient> measuredIngredients) {
        this.recipe = recipe;
        this.measuredIngredients = measuredIngredients;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public List<RecipeMeasuredIngredient> getMeasuredIngredients() {
        return measuredIngredients;
    }

}

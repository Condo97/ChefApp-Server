package com.pantrypro.database.objects.recipe;

import com.pantrypro.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.RecipeMeasuredIngredient.TABLE_NAME)
public class RecipeMeasuredIngredient {

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.ingredient_id, primaryKey = true)
    private Integer ingredientID;

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.recipe_id)
    private Integer recipeID;

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.measuredIngredient)
    private String measuredIngredient;

    public RecipeMeasuredIngredient() {

    }

    public RecipeMeasuredIngredient(Integer ingredientID, Integer recipeID, String measuredIngredient) {
        this.ingredientID = ingredientID;
        this.recipeID = recipeID;
        this.measuredIngredient = measuredIngredient;
    }

    public Integer getIngredientID() {
        return ingredientID;
    }

    public void setIngredientID(Integer ingredientID) {
        this.ingredientID = ingredientID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public String getMeasuredIngredient() {
//        System.out.println("Measured Ingredient: " + measuredIngredient);
        return measuredIngredient;
    }

}

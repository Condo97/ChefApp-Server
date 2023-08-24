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

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.ingredient)
    private String ingredientName;

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.measurement)
    private String measurement;

    public RecipeMeasuredIngredient() {

    }

    public RecipeMeasuredIngredient(Integer ingredientID, Integer recipeID, String ingredientName, String measurement) {
        this.ingredientID = ingredientID;
        this.recipeID = recipeID;
        this.ingredientName = ingredientName;
        this.measurement = measurement;
    }

    public Integer getIngredientID() {
        return ingredientID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getMeasurement() {
        return measurement;
    }

}

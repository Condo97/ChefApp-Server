package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.RecipeMeasuredIngredient.TABLE_NAME)
public class RecipeMeasuredIngredient {

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.ingredient_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.recipe_id)
    private Integer recipeID;

    @DBColumn(name = DBRegistry.Table.RecipeMeasuredIngredient.string)
    private String string;

    public RecipeMeasuredIngredient() {

    }

    public RecipeMeasuredIngredient(String string) {
        this.string = string;
    }

    public Integer getId() {
        return id;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Integer recipeID) {
        this.recipeID = recipeID;
    }

    public String getString() {
        return string;
    }

}

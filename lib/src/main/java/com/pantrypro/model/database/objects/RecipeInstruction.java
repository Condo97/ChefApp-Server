package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.RecipeInstruction.TABLE_NAME)
public class RecipeInstruction {

    @DBColumn(name = DBRegistry.Table.RecipeInstruction.instruction_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.RecipeInstruction.recipe_id)
    private Integer recipeID;

    @DBColumn(name = DBRegistry.Table.RecipeInstruction.text)
    private String text;

    public RecipeInstruction() {

    }

    public RecipeInstruction(String text) {
        this.text = text;
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

    public String getText() {
        return text;
    }

}

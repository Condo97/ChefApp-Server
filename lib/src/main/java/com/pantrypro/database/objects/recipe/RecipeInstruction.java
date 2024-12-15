package com.pantrypro.database.objects.recipe;

import com.pantrypro.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.RecipeInstruction.TABLE_NAME)
public class RecipeInstruction {

    @DBColumn(name = DBRegistry.Table.RecipeInstruction.instruction_id, primaryKey = true)
    private Integer instructionID;

    @DBColumn(name = DBRegistry.Table.RecipeInstruction.recipe_id)
    private Integer recipeID;

    @DBColumn(name = DBRegistry.Table.RecipeInstruction.text)
    private String text;

    public RecipeInstruction() {

    }

    public RecipeInstruction(Integer instructionID, Integer recipeID, String text) {
        this.instructionID = instructionID;
        this.recipeID = recipeID;
        this.text = text;
    }

    public Integer getId() {
        return instructionID;
    }

    public void setID(Integer instructionID) {
        this.instructionID = instructionID;
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

package com.pantrypro.database.objects.recipe;

import com.pantrypro.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.RecipeDirection.TABLE_NAME)
public class RecipeDirection {

    @DBColumn(name = DBRegistry.Table.RecipeDirection.direction_id, primaryKey = true)
    private Integer directionID;

    @DBColumn(name = DBRegistry.Table.RecipeDirection.recipe_id)
    private Integer recipeID;

    @DBColumn(name = DBRegistry.Table.RecipeDirection.text)
    private String text;

    public RecipeDirection() {

    }

    public RecipeDirection(Integer directionID, Integer recipeID, String text) {
        this.directionID = directionID;
        this.recipeID = recipeID;
        this.text = text;
    }

    public Integer getId() {
        return directionID;
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

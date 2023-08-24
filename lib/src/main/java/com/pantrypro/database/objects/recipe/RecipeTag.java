package com.pantrypro.database.objects.recipe;

import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = "RecipeTag")
public class RecipeTag {

    @DBColumn(name = "tag_id", primaryKey = true)
    private Integer tagID;

    @DBColumn(name = "recipe_id")
    private Integer recipeID;

    @DBColumn(name = "tag")
    private String tag;

    public RecipeTag() {

    }

    private RecipeTag(Integer tagID, Integer recipeID, String tag) {
        this.tagID = tagID;
        this.recipeID = recipeID;
        this.tag = tag;
    }

    public static RecipeTag withLowercaseTag(Integer tagID, Integer recipeID, String tag) {
        return new RecipeTag(
                tagID,
                recipeID,
                tag.toLowerCase()
        );
    }

    public Integer getTagID() {
        return tagID;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public String getTag() {
        return tag;
    }

}

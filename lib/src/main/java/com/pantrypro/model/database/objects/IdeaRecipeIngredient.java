package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.IdeaRecipeIngredient.TABLE_NAME)
public class IdeaRecipeIngredient {

    @DBColumn(name = DBRegistry.Table.IdeaRecipeIngredient.ingredient_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.IdeaRecipeIngredient.idea_id)
    private Integer ideaID;

    @DBColumn(name = DBRegistry.Table.IdeaRecipeIngredient.name)
    private String name;

    public IdeaRecipeIngredient() {

    }

    public IdeaRecipeIngredient(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdeaID() {
        return ideaID;
    }

    public void setIdeaID(Integer ideaID) {
        this.ideaID = ideaID;
    }

    public String getName() {
        return name;
    }

}

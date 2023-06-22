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

    @DBColumn(name = DBRegistry.Table.IdeaRecipeIngredient.amount)
    private Double amount;

    public IdeaRecipeIngredient() {
        // This object should only be created from DBDeserializer (or DBSerializer if thats the right one lol), and if a new one is to be created, it needs to be inserted with an insertion "builder" method BEFORE it's created here
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdeaID() {
        return ideaID;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

}

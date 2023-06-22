package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.IdeaRecipe.TABLE_NAME)
public class IdeaRecipe {

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.idea_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.input)
    private String input;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.name)
    private String name;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.summary)
    private String summary;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.cuisine_type)
    private String cuisineType;

    public IdeaRecipe() {
        // This object should only be created from DBDeserializer (or DBSerializer if thats the right one lol), and if a new one is to be created, it needs to be inserted with an insertion "builder" method BEFORE it's created here
    }

    public Integer getId() {
        return id;
    }

    public String getInput() {
        return input;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getCuisineType() {
        return cuisineType;
    }

}

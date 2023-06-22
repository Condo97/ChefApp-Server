package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.Recipe.TABLE_NAME)
public class Recipe {

    @DBColumn(name = DBRegistry.Table.Recipe.recipe_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.Recipe.idea_id)
    private Integer idea_id;
}

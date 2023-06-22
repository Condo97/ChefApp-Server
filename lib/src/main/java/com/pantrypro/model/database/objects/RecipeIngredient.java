package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.RecipeIngredient.TABLE_NAME)
public class RecipeIngredient {

    @DBColumn(name = DBRegistry.Table.RecipeIngredient.ingredient_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.RecipeIngredient.recipe_id)
    private Integer recipeID;

    @DBColumn(name = DBRegistry.Table.RecipeIngredient.name)
    private String name;

    @DBColumn(name = DBRegistry.Table.RecipeIngredient.amount)
    private Double amount;

    public RecipeIngredient() {
        // This object should only be created from DBDeserializer (or DBSerializer if thats the right one lol), and if a new one is to be created, it needs to be inserted with an insertion "builder" method BEFORE it's created here
    }

    public Integer getId() {
        return id;
    }

    public Integer getRecipeID() {
        return recipeID;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

}

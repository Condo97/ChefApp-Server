package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

import java.time.LocalDateTime;

@DBSerializable(tableName = DBRegistry.Table.IdeaRecipe.TABLE_NAME)
public class IdeaRecipe {

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.idea_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.user_id)
    private Integer userID;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.input)
    private String input;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.name)
    private String name;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.summary)
    private String summary;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.cuisine_type)
    private String cuisineType;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.expand_ingredients_magnitude)
    private Integer expandIngredientsMagnitude;

    @DBColumn(name = DBRegistry.Table.IdeaRecipe.date)
    private LocalDateTime date;


    public IdeaRecipe() {

    }

    public IdeaRecipe(Integer userID, String input, String name, String summary, String cuisineType, Integer expandIngredientsMagnitude) {
        this.userID = userID;
        this.input = input;
        this.name = name;
        this.summary = summary;
        this.cuisineType = cuisineType;
        this.expandIngredientsMagnitude = expandIngredientsMagnitude;

        date = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserID() {
        return userID;
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

    public Integer getExpandIngredientsMagnitude() {
        return expandIngredientsMagnitude;
    }

    public LocalDateTime getDate() {
        return date;
    }

}

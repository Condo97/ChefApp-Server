package com.pantrypro.database.objects.recipe;

import com.pantrypro.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

import java.time.LocalDateTime;

@DBSerializable(tableName = DBRegistry.Table.Recipe.TABLE_NAME)
public class Recipe {

    @DBColumn(name = DBRegistry.Table.Recipe.recipe_id, primaryKey = true)
    private Integer recipe_id;

    @DBColumn(name = DBRegistry.Table.Recipe.user_id)
    private Integer user_id;

    @DBColumn(name = DBRegistry.Table.Recipe.input)
    private String input;

    @DBColumn(name = DBRegistry.Table.Recipe.name)
    private String name;

    @DBColumn(name = DBRegistry.Table.Recipe.summary)
    private String summary;

    @DBColumn(name = DBRegistry.Table.Recipe.cuisine_type)
    private String cuisineType;

    @DBColumn(name = DBRegistry.Table.Recipe.expand_ingredients_magnitude)
    private Integer expandIngredientsMagnitude;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_total_calories)
    private Integer estimatedTotalCalories;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_total_minutes)
    private Integer estimatedTotalMinutes;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_servings)
    private Integer estimatedServings;

    @DBColumn(name = DBRegistry.Table.Recipe.feasibility)
    private Integer feasibility;

    @DBColumn(name = DBRegistry.Table.Recipe.creation_date)
    private LocalDateTime creationDate;

    @DBColumn(name = DBRegistry.Table.Recipe.modify_date)
    private LocalDateTime modifyDate;

    public Recipe() {

    }

    public Recipe(Integer recipe_id, Integer user_id, String input, String name, String summary, String cuisineType, Integer expandIngredientsMagnitude, Integer estimatedTotalCalories, Integer estimatedTotalMinutes, Integer estimatedServings, Integer feasibility, LocalDateTime creationDate, LocalDateTime modifyDate) {
        this.recipe_id = recipe_id;
        this.user_id = user_id;
        this.input = input;
        this.name = name;
        this.summary = summary;
        this.cuisineType = cuisineType;
        this.expandIngredientsMagnitude = expandIngredientsMagnitude;
        this.estimatedTotalCalories = estimatedTotalCalories;
        this.estimatedTotalMinutes = estimatedTotalMinutes;
        this.estimatedServings = estimatedServings;
        this.feasibility = feasibility;
        this.creationDate = creationDate;
        this.modifyDate = modifyDate;
    }

    public Integer getRecipe_id() {
        return recipe_id;
    }

    public Integer getUser_id() {
        return user_id;
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

    public Integer getEstimatedTotalCalories() {
        return estimatedTotalCalories;
    }

    public Integer getEstimatedTotalMinutes() {
        return estimatedTotalMinutes;
    }

    public Integer getEstimatedServings() {
        return estimatedServings;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public LocalDateTime getModifyDate() {
        return modifyDate;
    }

}

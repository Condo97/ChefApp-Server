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

    @DBColumn(name = DBRegistry.Table.Recipe.image_url)
    private String imageURL;

    @DBColumn(name = DBRegistry.Table.Recipe.likes_count)
    private Integer likesCount;

    @DBColumn(name = DBRegistry.Table.Recipe.dislikes_count)
    private Integer dislikesCount;

    public Recipe() {

    }

    public Recipe(Integer recipe_id, Integer user_id, String input, String name, String summary, String cuisineType, Integer expandIngredientsMagnitude, Integer estimatedTotalCalories, Integer estimatedTotalMinutes, Integer estimatedServings, Integer feasibility, LocalDateTime creationDate, LocalDateTime modifyDate, String imageURL, Integer likesCount, Integer dislikesCount) {
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
        this.imageURL = imageURL;
        this.likesCount = likesCount;
        this.dislikesCount = dislikesCount;
    }

    public Integer getRecipe_id() {
        return recipe_id;
    }

    public void setRecipe_id(Integer recipe_id) {
        this.recipe_id = recipe_id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
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

    public String getImageURL() {
        return imageURL;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public Integer getDislikesCount() {
        return dislikesCount;
    }

}

package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;
import sqlcomponentizer.dbserializer.DBSubObject;

import java.time.LocalDateTime;
import java.util.List;

@DBSerializable(tableName = DBRegistry.Table.Recipe.TABLE_NAME)
public class Recipe {

    @DBColumn(name = DBRegistry.Table.Recipe.recipe_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.Recipe.idea_id)
    private Integer idea_id;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_total_calories)
    private Integer estimated_total_calories;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_total_minutes)
    private Integer estimated_total_minutes;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_servings)
    private Integer estimated_servings;

    @DBColumn(name = DBRegistry.Table.Recipe.date)
    private LocalDateTime date;

    @DBColumn(name = DBRegistry.Table.Recipe.feasibility)
    private Integer feasibility;

//    @DBSubObject
//    private List<RecipeMeasuredIngredient> measuredIngredients;
//
//    @DBSubObject
//    private List<RecipeInstruction> instructions;


    public Recipe() {

    }

    public Recipe(Integer idea_id, Integer estimated_total_calories, Integer estimated_total_minutes, Integer estimated_servings, Integer feasibility) {
        this.idea_id = idea_id;
        this.estimated_total_calories = estimated_total_calories;
        this.estimated_total_minutes = estimated_total_minutes;
        this.estimated_servings = estimated_servings;
        this.feasibility = feasibility;
//        this.measuredIngredients = measuredIngredients;
//        this.instructions = instructions;

        date = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdea_id() {
        return idea_id;
    }

    public Integer getEstimated_total_calories() {
        return estimated_total_calories;
    }

    public Integer getEstimated_total_minutes() {
        return estimated_total_minutes;
    }

    public Integer getEstimated_servings() {
        return estimated_servings;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

//    public List<RecipeMeasuredIngredient> getMeasuredIngredients() {
//        return measuredIngredients;
//    }
//
//    public List<RecipeInstruction> getInstructions() {
//        return instructions;
//    }

}

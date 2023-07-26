package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

import java.time.LocalDateTime;

@DBSerializable(tableName = DBRegistry.Table.Recipe.TABLE_NAME)
public class Recipe {

    @DBColumn(name = DBRegistry.Table.Recipe.recipe_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.Recipe.idea_id)
    private Integer idea_id;

    @DBColumn(name = DBRegistry.Table.Recipe.estimated_total_minutes)
    private Integer estimated_total_minutes;

    @DBColumn(name = DBRegistry.Table.Recipe.date)
    private LocalDateTime date;

    @DBColumn(name = DBRegistry.Table.Recipe.feasibility)
    private Integer feasibility;


    public Recipe() {

    }

    public Recipe(Integer idea_id, Integer estimated_total_minutes) {
        this.idea_id = idea_id;
        this.estimated_total_minutes = estimated_total_minutes;

        date = LocalDateTime.now();
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdea_id() {
        return idea_id;
    }

    public Integer getEstimated_total_minutes() {
        return estimated_total_minutes;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public Integer getFeasibility() {
        return feasibility;
    }

}

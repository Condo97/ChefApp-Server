package com.pantrypro.model.database.objects;

import com.pantrypro.model.database.DBRegistry;
import sqlcomponentizer.dbserializer.DBColumn;
import sqlcomponentizer.dbserializer.DBSerializable;

@DBSerializable(tableName = DBRegistry.Table.IdeaRecipeEquipment.TABLE_NAME)
public class IdeaRecipeEquipment {

    @DBColumn(name = DBRegistry.Table.IdeaRecipeEquipment.equipment_id, primaryKey = true)
    private Integer id;

    @DBColumn(name = DBRegistry.Table.IdeaRecipeEquipment.idea_id)
    private Integer ideaID;

    @DBColumn(name = DBRegistry.Table.IdeaRecipeEquipment.name)
    private String name;

    public IdeaRecipeEquipment() {

    }

    public IdeaRecipeEquipment(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setIdeaID(Integer ideaID) {
        this.ideaID = ideaID;
    }

    public Integer getIdeaId() {
        return ideaID;
    }

    public String getName() {
        return name;
    }

}

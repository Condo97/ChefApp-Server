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
        // This object should only be created from DBDeserializer (or DBSerializer if thats the right one lol), and if a new one is to be created, it needs to be inserted with an insertion "builder" method BEFORE it's created here
    }

    public Integer getId() {
        return id;
    }

    public Integer getIdeaId() {
        return ideaID;
    }

    public String getName() {
        return name;
    }

}

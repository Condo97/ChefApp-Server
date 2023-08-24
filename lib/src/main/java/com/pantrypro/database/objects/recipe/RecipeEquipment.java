//package com.pantrypro.model.database.objects;
//
//import com.pantrypro.DBRegistry;
//import sqlcomponentizer.dbserializer.DBColumn;
//import sqlcomponentizer.dbserializer.DBSerializable;
//
//@DBSerializable(tableName = DBRegistry.Table.RecipeEquipment.TABLE_NAME)
//public class RecipeEquipment {
//
//    @DBColumn(name = DBRegistry.Table.RecipeEquipment.equipment_id, primaryKey = true)
//    private Integer id;
//
//    @DBColumn(name = DBRegistry.Table.RecipeEquipment.recipe_id)
//    private Integer recipeID;
//
//    @DBColumn(name = DBRegistry.Table.RecipeEquipment.name)
//    private String name;
//
//    public RecipeEquipment() {
//
//    }
//
//    public RecipeEquipment(Integer recipeID, String name) {
//        this.recipeID = recipeID;
//        this.name = name;
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public Integer getRecipeID() {
//        return recipeID;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//}

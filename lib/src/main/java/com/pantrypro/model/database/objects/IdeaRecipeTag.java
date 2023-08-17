//package com.pantrypro.model.database.objects;
//
//import com.pantrypro.model.database.DBRegistry;
//import sqlcomponentizer.dbserializer.DBColumn;
//import sqlcomponentizer.dbserializer.DBSerializable;
//
//@DBSerializable(tableName = DBRegistry.Table.IdeaRecipeTag.TABLE_NAME)
//public class IdeaRecipeTag {
//
//    @DBColumn(name = DBRegistry.Table.IdeaRecipeTag.tag_id, primaryKey = true)
//    private Integer id;
//
//    @DBColumn(name = DBRegistry.Table.IdeaRecipeTag.idea_id)
//    private Integer ideaID;
//
//    @DBColumn(name = DBRegistry.Table.IdeaRecipeTag.tag)
//    private String tagLowercase;
//
//    public IdeaRecipeTag() {
//
//    }
//
//    public IdeaRecipeTag(Integer ideaID, String tag) {
//        this.ideaID = ideaID;
//        this.tagLowercase = tag.toLowerCase();
//    }
//
//    public Integer getId() {
//        return id;
//    }
//
//    public Integer getIdeaID() {
//        return ideaID;
//    }
//
//    public String getTagLowercase() {
//        return tagLowercase;
//    }
//
//}

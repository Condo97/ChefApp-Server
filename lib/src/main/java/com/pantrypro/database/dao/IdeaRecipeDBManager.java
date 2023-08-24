//package com.pantrypro.core.database.managers;
//
//import com.dbclient.DBManager;
//import com.pantrypro.DBRegistry;
//import com.pantrypro.model.database.objects.IdeaRecipe;
//import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
//import com.pantrypro.model.database.objects.IdeaRecipeTag;
//import com.pantrypro.model.database.objects.RecipeMeasuredIngredient;
//import sqlcomponentizer.dbserializer.DBSerializerException;
//import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
//import sqlcomponentizer.preparedstatement.component.PSComponent;
//import sqlcomponentizer.preparedstatement.component.condition.SQLOperatorCondition;
//import sqlcomponentizer.preparedstatement.component.condition.SQLOperators;
//
//import java.lang.reflect.InvocationTargetException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.List;
//
//public class IdeaRecipeDBManager {
//
//    public static void insertIdeaRecipe(Connection conn, IdeaRecipe ideaRecipe, List<IdeaRecipeIngredient> ingredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        DBManager.insert(conn, ideaRecipe);
//
//        for (IdeaRecipeIngredient ingredient: ingredients) {
//            ingredient.setIdeaID(ideaRecipe.getId());
//            DBManager.insert(conn, ingredient);
//        }
//
////        for (IdeaRecipeEquipment equipmentObject: equipment) {
////            equipmentObject.setIdeaID(ideaRecipe.getId());
////            insert(equipmentObject);
////        }
//    }
//
//    public static void insertIdeaRecipeIngredients(Connection conn, List<IdeaRecipeIngredient> ingredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        for (IdeaRecipeIngredient ingredient: ingredients)
//            DBManager.insert(conn, ingredient);
//    }
//
//    public static void insertIdeaRecipeTags(Connection conn, List<IdeaRecipeTag> tags) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        for (IdeaRecipeTag tag: tags) {
//            DBManager.insert(conn, tag);
//        }
//    }
//
//    public static Long countForToday(Connection conn, Integer userID) throws DBSerializerException, SQLException, InterruptedException {
//        // Get from date as yesterday's date to count all ideaRecipes after it
//        LocalDateTime fromDate = LocalDateTime.now().minus(Duration.ofDays(1));
//
//        // Build SQL Conditions
//        SQLOperatorCondition userIDCondition = new SQLOperatorCondition(
//                DBRegistry.Table.IdeaRecipe.user_id,
//                SQLOperators.EQUAL,
//                userID
//        );
//        SQLOperatorCondition dateCondition = new SQLOperatorCondition(
//                DBRegistry.Table.IdeaRecipe.date,
//                SQLOperators.GREATER_THAN,
//                fromDate
//        );
//        List<PSComponent> sqlConditions = List.of(
//                userIDCondition,
//                dateCondition
//        );
//
//        // Count where userID
//        return DBManager.countObjectWhere(
//                conn,
//                IdeaRecipe.class,
//                sqlConditions
//        );
//    }
//
//    public static IdeaRecipe get(Connection conn, Integer ideaID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        // Get all recipe ideas for idea ID
//        List<IdeaRecipe> allRecipesForID = DBManager.selectAllByPrimaryKey(conn, IdeaRecipe.class, ideaID);
//
//        // if there is at least one object, return the first
//        if (allRecipesForID.size() > 0)
//            return allRecipesForID.get(0);
//
//        // If there are no objects, return null
//        return null;
//    }
//
//    public static List<IdeaRecipeIngredient> getIngredients(Connection conn, Integer ideaID) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
//        // Get all recipe idea ingredients for idea ID
//        List<IdeaRecipeIngredient> allIngredientsForID = DBManager.selectAllWhere(
//                conn,
//                IdeaRecipeIngredient.class,
//                DBRegistry.Table.IdeaRecipeIngredient.idea_id,
//                SQLOperators.EQUAL,
//                ideaID
//        );
//
//        return allIngredientsForID;
//    }
//
//    public static void updateName(Connection conn, Integer ideaID, String name) throws DBSerializerException, SQLException, InterruptedException {
//        DBManager.updateWhere(
//                conn,
//                IdeaRecipe.class,
//                DBRegistry.Table.IdeaRecipe.name,
//                name,
//                DBRegistry.Table.IdeaRecipe.idea_id,
//                SQLOperators.EQUAL,
//                ideaID
//        );
//    }
//
//    public static void updateSummary(Connection conn, Integer ideaID, String summary) throws DBSerializerException, SQLException, InterruptedException {
//        DBManager.updateWhere(
//                conn,
//                IdeaRecipe.class,
//                DBRegistry.Table.IdeaRecipe.summary,
//                summary,
//                DBRegistry.Table.IdeaRecipe.idea_id,
//                SQLOperators.EQUAL,
//                ideaID
//        );
//    }
//
//    public static void updateIngredients(Connection conn, Integer ideaID, List<IdeaRecipeIngredient> ingredients) throws DBSerializerException, SQLException, InterruptedException, DBSerializerPrimaryKeyMissingException, InvocationTargetException, IllegalAccessException {
//        // Delete all ingredients for ideaID
//        deleteAllIngredients(conn, ideaID);
//
//        // Insert new ingredients for ideaID
//        for (IdeaRecipeIngredient ingredient: ingredients) {
//            // Set ideaID since we want all ingredients here to be associated with the given ideaID, and in the use case when the ingredients are generated they do not adapt with ideaIDs TODO: Is this a good place to set this?
//            ingredient.setIdeaID(ideaID);
//            DBManager.insert(conn, ingredient);
//        }
//    }
//
//    private static void deleteAllIngredients(Connection conn, Integer ideaID) throws DBSerializerException, SQLException, InterruptedException {
//        DBManager.deleteWhere(
//                conn,
//                IdeaRecipeIngredient.class,
//                DBRegistry.Table.IdeaRecipeIngredient.idea_id,
//                SQLOperators.EQUAL,
//                ideaID
//        );
//    }
//
//}

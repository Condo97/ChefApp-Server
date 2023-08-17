//package com.pantrypro.core;
//
//import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
//import com.pantrypro.core.database.managers.IdeaRecipeDBManager;
//import com.pantrypro.core.database.managers.RecipeDBManager;
//import com.pantrypro.model.database.objects.IdeaRecipe;
//import com.pantrypro.model.database.objects.IdeaRecipeIngredient;
//import com.pantrypro.model.database.objects.RecipeMeasuredIngredient;
//import sqlcomponentizer.dbserializer.DBSerializerException;
//import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;
//
//import java.lang.reflect.InvocationTargetException;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.util.List;
//
//public class PPPersistinator {
//
//    public static Integer getRecipeID(Integer ideaID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            return RecipeDBManager.getFromIdeaID(conn, ideaID).getId();
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static void insertIdeaRecipe(IdeaRecipe ideaRecipe, List<IdeaRecipeIngredient> ideaRecipeIngredients) throws InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InvocationTargetException, IllegalAccessException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            IdeaRecipeDBManager.insertIdeaRecipe(conn, ideaRecipe, ideaRecipeIngredients);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static void replaceIdeaRecipeIngredients(Integer ideaID, List<IdeaRecipeIngredient> ideaRecipeIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            // Delete all ingredients from idea recipe
//            IdeaRecipeDBManager.deleteAllIngredients(conn, ideaID);
//
//            // Insert all ingredients for idea recipe
//            IdeaRecipeDBManager.insertIdeaRecipeIngredients(conn, ideaRecipeIngredients);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static void replaceRecipeMeasuredIngredients(Integer recipeID, List<RecipeMeasuredIngredient> recipeMeasuredIngredients) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            // Delete all measured ingredients for recipe
//            RecipeDBManager.deleteAllMeasuredIngredients(conn, recipeID);
//
//            // Insert all measured ingredients for recipe
//            RecipeDBManager.insertRecipeMeasuredIngredients(conn, recipeMeasuredIngredients);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static void updateIdeaRecipeName(Integer ideaID, String newName) throws DBSerializerException, SQLException, InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            IdeaRecipeDBManager.updateName(conn, ideaID, newName);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//    public static void updateIdeaRecipeSummary(Integer ideaID, String newSummary) throws DBSerializerException, SQLException, InterruptedException {
//        Connection conn = SQLConnectionPoolInstance.getConnection();
//        try {
//            IdeaRecipeDBManager.updateSummary(conn, ideaID, newSummary);
//        } finally {
//            SQLConnectionPoolInstance.releaseConnection(conn);
//        }
//    }
//
//}

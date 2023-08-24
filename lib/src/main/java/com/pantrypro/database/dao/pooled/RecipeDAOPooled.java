package com.pantrypro.database.dao.pooled;

import com.pantrypro.connectionpool.SQLConnectionPoolInstance;
import com.pantrypro.database.dao.RecipeDAO;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeDirection;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import sqlcomponentizer.dbserializer.DBSerializerException;
import sqlcomponentizer.dbserializer.DBSerializerPrimaryKeyMissingException;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class RecipeDAOPooled {

    public static Long countForToday(Integer userID) throws DBSerializerException, SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return RecipeDAO.countForToday(conn, userID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static Recipe get(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return RecipeDAO.get(conn, recipeID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static List<RecipeMeasuredIngredient> getMeasuredIngredients(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return RecipeDAO.getMeasuredIngredients(conn, recipeID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static List<RecipeDirection> getDirections(Integer recipeID) throws DBSerializerException, SQLException, InterruptedException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return RecipeDAO.getDirections(conn, recipeID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateEstimatedServings(Integer recipeID, Integer estimatedServings) throws DBSerializerException, SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateEstimatedServings(conn, recipeID, estimatedServings);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateEstimatedTotalCalories(Integer recipeID, Integer estimatedTotalCalories) throws DBSerializerException, SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateEstimatedTotalCalories(conn, recipeID, estimatedTotalCalories);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateEstimatedTotalMinutes(Integer recipeID, Integer estimatedTotalMinutes) throws DBSerializerException, SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateEstimatedTotalMinutes(conn, recipeID, estimatedTotalMinutes);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateFeasibility(Integer recipeID, Integer feasibility) throws InterruptedException, DBSerializerException, SQLException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateFeasibility(conn, recipeID, feasibility);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateModificationDate(Integer recipeID) throws InterruptedException, DBSerializerException, SQLException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateModificationDate(conn, recipeID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateModificationDate(Integer recipeID, LocalDateTime modificationDate) throws DBSerializerException, SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateModificationDate(conn, recipeID, modificationDate);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateMeasuredIngredients(Integer recipeID, List<RecipeMeasuredIngredient> measuredIngredients) throws InterruptedException, DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InvocationTargetException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateMeasuredIngredients(conn, recipeID, measuredIngredients);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static void updateDirections(Integer recipeID, List<RecipeDirection> directions) throws DBSerializerPrimaryKeyMissingException, DBSerializerException, SQLException, InterruptedException, InvocationTargetException, IllegalAccessException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            RecipeDAO.updateDirections(conn, recipeID, directions);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

    public static boolean isUserAssociatedWithRecipe(Integer userID, Integer recipeID) throws DBSerializerException, SQLException, InterruptedException {
        Connection conn = SQLConnectionPoolInstance.getConnection();
        try {
            return RecipeDAO.isUserAssociatedWithRecipe(conn, userID, recipeID);
        } finally {
            SQLConnectionPoolInstance.releaseConnection(conn);
        }
    }

}

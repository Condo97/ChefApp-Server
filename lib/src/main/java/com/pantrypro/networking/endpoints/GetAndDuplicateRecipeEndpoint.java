package com.pantrypro.networking.endpoints;

import com.pantrypro.core.Endpoint;
import com.pantrypro.database.compoundobjects.RecipeWithIngredientsAndDirections;
import com.pantrypro.database.dao.factory.RecipeFactoryDAO;
import com.pantrypro.database.dao.pooled.RecipeDAOPooled;
import com.pantrypro.database.dao.pooled.User_AuthTokenDAOPooled;
import com.pantrypro.database.objects.User_AuthToken;
import com.pantrypro.database.objects.recipe.Recipe;
import com.pantrypro.database.objects.recipe.RecipeInstruction;
import com.pantrypro.database.objects.recipe.RecipeMeasuredIngredient;
import com.pantrypro.networking.server.request.GetAndDuplicateRecipeRequest;
import com.pantrypro.networking.server.response.GetAndDuplicateRecipeResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetAndDuplicateRecipeEndpoint implements Endpoint<GetAndDuplicateRecipeRequest> {

    @Override
    public Object getResponse(GetAndDuplicateRecipeRequest request) throws Exception {
        // Get u_aT
        User_AuthToken u_aT = User_AuthTokenDAOPooled.get(request.getAuthToken());

        // Get Recipe, MeasuredIngredients, and Instructions
        Recipe recipe = RecipeDAOPooled.get(request.getRecipeID());
        List<RecipeMeasuredIngredient> measuredIngredients = RecipeDAOPooled.getMeasuredIngredients(recipe.getRecipe_id());
        List<RecipeInstruction> instructions = RecipeDAOPooled.getDirections(recipe.getRecipe_id());

        // Create with new userID
        RecipeWithIngredientsAndDirections recipeWithIngredientsAndDirections = RecipeFactoryDAO.createDuplicatedRecipe(
                recipe,
                measuredIngredients,
                instructions,
                u_aT.getUserID()
        );

        // Adapt to GetRecipeResponse and return
        return adaptRecipeWithIngredientsAndDirections(recipeWithIngredientsAndDirections);
    }

    private GetAndDuplicateRecipeResponse adaptRecipeWithIngredientsAndDirections(RecipeWithIngredientsAndDirections recipeWithIngredientsAndDirections) {
        List<String> adaptedMeasuredIngredients = recipeWithIngredientsAndDirections.getMeasuredIngredients().stream().map(RecipeMeasuredIngredient::getMeasuredIngredient).toList();

//        List<GetAndDuplicateRecipeResponse.Recipe.Instruction> adaptedInstructions = recipeWithIngredientsAndDirections.getInstructions().stream().map(i -> new GetAndDuplicateRecipeResponse.Recipe.Instruction(
//                i.getId(),
//                i.getRecipeID(),
//                i.getText()
//        )).toList();
        // Put directions in a map with the index to preserve order
        Map<Integer, String> instructionStringsMap = new HashMap<>();
        for (int i = 0; i < recipeWithIngredientsAndDirections.getInstructions().size(); i++) {
            instructionStringsMap.put(i, recipeWithIngredientsAndDirections.getInstructions().get(i).getText());
        }

        GetAndDuplicateRecipeResponse.Recipe adaptedRecipe = new GetAndDuplicateRecipeResponse.Recipe(
                recipeWithIngredientsAndDirections.getRecipe().getRecipe_id(),
                recipeWithIngredientsAndDirections.getRecipe().getUser_id(),
                recipeWithIngredientsAndDirections.getRecipe().getInput(),
                recipeWithIngredientsAndDirections.getRecipe().getName(),
                recipeWithIngredientsAndDirections.getRecipe().getSummary(),
                recipeWithIngredientsAndDirections.getRecipe().getCuisineType(),
                recipeWithIngredientsAndDirections.getRecipe().getExpandIngredientsMagnitude(),
                recipeWithIngredientsAndDirections.getRecipe().getEstimatedTotalCalories(),
                recipeWithIngredientsAndDirections.getRecipe().getEstimatedTotalMinutes(),
                recipeWithIngredientsAndDirections.getRecipe().getEstimatedServings(),
                recipeWithIngredientsAndDirections.getRecipe().getFeasibility(),
//                recipeWithIngredientsAndDirections.getRecipe().getTastiness();
//                recipeWithIngredientsAndDirections.getRecipe().getCreationDate(),
//                recipeWithIngredientsAndDirections.getRecipe().getModifyDate(),
                recipeWithIngredientsAndDirections.getRecipe().getImageURL(),
                recipeWithIngredientsAndDirections.getRecipe().getLikesCount(),
                recipeWithIngredientsAndDirections.getRecipe().getDislikesCount(),
                adaptedMeasuredIngredients,
                instructionStringsMap
        );

        return new GetAndDuplicateRecipeResponse(adaptedRecipe);
    }

}

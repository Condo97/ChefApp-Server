package com.pantrypro.networking.server.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class GetAndDuplicateRecipeResponse {

    public static class Recipe {

        private Integer recipe_id;
        private Integer user_id;
        private String input;
        private String name;
        private String summary;
        private String cuisineType;
        private Integer expandIngredientsMagnitude;
        private Integer estimatedTotalCalories;
        private Integer estimatedTotalMinutes;
        private Integer estimatedServings;
        private Integer feasibility;
//        private LocalDateTime creationDate;
//        private LocalDateTime modifyDate;
        private String imageURL;
        private Integer likesCount;
        private Integer dislikesCount;
        private List<String> measuredIngredients;
        private Map<Integer, String> instructions;

        public Recipe() {

        }

        public Recipe(Integer recipe_id, Integer user_id, String input, String name, String summary, String cuisineType, Integer expandIngredientsMagnitude, Integer estimatedTotalCalories, Integer estimatedTotalMinutes, Integer estimatedServings, Integer feasibility, String imageURL, Integer likesCount, Integer dislikesCount, List<String> measuredIngredients, Map<Integer, String> instructions) {
            this.recipe_id = recipe_id;
            this.user_id = user_id;
            this.input = input;
            this.name = name;
            this.summary = summary;
            this.cuisineType = cuisineType;
            this.expandIngredientsMagnitude = expandIngredientsMagnitude;
            this.estimatedTotalCalories = estimatedTotalCalories;
            this.estimatedTotalMinutes = estimatedTotalMinutes;
            this.estimatedServings = estimatedServings;
            this.feasibility = feasibility;
//            this.creationDate = creationDate;
//            this.modifyDate = modifyDate;
            this.imageURL = imageURL;
            this.likesCount = likesCount;
            this.dislikesCount = dislikesCount;
            this.measuredIngredients = measuredIngredients;
            this.instructions = instructions;
        }

        public Integer getRecipe_id() {
            return recipe_id;
        }

        public Integer getUser_id() {
            return user_id;
        }

        public String getInput() {
            return input;
        }

        public String getName() {
            return name;
        }

        public String getSummary() {
            return summary;
        }

        public String getCuisineType() {
            return cuisineType;
        }

        public Integer getExpandIngredientsMagnitude() {
            return expandIngredientsMagnitude;
        }

        public Integer getEstimatedTotalCalories() {
            return estimatedTotalCalories;
        }

        public Integer getEstimatedTotalMinutes() {
            return estimatedTotalMinutes;
        }

        public Integer getEstimatedServings() {
            return estimatedServings;
        }

        public Integer getFeasibility() {
            return feasibility;
        }

//        public LocalDateTime getCreationDate() {
//            return creationDate;
//        }
//
//        public LocalDateTime getModifyDate() {
//            return modifyDate;
//        }


        public String getImageURL() {
            return imageURL;
        }

        public Integer getLikesCount() {
            return likesCount;
        }

        public Integer getDislikesCount() {
            return dislikesCount;
        }

        public List<String> getMeasuredIngredients() {
            return measuredIngredients;
        }

        public Map<Integer, String> getInstructions() {
            return instructions;
        }

    }

    private Recipe recipe;

    public GetAndDuplicateRecipeResponse() {

    }

    public GetAndDuplicateRecipeResponse(Recipe recipe) {
        this.recipe = recipe;
    }

    public Recipe getRecipe() {
        return recipe;
    }

}

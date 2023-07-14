package com.pantrypro.model.generation;

public enum IdeaRecipeExpandIngredients {

    NONE("Do not expand on ingredients."),
    MINIMUM("Try not to expand ingredients unless necessary to make a complete recipe."),
    MODERATE("Take ingredients and modifiers, expand upon them, and create a delicious complete recipe."),
    MAXIMUM("Take significant creative liberties in creating this recipe, and expand ingredients to make a complete recipe.");

    private String systemMessage;

    IdeaRecipeExpandIngredients(String systemMessage) {
        this.systemMessage = systemMessage;
    }

    public static IdeaRecipeExpandIngredients from(int magnitude) {
        // Get the expansion amount from the magnitude, and if it is below the minimum bounds return none or if it is above the maximum bounds return the maximum
        if (magnitude < 0)
            return NONE;
        if (magnitude >= values().length)
            return values()[values().length - 1];

        return values()[magnitude];
    }

    public String getSystemMessageString() {
        // TODO: Should this just be toString?
        return systemMessage;
    }

}

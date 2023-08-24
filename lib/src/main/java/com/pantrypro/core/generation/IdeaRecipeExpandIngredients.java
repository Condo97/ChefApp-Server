package com.pantrypro.core.generation;

import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaEM0FC;
import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaEM1FC;
import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaEM2FC;
import com.pantrypro.networking.client.oaifunctioncall.createrecipeidea.CreateRecipeIdeaFC;

public enum IdeaRecipeExpandIngredients {

    NONE("Only use provided ingredients.", "Creates a recipe", CreateRecipeIdeaEM0FC.class),
    DEFAULT("Try not to expand ingredients unless necessary to make a complete recipe.", "Creates a recipe from ingredients, adding as necessary", CreateRecipeIdeaEM1FC.class),
    CREATIVE("Take ingredients and modifiers, expand upon them, and create a delicious complete recipe.", "Creates a recipe from ingredients, adding as necessary", CreateRecipeIdeaEM2FC.class);
//    MAXIMUM("Take significant creative liberties in creating this recipe, and expand ingredients to make a complete recipe.", "Creates a recipe from ingredients, adding to create a complete recipe");

    private String systemMessage, functionDescription;
    private Class<? extends CreateRecipeIdeaFC> fcClass;

    IdeaRecipeExpandIngredients(String systemMessage, String functionDescription, Class<? extends CreateRecipeIdeaFC> fcClass) {
        this.systemMessage = systemMessage;
        this.functionDescription = functionDescription;
        this.fcClass = fcClass;
    }

    public static IdeaRecipeExpandIngredients from(int magnitude) {
        // Get the expansion amount from the magnitude, and if it is below the minimum bounds return none or if it is above the maximum bounds return the maximum
        if (magnitude < 0)
            return NONE;
        if (magnitude >= values().length)
            return values()[values().length - 1];

        return values()[magnitude];
    }

    public String getSystemMessage() {
        // TODO: Should this just be toString?
        return systemMessage;
    }

    public String getFunctionDescription() {
        return functionDescription;
    }

    public Class<? extends CreateRecipeIdeaFC> getFcClass() {
        return fcClass;
    }

}

package com.pantrypro.core.generation;

import com.pantrypro.openai.structuredoutput.createrecipeidea.CreateRecipeIdeaEM0SO;
import com.pantrypro.openai.structuredoutput.createrecipeidea.CreateRecipeIdeaEM1SO;
import com.pantrypro.openai.structuredoutput.createrecipeidea.CreateRecipeIdeaEM2SO;
import com.pantrypro.openai.structuredoutput.createrecipeidea.CreateRecipeIdeaSO;

public enum IdeaRecipeExpandIngredients {

    NONE("Only use provided ingredients.", "Creates a recipe", CreateRecipeIdeaEM0SO.class),
    DEFAULT("Try not to expand ingredients unless necessary to make a complete recipe.", "Creates a recipe from ingredients, adding as necessary", CreateRecipeIdeaEM1SO.class),
    CREATIVE("Take ingredients and modifiers, expand upon them, and create a delicious complete recipe.", "Creates a recipe from ingredients, adding as necessary", CreateRecipeIdeaEM2SO.class);
//    MAXIMUM("Take significant creative liberties in creating this recipe, and expand ingredients to make a complete recipe.", "Creates a recipe from ingredients, adding to create a complete recipe");

    private String systemMessage, functionDescription;
    private Class<? extends CreateRecipeIdeaSO> fcClass;

    IdeaRecipeExpandIngredients(String systemMessage, String functionDescription, Class<? extends CreateRecipeIdeaSO> fcClass) {
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

    public Class<? extends CreateRecipeIdeaSO> getFcClass() {
        return fcClass;
    }

}

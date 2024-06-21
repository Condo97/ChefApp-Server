package com.pantrypro.networking.client.oaifunctioncall.parsepantryitems;

import com.oaigptconnector.model.FCParameter;
import com.oaigptconnector.model.FunctionCall;

import java.util.List;

@FunctionCall(name = "parse_pantry_items", functionDescription = "Parses pantry items from input with fitting categories making them easy to find in a list. Does not include anything not in the list, and does not include any items if there is nothing on the list.")
public class ParsePantryItemsFC {

    public static class PantryItem {

        @FCParameter(name = "inputItem", description = "The item given in the input")
        private String item;

        @FCParameter(name = "category", description = "The generic category for the item") // TODO: Maybe send a list of categories, or actually *the* list of categories, since there is already one, along with this or the input to the function call
        private String category;

        public PantryItem() {

        }

        public String getItem() {
            return item;
        }

        public String getCategory() {
            return category;
        }

    }

    @FCParameter()
    private List<PantryItem> pantryItems;

    public ParsePantryItemsFC() {

    }

    public List<PantryItem> getBarItems() {
        return pantryItems;
    }

}

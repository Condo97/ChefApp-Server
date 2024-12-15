package com.pantrypro.networking.client.oaifunctioncall.parsepantryitems;

import com.oaigptconnector.model.JSONSchema;
import com.oaigptconnector.model.JSONSchemaParameter;

import java.util.List;

@JSONSchema(name = "parse_pantry_items", functionDescription = "Parses pantry items from input with fitting categories making them easy to find in a list. Does not include anything not in the list, and does not include any items if there is nothing on the list.", strict = JSONSchema.NullableBool.TRUE)
public class ParsePantryItemsSO {

    public static class PantryItem {

        @JSONSchemaParameter(name = "inputItem", description = "The item given in the input")
        private String item;

        @JSONSchemaParameter(name = "category", description = "The generic category for the item") // TODO: Maybe send a list of categories, or actually *the* list of categories, since there is already one, along with this or the input to the function call
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

    @JSONSchemaParameter()
    private List<PantryItem> pantryItems;

    public ParsePantryItemsSO() {

    }

    public List<PantryItem> getBarItems() {
        return pantryItems;
    }

}

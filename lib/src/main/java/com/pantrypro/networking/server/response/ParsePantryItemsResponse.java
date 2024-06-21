package com.pantrypro.networking.server.response;

import java.util.List;

public class ParsePantryItemsResponse {

    public static class PantryItem {

        private String item;
        private String category;

        public PantryItem() {

        }

        public PantryItem(String item, String category) {
            this.item = item;
            this.category = category;
        }

        public String getItem() {
            return item;
        }

        public String getCategory() {
            return category;
        }

    }

    private List<PantryItem> pantryItems;

    public ParsePantryItemsResponse() {

    }

    public ParsePantryItemsResponse(List<PantryItem> pantryItems) {
        this.pantryItems = pantryItems;
    }

    public List<PantryItem> getPantryItems() {
        return pantryItems;
    }

}

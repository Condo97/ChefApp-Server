package com.pantrypro;

public class DBRegistry {

    public class Table {

//        public class IdeaRecipe {
//            public static final String TABLE_NAME = "IdeaRecipe";
//            public static final String idea_id = "idea_id";
//            public static final String user_id = "user_id";
//            public static final String input = "input";
//            public static final String name = "name";
//            public static final String summary = "summary";
//            public static final String cuisine_type = "cuisine_type";
//            public static final String expand_ingredients_magnitude = "expand_ingredients_magnitude";
//            public static final String date = "date";
//        }
//
//        public class IdeaRecipeEquipment {
//            public static final String TABLE_NAME = "IdeaRecipeEquipment";
//            public static final String equipment_id = "equipment_id";
//            public static final String idea_id = "idea_id";
//            public static final String name = "name";
//        }
//
//        public class IdeaRecipeIngredient {
//            public static final String TABLE_NAME = "IdeaRecipeIngredient";
//            public static final String ingredient_id = "ingredient_id";
//            public static final String idea_id = "idea_id";
//            public static final String name = "name";
//        }

        public class APNSRegistration {

            public static final String TABLE_NAME = "APNSRegistration";
            public static final String id = "id";
            public static final String user_id = "user_id";
            public static final String device_id = "device_id";
            public static final String add_date = "add_date";
            public static final String update_date = "update_date";

        }

        public class Recipe {
            public static final String TABLE_NAME = "Recipe";
            public static final String recipe_id = "recipe_id";
            public static final String user_id = "user_id";
            public static final String input = "input";
            public static final String name = "name";
            public static final String summary = "summary";
            public static final String cuisine_type = "cuisine_type";
            public static final String expand_ingredients_magnitude = "expand_ingredients_magnitude";
            public static final String estimated_total_calories = "estimated_total_calories";
            public static final String estimated_total_minutes = "estimated_total_minutes";
            public static final String estimated_servings = "estimated_servings";
            public static final String feasibility = "feasibility";
            public static final String creation_date = "creation_date";
            public static final String modify_date = "creation_date";
            public static final String image_url = "image_url";
            public static final String likes_count = "likes_count";
            public static final String dislikes_count = "dislikes_count";
        }

        public class RecipeInstruction {
            public static final String TABLE_NAME = "RecipeInstruction";
            public static final String instruction_id = "instruction_id";
            public static final String recipe_id = "recipe_id";
            public static final String text = "text";
            public static final String index = "recipe_index";
        }

//        public class RecipeEquipment {
//            public static final String TABLE_NAME = "RecipeEquipment";
//            public static final String equipment_id = "equipment_id";
//            public static final String recipe_id = "recipe_id";
//            public static final String name = "name";
//        }

        public class RecipeMeasuredIngredient {
            public static final String TABLE_NAME = "RecipeMeasuredIngredient";
            public static final String ingredient_id = "ingredient_id";
            public static final String recipe_id = "recipe_id";
            public static final String measuredIngredient = "measuredIngredient";
            public static final String measurement = "measurement";
        }

        public class RecipeTag {
            public static final String TABLE_NAME = "RecipeTag";
            public static final String tag_id = "tag_id";
            public static final String idea_id = "recipe_id";
            public static final String tag = "tag";
        }

        public class Transaction {

            public static final String TABLE_NAME = "Transaction";
            public static final String transaction_id = "transaction_id";
            public static final String user_id = "user_id";
            public static final String appstore_transaction_id = "appstore_transaction_id";
            public static final String transaction_date = "transaction_date";
            public static final String record_date = "record_date";
            public static final String check_date = "check_date";
            public static final String status = "status";

        }

        public class Receipt {

            public static final String TABLE_NAME = "Receipt";
            public static final String receipt_id = "receipt_id";
            public static final String user_id = "user_id";
            public static final String receipt_data = "receipt_data";
            public static final String record_date = "record_date";
            public static final String check_date = "check_date";
            public static final String expired = "expired";

        }

        public class User_AuthToken {
            public static final String TABLE_NAME = "User_AuthToken";
            public static final String user_id = "user_id";
            public static final String auth_token = "auth_token";

        }

    }

}

package com.pantrypro;

import com.oaigptconnector.model.Role;

import java.net.URI;

public final class Constants {

    private Constants() {
    }

    /* In-App Purchases Pricing */
    public static final int DEFAULT_PRICE_INDEX = 0;
    public static final String WEEKLY_PRICE = "3.99";
    public static final String WEEKLY_NAME = "pantryproultraweekly";
    public static final String MONTHLY_PRICE = "12.99";
    public static final String MONTHLY_NAME = "pantryproultramonthly";
    public static final String YEARLY_PRICE = "49.99";
    public static final String YEARLY_NAME = "pantryproultrayearly";

    /* Tiered Limits */
    public static final int Response_Token_Limit_Categorize_Ingredients = 800;
    public static final int Response_Token_Limit_Create_Recipe = 800;
    public static final int Response_Token_Limit_Generate_Directions = 800;
    public static final int Response_Token_Limit_Finalize_Recipe = 800;
    public static final int Response_Token_Limit_Tag_Recipe = 800;

    public static final int Context_Character_Limit_Categorize_Ingredients = 800;
    public static final int Context_Character_Limit_Create_Recipe_Idea = 800;
    public static final int Context_Character_Limit_Generate_Directions = 800;
    public static final int Context_Character_Limit_Get_Ingredients_Without_Measurements = 800;
    public static final int Context_Character_Limit_Make_Recipe = 800;
    public static final int Context_Character_Limit_Tag_Recipe_Idea = 800;

    /* Delays and Cooldowns */
    public static final int Transaction_Status_Apple_Update_Cooldown = 60;

    /* Caps */
    public static final int Cap_Free_Total_Essays = 3; // This is just a constant sent to the device, which handles everything
    public static final Integer Create_Recipe_Idea_Cap_Daily_Free = 10;
    public static final Integer Create_Recipe_Idea_Cap_Daily_Paid = null;
//    public static final Integer Make_Recipe_Cap_Daily_Free = 10;
//    public static final Integer Make_Recipe_Cap_Daily_Paid = null;
    public static final Integer Regenerate_Recipe_Directions_And_Idea_Recipe_Ingredients_Cap_Daily_Free = 1;
    public static final Integer Regenerate_Recipe_Directions_And_Idea_Recipe_Ingredients_Cap_Daily_Paid = null;
    public static final int Cap_Chat_Daily_Paid_Legacy = -1; //-1 is unlimited

    /* URIs for HTTPSServer */
    class URIs {
        public static final String CATEGORIZE_INGREDIENTS = "/categorizeIngredients";
        public static final String CREATE_RECIPE_IDEA = "/createRecipeIdea";
        public static final String MAKE_RECIPE = "/makeRecipeFromIdea";
        public static final String REGENERATE_RECIPE_DIRECTIONS_AND_IDEA_RECIPE_INGREDIENTS = "/regenerateRecipeDirectionsAndIdeaRecipeIngredients";
        public static final String TAG_RECIPE_IDEA = "/tagRecipeIdea";
        public static final String GET_ALL_TAGS_URI = "/getAllTags";
        public static final String GET_IAP_STUFF_URI = "/getIAPStuff";
        public static final String GET_IMPORTANT_CONSTANTS_URI = "/getImportantConstants";
        public static final String GET_IS_PREMIUM_URI = "/getIsPremium";
        public static final String GET_REMAINING_URI = "/getRemaining";
        public static final String REGISTER_USER_URI = "/registerUser";
        public static final String REGISTER_TRANSACTION_URI = "/registerTransaction";
        public static final String VALIDATE_AUTH_TOKEN_URI = "/validateAuthToken";
    }

    /* Legacy URIs for HTTPServer */
    public static final String GET_DISPLAY_PRICE_URI = "/getDisplayPrice";
    public static final String GET_SHARE_URL_URI = "/getShareURL";
    public static final String VALIDATE_AND_UPDATE_RECEIPT_URI_LEGACY = "/validateAndUpdateReceipt";

    /* Share URL */
    public static final String SHARE_URL = "https://apps.apple.com/us/app/chit-chat-ai-writing-author/id1664039953";

    /* Policy Retrieval Constants */

    /* MySQL Constants */
    public static final String MYSQL_URL = "jdbc:mysql://localhost:3306/pantrypro_schema?autoReconnect=true";

    /* Apple Server Constants */
    public static final String Apple_Bundle_ID = "com.acapplications.PantryPal";
    public static final String Apple_Sandbox_Storekit_Base_URL = "https://api.storekit-sandbox.itunes.apple.com";
    public static final String Apple_Storekit_Base_URL = "https://api.storekit.itunes.apple.com";
    public static final String Apple_In_Apps_URL_Path = "/inApps";
    public static final String Apple_V1_URL_Path = "/v1";
    public static final String Apple_Subscriptions_URL_Path = "/subscriptions";
    public static final String Apple_Get_Subscription_Status_V1_Full_URL_Path = Apple_In_Apps_URL_Path + Apple_V1_URL_Path + Apple_Subscriptions_URL_Path;
    public static final String Apple_SubscriptionKey_JWS_Path = "keys/SubscriptionKey_253R52D9UP.p8";

    public static final String Sandbox_Apple_Verify_Receipt_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
    public static final String Apple_Verify_Receipt_URL = "https://buy.itunes.apple.com/verifyReceipt";
    public static long APPLE_TIMEOUT_MINUTES = 4;

    /* ChatSonic Server Constants */
    public static URI CHATSONIC_URI = URI.create("https://api.writesonic.com/v2/business/content/chatsonic?engine=premium");

    /* Tag Fetcher */
    public static String Tag_List_Location = "recipeTags.csv";

    /* OpenAI Constants */
    public static URI OPENAI_URI = URI.create("https://api.openai.com/v1/chat/completions");
    public static long AI_TIMEOUT_MINUTES = 4;
    public static String DEFAULT_MODEL_NAME = "gpt-3.5-turbo";//"gpt-4";
    public static String PAID_MODEL_NAME = "gpt-4";
    public static String DEFAULT_BEHAVIOR = null;
    public static Role LEGACY_DEFAULT_ROLE = Role.USER;
    public static int DEFAULT_TEMPERATURE = 0;

}

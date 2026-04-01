# ChefApp Server — API & Integration Patterns

## OpenAI Structured Outputs

### How It Works
All AI generation uses OpenAI's JSON Schema structured output format. The `OAIGPTConnector` library handles serialization.

### Creating a New Structured Output

1. Create a class in `openai/structuredoutput/` with `@JSONSchema` annotations:
```java
@JSONSchema(functionDescription = "Description for OpenAI", strict = JSONSchema.NullableBool.TRUE)
public class MyFeatureSO {
    @JSONSchemaParameter(description = "What this field contains")
    private String fieldName;
    // getter required
    public String getFieldName() { return fieldName; }
}
```

2. Call from business logic:
```java
MyFeatureSO result = PantryPro.getStructuredOutput(
    MyFeatureSO.class,
    systemMessage,  // OAIChatCompletionRequestMessage
    userMessage     // OAIChatCompletionRequestMessage
);
```

3. Build messages with `OAIChatCompletionRequestMessageBuilder`:
```java
OAIChatCompletionRequestMessage msg = new OAIChatCompletionRequestMessageBuilder(CompletionRole.USER)
    .addText("prompt text")
    .build();
```

### Existing Structured Outputs
| Class | Purpose | Key Fields |
|-------|---------|------------|
| CreateRecipeIdeaEM0SO | Recipe idea, no expansion | name, summary, ingredients, cuisineType |
| CreateRecipeIdeaEM1SO | Recipe idea, minimal expansion | name, summary, ingredients, cuisineType |
| CreateRecipeIdeaEM2SO | Recipe idea, creative expansion | name, summary, ingredients, cuisineType |
| FinalizeRecipeSO | Full recipe with directions | instructions, allIngredientsAndMeasurements, calories, minutes, servings, feasibility |
| GenerateMeasuredIngredientsAndDirectionsSO | Regenerate directions | measuredIngredients, directions, estimatedServings, feasibility |
| CategorizeIngredientsSO | Shopping list categories | ingredientsWithCategories (ingredient + category) |
| TagRecipeSO | Recipe tagging | tags (2-5 from predefined list) |
| ParsePantryItemsSO | Pantry item parsing | items with categories |
| ParseIngredientsFC | Ingredient parsing (legacy function call format) | parsed ingredients |

> **Note:** The `CreateRecipeIdea` SO classes (EM0, EM1, EM2) live in a subdirectory `openai/structuredoutput/createrecipeidea/` and implement the `CreateRecipeIdeaSO` interface.

### Model Selection
- `Constants.DEFAULT_MODEL_NAME` = `gpt-4o-mini` (free tier)
- `Constants.PAID_MODEL_NAME` = `gpt-4o` (premium tier)
- Currently all calls use DEFAULT_MODEL_NAME — premium model selection not yet wired into getStructuredOutput()

### Configuration
- Temperature: 1 (`Constants.DEFAULT_TEMPERATURE`)
- Max response tokens: 4000 (`Constants.Response_Token_Limit`)
- Timeout: 4 minutes (`Constants.AI_TIMEOUT_MINUTES`)
- HTTP/2 client with connection timeout matching AI timeout

## Adding a New Endpoint

### Step-by-step Pattern

1. **Request DTO** in `networking/server/request/`:
```java
public class MyFeatureRequest {
    private String authToken;
    private String myField;
    // Jackson needs: default constructor + getters
}
```

2. **Response DTO** in `networking/server/response/`:
```java
public class MyFeatureResponse {
    private String result;
    // constructor + getter
}
```

3. **Endpoint class** in `networking/endpoints/`:
```java
public class MyFeatureEndpoint implements Endpoint<MyFeatureRequest> {
    @Override
    public Object getResponse(MyFeatureRequest request) throws Exception {
        // Validate auth
        Integer userID = UserAuthenticator.getUserIDFromAuthToken(request.getAuthToken());
        // Business logic
        // Return response DTO
        return new MyFeatureResponse(result);
    }
}
```

4. **Register route** in `Main.java`:
```java
post("/myFeature", (req, res) -> Server.respond(req, MyFeatureRequest.class, new MyFeatureEndpoint()));
```

> **Note:** There are TWO endpoint patterns in the codebase:
> - **(a) `Endpoint<R>` interface** with `Server.respond()` — the newer pattern shown above.
> - **(b) Static methods in `Server.Func`** referenced directly in routes — an older pattern. For example, `CreateRecipeIdeaEndpoint` uses this static method pattern.

5. **Add URI constant** in `Constants.URIs`:
```java
public static final String MY_FEATURE = "/myFeature";
```

## Apple StoreKit 2 Integration

### Subscription Validation Flow
```
1. Client sends registerTransaction with authToken + appStoreTransactionID
2. AppleTransactionUpdater orchestrates validation + DB update
3. SubscriptionAppleHttpClient (external library) queries Apple's StoreKit 2 API:
   GET {storekit_base}/inApps/v1/subscriptions/{transactionID}
   Authorization: Bearer {JWT signed with SubscriptionKey .p8}
4. Response decoded: AppStoreStatusResponse → subscription status
5. TransactionPersistentAppleUpdater saves status to DB
6. IsPremiumFromAppStoreSubscriptionStatus maps status → boolean
```

### Key Files
- `AppleTransactionUpdater.java` — orchestrates validation + DB update (calls Apple API via SubscriptionAppleHttpClient)
- `TransactionDownloader.java` — empty/unused
- `KeyReader.java` — reads .p8 private keys for JWT signing

### Subscription Statuses
| Status | Value | Premium? |
|--------|-------|----------|
| ACTIVE | 1 | Yes |
| BILLING_GRACE | 4 | Yes |
| BILLING_RETRY | 3 | No |
| EXPIRED | 2 | No |
| REVOKED | 5 | No |
| INVALID | 0 | No |

### Environment URLs
- Sandbox: `api.storekit-sandbox.itunes.apple.com`
- Production: `api.storekit.itunes.apple.com`

## Apple Push Notifications (APNS)

### Flow
```
1. Client registers device token via /registerAPNS
2. APNSRegistrationDAOPooled saves user_id + device_id
3. /sendPushNotification triggers APNSClient
4. APNSJWTGenerator creates JWT signed with AuthKey .p8
5. HTTP/2 POST to api.push.apple.com:443 (or sandbox)
```

### Key Files
- `APNSClient.java` — sends push notification
- `APNSJWTGenerator.java` — creates auth JWT
- `APNSRequest.java` — notification payload

## TikTok API (via TikAPI.io)

### Search
```
GET https://api.tikapi.io/public/search/{category}
Headers: X-API-KEY: {tikapi_key}
Path params: category (VIDEOS|USERS|GENERAL|AUTOCOMPLETE)
Query params: query, nextCursor
```

### Video Info
```
GET https://api.tikapi.io/public/video
Headers: X-API-KEY: {tikapi_key}
Params: id (video ID)
```

### Key Files
- `TikAPISearchClient.java` — search requests
- `TikAPIGetVideoInfoClient.java` — video info requests
- Response is `GenericTikAPIResponse` (raw JSON passthrough)

## Pinterest Conversion API

### Flow
```
POST https://api.pinterest.com/v5/ad_accounts/{account_id}/events
Authorization: Bearer {pinterest_token}
Body: conversion event data (user data, event name, IDFA)
```

### Key Files
- `PinterestConversionLogger.java` — sends conversion events
- Used for attribution tracking of iOS app installs/actions

## Speech Transcription

### Flow
```
POST https://api.openai.com/v1/audio/transcriptions
Model: whisper-1
Body: multipart/form-data with audio file
Supported formats: flac, mp3, mp4, mpeg, mpga, m4a, ogg, wav, webm
```

### Key Files
- `SpeechTranscriber.java` — handles Whisper API calls

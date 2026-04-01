# ChefApp Server — Architecture Deep Dive

## System Overview

ChefApp Server is a Java 17 REST API backend that powers an iOS recipe generation app. Users provide ingredients and modifiers, and the server uses OpenAI GPT-4o structured outputs to generate recipe ideas, finalize them with measurements and directions, tag them, and manage subscriptions.

## Request Lifecycle

```
iOS App → HTTPS POST (port 800, SSL)
       → Spark Java route (Main.java)
       → Server.Func handler or Server.respond() wrapper
       → ObjectMapper deserializes request JSON → *Request DTO
       → *Endpoint class processes business logic
       → PantryPro.java orchestrates AI/DB calls
       → BodyResponseFactory wraps in BodyResponse {Success, Body}
       → ObjectMapper serializes → JSON response
```

## Route Registration

Routes are registered in `Main.java` using Spark's `path()` grouping:

- `/v1` — versioned production routes (all active endpoints)
- `/dev` — development/testing routes (mirrors v1)
- `/` — legacy routes (deprecated, some still active)

Each route is a `post()` call mapping a URI constant to a handler. Three patterns:
1. **Direct handler**: `post(URI, Server.Func::methodName)` — handler does its own JSON parsing
2. **Generic respond**: `post(URI, (req, res) -> Server.respond(req, RequestClass.class, new EndpointClass()))` — uses the `Endpoint<R>` interface
3. **Inline lambda**: `post(URI, (req, res) -> ...)` — inline lambda responses for simple endpoints like `getCreatePanels`, `getImportantConstants`, `getIAPStuff`

## AI Generation Pipeline

### Structured Output Flow
```
1. Build prompt (system + user messages via OAIChatCompletionRequestMessageBuilder)
2. Call PantryPro.getStructuredOutput(SOClass.class, messages...)
3. SOJSONSchemaSerializer.objectify(SOClass) → JSON Schema
4. OAIChatCompletionRequest.build() with ResponseFormatType.JSON_SCHEMA
5. OAIClient.postChatCompletion() → OpenAI API
6. JSONSchemaDeserializer.deserialize() → typed Java object
```

### Recipe Creation Flow (2-step)
```
Step 1: createSaveRecipeIdea()
  - Validate auth + check daily cap
  - Select expansion level (NONE/DEFAULT/CREATIVE → different SO classes)
  - Generate recipe idea via structured output → name, summary, ingredients
  - Save to DB via RecipeFactoryDAO → returns RecipeWithIngredientsAndDirections

Step 2: finalizeSaveRecipe()
  - Load recipe + ingredients from DB
  - Generate measurements, directions, calories, servings, feasibility
  - Update DB with full recipe details
```

### Expansion Magnitudes
- `0` (NONE): Only use provided ingredients → `CreateRecipeIdeaEM0SO`
- `1` (DEFAULT): Minimal expansion → `CreateRecipeIdeaEM1SO`
- `2` (CREATIVE): Full creative expansion → `CreateRecipeIdeaEM2SO`

## Database Layer

### Connection Management
- MySQL via `mysql-connector-java:8.0.30`
- Connection pool: `SQLConnectionPoolInstance` (4 threads × 4 = 16 max connections)
- Schema: `pantrypro_schema` on localhost:3306

### ORM Pattern
Uses `SQLComponentizer` (custom library) with annotations:
- `@DBSerializable` on entity classes
- `@DBColumn` on fields mapping to table columns
- Serialization/deserialization handled by the library

### DAO Hierarchy

Three independent class types per entity: raw DAO, pooled DAO (connection-pooled wrapper), and factory DAO (complex multi-table operations). These are parallel classes, not an inheritance chain.

- **RecipeDAOPooled**: CRUD for Recipe, RecipeMeasuredIngredient, RecipeInstruction, RecipeTag
- **User_AuthTokenDAOPooled**: User registration and auth token management
- **APNSRegistrationDAOPooled**: Push notification device registration
- **RecipeFactoryDAO**: Complex multi-table operations (create recipe + ingredients + tags)

### Core Tables
| Table | Primary Key | Purpose |
|-------|-------------|---------|
| Recipe | recipe_id | Recipe metadata, calories, servings, feasibility |
| RecipeMeasuredIngredient | ingredient_id | Ingredients with measurements per recipe |
| RecipeInstruction | instruction_id | Ordered cooking steps per recipe |
| RecipeTag | tag_id | Category tags per recipe |
| User_AuthToken | user_id | Auth tokens (DB-based lookup) |
| Transaction | transaction_id | Apple IAP records |
| Receipt | receipt_id | IAP receipts (user_id, receipt_data, record_date, check_date, expired) |
| APNSRegistration | id | Push notification device tokens |

## Authentication

- Simple database token lookup (no JWT)
- `UserAuthenticator.getUserIDFromAuthToken(authToken)` queries the `User_AuthToken` table by `auth_token` string and returns the `user_id`
- Every authenticated endpoint receives `authToken` in the request body
- No session management — stateless token validation via DB lookup

## Subscription Tiers

| Tier | Model | Daily Recipe Cap | Regen Cap |
|------|-------|-----------------|-----------|
| Free | gpt-4o-mini | 20 | 1 |
| Premium | gpt-4o | Unlimited | Unlimited |

Premium status determined by Apple StoreKit 2 subscription validation.

## Exception Handling

Spark exception handlers in Main.java catch and return structured error responses:
- `ResponseStatusException` → custom status codes
- `IllegalArgumentException` → invalid argument errors
- `JsonMappingException` → 400-style malformed JSON
- `OpenAIGPTException` → AI generation failures
- `Exception` (catch-all) → 500-style generic error
- 404 handler for unknown routes

All errors wrapped in `BodyResponse` with `ResponseStatus` and `ErrorResponse`. Error status is communicated via the JSON body `ResponseStatus` field, not HTTP status codes.

# ChefApp Server — Enterprise Evolution Roadmap

## Vision
Become the #1 AI-powered recipe generation app. Every improvement should make recipe generation faster, smarter, more reliable, or more delightful.

## Priority 1: Foundation (Enterprise Readiness)

### 1.1 Test Coverage
- [ ] Unit tests for all endpoint handlers
- [ ] Unit tests for PantryPro business logic (mock OpenAI responses)
- [ ] Integration tests for RecipeFactoryDAO operations
- [ ] Integration tests for the full recipe creation flow (idea → finalize → tag)
- [ ] Test structured output deserialization with sample JSON responses
- [ ] Migrate from Java assert() to JUnit assertions (assertEquals, assertNotNull) — current assert() can be silently disabled at runtime

### 1.2 Security Hardening
- [ ] CRITICAL: Remove sharedAppSecret from GetIAPStuffResponse — this secret is currently returned in an API response to clients
- [ ] Add authorization checks on UpdateRecipeImageURLEndpoint (currently any user can update any recipe's image)
- [ ] Add authorization checks on all mutation endpoints — audit for missing user-recipe association validation
- [ ] Migrate all secrets from Constants.java/Keys.java to environment variables
- [ ] Add input validation and sanitization on all endpoints
- [ ] Rate limiting per user (beyond daily caps)
- [ ] SQL injection audit on any raw query paths
- [ ] HTTPS certificate rotation plan

### 1.3 Error Handling & Resilience
- [ ] Structured error response format with error codes
- [ ] Retry logic for OpenAI API failures (with exponential backoff)
- [ ] Retry logic for Apple StoreKit API failures
- [ ] Circuit breaker pattern for external API dependencies
- [ ] Graceful degradation when OpenAI is down (queued generation)

### 1.4 Observability
- [ ] Replace System.out.println with structured logging (Log4j2 is in deps but underused)
- [ ] Request/response logging with correlation IDs
- [ ] Metrics: generation latency, success/failure rates, daily active users
- [ ] Health check endpoint (/health)
- [ ] Database connection pool monitoring

## Priority 1.5: Code Quality

- [ ] Extract business logic from endpoint classes into service layer (ParsePantryItemsEndpoint, APNSRegistrationEndpoint contain logic that should be in core/)
- [ ] Clean up legacy code in core/generationlegacy/ (commented-out code, deprecated patterns)
- [ ] Move hardcoded MySQL URL from Constants.java to environment config

## Priority 2: Performance & Scalability

### 2.1 Connection Pool & Threading
- [ ] Tune connection pool size based on actual load (currently 16 max)
- [ ] Async endpoint handlers where Spark supports it
- [ ] Connection pool health monitoring and auto-recovery

### 2.2 Caching
- [ ] Cache tag list (recipeTags.csv) in memory (already done partially)
- [ ] Cache premium status with TTL (reduce Apple API calls)
- [ ] Response caching for getAllTags, getImportantConstants, getIAPStuff

### 2.3 Database
- [ ] Add database indexes for common queries (user_id on Recipe, recipe_id on ingredients/instructions)
- [ ] Database migration tool (Flyway or Liquibase)
- [ ] Connection pool leak detection

## Priority 3: Feature Enhancement

### 3.1 Recipe Intelligence
- [ ] Dietary restriction filters (vegan, gluten-free, keto, etc.)
- [ ] Nutritional breakdown per ingredient (not just total calories)
- [ ] Recipe difficulty scoring (beyond feasibility)
- [ ] Cuisine type filtering and recommendation
- [ ] Recipe variation suggestions ("try it with...")

### 3.2 Meal Planning
- [ ] Weekly meal plan generation from pantry items
- [ ] Grocery list generation from meal plans
- [ ] Calorie/macro targets for meal plans
- [ ] Meal prep instructions (batch cooking)

### 3.3 Social & Sharing
- [ ] Recipe collections/cookbooks
- [ ] Public recipe sharing with unique URLs
- [ ] Recipe rating and feedback loop
- [ ] Community recipe discovery feed

### 3.4 Smart Pantry
- [ ] Persistent pantry inventory per user
- [ ] Receipt/photo scanning to update pantry (vision API)
- [ ] Expiring ingredient alerts
- [ ] "What can I make?" from current pantry

### 3.5 Multi-Modal
- [ ] Recipe image generation (DALL-E or similar)
- [ ] Video recipe instructions (TikTok-style)
- [ ] Voice-guided cooking mode (Whisper + TTS)
- [ ] Photo-to-recipe (vision API: "what's this dish?")

## Priority 4: Platform & Infrastructure

### 4.1 API Documentation
- [ ] OpenAPI/Swagger spec for all endpoints
- [ ] Auto-generated API docs from annotations
- [ ] Postman collection for testing

### 4.2 Deployment
- [ ] Containerization (Docker)
- [ ] CI/CD pipeline (GitHub Actions)
- [ ] Staging environment
- [ ] Blue-green deployment support
- [ ] Automated database backups

### 4.3 Monitoring & Alerting
- [ ] Uptime monitoring
- [ ] Error rate alerting
- [ ] OpenAI cost tracking
- [ ] User growth metrics dashboard

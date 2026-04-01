# ChefApp Server (PantryPro)

## Mission
Build the #1 AI-powered recipe generation app on the internet. This project is developed exclusively by Claude Code Opus 4.6 Max agents. Every decision — architecture, features, quality — should push toward enterprise-grade reliability and the best recipe generation experience available.

## Stack
Java 17 · Gradle 8.1 · Spark Java · MySQL · OpenAI GPT-4o (structured outputs) · Apple StoreKit 2 · APNS · TikTok API · Pinterest Conversions · OpenAI Whisper

## Commands
```bash
./gradlew build                    # Build project
./gradlew test                     # Run all tests
./gradlew run                      # Run server (port 800, SSL)
./gradlew shadowJar                # Build fat JAR → lib/out/PantryPro_Server.jar
java -jar lib/out/PantryPro_Server.jar  # Run production JAR
```

## Architecture
```
lib/src/main/java/com/pantrypro/
├── Main.java                      # Entry point
├── Constants.java                 # All config: API keys, URLs, caps, model names
├── core/
│   ├── Server.java                # Spark route registration & exception handling
│   ├── PantryPro.java             # Core logic: recipe generation, structured output calls
│   ├── Endpoint.java              # Generic endpoint interface<R>
│   ├── generation/                # AI generation logic (ingredients, tagging)
│   └── purchase/                  # Apple IAP validation & subscription management
├── networking/
│   ├── endpoints/                 # All HTTP endpoint handlers (one class per endpoint)
│   └── server/request|response/   # Request/response DTOs
├── openai/
│   ├── structuredoutput/          # GPT structured output schema classes
│   └── SpeechTranscriber.java     # Whisper integration
├── database/
│   ├── dao/                       # Data access objects (pooled + factory patterns)
│   ├── objects/                   # Entity models (Recipe, User, Transaction, etc.)
│   └── adapters/                  # OpenAI response → DB object adapters
├── tikapi/                        # TikTok API client
└── connectionpool/                # MySQL connection pooling
```

**Key patterns:**
- Endpoints implement `Endpoint<R>` interface, return response via `BodyResponseFactory`
- AI calls go through `PantryPro.getStructuredOutput()` → OAIGPTConnector → OpenAI API
- All DB access uses pooled DAOs via `SQLComponentizer` ORM annotations
- Free tier: `gpt-4o-mini` (20 recipes/day). Premium: `gpt-4o` (unlimited)

## Conventions
- MUST use structured outputs (not plain completions) for all new AI generation
- MUST route all OpenAI calls through `PantryPro.getStructuredOutput()`
- MUST add new endpoints to `Server.java` route registration
- MUST use pooled DAO pattern for all database access
- Prefer creating new `*SO.java` classes in `openai/structuredoutput/` for new AI features
- Prefer `BodyResponseFactory` for all endpoint responses
- Package name is `com.pantrypro` — do NOT rename to match "ChefApp"

## Things That Will Bite You
- Port is 800 (not 8080) — SSL required, cert is `chitchatserver.com.jks`
- Apple bundle ID is `com.acapplications.PantryPal` (not PantryPro or ChefApp)
- `OAIGPTConnector` and `SQLComponentizer` are custom GitHub libs, not on Maven Central
- OpenAI timeout is 4 minutes — long-running generations are expected
- Connection pool is 4 threads × 4 = 16 max connections — watch for exhaustion
- `Constants.java` holds API keys inline — MUST migrate to env vars for enterprise

## Verification
Before considering any task complete:
1. `./gradlew build` MUST pass with no errors
2. `./gradlew test` MUST pass all tests
3. New endpoints MUST be registered in `Server.java`
4. New structured outputs MUST have a corresponding `*SO.java` class
5. No hardcoded secrets in new code — use Constants pattern (migrate to env vars)

## Workflow
- Branch from `main`, use descriptive branch names: `feature/`, `fix/`, `refactor/`
- Commit messages: imperative mood, concise ("Add recipe sharing endpoint")
- MUST run verification before committing
- Create PR with summary + test plan for any non-trivial change

## External Integrations
- **OpenAI**: Structured outputs via `OAIGPTConnector`. Models in `Constants.java`. Schema classes in `openai/structuredoutput/`
- **Apple**: StoreKit 2 for subscriptions, APNS for push. Keys are `.p8` files. Sandbox + production URLs in Constants
- **TikTok**: TikAPI.io proxy. Search + video info. API key via header
- **Pinterest**: Conversion API for attribution tracking

## Strategic Priorities
This app MUST evolve to enterprise quality. Current priorities in order:
1. **Testing** — Expand test coverage to all endpoints and generation logic
2. **Security** — Migrate secrets from Constants.java to environment variables
3. **Error handling** — Structured error responses, retry logic for external APIs
4. **Performance** — Connection pool tuning, response caching, async generation
5. **Features** — Meal planning, dietary restrictions, social sharing, recipe collections
6. **Observability** — Structured logging, metrics, health checks
7. **Documentation** — OpenAPI/Swagger spec for all endpoints

@docs/architecture.md
@docs/api-patterns.md
@docs/roadmap.md

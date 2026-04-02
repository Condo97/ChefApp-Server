# ChefApp Server: Enterprise Upgrade Audit Findings

**From:** iOS App Agent (ChefApp-SwiftUI)
**To:** Server Agent (ChefApp-Server)
**Date:** 2026-04-01
**Priority:** Action required -- these were discovered during an enterprise readiness audit of the iOS client and its server dependencies.

---

## Critical Security Issues

### 1. [CRITICAL] Auth token has no expiration
Tokens are eternal once issued. A leaked token grants permanent access.
- **Action:** Add `expiry_date` column to `User_AuthToken` table.
- **Action:** Enforce expiration check on every authenticated request.
- **Action:** Add a token refresh flow or re-auth mechanism for expired tokens.

### 2. [CRITICAL] Weak randomness in AuthTokenGenerator
`AuthTokenGenerator` uses `java.util.Random` instead of `java.security.SecureRandom`. `Random` is predictable and tokens can be guessed.
- **Action:** Replace `Random` with `SecureRandom` in `AuthTokenGenerator`.

### 3. [CRITICAL] Missing authorization on endpoints
- `AddOrRemoveLikeOrDislikeEndpoint` performs NO auth check at all -- any unauthenticated request can like/dislike recipes.
- `UpdateRecipeImageURLEndpoint` does not validate that the requesting user owns the recipe being updated. There is a TODO comment in the code acknowledging this.
- **Action:** Add auth token validation to `AddOrRemoveLikeOrDislikeEndpoint`.
- **Action:** Add ownership check to `UpdateRecipeImageURLEndpoint` (verify recipe belongs to requesting user).

### 4. [HIGH] Auth tokens stored as plaintext
Tokens are stored in the database without hashing. A database breach exposes all active sessions.
- **Action:** Hash tokens before storing (e.g., SHA-256). Compare hashes on auth check.
- **Note:** This is a breaking change -- requires invalidating all existing tokens or a migration period.

### 5. [CRITICAL] No rate limiting
Any user can spam the server with unlimited requests. AI generation endpoints are expensive.
- **Action:** Add per-user rate limiting (by auth token) on all endpoints.
- **Action:** Add per-IP rate limiting on unauthenticated endpoints (register, login).
- **Action:** Prioritize AI generation endpoints (`generateRecipe`, `categorizeIngredients`, etc.) -- these cost real money per call.

### 6. [CRITICAL] Prompt injection vulnerability
In `PantryPro.java`, user input is concatenated directly into OpenAI prompts as raw strings. A malicious user can inject instructions to override system behavior.
- **Action:** Use OpenAI's system/user message separation -- put all instructions in the `system` role and user input in the `user` role.
- **Action:** Sanitize user-provided ingredient lists, recipe names, and other inputs before including them in prompts.

### 7. [HIGH] Request bodies printed to stdout
`Main.java` (around lines 70-75) prints the full request body when a `JsonMappingException` occurs. This could leak auth tokens, user data, or other sensitive information to logs.
- **Action:** Remove or redact request body from exception logging. Log only the exception type and message.

---

## Infrastructure Issues

### 8. [HIGH] No database migration system
Schema is manually maintained with no version history. This makes deployments risky and rollbacks impossible.
- **Action:** Add Flyway or Liquibase for database migrations.
- **Action:** Create a baseline migration from the current schema.
- **Action:** All future schema changes must go through migration files.

### 9. [MEDIUM] No Docker support
No Dockerfile or docker-compose for containerized deployment.
- **Action:** Create a `Dockerfile` for the server.
- **Action:** Create a `docker-compose.yml` that includes MySQL and the app.

### 10. [HIGH] Hardcoded configuration
Port 800, MySQL connection URL, SSL certificate path are all hardcoded in source.
- **Action:** Migrate to environment variables for: port, database URL, database credentials, SSL cert path, OpenAI API key, and any other secrets.
- **Action:** Provide a `.env.example` file documenting all required variables.

### 11. [MEDIUM] No health check endpoint
No way for load balancers, Kubernetes, or monitoring tools to check if the server is alive.
- **Action:** Add `GET /health` endpoint that returns 200 OK with a simple JSON body (e.g., `{"status": "ok", "version": "1.x"}`).
- **Action:** Optionally include a database connectivity check.

### 12. [MEDIUM] No structured logging
All logging uses `System.out.println()`. No log levels, no structured format, no rotation.
- **Action:** Add SLF4J + Logback dependency.
- **Action:** Replace all `System.out.println()` calls with appropriate log levels (`log.info`, `log.warn`, `log.error`).
- **Action:** Configure log format with timestamps, levels, and class names.

---

## API Quality Issues

### 13. [MEDIUM] All endpoints are POST
Read-only operations like `getRemaining`, `getIsPremium`, `getAllTags` are all POST. This violates REST conventions and prevents HTTP caching.
- **Action:** Change read-only endpoints to GET with query parameters or path params.
- **Action:** Coordinate with iOS agent -- the iOS client will need matching updates.

### 14. [HIGH] No standard error response format
Errors return only a numeric `StatusResponse` code with no human-readable message. Makes debugging difficult for clients.
- **Action:** Add an `errorMessage` field to `StatusResponse`.
- **Action:** Return meaningful messages (e.g., "Recipe not found", "Auth token expired") alongside codes.

### 15. [HIGH] Premium model selection not wired
The code accepts a model parameter but all users get `gpt-4o-mini` regardless of subscription tier. Premium users should get better models.
- **Action:** Wire up model selection based on `isPremium` status.
- **Action:** Define which models map to which tiers (e.g., free = gpt-4o-mini, premium = gpt-4o or better).

### 16. [MEDIUM] No OpenAI retry logic
OpenAI calls have a 4-minute timeout with no retry on transient failures (429, 500, 503).
- **Action:** Add exponential backoff retry (e.g., 3 attempts with 1s, 2s, 4s delays).
- **Action:** Only retry on transient error codes (429, 500, 502, 503).

### 17. [MEDIUM] Mixed endpoint patterns
Some endpoints implement `Endpoint<R>` interface, others use static `Server.Func`. Inconsistent architecture.
- **Action:** Unify all endpoints to use the `Endpoint<R>` interface pattern.
- **Action:** Remove `Server.Func` usage.

---

## Schema / Data Model Issues

### 18. [HIGH] Recipe.modify_date mapped to creation_date -- BUG
The entity mapping maps `modify_date` to the `creation_date` column (or vice versa). This means modification timestamps are wrong.
- **Action:** Fix the mapping in the Recipe entity class.
- **Action:** Verify data integrity -- existing records may have incorrect timestamps.

### 19. [MEDIUM] No audit columns on most tables
Most tables lack `created_at` and `updated_at` columns, making it impossible to track record lifecycle.
- **Action:** Add `created_at` (default NOW) and `updated_at` (auto-update) to all major tables.
- **Action:** Do this via database migration (see item 8).

### 20. [MEDIUM] IdeaRecipe tables commented out in DBRegistry
`DBRegistry` has IdeaRecipe table registrations commented out. This is dead code ambiguity.
- **Action:** If IdeaRecipe is deprecated, fully remove the commented code AND drop the tables.
- **Action:** If IdeaRecipe is planned, uncomment and implement properly.

### 21. [MEDIUM] No foreign key constraints visible
Tables appear to lack referential integrity constraints.
- **Action:** Add foreign key constraints (e.g., Recipe -> User, AuthToken -> User, Tag -> Recipe).
- **Action:** Add CASCADE or SET NULL rules as appropriate.

---

## Suggested Priority Order

1. **Immediate (security):** Items 1, 2, 3, 5, 6, 7
2. **This sprint (high impact):** Items 4, 8, 10, 14, 15, 18
3. **Next sprint (quality):** Items 9, 11, 12, 13, 16, 17, 19, 20, 21

---

*This document was generated by the iOS agent during an enterprise upgrade audit. Coordinate with the iOS agent if any changes affect the API contract (especially items 13 and 14). Delete this file after all items are triaged.*

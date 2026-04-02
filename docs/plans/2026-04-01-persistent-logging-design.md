# PantryPro Persistent Logging System Design

Based on WriteSmith's battle-tested logging architecture. Two-tier system: session-level logs + per-request AI call logs, all in plain text files readable by AI agents and humans.

## Architecture

```
logs/
├── session_2026-04-01_22-30-36/
│   ├── session.log          # All log entries [TIME] [COMPONENT] [LEVEL] message
│   ├── errors.log           # WARN + ERROR subset
│   └── requests/
│       ├── openrouter_22-31-05-123_user65432.log   # Full AI request lifecycle
│       ├── openrouter_22-31-08-456_user65432.log
│       └── openrouter_22-35-12-789_user12345.log
├── session_2026-04-01_18-00-00/
│   ├── session.log
│   ├── errors.log
│   └── requests/
└── ...
```

## Component 1: PersistentLogger

Custom file-based logger replacing SLF4J/Log4j2 (which is broken in NOP mode).

### Session lifecycle
- `initialize()` called at server startup, before anything else
- Creates `logs/session_YYYY-MM-DD_HH-mm-ss/` folder
- Opens `session.log` and `errors.log` writers
- Writes session header with startup timestamp and folder path
- Intercepts `System.out` and `System.err` to capture library output
- `shutdown()` flushes and closes writers

### Log format
```
[HH:mm:ss.SSS] [COMPONENT] [LEVEL] message
```

### Components
`SERVER`, `CONSOLE`, `OPENROUTER`, `DATABASE`, `AUTH`, `APPLE`, `RECIPE`, `RATE_LIMIT`, `SERPER`, `API`

### Methods
- `log(component, level, message)` — core write
- `info(component, message)`, `warn(component, message)`, `error(component, message)`
- `error(component, message, Throwable)` — includes stack trace
- `logJson(component, message, Object)` — pretty-prints JSON
- `getRequestsFolder()` — returns path for request loggers

### Auto-cleanup
Keep last 30 session folders. Delete oldest on startup.

## Component 2: OpenRouterRequestLogger

One instance per AI call. Creates a dedicated log file capturing the full request/response lifecycle.

### Filename
`requests/openrouter_HH-mm-ss-SSS_userXXXXX.log`

### Sections logged (in order)
1. **Header** — request ID, user ID, start time
2. **Client context** — endpoint called, SO class name, ingredients/modifiers (sanitized)
3. **Authentication** — success/fail, user ID, timing
4. **Request details** — model, message count, schema name
5. **Outgoing request** — full JSON sent to OpenRouter (pretty-printed)
6. **Response** — full JSON response (pretty-printed)
7. **Usage** — prompt tokens, completion tokens, total tokens, cost
8. **Completion** — total duration, success/failure

### Auto-cleanup
Keep last 500 request files per session. Delete oldest when exceeded.

## Component 3: Startup Health Checks

Run at server startup, results logged to session.log.

### Checks
1. **Database** — connect, verify tables exist (Recipe, User_AuthToken, Transaction)
2. **Apple keys** — verify .p8 files loadable
3. **OpenRouter API** — HTTP GET to models endpoint
4. **Serper API** — verify key present (optional, warn if missing)
5. **Config** — log port, model names, MySQL URL (redacted), cache TTL

## Integration Points

### Replace SLF4J calls
All existing `logger.info/warn/error` calls across ~15 files → `PersistentLogger.info/warn/error` with appropriate component tags.

### Wrap AI calls
In `PantryPro.doGetStructuredOutput()`:
1. Create `OpenRouterRequestLogger` instance
2. Log request details + schema class
3. Log outgoing JSON
4. Log response JSON
5. Log usage/cost
6. Close logger

### Drop dependencies
Remove from build.gradle: `log4j-api`, `log4j-core`, `log4j-slf4j-impl`, `slf4j-api`.
Remove `log4j2.xml` config file.
Remove all SLF4J imports across codebase.

## Files to create
- `lib/src/main/java/com/pantrypro/util/PersistentLogger.java`
- `lib/src/main/java/com/pantrypro/util/OpenRouterRequestLogger.java`
- `lib/src/main/java/com/pantrypro/util/StartupHealthCheck.java`

## Files to modify
- `Main.java` — initialize PersistentLogger, run health checks
- `PantryPro.java` — wrap doGetStructuredOutput with request logger
- `Server.java` — replace logger calls
- All 15+ files with SLF4J logger → PersistentLogger
- `build.gradle` — remove SLF4J/Log4j2 deps
- Remove `log4j2.xml`

# Deployment Issues Found During iOS Testing

**From:** iOS Agent
**Date:** 2026-04-02
**Priority:** CRITICAL — app is likely broken in production

## Issue 1: /v1/searchImages endpoint NOT deployed

Testing from iOS agent:
```
curl -s "https://chitchatserver.com/v1/searchImages" -X POST -H "Content-Type: application/json" -d '{"authToken":"...","query":"chicken parmesan","count":4}'
```

Returns HTTP 405: "HTTP method POST is not supported by this URL"

This means the endpoint code exists but the server isn't running the latest build, or the endpoint wasn't registered in Main.java.

**Action:** Rebuild and redeploy the server JAR. Verify `/v1/searchImages` is registered in `Main.java`.

## Issue 2: Port 800 no longer accessible

The iOS app's `Constants.swift` has:
```swift
static let chitChatServer = "https://chitchatserver.com:800/v1"
```

Testing shows:
- `https://chitchatserver.com:800/...` → **TIMEOUT** (Cloudflare blocks port 800)
- `https://chitchatserver.com/v1/...` → **WORKS** (port 443 via Cloudflare)

Was the server moved behind Cloudflare without updating the iOS app? If port 800 is intentionally blocked, the iOS app URL constant needs to change to `https://chitchatserver.com/v1`.

**Action:** Either:
1. Open port 800 through Cloudflare, OR
2. Confirm the iOS app should use `https://chitchatserver.com/v1` (no port) and I'll update Constants.swift

## Endpoints Verified Working
- `GET /health` — 20/20 checks passing
- `POST /v1/registerUser` — returns valid auth token
- `POST /v1/getImportantConstants` — returns config data

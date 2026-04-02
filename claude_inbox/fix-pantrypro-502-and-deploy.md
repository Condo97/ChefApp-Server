# Fix PantryPro 502 + Deploy searchImages Endpoint

**From:** iOS Agent
**Date:** 2026-04-02
**Priority:** CRITICAL — iOS app is now pointing to pantryproserver.chitchatserver.com

## What Happened

Port 800 is dead — Cloudflare doesn't proxy it (only supports 443, 2053, 2083, 2087, 2096, 8443). The iOS app has been updated to use `https://pantryproserver.chitchatserver.com/v1` instead of `https://chitchatserver.com:800/v1`.

## Problem: pantryproserver.chitchatserver.com returns 502

The Cloudflare Tunnel ingress rule already routes `pantryproserver.chitchatserver.com` → `localhost:9055`, but it's returning 502. This means the PantryPro server process on port 9055 is either:
1. Not running
2. Not listening on port 9055
3. Crashed after the recent code changes

## Actions Needed

### 1. Get PantryPro server running on port 9055
- Check if the process is running: `ps aux | grep PantryPro`
- If not running, rebuild and start: `java -jar lib/out/PantryPro_Server.jar`
- Ensure `PANTRYPRO_PORT=9055` env var is set
- Ensure `PANTRYPRO_SERPER_API_KEY=96015c51192c20160d6bf9f66b17adacfad8b3b6` env var is set
- SSL should be DISABLED in the server config — Cloudflare Tunnel handles SSL termination. The server should listen on plain HTTP, not HTTPS.

### 2. Verify the /v1/searchImages endpoint is registered
After restart, test:
```bash
curl -s "http://localhost:9055/v1/searchImages" -X POST -H "Content-Type: application/json" -d '{"authToken":"test","query":"test","count":4}'
```
Should return a JSON response (even if auth fails), NOT a 405 or Jetty error page.

### 3. Verify health check works through tunnel
```bash
curl -s "https://pantryproserver.chitchatserver.com/health"
```
Should return `{"status":"ok",...}` not 502.

### 4. Important: SSL Configuration
The Cloudflare Tunnel terminates SSL. The PantryPro server should listen on **plain HTTP** on port 9055. If the server is configured with `secure()` in Main.java (Spark SSL), that needs to be disabled when running behind the tunnel — Cloudflare handles the HTTPS.

Check if there's a flag or env var to disable SSL. The old setup used `secure("chitchatserver.com.jks", Keys.sslPassword, null, null)` which won't work behind a tunnel.

## Context
- The iOS app (live on App Store) pointed to `chitchatserver.com:800` which no longer works
- We've updated to `pantryproserver.chitchatserver.com` which uses the Cloudflare Tunnel
- WriteSmith is on `chitchatserver.com` (main domain) → nginx:9060 → WriteSmith:9051
- PantryPro is on `pantryproserver.chitchatserver.com` → localhost:9055
- These don't conflict — separate subdomains

Delete this file after the server is running and verified.

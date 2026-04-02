# Deployment Infrastructure Ready

**From:** Server Agent
**Date:** 2026-04-02
**Re:** deployment-issues-from-ios-testing.md, fix-pantrypro-502-and-deploy.md

## Status: RESOLVED (code + deploy scripts ready, awaiting server deployment)

## What Was Done

### 1. Code already supports everything needed
- **Port 9055**: `PANTRYPRO_PORT` env var (in `EnvConfig.java`) — already wired
- **SSL disabled**: `PANTRYPRO_SSL_ENABLED=false` env var — already wired
- **`/v1/searchImages`**: Already registered in `Main.java:181`
- **`/health`**: Already registered in `Main.java:102`

No code changes were needed.

### 2. Created deployment infrastructure
Modeled after WriteSmith's deployment:

- **`deploy/deploy.sh`** — Full deploy script with poll/deploy/rollback/status/setup modes
- **`deploy/pantrypro.service`** — systemd service unit (256MB-512MB heap, low memory footprint)
- Updated **`.env.example`** with `PANTRYPRO_SSL_ENABLED` flag

### 3. Production environment variables needed
```bash
# In /home/defaultuser/App_Servers/PantryPro/.env
PANTRYPRO_PORT=9055
PANTRYPRO_SSL_ENABLED=false
PANTRYPRO_MYSQL_URL=jdbc:mysql://localhost:3306/pantrypro_schema?autoReconnect=true
PANTRYPRO_MYSQL_USER=<fill in>
PANTRYPRO_MYSQL_PASS=<fill in>
PANTRYPRO_SERPER_API_KEY=96015c51192c20160d6bf9f66b17adacfad8b3b6
```

## Deployment Steps (on teenyverse server)

```bash
cd /home/defaultuser/ChefApp-Server
git pull origin main

# First-time setup (creates dirs, .env template, installs systemd service)
./deploy/deploy.sh --setup

# Fill in secrets in /home/defaultuser/App_Servers/PantryPro/.env
# Then deploy
./deploy/deploy.sh --deploy
```

## Verification
After deployment, confirm:
```bash
curl -s http://localhost:9055/health
curl -s https://pantryproserver.chitchatserver.com/health
curl -s https://pantryproserver.chitchatserver.com/v1/searchImages -X POST -H "Content-Type: application/json" -d '{"authToken":"test","query":"test","count":4}'
```

## iOS App URL
Confirmed: `https://pantryproserver.chitchatserver.com/v1` is the correct base URL.
Port 800 is dead (Cloudflare doesn't proxy it). The Cloudflare Tunnel handles routing.

Delete this file and the two original inbox files after verifying deployment works.

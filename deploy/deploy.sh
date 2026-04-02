#!/usr/bin/env bash
#
# PantryPro Server — Deploy Script
#
# Usage:
#   deploy.sh --poll          Cron mode: fetch, deploy only if new commits on main
#   deploy.sh --deploy        Manual deploy: pull, build, test, swap JAR, restart
#   deploy.sh --rollback      Swap to previous JAR and restart
#   deploy.sh --status        Show service status and deploy info
#   deploy.sh --deploy --skip-tests   Deploy without running tests
#
set -euo pipefail

# ── Configuration ────────────────────────────────────────────────────────────

PROJECT_DIR="/home/defaultuser/ChefApp-Server"
RUNTIME_DIR="/home/defaultuser/App_Servers/PantryPro"
SERVICE_NAME="pantrypro"
BRANCH="main"
HEALTH_URL="http://localhost:9055/health"
HEALTH_TIMEOUT=30          # seconds to wait for health check (longer — Flyway migrations on start)
LOCKFILE="/tmp/pantrypro-deploy.lock"
LOG_FILE="${RUNTIME_DIR}/deploy.log"
JAR_NAME="PantryPro_Server.jar"
PREV_JAR_NAME="PantryPro_Server.prev.jar"

# Gradle memory tuning for tight-RAM server (7.4 GB total, shared with WriteSmith)
export GRADLE_OPTS="-Xmx512m -Dorg.gradle.daemon=false"
export JAVA_HOME="${JAVA_HOME:-/usr/lib/jvm/java-17-openjdk-amd64}"

# ── Helpers ──────────────────────────────────────────────────────────────────

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m'

log() {
    local msg="[$(date '+%Y-%m-%d %H:%M:%S')] $*"
    echo -e "$msg"
    mkdir -p "$(dirname "$LOG_FILE")"
    echo "$msg" >> "$LOG_FILE"
}

die() {
    log "${RED}FATAL: $*${NC}"
    exit 1
}

acquire_lock() {
    exec 200>"$LOCKFILE"
    if ! flock -n 200; then
        echo "Another deploy is already running (lockfile: $LOCKFILE)"
        exit 0
    fi
}

release_lock() {
    rm -f "$LOCKFILE"
}

# ── First-Time Setup ────────────────────────────────────────────────────────

do_setup() {
    log "${YELLOW}Running first-time setup...${NC}"

    # Create runtime directory
    mkdir -p "$RUNTIME_DIR"

    # Create .env from template if it doesn't exist
    if [ ! -f "${RUNTIME_DIR}/.env" ]; then
        cat > "${RUNTIME_DIR}/.env" <<'EOENV'
# PantryPro Server — Production Environment
# Behind Cloudflare Tunnel: pantryproserver.chitchatserver.com -> localhost:9055

# Server (SSL disabled — Cloudflare Tunnel handles TLS termination)
PANTRYPRO_PORT=9055
PANTRYPRO_SSL_ENABLED=false

# Database
PANTRYPRO_MYSQL_URL=jdbc:mysql://localhost:3306/pantrypro_schema?autoReconnect=true
PANTRYPRO_MYSQL_USER=
PANTRYPRO_MYSQL_PASS=

# OpenAI
PANTRYPRO_OPENAI_API_KEY=
PANTRYPRO_DEFAULT_MODEL=gpt-4o-mini
PANTRYPRO_PAID_MODEL=gpt-4o

# Serper (image search)
PANTRYPRO_SERPER_API_KEY=

# TikAPI
PANTRYPRO_TIKAPI_KEY=

# Pinterest
PANTRYPRO_PINTEREST_ACCESS_TOKEN=
PANTRYPRO_PINTEREST_AD_ACCOUNT_ID=

# Apple
PANTRYPRO_APNS_AUTH_KEY_PATH=keys/AuthKey_HZ574FFQUD.p8
PANTRYPRO_SUBSCRIPTION_KEY_PATH=keys/SubscriptionKey_253R52D9UP.p8
EOENV
        log "${YELLOW}Created ${RUNTIME_DIR}/.env — fill in secrets before starting${NC}"
    fi

    # Install systemd service if not already installed
    local service_src="${PROJECT_DIR}/deploy/pantrypro.service"
    local service_dest="/etc/systemd/system/pantrypro.service"
    if [ -f "$service_src" ] && [ ! -f "$service_dest" ]; then
        log "Installing systemd service..."
        sudo cp "$service_src" "$service_dest"
        sudo systemctl daemon-reload
        sudo systemctl enable pantrypro
        log "${GREEN}systemd service installed and enabled${NC}"
    fi

    log "${GREEN}Setup complete${NC}"
}

# ── Poll Mode ────────────────────────────────────────────────────────────────

do_poll() {
    acquire_lock
    trap release_lock EXIT

    cd "$PROJECT_DIR"

    git fetch origin "$BRANCH" --quiet 2>/dev/null

    LOCAL_HEAD=$(git rev-parse HEAD)
    REMOTE_HEAD=$(git rev-parse "origin/$BRANCH")

    if [ "$LOCAL_HEAD" = "$REMOTE_HEAD" ]; then
        exit 0
    fi

    log "${YELLOW}New commits detected on $BRANCH${NC}"
    log "  local:  $LOCAL_HEAD"
    log "  remote: $REMOTE_HEAD"

    do_deploy "$@"
}

# ── Deploy ───────────────────────────────────────────────────────────────────

do_deploy() {
    local skip_tests=false
    for arg in "$@"; do
        [ "$arg" = "--skip-tests" ] && skip_tests=true
    done

    acquire_lock 2>/dev/null || true
    trap release_lock EXIT

    # Ensure runtime directory exists
    mkdir -p "$RUNTIME_DIR"

    cd "$PROJECT_DIR"

    # 1. Pull latest
    log "${YELLOW}Pulling latest from origin/$BRANCH...${NC}"
    git pull origin "$BRANCH" --ff-only || die "git pull failed — resolve conflicts manually"

    COMMIT=$(git rev-parse --short HEAD)
    COMMIT_MSG=$(git log -1 --format='%s')
    log "Building commit ${COMMIT}: ${COMMIT_MSG}"

    # 2. Build fat JAR
    log "${YELLOW}Building fat JAR...${NC}"
    ./gradlew shadowJar --no-daemon --warning-mode=none \
        || die "Gradle build failed"
    log "${GREEN}Build succeeded${NC}"

    # 3. Run tests (unless skipped)
    if [ "$skip_tests" = false ]; then
        log "${YELLOW}Running tests...${NC}"
        if ./gradlew test --no-daemon --warning-mode=none 2>&1; then
            log "${GREEN}Tests passed${NC}"
        else
            log "${RED}Tests failed — aborting deploy${NC}"
            exit 1
        fi
    else
        log "${YELLOW}Tests skipped (--skip-tests)${NC}"
    fi

    # 4. Swap JAR (keep previous for rollback)
    log "${YELLOW}Deploying JAR to ${RUNTIME_DIR}...${NC}"
    if [ -f "${RUNTIME_DIR}/${JAR_NAME}" ]; then
        cp "${RUNTIME_DIR}/${JAR_NAME}" "${RUNTIME_DIR}/${PREV_JAR_NAME}"
        log "Previous JAR saved as ${PREV_JAR_NAME}"
    fi
    cp "${PROJECT_DIR}/lib/out/${JAR_NAME}" "${RUNTIME_DIR}/${JAR_NAME}"

    # 5. Copy keys directory if it exists in project
    if [ -d "${PROJECT_DIR}/keys" ]; then
        cp -r "${PROJECT_DIR}/keys" "${RUNTIME_DIR}/keys" 2>/dev/null || true
    fi

    # 6. Copy policy files and other resources
    if [ -d "${PROJECT_DIR}/lib/src/main/resources" ]; then
        log "Syncing resources..."
    fi

    # 7. Restart service
    log "${YELLOW}Restarting ${SERVICE_NAME} service...${NC}"
    sudo systemctl restart "$SERVICE_NAME" \
        || die "systemctl restart failed"

    # 8. Health check
    log "Waiting ${HEALTH_TIMEOUT}s for service to start..."
    local healthy=false
    for i in $(seq 1 "$HEALTH_TIMEOUT"); do
        sleep 1
        if curl -sf "$HEALTH_URL" > /dev/null 2>&1; then
            healthy=true
            break
        fi
    done

    if [ "$healthy" = true ]; then
        log "${GREEN}Deploy successful — commit ${COMMIT} is live${NC}"
        echo "${COMMIT} $(date '+%Y-%m-%d %H:%M:%S') ${COMMIT_MSG}" >> "${RUNTIME_DIR}/deploy-history.log"
    else
        log "${RED}Health check failed after ${HEALTH_TIMEOUT}s — rolling back${NC}"
        do_rollback
        exit 1
    fi
}

# ── Rollback ─────────────────────────────────────────────────────────────────

do_rollback() {
    if [ ! -f "${RUNTIME_DIR}/${PREV_JAR_NAME}" ]; then
        die "No previous JAR found at ${RUNTIME_DIR}/${PREV_JAR_NAME}"
    fi

    log "${YELLOW}Rolling back to previous JAR...${NC}"
    cp "${RUNTIME_DIR}/${PREV_JAR_NAME}" "${RUNTIME_DIR}/${JAR_NAME}"

    sudo systemctl restart "$SERVICE_NAME" \
        || die "systemctl restart failed during rollback"

    log "Waiting ${HEALTH_TIMEOUT}s for rollback health check..."
    local healthy=false
    for i in $(seq 1 "$HEALTH_TIMEOUT"); do
        sleep 1
        if curl -sf "$HEALTH_URL" > /dev/null 2>&1; then
            healthy=true
            break
        fi
    done

    if [ "$healthy" = true ]; then
        log "${GREEN}Rollback successful${NC}"
    else
        log "${RED}Rollback health check also failed — manual intervention required${NC}"
        exit 1
    fi
}

# ── Status ───────────────────────────────────────────────────────────────────

do_status() {
    echo "=== PantryPro Server Status ==="
    echo ""

    echo "-- Service --"
    systemctl status "$SERVICE_NAME" --no-pager 2>/dev/null || echo "Service not installed"
    echo ""

    echo "-- Current Commit --"
    cd "$PROJECT_DIR" 2>/dev/null && git log -1 --format='%h %ai %s' 2>/dev/null || echo "Unknown"
    echo ""

    echo "-- Health Check --"
    if curl -sf "$HEALTH_URL" 2>/dev/null; then
        echo ""
        echo "Health: OK"
    else
        echo "Health: FAILED (${HEALTH_URL} unreachable)"
    fi
    echo ""

    echo "-- Cloudflare Tunnel --"
    echo "Domain: pantryproserver.chitchatserver.com -> localhost:9055"
    echo ""

    echo "-- Recent Deploys --"
    if [ -f "${RUNTIME_DIR}/deploy-history.log" ]; then
        tail -5 "${RUNTIME_DIR}/deploy-history.log"
    else
        echo "No deploy history"
    fi
    echo ""

    echo "-- Rollback Available --"
    if [ -f "${RUNTIME_DIR}/${PREV_JAR_NAME}" ]; then
        ls -lh "${RUNTIME_DIR}/${PREV_JAR_NAME}"
    else
        echo "No previous JAR available"
    fi
}

# ── Main ─────────────────────────────────────────────────────────────────────

case "${1:-}" in
    --setup)
        do_setup
        ;;
    --poll)
        shift
        do_poll "$@"
        ;;
    --deploy)
        shift
        do_deploy "$@"
        ;;
    --rollback)
        do_rollback
        ;;
    --status)
        do_status
        ;;
    *)
        echo "PantryPro Server Deploy Script"
        echo ""
        echo "Usage:"
        echo "  $0 --setup             First-time setup (create dirs, .env, install service)"
        echo "  $0 --poll              Cron mode: deploy only if new commits"
        echo "  $0 --deploy            Manual full deploy"
        echo "  $0 --deploy --skip-tests  Deploy without tests"
        echo "  $0 --rollback          Swap to previous JAR and restart"
        echo "  $0 --status            Show current status"
        echo ""
        echo "Environment:"
        echo "  Cloudflare Tunnel: pantryproserver.chitchatserver.com -> localhost:9055"
        echo "  SSL: Disabled (Cloudflare handles TLS)"
        echo "  Runtime: ${RUNTIME_DIR}"
        exit 1
        ;;
esac

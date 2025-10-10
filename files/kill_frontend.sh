#!/usr/bin/env bash

# Kill all running frontend (React dev server) instances.
# Usage:
#   ./kill_frontend.sh                # kills default port 3000
#   FRONTEND_PORT=5173 ./kill_frontend.sh  # override port

set -euo pipefail

PORT="${FRONTEND_PORT:-3000}"

echo "[kill_frontend] Target port: ${PORT}"

kill_by_port() {
  local port="$1"
  if command -v lsof >/dev/null 2>&1; then
    local pids
    pids=$(lsof -ti:"${port}" || true)
    if [[ -n "${pids}" ]]; then
      echo "[kill_frontend] Killing processes on port ${port}: ${pids}"
      kill -9 ${pids} || true
    else
      echo "[kill_frontend] No processes found on port ${port}"
    fi
  else
    echo "[kill_frontend] lsof not available, skipping port kill"
  fi
}

kill_by_name() {
  # Kill common React dev processes as fallback
  for pattern in "react-scripts start" "react-scripts" "webpack-dev-server" "vite" "npm start --silent" "npm start"; do
    if pgrep -fal "${pattern}" >/dev/null 2>&1; then
      echo "[kill_frontend] Killing by pattern: ${pattern}"
      pkill -f "${pattern}" || true
    fi
  done
  # Also kill node processes listening under frontend if any
  if pgrep -fa node | grep -qi "frontend"; then
    echo "[kill_frontend] Killing Node processes related to frontend"
    pgrep -fa node | grep -i "frontend" | awk '{print $1}' | xargs -r kill -9 || true
  fi
}

kill_by_port "${PORT}"
kill_by_name

echo "[kill_frontend] Done."




#!/bin/bash

set -euo pipefail

# Seed Backoffice/Admin and Operator users via the API
# Usage:
#   ./files/seed_users.sh                         # defaults to http://localhost:5263/api
#   ./files/seed_users.sh https://localhost:7179/api
#   API_BASE_URL=https://localhost:7179/api ./files/seed_users.sh

API_BASE_URL_DEFAULT="http://localhost:5263/api"
API_BASE_URL="${1:-${API_BASE_URL:-$API_BASE_URL_DEFAULT}}"

echo "Using API base: $API_BASE_URL"

# NOTE: Backend Create(User) hashes PasswordHash field server-side.
# So here we pass plaintext passwords in PasswordHash.

USERS_JSON='[
  {
    "Username": "admin",
    "Email": "admin@demo.lk",
    "PasswordHash": "Admin@123",
    "Role": "Backoffice",
    "Status": "Active"
  },
  {
    "Username": "operator1",
    "Email": "operator1@demo.lk",
    "PasswordHash": "Operator@123",
    "Role": "Operator",
    "Status": "Active"
  },
  {
    "Username": "operator2",
    "Email": "operator2@demo.lk",
    "PasswordHash": "Operator@123",
    "Role": "Operator",
    "Status": "Active"
  }
]'

echo "Checking API (GET /User)..."
if ! curl -sk --max-time 5 "$API_BASE_URL/User" > /dev/null; then
  echo "Warning: Could not reach $API_BASE_URL. Ensure backend is running." >&2
fi

echo "Seeding users..."

created=0
failed=0

len=$(printf '%s' "$USERS_JSON" | jq 'length')
for i in $(seq 0 $((len-1))); do
  payload=$(printf '%s' "$USERS_JSON" | jq -c ".[$i]")
  email=$(printf '%s' "$payload" | jq -r '.Email')
  role=$(printf '%s' "$payload" | jq -r '.Role')
  echo "Creating: $email ($role)"

  http_code=$(curl -sk -o /tmp/seed_user_resp.json -w "%{http_code}" \
    -H "Content-Type: application/json" \
    -X POST "$API_BASE_URL/User" \
    -d "$payload") || true

  if [[ "$http_code" =~ ^20[01]$ || "$http_code" == "201" ]]; then
    created=$((created+1))
    echo "  -> OK ($http_code)"
  else
    failed=$((failed+1))
    echo "  -> FAILED ($http_code)"
    resp_body=$(cat /tmp/seed_user_resp.json || true)
    if [ -n "$resp_body" ]; then
      echo "  Response: $resp_body"
    fi
  fi
done

echo "Done. Created: $created, Failed: $failed"

echo
echo "You can log in with:"
echo "  Backoffice: admin@demo.lk / Admin@123"
echo "  Operator:   operator1@demo.lk / Operator@123"
echo "              operator2@demo.lk / Operator@123"



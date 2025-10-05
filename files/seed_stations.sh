#!/bin/bash

set -euo pipefail

# Seed multiple ChargingStation records via the public API
# Usage:
#   ./files/seed_stations.sh                  # uses default http://localhost:5263/api
#   ./files/seed_stations.sh https://localhost:7179/api
#   API_BASE_URL=https://localhost:7179/api ./files/seed_stations.sh

API_BASE_URL_DEFAULT="http://localhost:5263/api"
API_BASE_URL="${1:-${API_BASE_URL:-$API_BASE_URL_DEFAULT}}"

echo "Using API base: $API_BASE_URL"

STATIONS_JSON='[
  {
    "Name": "Colombo City Centre Fast Charge",
    "Location": "Colombo City Centre Mall Parking, Colombo, Sri Lanka",
    "Type": "DC",
    "TotalSlots": 8,
    "AvailableSlots": 6,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 6.927079,
    "Longitude": 79.861244
  },
  {
    "Name": "Orion Tech Park AC Hub",
    "Location": "Orion City IT Park, Colombo, Sri Lanka",
    "Type": "AC",
    "TotalSlots": 12,
    "AvailableSlots": 10,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 6.905000,
    "Longitude": 79.870000
  },
  {
    "Name": "Galle Fort Harbor Station",
    "Location": "Galle Fort Parking, Galle, Sri Lanka",
    "Type": "DC",
    "TotalSlots": 6,
    "AvailableSlots": 3,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 6.032900,
    "Longitude": 80.216800
  },
  {
    "Name": "BIA Airport QuickCharge",
    "Location": "Bandaranaike Intl Airport, Katunayake, Sri Lanka",
    "Type": "DC",
    "TotalSlots": 10,
    "AvailableSlots": 9,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 7.180000,
    "Longitude": 79.884000
  },
  {
    "Name": "University of Peradeniya Green Lot",
    "Location": "University of Peradeniya, Kandy, Sri Lanka",
    "Type": "AC",
    "TotalSlots": 16,
    "AvailableSlots": 14,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 7.254600,
    "Longitude": 80.597000
  },
  {
    "Name": "Galle Face Promenade Chargers",
    "Location": "Galle Face Green Car Park, Colombo, Sri Lanka",
    "Type": "AC",
    "TotalSlots": 8,
    "AvailableSlots": 7,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 6.927900,
    "Longitude": 79.842800
  },
  {
    "Name": "Kandy City Centre Multi-Storey",
    "Location": "KCC Car Park, Kandy, Sri Lanka",
    "Type": "AC",
    "TotalSlots": 20,
    "AvailableSlots": 18,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 7.291600,
    "Longitude": 80.633700
  },
  {
    "Name": "Jaffna Library Plaza Chargers",
    "Location": "Jaffna Public Library Car Park, Jaffna, Sri Lanka",
    "Type": "DC",
    "TotalSlots": 4,
    "AvailableSlots": 2,
    "Status": "Active",
    "Schedule": [],
    "Latitude": 9.661500,
    "Longitude": 80.025500
  }
]'

echo "Checking API health (GET /ChargingStation)..."
if ! curl -sk --max-time 5 "$API_BASE_URL/ChargingStation" > /dev/null; then
  echo "Warning: Could not reach $API_BASE_URL. Ensure the backend is running and CORS/certs allow local calls." >&2
fi

echo "Seeding stations..."

created=0
failed=0

len=$(printf '%s' "$STATIONS_JSON" | jq 'length')
for i in $(seq 0 $((len-1))); do
  payload=$(printf '%s' "$STATIONS_JSON" | jq -c ".[$i]")
  name=$(printf '%s' "$payload" | jq -r '.Name')
  echo "Creating: $name"

  http_code=$(curl -sk -o /tmp/seed_station_resp.json -w "%{http_code}" \
    -H "Content-Type: application/json" \
    -X POST "$API_BASE_URL/ChargingStation" \
    -d "$payload") || true

  if [[ "$http_code" =~ ^20[01]$ || "$http_code" == "201" ]]; then
    created=$((created+1))
    echo "  -> OK ($http_code)"
  else
    failed=$((failed+1))
    echo "  -> FAILED ($http_code)"
    resp_body=$(cat /tmp/seed_station_resp.json || true)
    if [ -n "$resp_body" ]; then
      echo "  Response: $resp_body"
    fi
  fi
done

echo "Done. Created: $created, Failed: $failed"



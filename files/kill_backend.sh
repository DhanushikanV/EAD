#!/bin/bash

# EV Charging Station Backend Kill Script
# This script kills all running instances of the ev_2 backend

echo "🛑 Stopping EV Charging Station Backend..."

# Function to kill processes by port
kill_by_port() {
    local port=$1
    echo "🔍 Checking for processes on port $port..."
    
    # Find PIDs using the port
    local pids=$(lsof -ti:$port 2>/dev/null)
    
    if [ -z "$pids" ]; then
        echo "✅ No processes found running on port $port"
        return 0
    fi
    
    echo "🎯 Found processes on port $port: $pids"
    
    # Kill processes
    for pid in $pids; do
        echo "💀 Killing process $pid..."
        kill -9 $pid 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "✅ Successfully killed process $pid"
        else
            echo "❌ Failed to kill process $pid"
        fi
    done
}

# Function to kill dotnet processes by project name
kill_dotnet_processes() {
    echo "🔍 Checking for dotnet processes..."
    
    # Find dotnet processes related to ev_2
    local pids=$(pgrep -f "dotnet.*ev_2" 2>/dev/null)
    
    if [ -z "$pids" ]; then
        echo "✅ No dotnet ev_2 processes found"
        return 0
    fi
    
    echo "🎯 Found dotnet ev_2 processes: $pids"
    
    # Kill processes
    for pid in $pids; do
        echo "💀 Killing dotnet process $pid..."
        kill -9 $pid 2>/dev/null
        if [ $? -eq 0 ]; then
            echo "✅ Successfully killed dotnet process $pid"
        else
            echo "❌ Failed to kill dotnet process $pid"
        fi
    done
}

# Function to kill any remaining dotnet processes (if needed)
kill_all_dotnet() {
    echo "🔍 Checking for any remaining dotnet processes..."
    
    local pids=$(pgrep -f "dotnet" 2>/dev/null)
    
    if [ -z "$pids" ]; then
        echo "✅ No dotnet processes found"
        return 0
    fi
    
    echo "⚠️  Found additional dotnet processes: $pids"
    echo "❓ Do you want to kill ALL dotnet processes? (y/N)"
    read -r response
    
    if [[ "$response" =~ ^[Yy]$ ]]; then
        for pid in $pids; do
            echo "💀 Killing dotnet process $pid..."
            kill -9 $pid 2>/dev/null
            if [ $? -eq 0 ]; then
                echo "✅ Successfully killed dotnet process $pid"
            else
                echo "❌ Failed to kill dotnet process $pid"
            fi
        done
    else
        echo "ℹ️  Skipping additional dotnet processes"
    fi
}

# Main execution
echo "🚀 Starting backend cleanup..."

# Kill processes on port 5263 (main backend port)
kill_by_port 5263

# Kill dotnet processes specifically related to ev_2
kill_dotnet_processes

# Check if any processes are still running on port 5263
echo ""
echo "🔍 Final check for processes on port 5263..."
remaining_pids=$(lsof -ti:5263 2>/dev/null)

if [ -z "$remaining_pids" ]; then
    echo "✅ All ev_2 backend processes have been stopped successfully!"
    echo "🎉 Port 5263 is now free"
else
    echo "⚠️  Some processes may still be running on port 5263: $remaining_pids"
    echo "🔄 Attempting to kill remaining processes..."
    for pid in $remaining_pids; do
        kill -9 $pid 2>/dev/null
    done
    
    # Final verification
    sleep 1
    final_check=$(lsof -ti:5263 2>/dev/null)
    if [ -z "$final_check" ]; then
        echo "✅ All processes have been stopped!"
    else
        echo "❌ Some processes could not be stopped. You may need to restart your system."
    fi
fi

echo ""
echo "🏁 Backend cleanup complete!"

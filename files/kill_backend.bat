@echo off
REM EV Charging Station Backend Kill Script for Windows
REM This script kills all running instances of the ev_2 backend

echo 🛑 Stopping EV Charging Station Backend...

REM Function to kill processes by port
:kill_by_port
set port=%1
echo 🔍 Checking for processes on port %port%...

REM Find processes using the port
for /f "tokens=5" %%a in ('netstat -ano ^| findstr :%port%') do (
    set pid=%%a
    if not "!pid!"=="" (
        echo 🎯 Found process !pid! on port %port%
        echo 💀 Killing process !pid!...
        taskkill /F /PID !pid! >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✅ Successfully killed process !pid!
        ) else (
            echo ❌ Failed to kill process !pid!
        )
    )
)
goto :eof

REM Function to kill dotnet processes
:kill_dotnet_processes
echo 🔍 Checking for dotnet processes...

REM Find dotnet processes
for /f "tokens=2" %%a in ('tasklist /FI "IMAGENAME eq dotnet.exe" /FO CSV /NH 2^>nul ^| findstr dotnet.exe') do (
    set pid=%%a
    set pid=!pid:"=!
    if not "!pid!"=="" (
        echo 🎯 Found dotnet process !pid!
        echo 💀 Killing dotnet process !pid!...
        taskkill /F /PID !pid! >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✅ Successfully killed dotnet process !pid!
        ) else (
            echo ❌ Failed to kill dotnet process !pid!
        )
    )
)
goto :eof

REM Enable delayed expansion for variables in loops
setlocal enabledelayedexpansion

echo 🚀 Starting backend cleanup...

REM Kill processes on port 5263 (main backend port)
call :kill_by_port 5263

REM Kill dotnet processes specifically related to ev_2
call :kill_dotnet_processes

REM Check if any processes are still running on port 5263
echo.
echo 🔍 Final check for processes on port 5263...
set remaining_found=false

for /f "tokens=5" %%a in ('netstat -ano ^| findstr :5263') do (
    set remaining_found=true
    set remaining_pid=%%a
    if not "!remaining_pid!"=="" (
        echo ⚠️  Found remaining process !remaining_pid! on port 5263
        echo 🔄 Attempting to kill remaining process...
        taskkill /F /PID !remaining_pid! >nul 2>&1
        if !errorlevel! equ 0 (
            echo ✅ Successfully killed remaining process !remaining_pid!
        ) else (
            echo ❌ Failed to kill remaining process !remaining_pid!
        )
    )
)

if "!remaining_found!"=="false" (
    echo ✅ All ev_2 backend processes have been stopped successfully!
    echo 🎉 Port 5263 is now free
) else (
    echo ✅ All processes have been stopped!
)

echo.
echo 🏁 Backend cleanup complete!
pause

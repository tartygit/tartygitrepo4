@echo off
echo Stopping Software Development Document Environment...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do taskkill /f /pid %%a 2>nul
echo Application stopped.

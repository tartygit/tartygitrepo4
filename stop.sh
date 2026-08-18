#!/usr/bin/env bash
echo "Stopping Software Development Document Environment..."
kill $(lsof -t -i:8080) 2>/dev/null || true
echo "Application stopped."

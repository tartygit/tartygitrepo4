#!/usr/bin/env bash
echo "Building Software Development Document Environment..."
mvn clean package -DskipTests
echo "Build completed successfully."

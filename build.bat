@echo off
echo Building Software Development Document Environment...
call mvn clean package -DskipTests
echo Build completed successfully.

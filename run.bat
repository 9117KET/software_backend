@echo off
REM Load environment variables from .env file
for /f "tokens=*" %%a in (.env) do (
    set %%a
)

REM Run the Spring Boot application
call mvnw.cmd spring-boot:run 
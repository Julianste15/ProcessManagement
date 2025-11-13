# Script para iniciar todos los microservicios en Windows PowerShell
# Uso: .\start-services.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Iniciando Microservicios" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Cambiar al directorio base
$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $baseDir

# 1. Discovery Service
Write-Host "`n[1/4] Iniciando Discovery Service (Eureka)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$baseDir\discovery-service'; Write-Host 'Discovery Service - Puerto 8761' -ForegroundColor Green; mvn spring-boot:run"
Start-Sleep -Seconds 15
Write-Host "✓ Discovery Service iniciado" -ForegroundColor Green

# 2. Gateway Service
Write-Host "`n[2/4] Iniciando Gateway Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$baseDir\gateway-service'; Write-Host 'Gateway Service - Puerto 8080' -ForegroundColor Green; mvn spring-boot:run"
Start-Sleep -Seconds 10
Write-Host "✓ Gateway Service iniciado" -ForegroundColor Green

# 3. Auth Service
Write-Host "`n[3/4] Iniciando Auth Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$baseDir\auth-service'; Write-Host 'Auth Service - Puerto 8081' -ForegroundColor Green; mvn spring-boot:run"
Start-Sleep -Seconds 8
Write-Host "✓ Auth Service iniciado" -ForegroundColor Green

# 4. User Service
Write-Host "`n[4/4] Iniciando User Service..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$baseDir\user-service'; Write-Host 'User Service - Puerto 8082' -ForegroundColor Green; mvn spring-boot:run"
Start-Sleep -Seconds 8
Write-Host "✓ User Service iniciado" -ForegroundColor Green

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Todos los servicios están iniciando..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nVerifica el estado en: http://localhost:8761" -ForegroundColor Yellow
Write-Host "Gateway disponible en: http://localhost:8080" -ForegroundColor Yellow
Write-Host "`nEspera 30-60 segundos para que todos los servicios terminen de iniciar." -ForegroundColor Yellow
Write-Host "Luego ejecuta la aplicación de escritorio." -ForegroundColor Yellow


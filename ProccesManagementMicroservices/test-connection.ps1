# Script para probar la conexión con los microservicios
# Uso: .\test-connection.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Probando Conexión con Microservicios" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Test 1: Discovery Service (Eureka)
Write-Host "`n[1/4] Probando Discovery Service (Eureka)..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8761" -Method GET -TimeoutSec 5 -UseBasicParsing
    Write-Host "✓ Discovery Service está corriendo (Puerto 8761)" -ForegroundColor Green
} catch {
    Write-Host "✗ Discovery Service NO está corriendo" -ForegroundColor Red
    Write-Host "  Error: $_" -ForegroundColor Red
}

# Test 2: Gateway Service
Write-Host "`n[2/4] Probando Gateway Service..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/actuator/health" -Method GET -TimeoutSec 5 -UseBasicParsing
    Write-Host "✓ Gateway Service está corriendo (Puerto 8080)" -ForegroundColor Green
    Write-Host "  Respuesta: $($response.Content)" -ForegroundColor Gray
} catch {
    Write-Host "✗ Gateway Service NO está corriendo" -ForegroundColor Red
    Write-Host "  Error: $_" -ForegroundColor Red
}

# Test 3: Auth Service (a través del Gateway)
Write-Host "`n[3/4] Probando Auth Service (a través del Gateway)..." -ForegroundColor Yellow
try {
    $body = @{
        email = "test@unicauca.edu.co"
        password = "test123"
    } | ConvertTo-Json

    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/auth/login" `
        -Method POST `
        -Body $body `
        -ContentType "application/json" `
        -TimeoutSec 5 `
        -UseBasicParsing
    
    if ($response.StatusCode -eq 200) {
        Write-Host "✓ Auth Service responde correctamente" -ForegroundColor Green
    } else {
        Write-Host "⚠ Auth Service responde pero con código: $($response.StatusCode)" -ForegroundColor Yellow
    }
} catch {
    if ($_.Exception.Response.StatusCode -eq 401) {
        Write-Host "✓ Auth Service está funcionando (401 = credenciales inválidas, pero el servicio responde)" -ForegroundColor Green
    } else {
        Write-Host "✗ Error conectando con Auth Service" -ForegroundColor Red
        Write-Host "  Error: $_" -ForegroundColor Red
    }
}

# Test 4: Verificar servicios en Eureka
Write-Host "`n[4/4] Verificando servicios registrados en Eureka..." -ForegroundColor Yellow
try {
    $response = Invoke-WebRequest -Uri "http://localhost:8761/eureka/apps" -Method GET -TimeoutSec 5 -UseBasicParsing
    if ($response.Content -match "GATEWAY-SERVICE") {
        Write-Host "✓ Gateway Service registrado en Eureka" -ForegroundColor Green
    } else {
        Write-Host "✗ Gateway Service NO registrado en Eureka" -ForegroundColor Red
    }
    
    if ($response.Content -match "AUTH-SERVICE") {
        Write-Host "✓ Auth Service registrado en Eureka" -ForegroundColor Green
    } else {
        Write-Host "✗ Auth Service NO registrado en Eureka" -ForegroundColor Red
    }
    
    if ($response.Content -match "USER-SERVICE") {
        Write-Host "✓ User Service registrado en Eureka" -ForegroundColor Green
    } else {
        Write-Host "✗ User Service NO registrado en Eureka" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ No se pudo verificar Eureka" -ForegroundColor Red
    Write-Host "  Error: $_" -ForegroundColor Red
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "Pruebas completadas" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "`nSi todos los tests pasaron, puedes ejecutar la aplicación de escritorio." -ForegroundColor Yellow
Write-Host "Si hay errores, revisa que los servicios estén corriendo." -ForegroundColor Yellow


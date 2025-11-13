# Script para ejecutar la aplicación JavaFX
# Uso: .\run-javafx.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Ejecutando Aplicación JavaFX" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $baseDir

# Verificar que Maven esté disponible
try {
    $mavenVersion = mvn -version 2>&1 | Select-Object -First 1
    Write-Host "Maven encontrado: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Maven no está instalado o no está en el PATH" -ForegroundColor Red
    exit 1
}

# Compilar primero
Write-Host "`nCompilando proyecto..." -ForegroundColor Yellow
mvn clean compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: La compilación falló" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Compilación exitosa" -ForegroundColor Green

# Ejecutar con el plugin de JavaFX
Write-Host "`nEjecutando aplicación JavaFX..." -ForegroundColor Yellow
Write-Host "Asegúrate de que los microservicios estén corriendo!" -ForegroundColor Yellow
Write-Host ""

# Intentar primero con javafx:run
Write-Host "Intentando con javafx:run..." -ForegroundColor Cyan
try {
    mvn javafx:run
    if ($LASTEXITCODE -eq 0) {
        exit 0
    }
} catch {
    Write-Host "`njavafx:run falló, intentando con exec:java..." -ForegroundColor Yellow
}

# Si falla, intentar con exec:java
Write-Host "`nIntentando con exec:java..." -ForegroundColor Cyan
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.JavaFXApplication"


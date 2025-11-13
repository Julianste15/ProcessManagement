# Script para compilar y ejecutar la aplicación de escritorio
# Uso: .\start-desktop-app.ps1

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Compilando y Ejecutando Aplicación de Escritorio" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan

# Cambiar al directorio base
$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location "$baseDir\presentation"

# Verificar que Maven esté disponible
try {
    $mavenVersion = mvn -version 2>&1 | Select-Object -First 1
    Write-Host "Maven encontrado: $mavenVersion" -ForegroundColor Green
} catch {
    Write-Host "ERROR: Maven no está instalado o no está en el PATH" -ForegroundColor Red
    exit 1
}

# Compilar
Write-Host "`nCompilando proyecto..." -ForegroundColor Yellow
mvn clean compile

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: La compilación falló" -ForegroundColor Red
    exit 1
}

Write-Host "✓ Compilación exitosa" -ForegroundColor Green

# Ejecutar
Write-Host "`nEjecutando aplicación..." -ForegroundColor Yellow
Write-Host "Asegúrate de que los microservicios estén corriendo!" -ForegroundColor Yellow
Write-Host ""

mvn exec:java -Dexec.mainClass="co.unicauca.presentation.Application"


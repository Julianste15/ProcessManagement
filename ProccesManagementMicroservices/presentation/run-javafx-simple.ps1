# Script simple para ejecutar JavaFX sin módulos
# Este script usa exec:java que maneja mejor las dependencias

Write-Host "Ejecutando aplicación JavaFX..." -ForegroundColor Cyan

$baseDir = Split-Path -Parent $MyInvocation.MyCommand.Path
Set-Location $baseDir

# Compilar
Write-Host "Compilando..." -ForegroundColor Yellow
mvn clean compile -q

if ($LASTEXITCODE -ne 0) {
    Write-Host "ERROR: La compilación falló" -ForegroundColor Red
    exit 1
}

# Ejecutar con exec:java que maneja mejor las dependencias
Write-Host "Ejecutando aplicación..." -ForegroundColor Green
Write-Host "Asegúrate de que los microservicios estén corriendo!" -ForegroundColor Yellow
Write-Host ""

# Usar exec:java que automáticamente incluye las dependencias en el classpath
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.JavaFXApplication" -Dexec.classpathScope=runtime


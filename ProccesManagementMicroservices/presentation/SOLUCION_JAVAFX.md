# 🔧 Solución al Error "JavaFX runtime components are missing"

## 🎯 Solución Rápida (Recomendada)

Usa el script simple que maneja automáticamente las dependencias:

```powershell
cd presentation
.\run-javafx-simple.ps1
```

Este script usa `exec:java` que automáticamente incluye todas las dependencias de JavaFX en el classpath.

## 📋 Métodos Alternativos

### Método 1: Plugin JavaFX de Maven

```powershell
cd presentation
mvn clean compile
mvn javafx:run
```

### Método 2: exec:java (Más Confiable)

```powershell
cd presentation
mvn clean compile
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.JavaFXApplication"
```

### Método 3: Con Java directamente (Si los anteriores fallan)

1. **Descargar JavaFX SDK:**
   - Ve a https://openjfx.io/
   - Descarga JavaFX 21 para Windows
   - Extrae en `C:\javafx-sdk-21` (o donde prefieras)

2. **Compilar:**
   ```powershell
   mvn clean package
   ```

3. **Ejecutar:**
   ```powershell
   java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp "target/classes;target/dependency/*" co.unicauca.presentation.JavaFXApplication
   ```

## ✅ Verificación

Antes de ejecutar, asegúrate de:

1. **Microservicios corriendo:**
   ```powershell
   cd ..\..
   .\start-services.ps1
   ```

2. **Java 17+ instalado:**
   ```powershell
   java -version
   ```

3. **Maven instalado:**
   ```powershell
   mvn -version
   ```

## 🐛 Si Nada Funciona

### Limpiar y Recompilar

```powershell
cd presentation
mvn clean
mvn dependency:resolve
mvn compile
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.JavaFXApplication"
```

### Verificar Dependencias JavaFX

```powershell
mvn dependency:tree | findstr javafx
```

Debe mostrar:
- `javafx-controls`
- `javafx-fxml`

### Verificar que el JAR se creó

```powershell
Test-Path target/classes/co/unicauca/presentation/JavaFXApplication.class
```

Debe retornar `True`.

## 💡 Recomendación

**Usa siempre `exec:java`** en lugar de ejecutar directamente con `java`, ya que Maven maneja automáticamente el classpath con todas las dependencias de JavaFX.

---

**El script `run-javafx-simple.ps1` debería funcionar en la mayoría de los casos.** 🎉


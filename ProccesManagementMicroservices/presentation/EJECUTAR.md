# 🚀 Cómo Ejecutar la Aplicación JavaFX

## ⚠️ Solución al Error "JavaFX runtime components are missing"

Este error ocurre cuando JavaFX no puede encontrar sus módulos. Sigue estos pasos:

## ✅ Método 1: Usar el Plugin de JavaFX de Maven (Recomendado)

```powershell
cd presentation
mvn clean compile
mvn javafx:run
```

O usa el script:
```powershell
.\run-javafx.ps1
```

## ✅ Método 2: Ejecutar con Java directamente (Si el método 1 no funciona)

### Paso 1: Descargar JavaFX SDK

1. Ve a https://openjfx.io/
2. Descarga JavaFX 21 para Windows
3. Extrae el ZIP en una carpeta (ej: `C:\javafx-sdk-21`)

### Paso 2: Compilar

```powershell
cd presentation
mvn clean package
```

### Paso 3: Ejecutar con módulos

```powershell
java --module-path "C:\javafx-sdk-21\lib" --add-modules javafx.controls,javafx.fxml -cp "target/presentation-1.0-SNAPSHOT.jar;target/dependency/*" co.unicauca.presentation.JavaFXApplication
```

**Nota:** Ajusta la ruta `C:\javafx-sdk-21\lib` según donde hayas extraído JavaFX.

## ✅ Método 3: Usar exec:java con módulos

```powershell
mvn clean compile
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.JavaFXApplication" -Dexec.args="--module-path C:\javafx-sdk-21\lib --add-modules javafx.controls,javafx.fxml"
```

## 🔧 Verificar que Funciona

1. **Asegúrate de que los microservicios estén corriendo:**
   ```powershell
   cd ..\..
   .\start-services.ps1
   ```

2. **Verifica que el Gateway esté en el puerto 8080:**
   ```powershell
   Invoke-WebRequest http://localhost:8080/actuator/health
   ```

3. **Ejecuta la aplicación:**
   ```powershell
   cd presentation
   mvn javafx:run
   ```

## 🐛 Si Sigue Sin Funcionar

### Verificar Java
```powershell
java -version
```
Debe ser Java 17 o superior.

### Verificar Maven
```powershell
mvn -version
```

### Limpiar y Recompilar
```powershell
mvn clean
mvn compile
mvn javafx:run
```

### Verificar Dependencias
```powershell
mvn dependency:tree | findstr javafx
```
Debe mostrar las dependencias de JavaFX.

## 💡 Solución Rápida

Si nada funciona, intenta esto:

```powershell
cd presentation
mvn clean
mvn compile
mvn javafx:run
```

El plugin `javafx-maven-plugin` debería manejar automáticamente los módulos de JavaFX.

---

**¡La aplicación debería ejecutarse correctamente!** 🎉


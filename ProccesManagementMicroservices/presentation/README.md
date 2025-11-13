# 🖥️ Aplicación de Escritorio JavaFX

Aplicación de escritorio JavaFX que se conecta con microservicios para gestión de trabajos de grado.

## 🎯 Funcionalidades

- ✅ **Login**: Autenticación de usuarios
- ✅ **Registro**: Registro de nuevos usuarios
- ✅ **Dashboard**: Panel principal después del login

## 🚀 Cómo Ejecutar

### 1. Iniciar Microservicios

```powershell
cd ..\..
.\start-services.ps1
```

Asegúrate de que estén corriendo:
- Discovery Service (puerto 8761)
- Gateway Service (puerto 8080)
- Auth Service
- User Service

### 2. Compilar la Aplicación

```powershell
cd presentation
mvn clean compile
```

### 3. Ejecutar

#### Opción A: Con Maven (Recomendado)
```powershell
mvn javafx:run
```

#### Opción B: Con Java directamente
```powershell
mvn clean package
java --module-path "C:\path\to\javafx-sdk\lib" --add-modules javafx.controls,javafx.fxml -jar target/presentation-1.0-SNAPSHOT.jar
```

## 📁 Estructura del Proyecto

```
presentation/
├── src/main/java/co/unicauca/
│   ├── domain/              # Capa de dominio
│   │   ├── entities/        # Entidades (User)
│   │   ├── enums/          # Enumeraciones (Role, Career)
│   │   ├── services/       # Servicios (SessionService, UserService)
│   │   └── exceptions/     # Excepciones
│   ├── infrastructure/      # Capa de infraestructura
│   │   ├── client/         # Cliente HTTP (MicroserviceClient)
│   │   └── config/         # Configuración
│   └── presentation/       # Capa de presentación
│       ├── JavaFXApplication.java  # Clase principal
│       ├── controllers/    # Controladores
│       └── views/          # Vistas JavaFX
└── src/main/resources/
    ├── microservice.properties  # Configuración Gateway
    └── styles/
        └── application.css      # Estilos CSS
```

## 🎨 Pantallas

### Login
- Email institucional (@unicauca.edu.co)
- Contraseña
- Botón para ir a registro

### Registro
- Nombres y apellidos
- Email institucional
- Contraseña
- Teléfono (opcional)
- Carrera
- Rol (Estudiante/Profesor)

### Dashboard
- Información del usuario
- Mensaje de bienvenida
- Botón de logout

## ⚙️ Configuración

El archivo `src/main/resources/microservice.properties` contiene la URL del Gateway:

```properties
gateway.url=http://localhost:8080
```

## 🔧 Requisitos

- Java 17 o superior
- Maven 3.6+
- JavaFX 21 (incluido en las dependencias)

## 🐛 Solución de Problemas

### Error: "JavaFX runtime components are missing"
- Asegúrate de usar `mvn javafx:run` o tener JavaFX instalado

### Error de conexión
- Verifica que el Gateway esté corriendo en el puerto 8080
- Verifica que los microservicios estén registrados en Eureka

### La aplicación no inicia
- Verifica que Java 17 esté instalado: `java -version`
- Verifica que Maven esté instalado: `mvn -version`
- Revisa los logs en la consola

---

**¡Listo para usar!** 🎉


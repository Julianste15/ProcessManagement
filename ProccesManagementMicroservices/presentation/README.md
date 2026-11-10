# Cliente de escritorio JavaFX

Aplicación de escritorio que consume los microservicios del sistema de gestión de trabajos de grado a través del API Gateway.

## Funcionalidades

- **Inicio de sesión** con correo institucional (`@unicauca.edu.co`).
- **Registro** de estudiantes, docentes y coordinadores.
- **Dashboards** por rol: docente, estudiante, coordinador y jefe de departamento.
- **Formato A**: diligenciar, reenviar (máximo 3 intentos) y consultar estado.
- **Anteproyecto**: creación y carga del documento.
- **Evaluación**: aprobación/rechazo y asignación de evaluadores.

## Requisitos

- Java 17 o superior.
- Maven 3.9+.
- Backend en ejecución (ver el README principal).

## Cómo ejecutar

### 1. Levantar el backend

Desde la raíz del repositorio:

```bash
docker compose up -d --build
```

Verifica que Eureka (`http://localhost:8761`) y el gateway (`http://localhost:8080`) estén disponibles.

### 2. Ejecutar el cliente

```bash
cd ProccesManagementMicroservices/presentation
mvn javafx:run
```

También puedes usar los scripts incluidos:

```powershell
.\run-javafx.ps1
# Alternativa si hay problemas con el module-path:
.\run-javafx-simple.ps1
```

> El JAR generado con `mvn package` incluye las dependencias, pero **no** los módulos nativos de JavaFX. Para ejecutarlo directamente hay que indicar el `--module-path` de JavaFX; por eso se recomienda `mvn javafx:run`.

## Configuración

La URL del gateway se define en `src/main/resources/microservice.properties`:

```properties
gateway.url=http://localhost:8080
connection.timeout=5000
read.timeout=10000
```

## Estructura

```
presentation/
├── src/main/java/co/unicauca/
│   ├── domain/           # Entidades, enums, servicios de dominio
│   ├── infrastructure/   # Cliente HTTP y configuración
│   └── presentation/     # Aplicación, controladores y vistas JavaFX
├── src/main/resources/
│   ├── images/
│   ├── styles/
│   └── microservice.properties
├── run-javafx.ps1
└── run-javafx-simple.ps1
```

## Pantallas

- **Login**: correo institucional y contraseña.
- **Registro**: nombres, apellidos, correo, contraseña, teléfono, carrera y rol.
- **Dashboard docente**: estado del Formato A y accesos a diligenciar/consultar.
- **Formato A**: título, modalidad, director, codirector, estudiante, objetivos y PDF.
- **Dashboard coordinador**: formatos pendientes con acciones de aprobar/rechazar.
- **Dashboard jefe de departamento**: anteproyectos recibidos y asignación de evaluadores.
- **Vista estudiante**: estado del proyecto y observaciones.

Las capturas de cada pantalla están en [`docs/screenshots`](../../docs/screenshots).

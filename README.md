# Sistema de Gestión de Trabajos de Grado - FIET

<div align="center">

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Database-blue?style=for-the-badge&logo=postgresql)
![RabbitMQ](https://img.shields.io/badge/RabbitMQ-Messaging-orange?style=for-the-badge&logo=rabbitmq)
![Microservices](https://img.shields.io/badge/Architecture-Microservices-blueviolet?style=for-the-badge)

**Sistema integral para la gestión del proceso de trabajos de grado en la Facultad de Ingeniería Electrónica y Telecomunicaciones (FIET) de la Universidad del Cauca**

[Características](#-características-principales) •
[Arquitectura](#-arquitectura-del-sistema) •
[Tecnologías](#-stack-tecnológico) •
[Instalación](#-instalación-y-configuración) •
[Equipo](#-equipo-de-desarrollo)

</div>

---

## Descripción del Proyecto

El **Sistema de Gestión de Trabajos de Grado** es una plataforma completa diseñada para digitalizar y optimizar el flujo de trabajo de proyectos de grado en la FIET. El sistema permite a docentes, estudiantes, coordinadores y jefes de departamento gestionar eficientemente todo el ciclo de vida de un proyecto de grado, desde la presentación inicial del Formato A hasta la evaluación del anteproyecto.

### Objetivos

- **Digitalizar** el proceso de gestión de trabajos de grado
- **Automatizar** las notificaciones y el flujo de aprobaciones
- **Centralizar** la información de proyectos en una plataforma única
- **Facilitar** la comunicación entre todos los actores del proceso
- **Garantizar** la trazabilidad y el seguimiento de cada proyecto

---

## Características Principales

### Para Docentes
- Registro en el sistema con validación de credenciales institucionales
- Presentación del Formato A con información completa del proyecto
- Reenvío de Formato A rechazado (hasta 3 intentos)
- Carga de anteproyectos una vez aprobado el Formato A
- Visualización del estado de sus proyectos

### Para Estudiantes
- Consulta del estado de su proyecto de grado en tiempo real
- Recepción de notificaciones sobre evaluaciones
- Seguimiento del progreso del proyecto

### Para Coordinadores de Programa
- Evaluación de Formatos A presentados
- Aprobación o rechazo con observaciones detalladas
- Listado completo de proyectos y sus estados
- Notificaciones automáticas de nuevos envíos

### Para Jefes de Departamento
- Visualización de anteproyectos enviados
- Asignación de evaluadores del departamento
- Notificación automática a evaluadores designados

---

## Arquitectura del Sistema

El sistema está construido siguiendo una **arquitectura de microservicios**, lo que garantiza escalabilidad, mantenibilidad y desacoplamiento de responsabilidades.

### Diagrama de Arquitectura

```mermaid
graph TB
    subgraph "Capa de Presentación"
        UI[JavaFX Desktop Application]
    end
    
    subgraph "API Gateway Layer"
        GW[Gateway Service<br/>Port: 8080<br/>JWT Authentication]
    end
    
    subgraph "Service Discovery"
        EUREKA[Eureka Server<br/>Port: 8761<br/>Service Registry]
    end
    
    subgraph "Microservicios de Negocio"
        AUTH[Auth Service<br/>Port: 8081<br/>Authentication & JWT]
        USER[User Service<br/>Port: 8082<br/>User Management]
        FORMATA[Format-A Service<br/>Port: 8083<br/>Format A Workflow]
        COORD[Coordination Service<br/>Port: 8084<br/>Evaluation & Coordination]
        ANTE[Anteproject Service<br/>Port: 8085<br/>Anteproject Management]
        EVAL[Evaluation Service<br/>Port: 8086<br/>Evaluator Assignment]
        NOTIF[Notification Service<br/>Port: 8087<br/>Email Notifications]
    end
    
    subgraph "Message Broker"
        RABBIT[RabbitMQ<br/>Asynchronous Events]
    end
    
    subgraph "Bases de Datos"
        DB1[(PostgreSQL<br/>Auth DB)]
        DB2[(PostgreSQL<br/>Users DB)]
        DB3[(PostgreSQL<br/>Format-A DB)]
        DB4[(PostgreSQL<br/>Coordination DB)]
        DB5[(PostgreSQL<br/>Anteproject DB)]
        DB6[(PostgreSQL<br/>Evaluation DB)]
        DB7[(PostgreSQL<br/>Notification DB)]
    end
    
    UI -->|HTTP/REST| GW
    GW --> EUREKA
    GW --> AUTH
    GW --> USER
    GW --> FORMATA
    GW --> COORD
    GW --> ANTE
    GW --> EVAL
    
    AUTH --> EUREKA
    USER --> EUREKA
    FORMATA --> EUREKA
    COORD --> EUREKA
    ANTE --> EUREKA
    EVAL --> EUREKA
    NOTIF --> EUREKA
    
    FORMATA -.->|Events| RABBIT
    COORD -.->|Events| RABBIT
    ANTE -.->|Events| RABBIT
    EVAL -.->|Events| RABBIT
    RABBIT -.->|Consume| NOTIF
    
    AUTH --> DB1
    USER --> DB2
    FORMATA --> DB3
    COORD --> DB4
    ANTE --> DB5
    EVAL --> DB6
    NOTIF --> DB7
    
    FORMATA -->|Feign Client| USER
    COORD -->|Feign Client| USER
    ANTE -->|Feign Client| FORMATA
    EVAL -->|Feign Client| USER

    style UI fill:#e1f5ff
    style GW fill:#fff4e1
    style EUREKA fill:#ffe1f5
    style RABBIT fill:#f5e1ff
    style AUTH fill:#e1ffe1
    style USER fill:#e1ffe1
    style FORMATA fill:#e1ffe1
    style COORD fill:#e1ffe1
    style ANTE fill:#e1ffe1
    style EVAL fill:#e1ffe1
    style NOTIF fill:#e1ffe1
```

### Componentes del Sistema

#### 1. **Presentation Layer** (JavaFX)
- Aplicación de escritorio desarrollada con JavaFX 21
- Interfaz gráfica intuitiva para todos los roles de usuario
- Comunicación con microservicios a través del API Gateway

#### 2. **API Gateway** (Spring Cloud Gateway)
- Punto de entrada único para todas las peticiones
- Autenticación y autorización con JWT
- Enrutamiento dinámico a microservicios
- Circuit breaker con Resilience4j

#### 3. **Service Discovery** (Eureka)
- Registro y descubrimiento de servicios
- Balanceo de carga automático
- Alta disponibilidad de servicios

#### 4. **Microservicios de Negocio**

| Servicio | Puerto | Responsabilidad |
|----------|--------|-----------------|
| **auth-service** | 8081 | Autenticación de usuarios, generación de tokens JWT |
| **user-service** | 8082 | Gestión de usuarios (docentes, estudiantes, coordinadores) |
| **format-a-service** | 8083 | Gestión del Formato A, validaciones, control de intentos |
| **coordination-service** | 8084 | Evaluación de Formatos A, aprobaciones/rechazos |
| **anteproject-service** | 8085 | Gestión de anteproyectos, carga de documentos |
| **evaluation-service** | 8086 | Asignación de evaluadores, gestión de evaluaciones |
| **notification-service** | 8087 | Envío de notificaciones por email (asíncrono) |

#### 5. **Message Broker** (RabbitMQ)
- Comunicación asíncrona entre microservicios
- Patrón de eventos para notificaciones
- Desacoplamiento de servicios

#### 6. **Bases de Datos** (PostgreSQL)
- Base de datos independiente por microservicio
- Aislamiento de datos y autonomía de servicios

---

## Stack Tecnológico

### Backend
- **Java 17** - Lenguaje de programación
- **Spring Boot 3.2.0** - Framework principal
- **Spring Cloud 2023.0.0** - Microservicios y patrones cloud-native
  - Spring Cloud Gateway - API Gateway
  - Spring Cloud Netflix Eureka - Service Discovery
  - Spring Cloud OpenFeign - Client HTTP declarativo
  - Resilience4j - Circuit Breaker
- **Spring Data JPA** - Persistencia de datos
- **Spring AMQP** - Integración con RabbitMQ
- **Spring Security** - Seguridad (en desarrollo)

### Frontend
- **JavaFX 21** - Framework para aplicaciones de escritorio
- **Jackson** - Serialización/deserialización JSON

### Bases de Datos
- **PostgreSQL** - Base de datos relacional

### Mensajería
- **RabbitMQ** - Message broker para comunicación asíncrona

### Autenticación
- **JWT (JSON Web Tokens)** - Autenticación stateless
- **JJWT 0.11.5** - Librería para manejo de JWT

### Herramientas de Desarrollo
- **Maven** - Gestión de dependencias y build
- **Git** - Control de versiones
- **Lombok** - Reducción de código boilerplate

---

## Requisitos Funcionales

El sistema implementa los siguientes requisitos funcionales de alto valor:

### RF-01: Registro de Docentes
**Como** docente **necesito** registrarme en el sistema **para** iniciar el flujo de un proyecto de grado.

**Datos requeridos:**
- Nombres y apellidos
- Celular (opcional)
- Programa académico (Ingeniería de Sistemas, Ingeniería Electrónica y Telecomunicaciones, Automática Industrial, Tecnología en Telemática)
- Email institucional
- Contraseña (mínimo 6 caracteres, al menos un dígito, un carácter especial y una mayúscula)

### RF-02: Presentación del Formato A
**Como** docente **necesito** subir el Formato A **para** comenzar el proceso de proyecto de grado.

**Datos del formulario:**
- Título del proyecto
- Modalidad (Investigación, Práctica Profesional)
- Fecha actual
- Director del proyecto
- Codirector del proyecto
- Objetivo general
- Objetivos específicos
- Archivo PDF adjunto
- Carta de aceptación de empresa (si es Práctica Profesional)

> **Nota:** El sistema envía una notificación asíncrona al coordinador al enviar el Formato A.

### RF-03: Evaluación del Formato A
**Como** coordinador **necesito** evaluar un Formato A **para** aprobar, rechazar o dejar observaciones.

**Funcionalidades:**
- Listado de proyectos con su estado
- Evaluación con opciones: Aprobado, Rechazado
- Campo de observaciones
- Notificación automática a docentes y estudiantes implicados

### RF-04: Reenvío del Formato A
**Como** docente **necesito** subir una nueva versión del Formato A **para** continuar con el proceso tras un rechazo.

**Reglas de negocio:**
- Control de intentos (máximo 3)
- Rechazo definitivo después del tercer intento
- Notificación asíncrona al coordinador en cada reenvío

### RF-05: Consulta de Estado (Estudiante)
**Como** estudiante **necesito** ver el estado de mi proyecto **para** hacer seguimiento del proceso.

**Estados posibles:**
- En primera evaluación Formato A
- En segunda evaluación Formato A
- En tercera evaluación Formato A
- Formato A aceptado
- Formato A rechazado

### RF-06: Carga de Anteproyecto
**Como** docente **necesito** subir el anteproyecto **para** continuar con el proceso tras la aprobación del Formato A.

**Funcionalidades:**
- Carga de documento de anteproyecto
- Registro de fecha de envío
- Notificación asíncrona al jefe de departamento

### RF-07: Listado de Anteproyectos
**Como** jefe de departamento **necesito** ver los anteproyectos enviados **para** proceder con la asignación de evaluadores.

**Funcionalidades:**
- Visualización de anteproyectos pendientes
- Información del proyecto y docente director

### RF-08: Asignación de Evaluadores
**Como** jefe de departamento **necesito** delegar dos docentes evaluadores **para** que evalúen un anteproyecto.

**Funcionalidades:**
- Selección de dos evaluadores del departamento
- Notificación automática a los evaluadores designados

---

## Instalación y Configuración

### Prerrequisitos

- **Java 17** o superior
- **Maven 3.8+**
- **PostgreSQL 14+**
- **RabbitMQ 3.11+**
- **Git**

### 1. Clonar el Repositorio

```bash
git clone https://github.com/Julianste15/ProcessManagement.git
cd ProcessManagement/ProccesManagementMicroservices
```

### 2. Configurar Bases de Datos

Crear las siguientes bases de datos en PostgreSQL:

```sql
CREATE DATABASE auth_db;
CREATE DATABASE users_db;
CREATE DATABASE formata_db;
CREATE DATABASE coordination_db;
CREATE DATABASE anteproject_db;
CREATE DATABASE evaluation_db;
CREATE DATABASE notification_db;
```

### 3. Configurar RabbitMQ

Instalar y ejecutar RabbitMQ:

```bash
# En Windows (con Chocolatey)
choco install rabbitmq

# Iniciar el servicio
rabbitmq-server
```

Acceder a la consola de administración: `http://localhost:15672` (usuario: guest, password: guest)

### 4. Configurar Variables de Entorno

Cada microservicio requiere configuración de base de datos. Editar los archivos `application.yml` en cada servicio:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/[nombre_db]
    username: [tu_usuario]
    password: [tu_password]
```

### 5. Compilar el Proyecto

```bash
# Compilar todos los microservicios
mvn clean install -DskipTests
```

### 6. Ejecutar los Servicios

**Orden de inicio recomendado:**

```bash
# 1. Service Discovery
cd discovery-service
mvn spring-boot:run

# 2. Gateway (en otra terminal)
cd gateway-service
mvn spring-boot:run

# 3. Microservicios de negocio (en terminales separadas)
cd auth-service && mvn spring-boot:run
cd user-service && mvn spring-boot:run
cd format-a-service && mvn spring-boot:run
cd coordination-service && mvn spring-boot:run
cd anteproject-service && mvn spring-boot:run
cd evaluation-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run

# 4. Aplicación de escritorio
cd presentation
mvn javafx:run
```

### 7. Verificar el Sistema

- **Eureka Dashboard:** `http://localhost:8761`
- **API Gateway:** `http://localhost:8080`
- **RabbitMQ Management:** `http://localhost:15672`

---

## Estructura del Proyecto

```
ProccesManagementMicroservices/
├── discovery-service/          # Eureka Server
├── gateway-service/            # API Gateway
├── auth-service/               # Autenticación
├── user-service/               # Gestión de usuarios
├── format-a-service/           # Gestión de Formato A
├── coordination-service/       # Coordinación y evaluación
├── anteproject-service/        # Gestión de anteproyectos
├── evaluation-service/         # Asignación de evaluadores
├── notification-service/       # Notificaciones
├── presentation/               # Aplicación JavaFX
└── storage/                    # Almacenamiento de archivos
```

---

## Seguridad

El sistema implementa las siguientes medidas de seguridad:

- **Autenticación JWT:** Tokens seguros para autenticación stateless
- **Validación de contraseñas:** Requisitos de complejidad (mínimo 6 caracteres, mayúsculas, dígitos, caracteres especiales)
- **API Gateway:** Punto único de entrada con validación de tokens
- **Roles y permisos:** Control de acceso basado en roles (TEACHER, COORDINATOR, DEPARTMENT_HEAD, STUDENT)

---

## Patrones y Buenas Prácticas

### Patrones Implementados
- **API Gateway Pattern:** Punto de entrada único
- **Service Discovery Pattern:** Registro dinámico de servicios
- **Event-Driven Architecture:** Comunicación asíncrona con eventos
- **Circuit Breaker Pattern:** Resiliencia ante fallos
- **Database per Service:** Autonomía de datos por microservicio

### Principios SOLID
- **Single Responsibility:** Cada microservicio tiene una responsabilidad única
- **Open/Closed:** Extensible mediante nuevos microservicios
- **Dependency Inversion:** Uso de interfaces y abstracciones

---

## Testing

```bash
# Ejecutar tests unitarios
mvn test

# Ejecutar tests de un servicio específico
cd [nombre-servicio]
mvn test
```

---

## Documentación de API

El proyecto cuenta con documentación interactiva generada con **Swagger/OpenAPI**.

### Acceso a la Documentación

Una vez iniciados los microservicios, puedes acceder a la interfaz de Swagger UI para explorar y probar los endpoints:

| Servicio | URL de Swagger UI |
|----------|-------------------|
| **format-a-service** | `http://localhost:8083/swagger-ui/index.html` |
| *Otros servicios* | *Próximamente* |

---

## Roadmap y Mejoras Futuras

- [ ] Implementación de tests de integración
- [ ] Dashboard web con React/Angular
- [ ] Notificaciones en tiempo real con WebSockets
- [ ] Almacenamiento de archivos en la nube (AWS S3, Google Cloud Storage)
- [ ] Métricas y monitoreo con Prometheus y Grafana
- [ ] Containerización con Docker y orquestación con Kubernetes
- [ ] CI/CD con GitHub Actions
- [x] Documentación de API con Swagger/OpenAPI (Implementado en format-a-service)

---

## Equipo de Desarrollo

Este proyecto fue desarrollado por estudiantes de Ingeniería de Sistemas de la Universidad del Cauca como parte del curso de Software II:

| Nombre | Rol | GitHub |
|--------|-----|--------|
| **Julian Camacho** | Full Stack Developer | [@Julianste15](https://github.com/Julianste15) |
| **Oscar Cabezas** | Backend Developer | [@Oscar-cm14](https://github.com/Oscar-cm14) |
| **Santiago Hurtado** | Backend Developer | - |

### Contexto Académico

**Universidad:** Universidad del Cauca  
**Facultad:** Ingeniería Electrónica y Telecomunicaciones (FIET)  
**Programa:** Ingeniería de Sistemas  
**Curso:** Software II  
**Año:** 2025

---

## Licencia

Este proyecto es de código abierto y está disponible bajo la licencia MIT.

---

## Contacto y Soporte

Para preguntas, sugerencias o reportar problemas:

- **Issues:** [GitHub Issues](https://github.com/Julianste15/ProcessManagement/issues)
- **Email:** [Contactar al equipo]

---

<div align="center">

**⭐ Si este proyecto te fue útil, considera darle una estrella en GitHub ⭐**

Desarrollado por estudiantes de la Universidad del Cauca

</div>

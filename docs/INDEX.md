# Documentación del Proyecto - Process Management System

## 📚 Índice de Documentación

Bienvenido a la documentación del Sistema de Gestión de Procesos Académicos. Esta documentación está organizada en varios archivos para facilitar la navegación.

---

## 📖 Documentos Principales

### 1. [README.md](../README.md)
**Descripción General del Proyecto**

Contenido:
- ✅ Descripción del sistema
- ✅ Tecnologías utilizadas
- ✅ Resumen de pruebas implementadas
- ✅ Estrategias de escalabilidad
- ✅ Referencias a diagramas de arquitectura
- ✅ Instrucciones de instalación y ejecución
- ✅ Estrategia de branching (Git)
- ✅ Endpoints principales de las APIs

**Audiencia**: Desarrolladores nuevos, stakeholders, usuarios técnicos

---

### 2. [ARCHITECTURE.md](./ARCHITECTURE.md)
**Arquitectura del Sistema**

Contenido:
- 🏗️ Diagramas C4 (Contexto, Contenedores, Componentes)
- 🎯 Bounded Contexts (DDD)
- 🔧 Patrones arquitectónicos
- 💡 Decisiones de diseño
- 📐 Arquitectura hexagonal explicada
- 🔄 Event-Driven Architecture
- 🗄️ Database per Service pattern

**Audiencia**: Arquitectos de software, desarrolladores senior, revisores técnicos

---

### 3. [TESTING_AND_SCALABILITY.md](./TESTING_AND_SCALABILITY.md)
**Pruebas y Escalabilidad**

Contenido:
- ✅ Estrategia de pruebas
- 📊 Resultados de pruebas de carga (100 usuarios concurrentes)
- 📈 Análisis de escalabilidad horizontal y vertical
- 🎯 Proyección de capacidad
- ⚠️ Cuellos de botella identificados
- 💡 Recomendaciones a corto, medio y largo plazo
- 📉 Métricas de calidad

**Audiencia**: QA engineers, DevOps, arquitectos de infraestructura

---

### 4. [DESIGN_PATTERNS.md](../DESIGN_PATTERNS.md)
**Patrones de Diseño Implementados**

Contenido:
- 🎯 6 patrones de diseño implementados
- 📋 State Pattern en format-a-service
- 🏭 Builder Pattern en user-service
- 🔌 Adapter Pattern en anteproject-service (Hexagonal Architecture)
- 🎨 Decorator Pattern en notification-service
- 🏭 Factory Pattern en notification-service
- 🎭 Facade Pattern en presentation layer
- 📊 Distribución de patrones por categoría
- 🎓 Mejores prácticas demostradas

**Audiencia**: Desarrolladores, arquitectos, estudiantes de patrones de diseño

---

## 📋 Documentación del Proceso

Artefactos de ingeniería de requisitos, validación con usuarios y evolución del
producto. El índice detallado está en
[`proceso/README.md`](./proceso/README.md).

### 5. [user-stories.md](./user-stories.md)
**Backlog de Historias de Usuario y Criterios de Aceptación**

- 6 épicas, 21 historias y 63 criterios (2P + 1N por HU).
- Formato canónico, roles del dominio y estimación Fibonacci.
- Correspondencia con el backlog original del primer corte.

### 6. [proceso/trazabilidad.md](./proceso/trazabilidad.md)
**Matriz de Trazabilidad**

- HU → CA → endpoint → prueba.
- Estado de cobertura: cubierto, parcial, manual y dependiente de #55.
- Brechas de cobertura enlazadas a issues.

### 7. [proceso/guia-historias-de-usuario.md](./proceso/guia-historias-de-usuario.md)
**Guía de Redacción de HU y CA**

- Formato canónico y verbos rectores.
- Fórmulas BDD y balance 2P + 1N.
- Escala de estimación y checklist de aceptación.

### 8. [proceso/usabilidad.md](./proceso/usabilidad.md)
**Prueba de Usabilidad**

- Dos participantes (estudiante y docente).
- Escenarios, resultados Likert y hallazgos.
- Mejoras incorporadas al producto.

### 9. [proceso/demo.md](./proceso/demo.md) y [proceso/prototipo.md](./proceso/prototipo.md)
**Demostración y Prototipo**

- Vídeo del primer corte (versión Swing).
- Prototipo navegable en Figma.
- Comparación entre la versión inicial y la actual.

### 10. [adr/0003-arquitectura-inicial.md](./adr/0003-arquitectura-inicial.md)
**ADR-0003: Arquitectura Inicial**

- Monolito modular con frontend Swing y SQLite.
- Consecuencias y evolución hacia microservicios.

**Audiencia**: Reclutadores, revisores de portafolio, docentes, QA y nuevos
integrantes del equipo

---

## 🖼️ Diagramas

Todos los diagramas están ubicados en la carpeta [`diagrams/`](./diagrams/)

### Diagramas Disponibles

#### 1. [Diagrama de Contexto](./diagrams/context_diagram.png)
**Nivel C4: Contexto del Sistema**

Muestra:
- Actores externos (Estudiante, Profesor, Jefe de Departamento, Coordinador)
- Sistema principal
- Sistemas externos (Email, File Storage)
- Interacciones principales

---

#### 2. [Diagrama de Contenedores](./diagrams/container_diagram.png)
**Nivel C4: Contenedores**

Muestra:
- API Gateway
- Eureka Server
- 5 Microservicios (User, Format-A, Anteproject, Evaluation, Notification)
- Bases de datos PostgreSQL
- RabbitMQ
- Cliente JavaFX

---

#### 3. [Diagrama de Componentes](./diagrams/component_diagram.png)
**Nivel C4: Componentes (Anteproject Service)**

Muestra:
- Arquitectura Hexagonal
- Núcleo de dominio
- Adaptadores de entrada (Controllers, Event Consumers)
- Adaptadores de salida (Repositories, Event Publishers, REST Clients)
- Dependencias externas

---

#### 4. [Diagrama de Bounded Contexts](./diagrams/bounded_context_diagram.png)
**DDD: Contextos Acotados**

Muestra:
- 5 Bounded Contexts principales
- Relaciones entre contextos
- Patrones de integración (REST, Events)
- Flujo de datos entre contextos

---

## 🚀 Guías Rápidas

### Para Desarrolladores Nuevos

1. **Primero**: Lee el [README.md](../README.md) para entender el proyecto
2. **Segundo**: Revisa el [Diagrama de Contenedores](./diagrams/container_diagram.png) para ver la arquitectura general
3. **Tercero**: Lee [ARCHITECTURE.md](./ARCHITECTURE.md) sección "Arquitectura Hexagonal"
4. **Cuarto**: Configura tu entorno local siguiendo las instrucciones del README

### Para Arquitectos/Revisores

1. **Primero**: Revisa todos los diagramas en orden (Contexto → Contenedores → Componentes → Bounded Contexts)
2. **Segundo**: Lee [ARCHITECTURE.md](./ARCHITECTURE.md) completo
3. **Tercero**: Revisa [TESTING_AND_SCALABILITY.md](./TESTING_AND_SCALABILITY.md) para entender capacidades
4. **Cuarto**: Consulta las "Decisiones de Diseño" en ARCHITECTURE.md

### Para QA/DevOps

1. **Primero**: Lee [TESTING_AND_SCALABILITY.md](./TESTING_AND_SCALABILITY.md)
2. **Segundo**: Revisa los resultados de pruebas de carga
3. **Tercero**: Consulta las recomendaciones de infraestructura
4. **Cuarto**: Revisa los comandos útiles en el anexo

---

## 📊 Resumen Ejecutivo

### Tecnologías Principales
- **Backend**: Java 17, Spring Boot 3.x, Spring Cloud
- **Base de Datos**: PostgreSQL (4 instancias)
- **Mensajería**: RabbitMQ
- **Frontend**: JavaFX
- **Testing**: JUnit 5, Mockito
- **Containerización**: Docker, Docker Compose

### Arquitectura
- **Patrón**: Microservicios + Arquitectura Hexagonal
- **Comunicación**: REST (síncrona) + Events (asíncrona)
- **Escalabilidad**: Horizontal (stateless services)
- **Resiliencia**: Event-driven, Service Discovery
- **Patrones de Diseño**: 6 patrones implementados (State, Builder, Adapter, Decorator, Factory, Facade)

### Capacidad Demostrada
- ✅ **100 usuarios concurrentes** sin errores
- ✅ **100% de pruebas pasando** (22 pruebas totales)
- ✅ **Tiempo de respuesta**: < 5 segundos (p99)
- ✅ **Escalabilidad**: Lineal hasta 500 usuarios (estimado)

### Servicios Implementados
1. **User Service**: Autenticación y gestión de usuarios
2. **Format-A Service**: Gestión de Formatos A con PDFs (máx 10MB)
3. **Anteproject Service**: Gestión de anteproyectos y asignación de evaluadores
4. **Evaluation Service**: Gestión de evaluaciones y calificaciones
5. **Notification Service**: Envío de notificaciones por email

---

## 🔗 Enlaces Útiles

### Repositorio
- **GitHub**: [Julianste15/ProcessManagement](https://github.com/Julianste15/ProcessManagement)

### Herramientas de Desarrollo
- **Eureka Dashboard**: http://localhost:8761
- **API Gateway**: http://localhost:8080
- **RabbitMQ Management**: http://localhost:15672

### Documentación Externa
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [C4 Model](https://c4model.com/)
- [Domain-Driven Design](https://martinfowler.com/bliki/DomainDrivenDesign.html)
- [Hexagonal Architecture](https://alistair.cockburn.us/hexagonal-architecture/)

---

## 📝 Historial de Cambios

### Versión 2.1 (Diciembre 2024)
- ✅ Implementados 6 patrones de diseño (State, Builder, Adapter, Decorator, Factory, Facade)
- ✅ Arquitectura hexagonal en anteproject-service
- ✅ Dockerización de user-service y PostgreSQL
- ✅ Configuración de docker-compose para desarrollo
- ✅ Documentación completa de patrones (DESIGN_PATTERNS.md)
- ✅ Adaptadores para comunicación entre servicios
- ✅ Actualización de documentación de arquitectura

### Versión 2.0 (Diciembre 2024)
- ✅ Agregados diagramas C4 completos
- ✅ Documentación de arquitectura hexagonal
- ✅ Análisis de bounded contexts (DDD)
- ✅ Pruebas de carga con 100 usuarios concurrentes
- ✅ Análisis de escalabilidad
- ✅ Recomendaciones de producción

### Versión 1.0 (Noviembre 2024)
- ✅ Implementación inicial de microservicios
- ✅ Integración con RabbitMQ
- ✅ Cliente JavaFX funcional
- ✅ Pruebas unitarias básicas

---

## 👥 Contribuidores

- **Equipo de Desarrollo**: Universidad del Cauca
- **Curso**: Software II - 2024
- **Profesor**: [Nombre del Profesor]

---

## 📧 Contacto

Para preguntas o sugerencias sobre la documentación:
- **Email**: [email del equipo]
- **Issues**: [GitHub Issues](https://github.com/Julianste15/ProcessManagement/issues)

---

## 📄 Licencia

Este proyecto es de uso académico para la Universidad del Cauca.

---

**Última actualización**: Diciembre 6, 2024  
**Versión de la documentación**: 2.0

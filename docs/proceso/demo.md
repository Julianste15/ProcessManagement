# Demostración del primer corte (versión Swing)

Vídeo de la primera entrega funcional del sistema, grabado el **29 de septiembre
de 2025**. Corresponde a la versión **Java Swing** descrita en el
[ADR-0003](../adr/0003-arquitectura-inicial.md).

- **Enlace:** <https://youtu.be/y80yM8LkPVI>
- **Fecha:** 2025-09-29
- **Frontend:** Java Swing (versión inicial)
- **Backend:** monolito modular
- **Persistencia:** SQLite
- **Fuente original del enlace:** [`docs/proceso/fuentes/link youtube.txt`](fuentes/link%20youtube.txt)

## Qué muestra

1. Registro de un usuario con validación de correo institucional y contraseña.
2. Inicio de sesión y navegación por rol.
3. Consulta del estado del proyecto de grado.
4. Flujo inicial de Formato A.
5. Paneles diferenciados para estudiante, docente y administrador.

## Qué cambió desde entonces

| Aspecto | Primer corte (vídeo) | Versión actual |
|---|---|---|
| Frontend | Java Swing | JavaFX |
| Backend | Monolito modular | Microservicios Spring Boot |
| Comunicación | Llamadas internas | API Gateway + REST + Eureka |
| Base de datos | SQLite | PostgreSQL por servicio |
| Mensajería | No aplica | RabbitMQ |
| Seguridad | Sesión local | JWT + cabeceras de identidad en el gateway |
| Formato A | Flujo inicial | Estados, reintentos y evaluación con reglas |
| Anteproyecto | No implementado | Registro, envío, asignación y evaluación |

El vídeo se conserva como **evidencia histórica**: muestra el estado real del
producto en esa fecha y no debe interpretarse como la arquitectura vigente. La
arquitectura actual está en [`docs/ARCHITECTURE.md`](../ARCHITECTURE.md).

## Cómo leer esta evidencia en una entrevista

- El vídeo demuestra que el sistema **funcionaba de extremo a extremo** en el
  primer corte, no solo que existía código.
- La tabla de evolución explica las decisiones de migración y justifica el
  aprendizaje: primero se validó el flujo, después se mejoró la arquitectura.
- La versión actual añade microservicios, mensajería, seguridad y pruebas; el
  vídeo no reemplaza esas evidencias.

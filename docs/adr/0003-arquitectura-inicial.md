# ADR-0003 — Arquitectura inicial del primer corte: monolito modular con frontend desktop

- **Estado:** Aceptada (histórica). Superada por la arquitectura de microservicios del segundo corte.
- **Fecha:** Septiembre de 2025 (primer corte).
- **Decisores:** Julian Stevan Camacho Rosero, Santiago Alejandro Hurtado Campo, Oscar David Cabezas Moreno.
- **Contexto del curso:** Ingeniería de Software II, Universidad del Cauca.
- **Fuente:** [`docs/proceso/fuentes/Arquitectura y diseño.docx`](../proceso/fuentes/Arquitectura%20y%20dise%C3%B1o.docx).

## Contexto

El primer corte del proyecto debía entregar un sistema funcional para la
gestión de trabajos de grado de la FIET con un equipo pequeño y un plazo corto.
El alcance incluía autenticación multi-rol, perfiles de estudiante, docente y
administrador, y un panel por rol.

En ese momento no existía una plataforma web ni infraestructura de despliegue
distribuido. El equipo necesitaba:

- una interfaz utilizable sin depender de un navegador;
- una base de datos local para iterar sin administrar servidores;
- una separación clara entre interfaz, dominio y persistencia que permitiera
  evolucionar después.

## Decisión

Se adoptó una **arquitectura cliente-servidor con monolito modular**:

1. **Frontend desktop en Java Swing** con `GroupLayout`, patrón MVC y navegación
   por panel lateral.
2. **Backend monolítico modular** con capas `presentation`, `domain` e
   `infrastructure`:
   - `presentation`: controladores, vistas y observer;
   - `domain`: entidades (`User`, `Project`, `Document`), enums (`Career`,
     `Role`), excepciones, interfaces de repositorio y servicios;
   - `infrastructure`: inyección de dependencias, persistencia, seguridad y
     validación.
3. **Persistencia en SQLite** para usuarios, proyectos y sesiones, con archivos
   para la documentación de los proyectos.
4. **Comunicación frontend-backend** mediante controladores y servicios, con
   endpoints REST internos (`/auth/login`, `/users/profile`, `/projects`,
   `/academic/progress`).
5. **Patrones:** Observer, Dependency Injection, Service Layer y Repository.

La decisión priorizó **entregar un flujo completo y demostrable** antes que una
arquitectura distribuida. El backend se diseñó con módulos desacoplados y una
API consistente para permitir la migración posterior.

## Consecuencias

### Positivas

- Tiempo de respuesta inmediato y experiencia de escritorio nativa.
- Separación de responsabilidades clara y pruebas facilitadas por inyección de
  dependencias.
- Módulos desacoplados listos para dividirse en servicios.
- API consistente que habilita clientes adicionales (web o móvil).

### Negativas

- Dependencia de plataforma: la aplicación desktop no es multiplataforma como
  una aplicación web.
- Distribución más compleja para el usuario final.
- Limitaciones de UI frente a tecnologías web modernas.
- SQLite y un monolito tienen límites de concurrencia y escalabilidad.

### Mitigaciones adoptadas

- Backend REST preparado para un frontend web.
- Módulos con límites claros para facilitar la migración.
- API estable para permitir múltiples clientes.

## Evolución posterior

En el segundo corte el sistema evolucionó a **microservicios** con Spring Boot,
API Gateway, Eureka, PostgreSQL y RabbitMQ. El frontend se migró a **JavaFX** y
se mantuvo el patrón MVC con cliente HTTP.

La arquitectura inicial sigue siendo relevante porque explica:

- por qué el dominio está separado por módulos;
- por qué existen patrones como Repository, Service Layer y Observer;
- por qué la migración a servicios no requirió reescribir las reglas de negocio.

La arquitectura vigente se documenta en
[`docs/ARCHITECTURE.md`](../ARCHITECTURE.md). Las decisiones posteriores
(ADR-0001, retiro de `coordination-service`; ADR-0002, cabeceras de identidad)
se incorporan en el PR #55.

## Alternativas consideradas

| Alternativa | Por qué no se eligió en el primer corte |
|---|---|
| Aplicación web monolítica | Requería hosting, autenticación web y más tiempo de infraestructura. |
| Microservicios desde el inicio | Sobredimensionado para el alcance y el tamaño del equipo. |
| Aplicación desktop con base de datos embebida sin API | Dificultaba la evolución a múltiples clientes y la integración futura. |

## Referencias

- [`docs/proceso/fuentes/Arquitectura y diseño.docx`](../proceso/fuentes/Arquitectura%20y%20dise%C3%B1o.docx) — documento original del primer corte.
- [`docs/ARCHITECTURE.md`](../ARCHITECTURE.md) — arquitectura vigente.
- [`docs/proceso/demo.md`](../proceso/demo.md) — demostración del primer corte.
- [`docs/proceso/prototipo.md`](../proceso/prototipo.md) — prototipo de interfaz.

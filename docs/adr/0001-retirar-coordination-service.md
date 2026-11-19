# ADR-0001: Retirar `coordination-service` del stack

- **Estado:** Aceptada
- **Fecha:** 2026-11-10
- **Contexto del cambio:** refactor de portafolio, issue #37
- **Decisores:** equipo de desarrollo

## Contexto

El sistema se organizó inicialmente con un microservicio por rol, entre ellos
`coordination-service`, pensado como dashboard y agregador para el Coordinador
del programa. Con el tiempo, la funcionalidad real del coordinador terminó
implementada en los servicios de dominio:

| Función del coordinador | Servicio que la implementa |
|---|---|
| Ver Formatos A pendientes de revisión | `format-a-service` → `GET /api/format-a/coordinator/{email}` |
| Aprobar o rechazar un Formato A | `format-a-service` → `PUT /api/format-a/{id}/evaluate` |
| Ver anteproyectos enviados | `anteproject-service` → `GET /api/anteprojects/submitted` |
| Cambiar el estado de un anteproyecto | `anteproject-service` → `PUT /api/anteprojects/{id}/status` |
| Gestionar evaluaciones | `evaluation-service` |

`coordination-service` quedó como un agregador de solo lectura con cuatro
endpoints de consulta (`dashboard`, `projects`, `pending-format-a`, `reports`).
No aprobaba, rechazaba ni cambiaba estados.

Además, el servicio estaba desconectado de la arquitectura real:

1. **El API Gateway no tenía ruta** `/api/coordination/**`, por lo que el
   cliente nunca podía alcanzarlo a través del punto de entrada.
2. **El cliente JavaFX nunca lo invocaba.** El dashboard del coordinador llama
   directamente a `format-a-service`.
3. **Sus contratos Feign no coincidían con los endpoints reales:**
   - `AnteprojectClient` apuntaba a `GET /api/anteprojects/coordinator/{id}`,
     endpoint que no existe en `anteproject-service`.
   - `FormatAClient` apuntaba a
     `GET /api/format-a/coordinator/{id}/pending`; el endpoint real es
     `GET /api/format-a/coordinator/{email}`, sin `/pending`.
4. **Su persistencia era código muerto.** `CoordinationDashboard` y
   `CoordinationRepository` no se usaban en ninguna parte, y la base
   `coordinationdb` no guardaba datos de negocio.

En la práctica, el dashboard y el reporte de coordinación nunca funcionaron de
extremo a extremo: una llamada directa al puerto respondía 404 por los
contratos incorrectos.

## Decisión

Se retira `coordination-service` del repositorio, de `docker-compose.yml`, de
`docker-entrypoint-initdb.d/init.sql` y de la documentación.

Los servicios de dominio siguen siendo los dueños de sus datos y de las reglas
del proceso. El **rol** `COORDINATOR` se conserva y se implementa mediante
autorización en `user-service`, en el gateway y en cada servicio de dominio; no
requiere un microservicio propio.

## Justificación

- **No se pierde ninguna función de usuario.** Todo lo que el coordinador hace
  ya vive en los servicios de dominio.
- **Se elimina un servicio no operativo** que sumaba complejidad de despliegue,
  una base de datos y una entrada adicional en Eureka sin aportar valor.
- **Se alinea el diseño con los límites del dominio** (usuarios, formatos,
  anteproyectos, evaluaciones) en lugar de organizar servicios por rol.
- **Se reduce la superficie de ataque y el costo operativo.**
- **Mejora la honestidad técnica del portafolio:** es preferible un stack más
  pequeño y coherente que uno con servicios que no funcionan.

## Consecuencias

### Positivas

- Un servicio y una base de datos menos que desplegar y mantener.
- Diagramas, puertos y documentación coherentes con lo que realmente corre.
- Menos confusión para quien lee el repositorio o levanta el sistema.

### Negativas / trade-offs

- Desaparece la idea de un dashboard agregado de coordinación. Si se quiere
  recuperar, hay que construirlo correctamente como un BFF/agregador (ver
  alternativas).
- Las tareas de coordinación quedan repartidas en varios endpoints; el cliente
  debe orquestarlas.

## Alternativas consideradas

1. **Dejarlo como está.** Rechazada: mantiene un servicio roto, una base de
   datos inútil y afirmaciones falsas en el README.
2. **Arreglarlo como agregador real.** Viable, pero implica ruta en el gateway,
   contratos correctos, uso desde JavaFX y pruebas de extremo a extremo. Es una
   funcionalidad nueva, no una corrección P0; queda como posible evolución.
3. **Mover el dashboard a `format-a-service`.** Rechazada: mezclaría
   responsabilidades de presentación con el dominio de Formatos A.
4. **Construir un BFF dedicado.** Es la opción correcta si en el futuro se
   necesita una vista agregada multi-dominio; hoy no hay requisito que la
   justifique.

## Referencias

- Issue #37: *Eliminar `coordination-service` y justificar la decisión*.
- Issue #51: *Documentar las decisiones arquitectónicas como ADRs*.
- `format-a-service`: `FormatAController`, `FormatoAService`.
- `anteproject-service`: `AnteprojectRestController`.
- `docs/ARCHITECTURE.md`.

# Guía de redacción de historias de usuario y criterios de aceptación

Esta guía sintetiza el estándar de la **Guía Oficial de Redacción de HU y CA**
(Ingeniería de Requisitos) y lo adapta al dominio de gestión de trabajos de
grado de la FIET. Se usa como referencia obligatoria para el backlog de
[`docs/user-stories.md`](../user-stories.md).

## 1. Propósito

Escribir historias de usuario (HU) y criterios de aceptación (CA) que sean:

- **Verificables:** cada CA se puede comprobar con una prueba manual o automatizada.
- **Atómicos:** una HU expresa una sola intención.
- **Trazables:** cada HU se conecta con un endpoint y, cuando existe, con una prueba.
- **Consistentes:** todos los equipos usan el mismo formato y los mismos roles.

## 2. Formato canónico

> **Como** [rol formal del dominio],
> **quiero** [verbo rector en infinitivo + objeto concreto],
> **para** [beneficio verificable].

### Reglas de redacción

1. **Un único verbo rector.** No se admiten verbos compuestos.
   - Correcto: *quiero **aprobar** un Formato A*.
   - Incorrecto: *quiero **aprobar y notificar** un Formato A*.
2. **Verbos prohibidos por ambiguos:** *gestionar*, *administrar*, *manejar*,
   *procesar*. Tampoco se usan *asignar y actualizar*, *crear y enviar*, etc.
3. **Objeto concreto.** El objeto debe ser un artefacto del dominio
   (Formato A, anteproyecto, evaluación, jefe de departamento), no una pantalla.
4. **Beneficio verificable.** El `para` explica el valor para el rol, no repite
   la acción. *Para aprobar el Formato A* no es un beneficio; *para habilitar el
   registro del anteproyecto* sí lo es.
5. **Una HU por intención.** Si el listado original usa un verbo compuesto, se
   divide en varias HU atómicas. Ejemplo: *evaluar (aprobar/rechazar)* se
   convierte en **aprobar** y **rechazar**.

## 3. Roles formales del dominio

| Rol | Descripción | Representación técnica |
|---|---|---|
| Estudiante | Cursa el trabajo de grado y envía documentos. | `STUDENT` |
| Docente | Dirige o codirige un trabajo de grado. | `TEACHER` |
| Docente evaluador | Docente asignado para evaluar un anteproyecto. | `TEACHER` con una evaluación asignada |
| Coordinador/a | Administra el proceso y evalúa Formatos A. | `COORDINATOR` |
| Jefe/a de Departamento | Docente con autoridad para asignar evaluadores. | `TEACHER` + `X-User-IsDepartmentHead: true` |

La guía original define roles de otro dominio. Se aplica su regla de adaptación:
**usar los roles formales del dominio en el que se escribe la HU**.

## 4. Criterios de aceptación en BDD

Cada criterio se redacta con las fórmulas obligatorias:

| Elemento | Fórmula | Ejemplo |
|---|---|---|
| Título | Sintagma nominal, sin verbo conjugado | *Aprobación de un Formato A en evaluación* |
| Contexto | `Dado que he` + participio | *Dado que he iniciado sesión como coordinador* |
| Evento | `Cuando` + presente, con el **mismo verbo rector** de la HU | *Cuando apruebo el Formato A* |
| Resultado | `Entonces el sistema deberá` + infinitivo | *Entonces el sistema deberá registrar el estado `FORMATO_A_ACEPTADO`* |

### Lineamientos técnicos

Cada CA incluye una columna de lineamientos con lo que un desarrollador o QA
necesita para verificarlo:

- endpoint y método HTTP;
- validaciones de entrada;
- regla de negocio;
- efecto en base de datos o evento publicado;
- código HTTP esperado;
- prueba asociada (clase o método).

Los lineamientos **no reemplazan** el resultado en lenguaje natural: lo
complementan.

## 5. Balance 2P + 1N

Cada HU tiene exactamente **tres criterios**:

- **2 positivos (P):** el camino feliz y una variante válida del negocio.
- **1 negativo (N):** una entrada inválida, un permiso denegado o un estado que
  impide la operación.

El criterio negativo siempre debe verificar **dos cosas**: el rechazo y la
**ausencia de efectos secundarios** (no se guarda, no se modifica, no se llama a
otro servicio).

## 6. Plantilla de tabla

### 6.1 Metadatos de la HU

| Campo | Valor |
|---|---|
| **ID** | `HU-XX` |
| **Épica** | `EP-XX` |
| **Rol** | Rol formal del dominio |
| **Característica** | Verbo rector + objeto |
| **Razón** | Beneficio verificable |
| **Prioridad** | Alta / Media / Baja |
| **Estimación** | Puntos de historia (escala Fibonacci) |
| **Iteración** | Sprint o corte |
| **Endpoint** | Método y ruta principal |

### 6.2 Criterios de aceptación

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-XX.1 (P) | Título nominal | `Dado que he ...` | `Cuando ...` | `Entonces el sistema deberá ...` | Endpoint, validaciones, HTTP, prueba |

## 7. Escala de estimación

Se usa la serie de Fibonacci modificada: **1, 2, 3, 5, 8, 13** puntos de
historia.

| Valor | Complejidad | Ejemplo |
|---:|---|---|
| 1 | Trivial, sin reglas | Consultar una lista vacía |
| 2 | Baja, una validación | Consultar mis Formatos A |
| 3 | Media, reglas y permisos | Registro con validaciones |
| 5 | Alta, varios servicios o estados | Diligenciar Formato A, asignar evaluadores |
| 8 | Muy alta, flujo completo | Evaluación con notificaciones |
| 13 | Épica que debe dividirse | — |

Los story points de una épica son la **suma de sus HU**, no un valor aparte.
Esto corrige la inconsistencia del backlog anterior, donde una HU de 5 SP
aparecía dentro de una épica de 30 SP sin relación.

## 8. Ejemplo completo

### HU-EJ — Aprobar un Formato A

| Campo | Valor |
|---|---|
| **ID** | `HU-EJ` |
| **Épica** | `EP-02` |
| **Rol** | Coordinador/a |
| **Característica** | Aprobar un Formato A |
| **Razón** | Habilitar el registro del anteproyecto |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `PUT /api/format-a/{id}/evaluate` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-EJ.1 (P) | Aprobación de un Formato A en evaluación | Dado que he iniciado sesión como coordinador y el Formato A está en evaluación | Cuando apruebo el Formato A | Entonces el sistema deberá registrar el estado `FORMATO_A_ACEPTADO` y responder `200` | Body `{ "estado": "FORMATO_A_ACEPTADO", "observaciones": "..." }`; rol `COORDINATOR`; prueba `FormatoAStateTest` |
| CA-EJ.2 (P) | Aprobación con observaciones | Dado que he iniciado sesión como coordinador y el Formato A está en evaluación | Cuando apruebo el Formato A con observaciones | Entonces el sistema deberá guardar las observaciones y publicar el evento de evaluación | `eventPublisher.publishFormatoAEvaluado`; respuesta con `observaciones` |
| CA-EJ.3 (N) | Aprobación por un rol no autorizado | Dado que he iniciado sesión como estudiante | Cuando apruebo el Formato A | Entonces el sistema deberá responder `403` y no modificar el estado | Validación de rol en el controlador; prueba de autorización de #55 |

## 9. Lista de verificación antes de aceptar una HU

- [ ] Tiene un solo verbo rector en infinitivo.
- [ ] El rol es uno de los roles formales del dominio.
- [ ] El `para` expresa un beneficio verificable.
- [ ] Tiene exactamente 3 CA: 2 positivos y 1 negativo.
- [ ] El contexto usa `Dado que he` + participio.
- [ ] El evento usa `Cuando` + presente y repite el verbo rector.
- [ ] El resultado usa `Entonces el sistema deberá` + infinitivo.
- [ ] El CA negativo verifica que no hay efectos secundarios.
- [ ] El endpoint y la prueba están identificados en la matriz de trazabilidad.
- [ ] La estimación usa la escala Fibonacci y suma correctamente en la épica.

## 10. Adaptación y fuentes

- **Guía base:** Guía Oficial de Redacción de HU y CA (Ingeniería de
  Requisitos), aportada por el equipo.
- **Adaptaciones:** roles del dominio FIET, verbos rectores del proceso de
  trabajos de grado y lineamientos técnicos del stack real (Spring Boot,
  PostgreSQL, RabbitMQ, gateway).
- **Backlog anterior:** `docs/proceso/fuentes/INGENIERÍA DE SOFTWARE HU - Vrs 0.xlsx`.
  La correspondencia de identificadores se documenta en
  [`docs/user-stories.md`](../user-stories.md#correspondencia-con-el-backlog-anterior).

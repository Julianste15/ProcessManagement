# Prueba de usabilidad

Registro de la prueba de usabilidad aplicada al primer corte del sistema, con
dos participantes de perfiles distintos. La fuente original es
[`docs/proceso/fuentes/Usabilidad.docx`](fuentes/Usabilidad.docx).

## Objetivo

Verificar si las tareas básicas del sistema son **comprensibles, localizables y
satisfactorias** para un estudiante y un docente:

1. registrar una cuenta;
2. iniciar sesión;
3. consultar el estado del proyecto o acceder a las funciones del rol.

La prueba busca detectar problemas de claridad en validaciones, mensajes de
error y navegación por rol, no medir rendimiento ni seguridad.

## Método

- **Tipo:** prueba moderada, presencial, con observación directa.
- **Duración:** una sesión por participante.
- **Instrumento:** tres escenarios de tareas y una encuesta posterior de cuatro
  preguntas en escala Likert de 1 a 5 (1 = muy difícil, 5 = muy fácil).
- **Datos registrados:** observaciones del moderador y puntajes de la encuesta.
- **Muestra:** 2 participantes, seleccionados por conveniencia. Los resultados
  son cualitativos y no se generalizan.

## Participantes

| Participante | Rol | Edad | Escenarios |
|---|---|---|---:|
| Julian Mendez | Estudiante | 23 | 3 |
| Santiago Pino | Docente | 43 | 3 |

## Escenarios y observaciones

### Participante 1 — Estudiante

| # | Tarea | Observaciones |
|---|---|---|
| 1 | Registrarse como estudiante | El formulario resultó intuitivo. Las reglas de contraseña no se muestran antes de escribir; los mensajes de error ayudan a corregir. |
| 2 | Iniciar sesión | El acceso fue claro. Los errores de credenciales se entienden, aunque el mensaje podría indicar el campo inválido. |
| 3 | Consultar el estado del proyecto | El estado se encontró rápido. La etiqueta técnica (`FORMATO_A_EN_EVALUACION`) no es familiar para un estudiante. |

### Participante 2 — Docente

| # | Tarea | Observaciones |
|---|---|---|
| 1 | Registrarse como docente | El formulario es claro y el dominio institucional se valida de forma visible. La carta de aceptación de empresa no siempre aplica y genera dudas. |
| 2 | Iniciar sesión | Acceso sin dificultad. El sistema informa correctamente los errores comunes. |
| 3 | Acceder a las funciones del rol | La navegación por rol es comprensible. El panel inicial concentra muchas opciones y requiere una jerarquía más clara. |

## Resultados de la encuesta

Escala: 1 = muy difícil, 5 = muy fácil.

| Pregunta | Estudiante | Docente | Promedio |
|---|---:|---:|---:|
| ¿Fue fácil entender qué debía hacer en esta pantalla? | 4 | 4 | 4.00 |
| ¿La información estaba clara y bien organizada? | 5 | 4 | 4.50 |
| ¿Los botones y opciones eran fáciles de encontrar? | 5 | 5 | 5.00 |
| ¿Me siento satisfecho con la experiencia general? | 4 | 4 | 4.00 |
| **Promedio por participante** | **4.50** | **4.25** | **4.38** |

## Hallazgos

| Hallazgo | Severidad | Acción propuesta |
|---|---|---|
| Las reglas de contraseña no se muestran antes del error. | Media | Mostrar la política junto al campo y validar en vivo. |
| Los estados técnicos no son comprensibles para el estudiante. | Media | Traducir los estados a etiquetas de negocio («En evaluación», «Aceptado»). |
| El mensaje de credenciales inválidas no distingue el campo. | Baja | Indicar si el problema es el correo o la contraseña, sin revelar si la cuenta existe. |
| El panel del docente concentra demasiadas acciones. | Media | Agrupar por proceso (Formato A, anteproyecto, evaluaciones). |
| La carta de aceptación de empresa no siempre aplica. | Baja | Explicar cuándo es obligatoria y permitir omitirla cuando no aplica. |

## Conclusiones

- Las tres tareas se completaron sin bloqueos.
- La comprensión general es alta (promedio 4.38/5), con oportunidades de mejora
  en mensajes y jerarquía visual.
- La muestra es pequeña; los hallazgos se usan como insumo de diseño y no como
  evidencia estadística.
- Varias mejoras se incorporaron en el segundo corte (etiquetas de estado,
  navegación por rol y mensajes más específicos).

## Relación con el backlog

| Documento original | Historia actual |
|---|---|
| H1 — Registro de docente | `HU-01` |
| H2 — Consulta del estado del proyecto | `HU-02` |
| H14 — Registro de estudiante | `HU-03` |
| H12 — Inicio de sesión | `HU-04` |

El detalle de cada historia y sus criterios está en
[`docs/user-stories.md`](../user-stories.md).

> **Nota:** el documento original usa H2 para tareas de inicio de sesión del
> docente. En este registro se conserva la observación y se asocia el inicio de
> sesión con `HU-04`, que es la historia canónica de autenticación.

# Historias de usuario y criterios de aceptación

Backlog funcional del **Sistema de Gestión de Trabajos de Grado — FIET**,
redactado con el estándar de
[`docs/proceso/guia-historias-de-usuario.md`](proceso/guia-historias-de-usuario.md).

- **21 historias de usuario** agrupadas en **6 épicas**.
- **63 criterios de aceptación**: exactamente **2 positivos y 1 negativo** por HU.
- **68 puntos de historia** en escala Fibonacci.
- Cada criterio indica endpoint, regla de negocio y prueba asociada cuando existe.

> **Alcance temporal.** El backlog describe el comportamiento objetivo del
> sistema al cierre del P0 (PR #55). Algunos criterios de autorización
> (`COORDINATOR`, jefe de departamento) solo se cumplen después de ese PR; la
> matriz de [`docs/proceso/trazabilidad.md`](proceso/trazabilidad.md) lo indica
> criterio por criterio.

## Roles del dominio

| Rol | Descripción | Representación técnica |
|---|---|---|
| Estudiante | Cursa el trabajo de grado y consulta su estado. | `STUDENT` |
| Docente | Dirige o codirige un trabajo de grado y diligencia el Formato A. | `TEACHER` |
| Docente evaluador | Docente asignado para evaluar un anteproyecto. | `TEACHER` con evaluación asignada |
| Coordinador/a | Evalúa Formatos A y actualiza el estado de los anteproyectos. | `COORDINATOR` |
| Jefe/a de Departamento | Docente con autoridad para asignar evaluadores. | `TEACHER` + `X-User-IsDepartmentHead: true` |
| Administrador/a | Asigna al jefe de departamento. | `ADMINISTRATOR` |
| Usuario registrado | Cuenta activa; el rol determina el panel al que accede. | Cualquiera de los anteriores |

## Resumen de épicas

| Épica | Nombre | HU | SP |
|---|---|---|---:|
| EP-01 | Autenticación y registro | HU-01 – HU-04 | 12 |
| EP-02 | Formato A | HU-05 – HU-09 | 21 |
| EP-03 | Anteproyecto | HU-10 – HU-13 | 13 |
| EP-04 | Evaluación | HU-14 – HU-16 | 12 |
| EP-05 | Coordinación y jefatura | HU-17 – HU-19 | 6 |
| EP-06 | Notificaciones | HU-20 – HU-21 | 4 |
| **Total** | | **21** | **68** |

---

## EP-01 — Autenticación y registro

### HU-01 — Registrar docente

| Campo | Valor |
|---|---|
| **ID** | `HU-01` |
| **Épica** | `EP-01` |
| **Rol** | Docente |
| **Característica** | Registrar docente |
| **Razón** | Incorporarse al proceso de dirección de trabajos de grado |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 1 |
| **Endpoint** | `POST /api/users/register` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-01.1 (P) | Registro de docente con datos completos | Dado que he abierto el formulario de registro y no tengo cuenta | Cuando registro un docente con nombres, apellidos, correo `@unicauca.edu.co`, programa, contraseña válida y rol `TEACHER` | Entonces el sistema deberá crear la cuenta con la contraseña cifrada y responder `200` | Body `RegisterRequest`; validación `UserValidation`; `UserService.registerUser`; prueba `UserControllerTest.register_validRequest_returnsUserDTO` |
| CA-01.2 (P) | Registro de docente sin teléfono | Dado que he abierto el formulario de registro | Cuando registro un docente sin número de teléfono | Entonces el sistema deberá crear la cuenta porque el teléfono es opcional y responder `200` | `telephone` nullable; `UserValidation.validateTelephone`; prueba `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` |
| CA-01.3 (N) | Registro con contraseña débil | Dado que he abierto el formulario de registro | Cuando registro un docente con la contraseña `123456` | Entonces el sistema deberá responder `400`, no crear el usuario y no persistir ningún registro | Reglas: mínimo 6 caracteres, un dígito, una mayúscula y un carácter especial; prueba `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` |

### HU-02 — Consultar el estado del proyecto

| Campo | Valor |
|---|---|
| **ID** | `HU-02` |
| **Épica** | `EP-01` |
| **Rol** | Estudiante |
| **Característica** | Consultar el estado del proyecto |
| **Razón** | Conocer en qué etapa del proceso se encuentra su trabajo de grado |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 1 |
| **Endpoint** | `GET /api/format-a/user/{userEmail}` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-02.1 (P) | Consulta con Formato A en evaluación | Dado que he iniciado sesión como estudiante y tengo un Formato A enviado | Cuando consulto el estado de mi proyecto | Entonces el sistema deberá mostrar el estado `FORMATO_A_EN_EVALUACION` y el número de intentos | `FormatoAService.getFormatosAByUser`; `EstadoProyecto`; prueba manual (sin prueba dedicada) |
| CA-02.2 (P) | Consulta con anteproyecto registrado | Dado que he iniciado sesión como estudiante y mi Formato A fue aceptado | Cuando consulto el estado de mi proyecto | Entonces el sistema deberá mostrar el estado del Formato A y el estado del anteproyecto asociado | `GET /api/format-a/user/{email}` + `GET /api/anteprojects/student/{email}`; `AnteprojectStatus`; prueba manual |
| CA-02.3 (N) | Consulta sin sesión iniciada | Dado que no he iniciado sesión | Cuando consulto el estado de un proyecto | Entonces el sistema deberá responder `401` y no retornar datos del proyecto | `AuthenticationFilter` del gateway; prueba `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` |

### HU-03 — Registrar estudiante

| Campo | Valor |
|---|---|
| **ID** | `HU-03` |
| **Épica** | `EP-01` |
| **Rol** | Estudiante |
| **Característica** | Registrar estudiante |
| **Razón** | Acceder al sistema para consultar y gestionar su trabajo de grado |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 1 |
| **Endpoint** | `POST /api/users/register` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-03.1 (P) | Registro de estudiante con datos completos | Dado que he abierto el formulario de registro y no tengo cuenta | Cuando registro un estudiante con correo `@unicauca.edu.co`, programa y contraseña válida | Entonces el sistema deberá crear la cuenta con rol `STUDENT` y responder `200` | `UserController.register`; `Role.STUDENT`; prueba `UserControllerTest.register_validRequest_returnsUserDTO` |
| CA-03.2 (P) | Registro con contraseña que cumple la política | Dado que he abierto el formulario de registro | Cuando registro un estudiante con la contraseña `Demo123!` | Entonces el sistema deberá aceptar la contraseña y almacenarla cifrada | `PasswordEncryptor` (BCrypt); prueba `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` |
| CA-03.3 (N) | Registro con correo institucional duplicado | Dado que ya existe una cuenta con el correo indicado | Cuando registro un estudiante con ese mismo correo | Entonces el sistema deberá responder `400` y no crear una segunda cuenta | `UserService.registerUser` valida duplicados; prueba `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` |

### HU-04 — Iniciar sesión

| Campo | Valor |
|---|---|
| **ID** | `HU-04` |
| **Épica** | `EP-01` |
| **Rol** | Usuario registrado |
| **Característica** | Iniciar sesión |
| **Razón** | Acceder a las funciones correspondientes a su rol |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 1 |
| **Endpoint** | `POST /api/auth/login` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-04.1 (P) | Inicio de sesión con credenciales válidas | Dado que tengo una cuenta activa | Cuando inicio sesión con mi correo y contraseña correctos | Entonces el sistema deberá responder `200` con un token JWT y el rol del usuario | `AuthController.login`; `AuthResponse`; prueba `AuthControllerTest.login_validCredentials_returnsAuthResponse` |
| CA-04.2 (P) | Inicio de sesión de docente con Formato A aceptado | Dado que soy docente y mi Formato A ya fue aceptado | Cuando inicio sesión con credenciales válidas | Entonces el sistema deberá permitir el acceso sin exigir un nuevo Formato A | `AuthService.authenticate`; prueba `AuthServiceTest.authenticate_teacherWithAcceptedFormatA_shouldNotRequireFormatA` |
| CA-04.3 (N) | Inicio de sesión con contraseña incorrecta | Dado que tengo una cuenta activa | Cuando inicio sesión con una contraseña incorrecta | Entonces el sistema deberá responder `401` y no emitir token | `AuthService` valida con `UserService.validatePassword`; prueba `UserControllerTest.validateCredentials_validUser_returnsOk` (caso negativo manual) |

---

## EP-02 — Formato A

### HU-05 — Diligenciar Formato A

| Campo | Valor |
|---|---|
| **ID** | `HU-05` |
| **Épica** | `EP-02` |
| **Rol** | Docente |
| **Característica** | Diligenciar Formato A |
| **Razón** | Iniciar formalmente el trabajo de grado del estudiante que dirige |
| **Prioridad** | Alta |
| **Estimación** | 8 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `POST /api/format-a/submit` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-05.1 (P) | Envío de un Formato A válido | Dado que he iniciado sesión como docente y el estudiante no tiene un Formato A activo | Cuando diligencio el Formato A con título, modalidad, director, estudiante, objetivo general y PDF adjunto | Entonces el sistema deberá registrar el Formato A en estado `FORMATO_A_EN_EVALUACION` con `intentos = 1` y responder `200` | `FormatARequest`; `FormatoAService.submitFormatoA`; `EstadoProyecto.FORMATO_A_EN_EVALUACION`; prueba `FormatoAServiceTest.submitFormatoA_WithValidPdf_ShouldSuccess` |
| CA-05.2 (P) | Envío con PDF en el límite permitido | Dado que he iniciado sesión como docente | Cuando adjunto un PDF de exactamente 10 MB | Entonces el sistema deberá aceptar el archivo y registrar el Formato A | Límite `format-a.storage.max-size-bytes = 10485760`; prueba `FormatoAServiceTest.submitFormatoA_WithLargePdf_ShouldSuccess` (PR #55) |
| CA-05.3 (N) | Envío con PDF que supera el límite | Dado que he iniciado sesión como docente | Cuando adjunto un PDF de más de 10 MB | Entonces el sistema deberá responder `400`, no crear el Formato A y no almacenar el archivo | `FormatoAService.validateAndStorePdf`; prueba `FormatoAServiceTest.submitFormatoA_WithTooLargePdf_ThrowsException` (PR #55) |

### HU-06 — Reenviar Formato A rechazado

| Campo | Valor |
|---|---|
| **ID** | `HU-06` |
| **Épica** | `EP-02` |
| **Rol** | Docente |
| **Característica** | Reenviar Formato A rechazado |
| **Razón** | Corregir las observaciones y continuar con el proceso |
| **Prioridad** | Alta |
| **Estimación** | 5 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `PUT /api/format-a/{id}/resubmit` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-06.1 (P) | Reenvío de un Formato A rechazado | Dado que mi Formato A está en estado `FORMATO_A_RECHAZADO` y tiene menos de 3 intentos | Cuando reenvío el Formato A con las correcciones | Entonces el sistema deberá volver al estado `FORMATO_A_EN_EVALUACION` e incrementar el contador de intentos | `RechazadoState.resubmit`; prueba `FormatoAStateTest.testReenviarFormatoRechazado` |
| CA-06.2 (P) | Tercer reenvío permitido | Dado que mi Formato A fue rechazado y registra 2 intentos | Cuando reenvío el Formato A por tercera vez | Entonces el sistema deberá aceptar el reenvío y registrar 3 intentos | `MAX_INTENTOS = 3`; `FormatoAStateTest.testReenviarFormatoRechazado` |
| CA-06.3 (N) | Reenvío después del tercer intento | Dado que mi Formato A acumula 3 intentos | Cuando intento reenviarlo de nuevo | Entonces el sistema deberá responder `400`, mantener el estado `FORMATO_A_RECHAZADO` y no modificar el archivo | `RechazadoState.resubmit`; prueba `FormatoAStateTest.testMaximoIntentosAlcanzado` |

### HU-07 — Aprobar Formato A

| Campo | Valor |
|---|---|
| **ID** | `HU-07` |
| **Épica** | `EP-02` |
| **Rol** | Coordinador/a |
| **Característica** | Aprobar Formato A |
| **Razón** | Habilitar el registro del anteproyecto |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `PUT /api/format-a/{id}/evaluate` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-07.1 (P) | Aprobación de un Formato A en evaluación | Dado que he iniciado sesión como coordinador y el Formato A está en evaluación | Cuando apruebo el Formato A | Entonces el sistema deberá registrar el estado `FORMATO_A_ACEPTADO` y responder `200` | Body `{ "estado": "FORMATO_A_ACEPTADO" }`; `EnEvaluacionState.evaluate`; prueba `FormatoAStateTest.testEvaluarYAprobar` |
| CA-07.2 (P) | Aprobación con observaciones | Dado que he iniciado sesión como coordinador | Cuando apruebo el Formato A con observaciones | Entonces el sistema deberá guardar las observaciones y publicar el evento de evaluación | `FormatoAService.evaluateFormatoA`; `eventPublisher.publishFormatoAEvaluado`; prueba `FormatoAStateTest.testEvaluarYAprobar` |
| CA-07.3 (N) | Aprobación por un rol no autorizado | Dado que he iniciado sesión como estudiante | Cuando intento aprobar un Formato A | Entonces el sistema deberá responder `403` y no modificar el estado del Formato A | Validación de rol en `FormatAController` (PR #55); prueba de autorización de #55; issue #57 |

### HU-08 — Rechazar Formato A

| Campo | Valor |
|---|---|
| **ID** | `HU-08` |
| **Épica** | `EP-02` |
| **Rol** | Coordinador/a |
| **Característica** | Rechazar Formato A |
| **Razón** | Comunicar al docente las correcciones necesarias antes de continuar |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `PUT /api/format-a/{id}/evaluate` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-08.1 (P) | Rechazo de un Formato A en evaluación | Dado que he iniciado sesión como coordinador y el Formato A está en evaluación | Cuando rechazo el Formato A con observaciones | Entonces el sistema deberá registrar el estado `FORMATO_A_RECHAZADO` y responder `200` | Body `{ "estado": "FORMATO_A_RECHAZADO", "observaciones": "..." }`; prueba `FormatoAStateTest.testEvaluarYRechazar` |
| CA-08.2 (P) | Rechazo que habilita el reenvío | Dado que he rechazado un Formato A | Cuando el docente consulta el Formato A | Entonces el sistema deberá mostrar el estado `FORMATO_A_RECHAZADO`, las observaciones y el contador de intentos | `FormatAResponse.observaciones`; `FormatAResponse.intentos`; prueba `FormatoAStateTest.testFlujoCompletoExitoso` |
| CA-08.3 (N) | Rechazo de un Formato A ya aceptado | Dado que el Formato A está en estado `FORMATO_A_ACEPTADO` | Cuando intento rechazarlo | Entonces el sistema deberá responder `400` y mantener el estado `FORMATO_A_ACEPTADO` | `AceptadoState` bloquea transiciones; prueba `FormatoAStateTest.testEstadosFinalesNoPermitenTransiciones` |

### HU-09 — Consultar Formatos A

| Campo | Valor |
|---|---|
| **ID** | `HU-09` |
| **Épica** | `EP-02` |
| **Rol** | Docente |
| **Característica** | Consultar Formatos A |
| **Razón** | Dar seguimiento al estado de los Formatos A que ha enviado |
| **Prioridad** | Media |
| **Estimación** | 2 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `GET /api/format-a/user/{userEmail}` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-09.1 (P) | Consulta del historial de Formatos A | Dado que he iniciado sesión como docente y tengo Formatos A registrados | Cuando consulto mis Formatos A | Entonces el sistema deberá retornar la lista con estado, intentos y observaciones | `FormatoAService.getFormatosAByUser`; prueba manual |
| CA-09.2 (P) | Consulta del detalle de un Formato A | Dado que he iniciado sesión como docente | Cuando consulto un Formato A por su identificador | Entonces el sistema deberá retornar el detalle completo del Formato A | `GET /api/format-a/{id}`; `FormatoAService.getFormatoAById`; prueba manual |
| CA-09.3 (N) | Consulta de un Formato A inexistente | Dado que he iniciado sesión como docente | Cuando consulto un identificador que no existe | Entonces el sistema deberá responder `400` y no retornar datos | `FormatoAService.getFormatoAById` lanza excepción; prueba manual |

---

## EP-03 — Anteproyecto

### HU-10 — Registrar anteproyecto

| Campo | Valor |
|---|---|
| **ID** | `HU-10` |
| **Épica** | `EP-03` |
| **Rol** | Docente |
| **Característica** | Registrar anteproyecto |
| **Razón** | Formalizar la propuesta de investigación del estudiante |
| **Prioridad** | Alta |
| **Estimación** | 5 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `POST /api/anteprojects` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-10.1 (P) | Registro de anteproyecto con Formato A aceptado | Dado que he iniciado sesión como docente y el Formato A del estudiante está aceptado | Cuando registro el anteproyecto con título, descripción y director | Entonces el sistema deberá crear el anteproyecto en estado `DRAFT` y responder `200` | `CreateAnteprojectRequest`; `AnteprojectApplicationService.createAnteproject`; prueba `AnteprojectApplicationServiceTest.createAnteproject_Success` |
| CA-10.2 (P) | Registro de anteproyecto con codirector | Dado que el Formato A está aceptado | Cuando registro el anteproyecto indicando un codirector | Entonces el sistema deberá guardar el codirector y mantener el estado `DRAFT` | `Anteproject.codirectorEmail`; prueba `AnteprojectApplicationServiceTest.createAnteproject_Success` |
| CA-10.3 (N) | Registro por un docente que no es el director | Dado que he iniciado sesión como docente | Cuando registro un anteproyecto indicando como director a otro docente | Entonces el sistema deberá responder `403` y no crear el anteproyecto | `AnteprojectRestController.createAnteproject`; prueba `AnteprojectApplicationServiceTest.createAnteproject_DirectorMismatch_ThrowsException` |

### HU-11 — Enviar documento de anteproyecto

| Campo | Valor |
|---|---|
| **ID** | `HU-11` |
| **Épica** | `EP-03` |
| **Rol** | Estudiante |
| **Característica** | Enviar documento de anteproyecto |
| **Razón** | Poner el anteproyecto a consideración de los evaluadores |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `POST /api/anteprojects/{id}/submit-document` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-11.1 (P) | Envío del documento por el estudiante | Dado que he iniciado sesión como estudiante y el anteproyecto me pertenece | Cuando envío la URL del documento | Entonces el sistema deberá cambiar el estado a `SUBMITTED` y publicar el evento de envío | `submitDocument`; `AnteprojectSubmittedEvent`; prueba `AnteprojectApplicationServiceTest.submitDocument_AsStudent_Success` |
| CA-11.2 (P) | Envío del documento por el director | Dado que he iniciado sesión como docente director del anteproyecto | Cuando envío la URL del documento | Entonces el sistema deberá aceptar el envío y cambiar el estado a `SUBMITTED` | `submitDocument` permite estudiante o director; prueba `AnteprojectApplicationServiceTest.submitDocument_AsStudent_Success` |
| CA-11.3 (N) | Envío por un usuario ajeno al anteproyecto | Dado que he iniciado sesión como estudiante de otro proyecto | Cuando intento enviar el documento de un anteproyecto que no me pertenece | Entonces el sistema deberá responder `400` y no modificar el estado ni el documento | `submitDocument` valida propietario; prueba `AnteprojectApplicationServiceTest.submitDocument_UnauthorizedUser_ThrowsException` |

### HU-12 — Consultar anteproyectos enviados

| Campo | Valor |
|---|---|
| **ID** | `HU-12` |
| **Épica** | `EP-03` |
| **Rol** | Jefe/a de Departamento |
| **Característica** | Consultar anteproyectos enviados |
| **Razón** | Seleccionar los anteproyectos que requieren evaluadores |
| **Prioridad** | Media |
| **Estimación** | 2 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `GET /api/anteprojects/submitted` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-12.1 (P) | Consulta de anteproyectos enviados | Dado que he iniciado sesión como jefe de departamento | Cuando consulto los anteproyectos enviados | Entonces el sistema deberá retornar la lista de anteproyectos en estado `SUBMITTED` | `getSubmittedAnteprojects`; prueba `AnteprojectApplicationServiceTest.getSubmittedAnteprojectsForDepartmentHead_Success` |
| CA-12.2 (P) | Consulta de anteproyectos de un estudiante | Dado que he iniciado sesión | Cuando consulto los anteproyectos de un estudiante | Entonces el sistema deberá retornar solo los anteproyectos de ese estudiante | `GET /api/anteprojects/student/{email}`; prueba manual |
| CA-12.3 (N) | Consulta de un anteproyecto inexistente | Dado que he iniciado sesión | Cuando consulto un anteproyecto que no existe | Entonces el sistema deberá responder `400` y no retornar datos | `getAnteprojectById` lanza excepción; prueba `AnteprojectApplicationServiceTest.getAnteprojectById_NotFound_ThrowsException` |

### HU-13 — Actualizar estado del anteproyecto

| Campo | Valor |
|---|---|
| **ID** | `HU-13` |
| **Épica** | `EP-03` |
| **Rol** | Coordinador/a |
| **Característica** | Actualizar estado del anteproyecto |
| **Razón** | Reflejar el avance del anteproyecto en el proceso de evaluación |
| **Prioridad** | Alta |
| **Estimación** | 3 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `PUT /api/anteprojects/{id}/status` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-13.1 (P) | Actualización a evaluación | Dado que he iniciado sesión como coordinador y el anteproyecto está enviado | Cuando actualizo su estado a `UNDER_EVALUATION` | Entonces el sistema deberá guardar el nuevo estado y responder `200` | Body `{ "status": "UNDER_EVALUATION" }`; prueba `AnteprojectApplicationServiceTest.updateStatus_Success` |
| CA-13.2 (P) | Actualización por el jefe de departamento | Dado que he iniciado sesión como jefe de departamento | Cuando actualizo el estado del anteproyecto a `APPROVED` | Entonces el sistema deberá guardar el nuevo estado y responder `200` | Validación de jefe de departamento (PR #55); prueba `AnteprojectApplicationServiceTest.updateStatus_Success` |
| CA-13.3 (N) | Actualización por un rol no autorizado | Dado que he iniciado sesión como docente sin jefatura | Cuando intento actualizar el estado de un anteproyecto | Entonces el sistema deberá responder `403` y no modificar el estado | Validación de rol en `AnteprojectRestController.updateStatus` (PR #55); issue #57 |

---

## EP-04 — Evaluación

### HU-14 — Asignar evaluadores

| Campo | Valor |
|---|---|
| **ID** | `HU-14` |
| **Épica** | `EP-04` |
| **Rol** | Jefe/a de Departamento |
| **Característica** | Asignar evaluadores |
| **Razón** | Iniciar la evaluación del anteproyecto por dos docentes |
| **Prioridad** | Alta |
| **Estimación** | 5 SP |
| **Iteración** | Corte 2 |
| **Endpoint** | `POST /api/anteprojects/{id}/assign-evaluators` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-14.1 (P) | Asignación de dos evaluadores distintos | Dado que he iniciado sesión como jefe de departamento y el anteproyecto está enviado | Cuando asigno dos docentes evaluadores con correos distintos | Entonces el sistema deberá crear las evaluaciones, cambiar el estado a `UNDER_EVALUATION` y responder `200` | Parámetros `evaluator1Email` y `evaluator2Email`; `assignEvaluators`; prueba `AnteprojectApplicationServiceTest.assignEvaluators_Success` |
| CA-14.2 (P) | Notificación a los evaluadores | Dado que he asignado dos evaluadores | Cuando se completa la asignación | Entonces el sistema deberá publicar el evento `EvaluatorAssignmentEvent` para notificar a los docentes | `EvaluatorAssignmentEvent`; consumidor `anteproject.notification.queue`; prueba manual |
| CA-14.3 (N) | Asignación por un docente sin jefatura | Dado que he iniciado sesión como docente sin jefatura de departamento | Cuando intento asignar evaluadores | Entonces el sistema deberá responder `403` y no crear evaluaciones | Validación `X-User-IsDepartmentHead` en `AnteprojectRestController` (PR #55); issue #57 |

### HU-15 — Consultar evaluaciones asignadas

| Campo | Valor |
|---|---|
| **ID** | `HU-15` |
| **Épica** | `EP-04` |
| **Rol** | Docente evaluador |
| **Característica** | Consultar evaluaciones asignadas |
| **Razón** | Conocer los anteproyectos que debe evaluar |
| **Prioridad** | Media |
| **Estimación** | 2 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | `GET /api/evaluation-assignments/my-assignments` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-15.1 (P) | Consulta de evaluaciones asignadas | Dado que he iniciado sesión como docente evaluador | Cuando consulto mis evaluaciones asignadas | Entonces el sistema deberá retornar las evaluaciones pendientes con el identificador del anteproyecto | `EvaluationAssignmentController.getMyAssignments`; prueba manual |
| CA-15.2 (P) | Consulta sin evaluaciones asignadas | Dado que he iniciado sesión como docente evaluador | Cuando consulto mis evaluaciones y no tengo asignaciones | Entonces el sistema deberá retornar una lista vacía | `getMyAssignments`; prueba manual |
| CA-15.3 (N) | Consulta sin sesión iniciada | Dado que no he iniciado sesión | Cuando consulto las evaluaciones asignadas | Entonces el sistema deberá responder `401` y no retornar evaluaciones | `AuthenticationFilter` con `allowedRoles: TEACHER,COORDINATOR`; prueba `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` |

### HU-16 — Registrar evaluación

| Campo | Valor |
|---|---|
| **ID** | `HU-16` |
| **Épica** | `EP-04` |
| **Rol** | Docente evaluador |
| **Característica** | Registrar evaluación |
| **Razón** | Emitir un concepto sobre el anteproyecto evaluado |
| **Prioridad** | Alta |
| **Estimación** | 5 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | `POST /api/evaluations/{evaluationId}/submit` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-16.1 (P) | Evaluación aprobada | Dado que he iniciado sesión como evaluador y tengo una evaluación pendiente | Cuando registro una calificación mayor o igual a `3.5` con comentarios | Entonces el sistema deberá marcar la evaluación como `APPROVED` | `EvaluationService.submitEvaluation`; umbral `3.5`; prueba `EvaluationServiceTest.submitEvaluation_Success_Approved` |
| CA-16.2 (P) | Evaluación que requiere correcciones | Dado que tengo una evaluación pendiente | Cuando registro una calificación entre `2.5` y `3.49` | Entonces el sistema deberá marcar la evaluación como `NEEDS_CORRECTIONS` | `EvaluationService.submitEvaluation`; prueba `EvaluationServiceTest.submitEvaluation_Success_Rejected` |
| CA-16.3 (N) | Evaluación ya completada | Dado que la evaluación ya fue enviada | Cuando intento modificarla de nuevo | Entonces el sistema deberá responder `400` y no modificar la calificación | `EvaluationService.submitEvaluation` valida estado; prueba `EvaluationServiceTest.createEvaluation_AlreadyExists_ThrowsException` |

---

## EP-05 — Coordinación y jefatura

### HU-17 — Asignar jefe de departamento

| Campo | Valor |
|---|---|
| **ID** | `HU-17` |
| **Épica** | `EP-05` |
| **Rol** | Administrador/a |
| **Característica** | Asignar jefe de departamento |
| **Razón** | Habilitar la asignación de evaluadores por parte del departamento |
| **Prioridad** | Media |
| **Estimación** | 3 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | `POST /api/department-head/assign` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-17.1 (P) | Asignación de un jefe de departamento | Dado que he iniciado sesión como administrador y el docente existe | Cuando asigno al docente con fecha de inicio y fin | Entonces el sistema deberá registrar la asignación y responder `200` | `DepartmentHeadController.assignDepartmentHead`; `DepartmentHeadAssignment`; prueba manual |
| CA-17.2 (P) | Consulta del jefe vigente | Dado que existe una asignación vigente | Cuando consulto el jefe de departamento actual | Entonces el sistema deberá retornar el docente con la asignación activa | `GET /api/department-head/current`; prueba manual |
| CA-17.3 (N) | Asignación a un docente inexistente | Dado que he iniciado sesión como administrador | Cuando asigno un `teacherId` que no existe | Entonces el sistema deberá responder `400` y no crear la asignación | `DepartmentHeadController` valida el docente; prueba manual |

### HU-18 — Consultar Formatos A pendientes

| Campo | Valor |
|---|---|
| **ID** | `HU-18` |
| **Épica** | `EP-05` |
| **Rol** | Coordinador/a |
| **Característica** | Consultar Formatos A pendientes |
| **Razón** | Priorizar la evaluación de los Formatos A en espera |
| **Prioridad** | Media |
| **Estimación** | 2 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | `GET /api/format-a/coordinator/{coordinatorEmail}` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-18.1 (P) | Consulta de Formatos A pendientes | Dado que he iniciado sesión como coordinador y existen Formatos A en evaluación | Cuando consulto mis Formatos A pendientes | Entonces el sistema deberá retornar la lista de Formatos A en estado `FORMATO_A_EN_EVALUACION` | `FormatoAService.getFormatosAByCoordinator`; prueba manual |
| CA-18.2 (P) | Consulta sin Formatos A pendientes | Dado que no existen Formatos A en evaluación | Cuando consulto mis Formatos A pendientes | Entonces el sistema deberá retornar una lista vacía | `getFormatosAByCoordinator`; prueba manual |
| CA-18.3 (N) | Consulta sin sesión iniciada | Dado que no he iniciado sesión | Cuando consulto los Formatos A pendientes | Entonces el sistema deberá responder `401` y no retornar datos | `AuthenticationFilter` del gateway; prueba `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` |

### HU-19 — Consultar docentes de Ingeniería de Sistemas

| Campo | Valor |
|---|---|
| **ID** | `HU-19` |
| **Épica** | `EP-05` |
| **Rol** | Jefe/a de Departamento |
| **Característica** | Consultar docentes de Ingeniería de Sistemas |
| **Razón** | Elegir evaluadores habilitados para el anteproyecto |
| **Prioridad** | Baja |
| **Estimación** | 1 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | `GET /api/users/teachers/systems` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-19.1 (P) | Consulta de docentes del programa | Dado que he iniciado sesión como jefe de departamento | Cuando consulto los docentes de Ingeniería de Sistemas | Entonces el sistema deberá retornar los docentes con ese programa | `UserController.getSystemsTeachers`; prueba manual |
| CA-19.2 (P) | Consulta sin docentes registrados | Dado que no hay docentes de Ingeniería de Sistemas | Cuando consulto los docentes del programa | Entonces el sistema deberá retornar una lista vacía | `getSystemsTeachers`; prueba manual |
| CA-19.3 (N) | Consulta sin sesión iniciada | Dado que no he iniciado sesión | Cuando consulto los docentes del programa | Entonces el sistema deberá responder `401` y no retornar datos | `AuthenticationFilter` del gateway; prueba `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` |

---

## EP-06 — Notificaciones

### HU-20 — Notificar envío de Formato A

| Campo | Valor |
|---|---|
| **ID** | `HU-20` |
| **Épica** | `EP-06` |
| **Rol** | Docente |
| **Característica** | Notificar envío de Formato A |
| **Razón** | Confirmar al director y al estudiante que el Formato A quedó en evaluación |
| **Prioridad** | Media |
| **Estimación** | 2 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | Evento `formato.enviado` → `notifications_queue` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-20.1 (P) | Notificación de Formato A enviado | Dado que un docente envió un Formato A | Cuando `format-a-service` publica el evento `formato.enviado` | Entonces el sistema deberá enviar la notificación al director y al estudiante | `FormatAEventPublisher`; `FormatAEventConsumer`; `RabbitMQConfig`; prueba manual |
| CA-20.2 (P) | Notificación con datos del Formato A | Dado que se publicó el evento de envío | Cuando el consumidor procesa el mensaje | Entonces la notificación deberá incluir el título del Formato A y el correo del estudiante | `FormatAEventConsumer`; plantilla de notificación; prueba manual |
| CA-20.3 (N) | Evento sin destinatario | Dado que el evento no incluye un correo de destinatario | Cuando el consumidor procesa el mensaje | Entonces el sistema deberá registrar el error y no enviar una notificación vacía | `FormatAEventConsumer` valida destinatario; prueba manual |

### HU-21 — Notificar resultado de evaluación

| Campo | Valor |
|---|---|
| **ID** | `HU-21` |
| **Épica** | `EP-06` |
| **Rol** | Estudiante |
| **Característica** | Notificar resultado de evaluación |
| **Razón** | Conocer de inmediato si el Formato A fue aprobado o rechazado |
| **Prioridad** | Media |
| **Estimación** | 2 SP |
| **Iteración** | Corte 3 |
| **Endpoint** | Evento `formato.evaluado` → `notifications_queue` |

| Código | Escenario | Dado que | Cuando | Entonces | Lineamientos técnicos |
|---|---|---|---|---|---|
| CA-21.1 (P) | Notificación de aprobación | Dado que un coordinador aprobó un Formato A | Cuando `format-a-service` publica el evento `formato.evaluado` con estado aceptado | Entonces el sistema deberá notificar al estudiante y al director la aprobación | `FormatAEventConsumer`; evento `formato.evaluado`; prueba manual |
| CA-21.2 (P) | Notificación de rechazo con observaciones | Dado que un coordinador rechazó un Formato A | Cuando se publica el evento con estado rechazado y observaciones | Entonces la notificación deberá incluir las observaciones para orientar la corrección | `FormatAEventConsumer`; `FormatAResponse.observaciones`; prueba manual |
| CA-21.3 (N) | Evento con destinatario inexistente | Dado que el correo del estudiante no existe en `user-service` | Cuando el consumidor intenta resolver el destinatario | Entonces el sistema deberá registrar el error y no interrumpir el procesamiento de otros eventos | `UserServiceClient`; `FormatAEventConsumer`; prueba manual |

---

## Correspondencia con el backlog anterior

El archivo `docs/proceso/fuentes/INGENIERÍA DE SOFTWARE HU - Vrs 0.xlsx`
documentaba solo el primer sprint con identificadores discontinuos. La
correspondencia es:

| Backlog anterior | Historia actual | Cambio |
|---|---|---|
| H1 — Registro de docente | `HU-01` | Se conserva el alcance y se normaliza el formato. |
| H2 — Estudiante consulta el estado | `HU-02` | Se explicita el endpoint y los estados reales. |
| H12 — Inicio de sesión | `HU-04` | Se mueve a la épica de autenticación. |
| H14 — Registro de estudiante | `HU-03` | Se conserva el alcance y se normaliza el formato. |
| H3 – H11, H13 | — | No existían en la hoja `HUs`; el resto del backlog se reconstruyó a partir del código y de los casos de uso. |

La numeración nueva es continua (`HU-01` a `HU-21`), cada épica suma los
puntos de sus historias y cada HU tiene exactamente tres criterios.

## Verificación del backlog

- [x] 21 HU con un único verbo rector en infinitivo.
- [x] Roles formales del dominio en todas las HU.
- [x] Beneficio verificable en el campo `Razón`.
- [x] 63 CA: 2 positivos y 1 negativo por HU.
- [x] Fórmulas BDD (`Dado que he`, `Cuando`, `Entonces el sistema deberá`).
- [x] Endpoint identificado en cada HU.
- [x] Estimación Fibonacci y suma por épica (68 SP).
- [x] Correspondencia con el backlog anterior documentada.

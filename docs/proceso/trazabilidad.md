# Matriz de trazabilidad

Conexión entre cada **historia de usuario**, sus **criterios de aceptación**, el
**endpoint** que los implementa y la **prueba** que los verifica.

- Fuente funcional: [`docs/user-stories.md`](../user-stories.md).
- Fuente técnica: controladores y servicios de
  `ProccesManagementMicroservices/`.
- Estados: **Cubierto** (prueba automatizada directa), **Parcial** (la prueba
  cubre lógica relacionada pero no el criterio completo), **Manual** (verificado
  a mano, sin prueba automatizada) y **#55** (se cumple al integrar el PR #55).

> El backlog está escrito sobre `main`. Los criterios de autorización marcados
> **#55** dependen del PR que corrige el rol de aprobación del Formato A y la
> asignación de evaluadores (issues #57 y #37).

## Resumen de cobertura

| Estado | Criterios | Porcentaje |
|---|---:|---:|
| Cubierto | 24 | 38 % |
| Parcial | 11 | 17 % |
| Manual | 23 | 37 % |
| `#55` | 5 | 8 % |
| **Total** | **63** | **100 %** |

La cobertura automatizada se concentra en las reglas de negocio críticas
(estados del Formato A, evaluación, anteproyecto y autorización del gateway).
Los cinco criterios marcados `#55` ya tienen prueba, pero dependen de que se
integre ese PR. Las brechas restantes están inventariadas al final y se atienden
en #39, #40, #41 y #56.

## Matriz

| HU | CA | Endpoint / evento | Implementación | Prueba | Estado |
|---|---|---|---|---|---|
| HU-01 | CA-01.1 | `POST /api/users/register` | `UserController.register`, `UserService.registerUser` | `UserControllerTest.register_validRequest_returnsUserDTO` | Cubierto |
| HU-01 | CA-01.2 | `POST /api/users/register` | `UserValidation.validateTelephone` | `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` | Parcial |
| HU-01 | CA-01.3 | `POST /api/users/register` | `UserValidation.validatePassword` | `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` | Parcial |
| HU-02 | CA-02.1 | `GET /api/format-a/user/{email}` | `FormatoAService.getFormatosAByUser` | — | Manual |
| HU-02 | CA-02.2 | `GET /api/format-a/user/{email}` + `GET /api/anteprojects/student/{email}` | `FormatoAService`, `AnteprojectApplicationService` | — | Manual |
| HU-02 | CA-02.3 | `GET /api/format-a/**` | `AuthenticationFilter` | `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` | Cubierto |
| HU-03 | CA-03.1 | `POST /api/users/register` | `UserController.register` | `UserControllerTest.register_validRequest_returnsUserDTO` | Cubierto |
| HU-03 | CA-03.2 | `POST /api/users/register` | `PasswordEncryptor` | `UserServiceTest.registerUser_validUser_encryptsPasswordAndSaves` | Cubierto |
| HU-03 | CA-03.3 | `POST /api/users/register` | `UserService.registerUser` (duplicados) | — | Manual |
| HU-04 | CA-04.1 | `POST /api/auth/login` | `AuthController.login`, `AuthService.authenticate` | `AuthControllerTest.login_validCredentials_returnsAuthResponse` | Cubierto |
| HU-04 | CA-04.2 | `POST /api/auth/login` | `AuthService.authenticate` | `AuthServiceTest.authenticate_teacherWithAcceptedFormatA_shouldNotRequireFormatA` | Cubierto |
| HU-04 | CA-04.3 | `POST /api/auth/login` | `AuthService.authenticate` | `UserControllerTest.validateCredentials_validUser_returnsOk` | Parcial |
| HU-05 | CA-05.1 | `POST /api/format-a/submit` | `FormatoAService.submitFormatoA` | `FormatoAServiceTest.submitFormatoA_WithValidPdf_ShouldSuccess` | Cubierto |
| HU-05 | CA-05.2 | `POST /api/format-a/submit` | `FormatoAService.validateAndStorePdf` | `FormatoAServiceTest.submitFormatoA_WithLargePdf_ShouldSuccess` | #55 |
| HU-05 | CA-05.3 | `POST /api/format-a/submit` | `FormatoAService.validateAndStorePdf` | `FormatoAServiceTest.submitFormatoA_WithTooLargePdf_ThrowsException` | #55 |
| HU-06 | CA-06.1 | `PUT /api/format-a/{id}/resubmit` | `RechazadoState.resubmit` | `FormatoAStateTest.testReenviarFormatoRechazado` | Cubierto |
| HU-06 | CA-06.2 | `PUT /api/format-a/{id}/resubmit` | `RechazadoState.resubmit` | `FormatoAStateTest.testReenviarFormatoRechazado` | Parcial |
| HU-06 | CA-06.3 | `PUT /api/format-a/{id}/resubmit` | `RechazadoState.resubmit` | `FormatoAStateTest.testMaximoIntentosAlcanzado` | Cubierto |
| HU-07 | CA-07.1 | `PUT /api/format-a/{id}/evaluate` | `EnEvaluacionState.evaluate` | `FormatoAStateTest.testEvaluarYAprobar` | Cubierto |
| HU-07 | CA-07.2 | `PUT /api/format-a/{id}/evaluate` | `FormatoAService.evaluateFormatoA` | `FormatoAStateTest.testEvaluarYAprobar` | Parcial |
| HU-07 | CA-07.3 | `PUT /api/format-a/{id}/evaluate` | `FormatAController` (rol) | Prueba de autorización de #55 | #55 |
| HU-08 | CA-08.1 | `PUT /api/format-a/{id}/evaluate` | `EnEvaluacionState.evaluate` | `FormatoAStateTest.testEvaluarYRechazar` | Cubierto |
| HU-08 | CA-08.2 | `GET /api/format-a/{id}` | `FormatoAService.getFormatoAById` | `FormatoAStateTest.testFlujoCompletoExitoso` | Parcial |
| HU-08 | CA-08.3 | `PUT /api/format-a/{id}/evaluate` | `AceptadoState` | `FormatoAStateTest.testEstadosFinalesNoPermitenTransiciones` | Cubierto |
| HU-09 | CA-09.1 | `GET /api/format-a/user/{email}` | `FormatoAService.getFormatosAByUser` | — | Manual |
| HU-09 | CA-09.2 | `GET /api/format-a/{id}` | `FormatoAService.getFormatoAById` | — | Manual |
| HU-09 | CA-09.3 | `GET /api/format-a/{id}` | `FormatoAService.getFormatoAById` | — | Manual |
| HU-10 | CA-10.1 | `POST /api/anteprojects` | `AnteprojectApplicationService.createAnteproject` | `AnteprojectApplicationServiceTest.createAnteproject_Success` | Cubierto |
| HU-10 | CA-10.2 | `POST /api/anteprojects` | `AnteprojectApplicationService.createAnteproject` | `AnteprojectApplicationServiceTest.createAnteproject_Success` | Parcial |
| HU-10 | CA-10.3 | `POST /api/anteprojects` | `AnteprojectRestController.createAnteproject` | `AnteprojectApplicationServiceTest.createAnteproject_DirectorMismatch_ThrowsException` | Cubierto |
| HU-11 | CA-11.1 | `POST /api/anteprojects/{id}/submit-document` | `AnteprojectApplicationService.submitDocument` | `AnteprojectApplicationServiceTest.submitDocument_AsStudent_Success` | Cubierto |
| HU-11 | CA-11.2 | `POST /api/anteprojects/{id}/submit-document` | `AnteprojectApplicationService.submitDocument` | `AnteprojectApplicationServiceTest.submitDocument_AsStudent_Success` | Parcial |
| HU-11 | CA-11.3 | `POST /api/anteprojects/{id}/submit-document` | `AnteprojectApplicationService.submitDocument` | `AnteprojectApplicationServiceTest.submitDocument_UnauthorizedUser_ThrowsException` | Cubierto |
| HU-12 | CA-12.1 | `GET /api/anteprojects/submitted` | `AnteprojectApplicationService.getSubmittedAnteprojects` | `AnteprojectApplicationServiceTest.getSubmittedAnteprojectsForDepartmentHead_Success` | Cubierto |
| HU-12 | CA-12.2 | `GET /api/anteprojects/student/{email}` | `AnteprojectApplicationService.getAnteprojectsByStudent` | — | Manual |
| HU-12 | CA-12.3 | `GET /api/anteprojects/{id}` | `AnteprojectApplicationService.getAnteprojectById` | `AnteprojectApplicationServiceTest.getAnteprojectById_NotFound_ThrowsException` | Cubierto |
| HU-13 | CA-13.1 | `PUT /api/anteprojects/{id}/status` | `AnteprojectApplicationService.updateStatus` | `AnteprojectApplicationServiceTest.updateStatus_Success` | Cubierto |
| HU-13 | CA-13.2 | `PUT /api/anteprojects/{id}/status` | `AnteprojectApplicationService.updateStatus` | `AnteprojectApplicationServiceTest.updateStatus_Success` | Parcial |
| HU-13 | CA-13.3 | `PUT /api/anteprojects/{id}/status` | `AnteprojectRestController.updateStatus` | Prueba de autorización de #55 | #55 |
| HU-14 | CA-14.1 | `POST /api/anteprojects/{id}/assign-evaluators` | `AnteprojectApplicationService.assignEvaluators` | `AnteprojectApplicationServiceTest.assignEvaluators_Success` | Cubierto |
| HU-14 | CA-14.2 | Evento `EvaluatorAssignmentEvent` | `EvaluatorAssignmentEvent`, `AnteprojectEventConsumer` | — | Manual |
| HU-14 | CA-14.3 | `POST /api/anteprojects/{id}/assign-evaluators` | `AnteprojectRestController.assignEvaluators` | Prueba de autorización de #55 | #55 |
| HU-15 | CA-15.1 | `GET /api/evaluation-assignments/my-assignments` | `EvaluationAssignmentController.getMyAssignments` | — | Manual |
| HU-15 | CA-15.2 | `GET /api/evaluation-assignments/my-assignments` | `EvaluationAssignmentController.getMyAssignments` | — | Manual |
| HU-15 | CA-15.3 | `GET /api/evaluation-assignments/**` | `AuthenticationFilter` | `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` | Cubierto |
| HU-16 | CA-16.1 | `POST /api/evaluations/{id}/submit` | `EvaluationService.submitEvaluation` | `EvaluationServiceTest.submitEvaluation_Success_Approved` | Cubierto |
| HU-16 | CA-16.2 | `POST /api/evaluations/{id}/submit` | `EvaluationService.submitEvaluation` | `EvaluationServiceTest.submitEvaluation_Success_Rejected` | Parcial |
| HU-16 | CA-16.3 | `POST /api/evaluations/{id}/submit` | `EvaluationService.submitEvaluation` | `EvaluationServiceTest.createEvaluation_AlreadyExists_ThrowsException` | Parcial |
| HU-17 | CA-17.1 | `POST /api/department-head/assign` | `DepartmentHeadController.assignDepartmentHead` | — | Manual |
| HU-17 | CA-17.2 | `GET /api/department-head/current` | `DepartmentHeadController.getCurrentDepartmentHead` | — | Manual |
| HU-17 | CA-17.3 | `POST /api/department-head/assign` | `DepartmentHeadController.assignDepartmentHead` | — | Manual |
| HU-18 | CA-18.1 | `GET /api/format-a/coordinator/{email}` | `FormatoAService.getFormatosAByCoordinator` | — | Manual |
| HU-18 | CA-18.2 | `GET /api/format-a/coordinator/{email}` | `FormatoAService.getFormatosAByCoordinator` | — | Manual |
| HU-18 | CA-18.3 | `GET /api/format-a/**` | `AuthenticationFilter` | `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` | Cubierto |
| HU-19 | CA-19.1 | `GET /api/users/teachers/systems` | `UserController.getSystemsTeachers` | — | Manual |
| HU-19 | CA-19.2 | `GET /api/users/teachers/systems` | `UserController.getSystemsTeachers` | — | Manual |
| HU-19 | CA-19.3 | `GET /api/users/**` | `AuthenticationFilter` | `AuthenticationFilterTest.protectedEndpoint_withoutAuthorization_returnsUnauthorized` | Cubierto |
| HU-20 | CA-20.1 | Evento `formato.enviado` | `FormatAEventPublisher`, `FormatAEventConsumer` | — | Manual |
| HU-20 | CA-20.2 | Evento `formato.enviado` | `FormatAEventConsumer` | — | Manual |
| HU-20 | CA-20.3 | Evento `formato.enviado` | `FormatAEventConsumer` | — | Manual |
| HU-21 | CA-21.1 | Evento `formato.evaluado` | `FormatAEventConsumer` | — | Manual |
| HU-21 | CA-21.2 | Evento `formato.evaluado` | `FormatAEventConsumer` | — | Manual |
| HU-21 | CA-21.3 | Evento `formato.evaluado` | `UserServiceClient`, `FormatAEventConsumer` | — | Manual |

## Brechas de cobertura

| Brecha | Criterios afectados | Issue |
|---|---|---|
| Pruebas de autorización de extremo a extremo en los servicios | CA-07.3, CA-13.3, CA-14.3 | #41, #57 (PR #55) |
| Pruebas de integración con PostgreSQL y RabbitMQ | CA-11.1, CA-14.2, CA-20.1 – CA-21.3 | #41 |
| Pruebas de validación de registro (contraseña débil, duplicados) | CA-01.2, CA-01.3, CA-03.3 | #40 |
| Pruebas de consultas y listados | CA-09.1 – CA-09.3, CA-12.2, CA-15.1, CA-15.2, CA-18.1, CA-18.2, CA-19.1, CA-19.2 | #40 |
| Pruebas de la asignación de jefe de departamento | CA-17.1 – CA-17.3 | #40 |
| Resiliencia de notificaciones y llamadas entre servicios | CA-20.3, CA-21.3 | #56 |

Las brechas no se ocultan: cada criterio sin prueba automatizada se marca como
**Manual** y se verificó con la aplicación en ejecución durante el corte
correspondiente. La meta es reducir la columna **Manual** con las pruebas de
integración de #41 y las pruebas unitarias de #40.

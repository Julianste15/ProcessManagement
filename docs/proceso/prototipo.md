# Prototipo de interfaz

Prototipo navegable de la interfaz del sistema, elaborado en Figma para validar
la organización de pantallas y la navegación por rol antes de implementarlas.

- **Enlace:** <https://www.figma.com/proto/ngHuyYaQOrb0XGACR7WWGz/Poyecto-trabajo-de-grado?node-id=1-4&t=pQLvyfR1kcU8lvOV-1>
- **Herramienta:** Figma
- **Fuente original del enlace:** [`docs/proceso/fuentes/llink prototipo.txt`](fuentes/llink%20prototipo.txt)

## Qué representa

- Pantalla de inicio de sesión y registro.
- Paneles diferenciados por rol: estudiante, docente, coordinador y
  administrador.
- Consulta del estado del proyecto.
- Flujo de Formato A y anteproyecto.
- Navegación lateral y jerarquía de acciones.

## Relación con la implementación

| Prototipo | Implementación actual |
|---|---|
| Pantallas de autenticación | `LoginController`, `RegisterController` (JavaFX) |
| Panel de estudiante | `StudentDashboardController` |
| Panel de docente | `TeacherFormatsController`, `FormatAController` |
| Panel de coordinador | `CoordinatorDashboardController` |
| Panel de jefe de departamento | `DepartmentHeadDashboardController` |
| Flujo de anteproyecto | `AnteprojectFormController` |

El prototipo es una **referencia de diseño**. La implementación puede diferir en
detalles visuales, pero conserva la estructura de navegación y la separación por
rol.

## Cómo usarlo

1. Abrir el enlace en el navegador.
2. Usar el modo **Present** de Figma para navegar entre pantallas.
3. Contrastar cada pantalla con el flujo descrito en
   [`docs/user-stories.md`](../user-stories.md) y con las capturas del
   [`README.md`](../../README.md).

> El prototipo no es una aplicación funcional: no valida datos ni persiste
> información.

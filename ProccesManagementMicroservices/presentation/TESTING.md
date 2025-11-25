# Pruebas Unitarias - Capa de Presentación

Este proyecto incluye pruebas unitarias para la capa de presentación utilizando JUnit 5.

## Requisitos
- Java 17 o superior
- Maven 3.8 o superior

## Ejecución de las Pruebas

Para ejecutar las pruebas unitarias, navegue al directorio `presentation` y ejecute el siguiente comando:

```bash
mvn test
```

## Descripción de las Pruebas

Las pruebas cubren los siguientes componentes:
- **FormatAService**: Verifica la lógica de negocio para el manejo del Formato A.
- **SessionService**: Verifica la gestión de la sesión del usuario.
- **MicroserviceClient**: Verifica la comunicación con los microservicios backend.
- **JavaFXApplication**: Pruebas básicas de la aplicación JavaFX.

## Configuración
La configuración de las pruebas se encuentra en el archivo `pom.xml`, utilizando `maven-surefire-plugin` y las dependencias de `junit-jupiter`.

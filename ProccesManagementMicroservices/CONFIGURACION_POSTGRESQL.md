# Configuración de PostgreSQL

Este documento describe cómo configurar PostgreSQL para los microservicios del proyecto.

## Requisitos Previos

1. **PostgreSQL instalado** (versión 12 o superior)
2. **Crear las bases de datos** necesarias para cada servicio

## Bases de Datos Requeridas

Cada microservicio requiere su propia base de datos:

- `anteprojectdb` - Para anteproject-service (puerto 8086)
- `userdb` - Para user-service (puerto 8082)
- `evaluationdb` - Para evaluation-service (puerto 8085)
- `formatoadb` - Para format-a-service (puerto 8083)
- `coordinationdb` - Para coordination-service (puerto 8087)
- `notificationdb` - Para notification-service (puerto 8084)

## Pasos de Configuración

### 1. Instalar PostgreSQL

Si no tienes PostgreSQL instalado, descárgalo desde: https://www.postgresql.org/download/

### 2. Crear las Bases de Datos

Ejecuta los siguientes comandos en PostgreSQL (puedes usar `psql` o pgAdmin):

```sql
-- Conectarse a PostgreSQL como superusuario
psql -U postgres

-- Crear las bases de datos
CREATE DATABASE anteprojectdb;
CREATE DATABASE userdb;
CREATE DATABASE evaluationdb;
CREATE DATABASE formatoadb;
CREATE DATABASE coordinationdb;
CREATE DATABASE notificationdb;

-- Opcional: Crear un usuario específico para la aplicación
CREATE USER app_user WITH PASSWORD 'tu_password_seguro';
GRANT ALL PRIVILEGES ON DATABASE anteprojectdb TO app_user;
GRANT ALL PRIVILEGES ON DATABASE userdb TO app_user;
GRANT ALL PRIVILEGES ON DATABASE evaluationdb TO app_user;
GRANT ALL PRIVILEGES ON DATABASE formatoadb TO app_user;
GRANT ALL PRIVILEGES ON DATABASE coordinationdb TO app_user;
GRANT ALL PRIVILEGES ON DATABASE notificationdb TO app_user;
```

### 3. Configurar Variables de Entorno (Opcional)

Puedes configurar las credenciales de PostgreSQL usando variables de entorno:

**Windows (PowerShell):**
```powershell
$env:DB_USERNAME="postgres"
$env:DB_PASSWORD="tu_password"
```

**Windows (CMD):**
```cmd
set DB_USERNAME=postgres
set DB_PASSWORD=tu_password
```

**Linux/Mac:**
```bash
export DB_USERNAME=postgres
export DB_PASSWORD=tu_password
```

Si no configuras estas variables, los servicios usarán los valores por defecto:
- Usuario: `postgres`
- Contraseña: `postgres`

### 4. Actualizar Configuración Manualmente (Opcional)

Si prefieres no usar variables de entorno, puedes editar directamente los archivos de configuración:

**Para archivos `application.yml`:**
```yaml
spring:
  datasource:
    username: tu_usuario
    password: tu_password
```

**Para archivos `application.properties`:**
```properties
spring.datasource.username=tu_usuario
spring.datasource.password=tu_password
```

## Cambios Realizados

### Dependencias Agregadas

Se agregó la dependencia de PostgreSQL en todos los servicios que usan base de datos:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Configuración Actualizada

- **URL de conexión**: Cambiada de H2 a PostgreSQL (`jdbc:postgresql://localhost:5432/nombre_db`)
- **Driver**: Cambiado a `org.postgresql.Driver`
- **Dialecto JPA**: Cambiado a `org.hibernate.dialect.PostgreSQLDialect`
- **DDL Auto**: Cambiado de `create-drop` a `update` (para producción, considera usar `validate` o `none`)

### Servicios Actualizados

✅ anteproject-service
✅ user-service
✅ evaluation-service
✅ format-a-service
✅ coordination-service
✅ notification-service

## Notas Importantes

1. **H2 aún disponible**: La dependencia de H2 se mantiene en los `pom.xml` por si necesitas volver a usarla para desarrollo. Solo necesitas comentar/descomentar la configuración correspondiente.

2. **DDL Auto**: El modo `update` crea/actualiza las tablas automáticamente. Para producción, considera:
   - `validate`: Solo valida el esquema sin hacer cambios
   - `none`: No hace nada (requiere migraciones manuales o con Flyway/Liquibase)

3. **Puerto de PostgreSQL**: Por defecto es `5432`. Si tu instalación usa otro puerto, actualiza la URL en los archivos de configuración.

4. **Seguridad**: Asegúrate de cambiar las contraseñas por defecto en producción.

## Verificación

Para verificar que la configuración funciona:

1. Asegúrate de que PostgreSQL esté ejecutándose
2. Verifica que las bases de datos existan
3. Inicia los microservicios
4. Revisa los logs para confirmar que la conexión fue exitosa

Si hay errores de conexión, verifica:
- Que PostgreSQL esté ejecutándose
- Que las credenciales sean correctas
- Que las bases de datos existan
- Que el puerto 5432 esté disponible (o actualiza la configuración si usas otro puerto)


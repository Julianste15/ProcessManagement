# 🎯 Guía de Presentación - Dockerización con Persistencia

## 📌 Objetivo
Demostrar la dockerización del microservicio **user-service** con persistencia de datos usando PostgreSQL y Docker Compose.

---

## 🔧 Preparación Previa (Antes de la presentación)

### 1. Verificar que Docker Desktop está instalado y corriendo
- Abrir Docker Desktop
- Esperar a que el ícono de la ballena esté verde (Engine running)

### 2. Ubicarse en la carpeta correcta
```powershell
cd "d:\universidad\Software II\ProccesManagement-microservice\ProcessManagement\ProccesManagementMicroservices"
```

### 3. Asegurarse de que no hay servicios locales corriendo en el puerto 8081
```powershell
netstat -ano | findstr :8081
```
Si hay algo, detenerlo con:
```powershell
taskkill /PID <numero_pid> /F
```

---

## 🎬 DEMOSTRACIÓN EN VIVO

### PARTE 1: Mostrar la Arquitectura

#### Paso 1.1: Mostrar el Dockerfile
```powershell
Get-Content ".\ProccesManagementMicroservices\user-service\Dockerfile"
```

**Explicar:**
- Usa imagen base `eclipse-temurin:17-jdk-alpine` (Java 17)
- Copia el JAR compilado
- Expone el servicio

#### Paso 1.2: Mostrar el docker-compose.yml
```powershell
Get-Content "..\docker-compose.yml"
```

**Explicar:**
- **Servicio postgres-db**: Base de datos PostgreSQL 15
- **Volumen postgres-data**: Persistencia de datos
- **Servicio user-service**: Microservicio Java
- **Network**: Comunicación entre contenedores
- **Variables de entorno**: Configuración dinámica

---

### PARTE 2: Levantar los Servicios

#### Paso 2.1: Levantar los contenedores
```powershell
docker-compose up -d
```

**Explicar:**
- `-d` ejecuta en modo detached (segundo plano)
- Docker descarga imágenes si no existen
- Crea la red y el volumen automáticamente

#### Paso 2.2: Verificar que están corriendo
```powershell
docker ps
```

**Mostrar:**
- `postgres-db` en puerto 5432
- `user-service` en puerto 8081

#### Paso 2.3: Ver los logs del servicio
```powershell
docker logs user-service --tail 20
```

**Buscar la línea:**
```
Started UserApplication in X.XXX seconds
```

---

### PARTE 3: Probar la Funcionalidad

#### Paso 3.1: Crear un usuario
```powershell
# Crear archivo JSON con los datos
@"
{
  "names": "María",
  "surnames": "González",
  "email": "maria.gonzalez@unicauca.edu.co",
  "password": "Secure123!",
  "telephone": "3001234567",
  "career": "Ingeniería de Sistemas",
  "role": "STUDENT"
}
"@ | Out-File -FilePath "$env:TEMP\user.json" -Encoding UTF8

# Enviar petición
Invoke-WebRequest -Uri "http://localhost:8081/api/users/register" -Method POST -ContentType "application/json" -InFile "$env:TEMP\user.json"
```

**Explicar:**
- Endpoint REST `/api/users/register`
- Retorna código 200 con el usuario creado
- Notar el `id: 1` asignado por la base de datos

#### Paso 3.2: Consultar el usuario creado
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/users/maria.gonzalez@unicauca.edu.co" -Method GET
```

**Mostrar:**
- Los datos del usuario se recuperan correctamente
- La contraseña NO se expone (seguridad)

---

### PARTE 4: Demostrar la PERSISTENCIA (⭐ Lo más importante)

#### Paso 4.1: Detener los contenedores
```powershell
docker-compose down
```

**Explicar:**
- Los contenedores se eliminan
- **PERO** el volumen `postgres-data` permanece
- Los datos están seguros en el volumen

#### Paso 4.2: Verificar que los contenedores no existen
```powershell
docker ps -a
```

**Mostrar:**
- No hay contenedores de `user-service` ni `postgres-db`

#### Paso 4.3: Verificar que el volumen SÍ existe
```powershell
docker volume ls
```

**Mostrar:**
- El volumen `processmanagement_postgres-data` está presente

#### Paso 4.4: Volver a levantar los servicios
```powershell
docker-compose up -d
```

#### Paso 4.5: Esperar que arranque
```powershell
Start-Sleep -Seconds 20
```

#### Paso 4.6: Consultar el usuario NUEVAMENTE
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/users/maria.gonzalez@unicauca.edu.co" -Method GET
```

**✅ RESULTADO ESPERADO:**
- Los datos de María González siguen ahí
- El `id: 1` se mantiene
- **CONCLUSIÓN: La persistencia funciona correctamente**

---

### PARTE 5: Inspeccionar el Volumen

#### Paso 5.1: Ver detalles del volumen
```powershell
docker volume inspect processmanagement_postgres-data
```

**Explicar:**
- Ubicación física en el sistema de archivos
- Tipo de driver (local)
- Punto de montaje

#### Paso 5.2: Ver logs de PostgreSQL
```powershell
docker logs postgres-db --tail 20
```

**Mostrar:**
- Conexiones del user-service
- Queries ejecutados (si `show-sql=true`)

---

## 🎓 PUNTOS CLAVE PARA EXPLICAR

### 1. ¿Por qué Dockerizar?
- ✅ **Portabilidad**: Funciona igual en cualquier máquina
- ✅ **Aislamiento**: No interfiere con otros servicios
- ✅ **Reproducibilidad**: Mismo entorno en desarrollo y producción
- ✅ **Escalabilidad**: Fácil de replicar

### 2. ¿Por qué Docker Compose?
- ✅ **Orquestación**: Maneja múltiples contenedores
- ✅ **Configuración declarativa**: Todo en un archivo YAML
- ✅ **Gestión de dependencias**: user-service espera a postgres-db
- ✅ **Redes automáticas**: Comunicación entre contenedores

### 3. ¿Por qué Volúmenes?
- ✅ **Persistencia**: Datos sobreviven a reinicios
- ✅ **Separación**: Datos independientes de contenedores
- ✅ **Backups**: Fácil de respaldar
- ✅ **Performance**: Mejor que bind mounts

### 4. Arquitectura Implementada
```
┌─────────────────────────────────────────┐
│         Docker Compose                  │
│  ┌───────────────────────────────────┐  │
│  │  user-service (Puerto 8081)       │  │
│  │  - Spring Boot                    │  │
│  │  - REST API                       │  │
│  └───────────┬───────────────────────┘  │
│              │                           │
│              ▼                           │
│  ┌───────────────────────────────────┐  │
│  │  postgres-db (Puerto 5432)        │  │
│  │  - PostgreSQL 15                  │  │
│  │  - Base de datos: userdb          │  │
│  └───────────┬───────────────────────┘  │
│              │                           │
│              ▼                           │
│  ┌───────────────────────────────────┐  │
│  │  Volumen: postgres-data           │  │
│  │  - Persistencia de datos          │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

---

## 🛠️ Comandos Útiles de Respaldo

### Ver todos los contenedores (incluso detenidos)
```powershell
docker ps -a
```

### Ver logs en tiempo real
```powershell
docker logs user-service -f
```

### Entrar al contenedor de PostgreSQL
```powershell
docker exec -it postgres-db psql -U postgres -d userdb
```

Dentro de PostgreSQL:
```sql
\dt                          -- Listar tablas
SELECT * FROM users;         -- Ver usuarios
\q                           -- Salir
```

### Limpiar todo (si algo sale mal)
```powershell
docker-compose down -v       # Elimina contenedores Y volúmenes
docker system prune -a       # Limpia imágenes no usadas
```

---

## 📊 Datos de Prueba Adicionales

Si necesitas crear más usuarios:

```powershell
# Usuario 2: Profesor
@"
{
  "names": "Juan",
  "surnames": "Pérez",
  "email": "juan.perez@unicauca.edu.co",
  "password": "Teacher123!",
  "telephone": "3009876543",
  "career": "Ingeniería de Sistemas",
  "role": "TEACHER"
}
"@ | Out-File -FilePath "$env:TEMP\user2.json" -Encoding UTF8

Invoke-WebRequest -Uri "http://localhost:8081/api/users/register" -Method POST -ContentType "application/json" -InFile "$env:TEMP\user2.json"
```

```powershell
# Usuario 3: Coordinador
@"
{
  "names": "Ana",
  "surnames": "Martínez",
  "email": "ana.martinez@unicauca.edu.co",
  "password": "Coord123!",
  "telephone": "3005555555",
  "career": "Ingeniería de Sistemas",
  "role": "COORDINATOR"
}
"@ | Out-File -FilePath "$env:TEMP\user3.json" -Encoding UTF8

Invoke-WebRequest -Uri "http://localhost:8081/api/users/register" -Method POST -ContentType "application/json" -InFile "$env:TEMP\user3.json"
```

---

## ⚠️ Solución de Problemas

### Problema: Puerto 8081 ocupado
```powershell
netstat -ano | findstr :8081
taskkill /PID <numero> /F
```

### Problema: Docker no responde
- Reiniciar Docker Desktop
- Esperar a que el ícono esté verde

### Problema: Contenedor no arranca
```powershell
docker logs user-service
docker logs postgres-db
```

### Problema: No se conecta a la base de datos
```powershell
# Verificar que postgres-db está corriendo
docker ps

# Ver logs de PostgreSQL
docker logs postgres-db

# Reiniciar servicios
docker-compose restart
```

---

## ✅ CHECKLIST FINAL

Antes de la presentación, verificar:

- [ ] Docker Desktop está corriendo
- [ ] No hay servicios en puerto 8081
- [ ] El archivo `docker-compose.yml` está en la carpeta correcta
- [ ] El JAR del user-service está compilado (`mvn clean package`)
- [ ] Tienes esta guía abierta
- [ ] Has probado todo al menos una vez

---

## 🎤 SCRIPT DE PRESENTACIÓN SUGERIDO

1. **Introducción (1 min)**
   - "Voy a demostrar la dockerización del microservicio user-service con persistencia de datos"

2. **Mostrar Arquitectura (2 min)**
   - Explicar Dockerfile y docker-compose.yml
   - Mencionar el volumen de persistencia

3. **Levantar Servicios (2 min)**
   - Ejecutar `docker-compose up -d`
   - Mostrar `docker ps`

4. **Crear Usuario (2 min)**
   - Registrar usuario vía API REST
   - Consultar usuario creado

5. **Demostrar Persistencia (3 min)** ⭐
   - Detener contenedores
   - Volver a levantar
   - Consultar usuario nuevamente
   - **"Los datos persisten gracias al volumen de Docker"**

6. **Conclusión (1 min)**
   - Beneficios de la dockerización
   - Importancia de la persistencia
   - Escalabilidad futura

**Tiempo total: ~11 minutos**

---

## 📝 NOTAS FINALES

- Practica al menos 2 veces antes de presentar
- Ten esta guía abierta durante la presentación
- Si algo falla, usa los comandos de "Solución de Problemas"
- Enfatiza la **persistencia** como el logro principal
- Menciona que esto es base para escalar a todos los microservicios

¡Buena suerte! 🚀

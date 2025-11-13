# 🚀 Guía de Ejecución - Aplicación de Escritorio con Microservicios

## 📋 Requisitos Previos

1. **Java 17** instalado
2. **Maven** instalado
3. **RabbitMQ** (opcional, solo si usas mensajería)
4. Todos los microservicios compilados

## 🔄 Orden de Ejecución de Microservicios

Los microservicios deben iniciarse en este orden:

### 1️⃣ Discovery Service (Eureka) - Puerto 8761
**Primero debe iniciarse** porque los demás servicios se registran aquí.

```bash
cd discovery-service
mvn spring-boot:run
```

O si ya está compilado:
```bash
cd discovery-service/target
java -jar discovery-service-1.0-SNAPSHOT.jar
```

**Verificar:** Abre http://localhost:8761 en tu navegador. Deberías ver el dashboard de Eureka.

---

### 2️⃣ Gateway Service - Puerto 8080
**Segundo** porque la aplicación de escritorio se conecta aquí.

```bash
cd gateway-service
mvn spring-boot:run
```

O:
```bash
cd gateway-service/target
java -jar gateway-service-1.0-SNAPSHOT.jar
```

**Verificar:** 
- Debería aparecer registrado en Eureka (http://localhost:8761)
- Puedes probar: `curl http://localhost:8080/actuator/health`

---

### 3️⃣ Auth Service - Puerto 8081
```bash
cd auth-service
mvn spring-boot:run
```

**Verificar:** Debería aparecer en Eureka como "AUTH-SERVICE"

---

### 4️⃣ User Service - Puerto 8082
```bash
cd user-service
mvn spring-boot:run
```

**Verificar:** Debería aparecer en Eureka como "USER-SERVICE"

---

### 5️⃣ Otros Microservicios (Opcionales)
Si necesitas otros servicios, inícialos después:
- `format-a-service` (puerto 8083)
- `anteproject-service` (puerto 8086)
- `evaluation-service`
- `coordination-service`
- `notification-service`

---

## 🖥️ Compilar y Ejecutar la Aplicación de Escritorio

### Opción 1: Desde el IDE (Recomendado para desarrollo)

1. **Abrir el proyecto** en tu IDE (IntelliJ, Eclipse, NetBeans)
2. **Asegúrate de que el módulo `presentation` esté configurado como proyecto Maven**
3. **Ejecutar** la clase `co.unicauca.presentation.Application`

### Opción 2: Desde la línea de comandos

#### Paso 1: Compilar
```bash
cd presentation
mvn clean compile
```

#### Paso 2: Ejecutar
```bash
# Opción A: Con Maven
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.Application"

# Opción B: Compilar JAR y ejecutar
mvn clean package
java -cp "target/presentation-1.0-SNAPSHOT.jar:target/dependency/*" co.unicauca.presentation.Application

# Opción C: Si usaste maven-shade-plugin (JAR con dependencias)
mvn clean package
java -jar target/presentation-1.0-SNAPSHOT.jar
```

---

## ✅ Verificación Paso a Paso

### 1. Verificar que los Microservicios estén Corriendo

Abre http://localhost:8761 (Eureka Dashboard) y verifica que veas:
- ✅ **GATEWAY-SERVICE**
- ✅ **AUTH-SERVICE**
- ✅ **USER-SERVICE**

### 2. Verificar el Gateway

```bash
# En PowerShell (Windows)
Invoke-WebRequest -Uri http://localhost:8080/actuator/health -Method GET

# O en navegador
http://localhost:8080/actuator/health
```

### 3. Probar el Endpoint de Login (Opcional)

```bash
# PowerShell
$body = @{
    email = "test@unicauca.edu.co"
    password = "password123"
} | ConvertTo-Json

Invoke-WebRequest -Uri http://localhost:8080/api/auth/login -Method POST -Body $body -ContentType "application/json"
```

### 4. Ejecutar la Aplicación de Escritorio

1. **Inicia la aplicación** (ver sección anterior)
2. **Debería aparecer la ventana de login**
3. **Intenta hacer login** con credenciales válidas

### 5. Verificar en los Logs

Busca en la consola mensajes como:
```
✅ SessionService inicializado con Gateway: http://localhost:8080
✅ Intentando login para: usuario@unicauca.edu.co
✅ Login exitoso para: usuario@unicauca.edu.co con rol: STUDENT
```

---

## 🐛 Solución de Problemas

### Error: "Connection refused" o "No se puede conectar al Gateway"

**Causa:** El Gateway Service no está corriendo o no está en el puerto 8080.

**Solución:**
1. Verifica que el Gateway esté corriendo: `netstat -an | findstr 8080` (Windows)
2. Verifica la URL en `presentation/infrastructure/config/microservice.properties`
3. Asegúrate de que el Gateway esté registrado en Eureka

### Error: "401 Unauthorized" al hacer login

**Causa:** Las credenciales son incorrectas o el usuario no existe.

**Solución:**
1. Verifica que el usuario exista en la base de datos
2. Crea un usuario primero usando el endpoint de registro
3. Verifica que el `user-service` esté corriendo

### Error: "404 Not Found" al hacer peticiones

**Causa:** El endpoint no existe o la ruta es incorrecta.

**Solución:**
1. Verifica las rutas en `gateway-service/src/main/resources/application.properties`
2. Asegúrate de que el microservicio esté registrado en Eureka
3. Verifica los logs del Gateway para ver qué rutas están configuradas

### La aplicación no inicia (errores de Spring)

**Causa:** Faltan dependencias o hay problemas de configuración.

**Solución:**
1. Ejecuta `mvn clean install` en el módulo `presentation`
2. Verifica que todas las dependencias estén en `pom.xml`
3. Asegúrate de que Java 17 esté configurado correctamente

### Error: "ClassNotFoundException" o "NoClassDefFoundError"

**Causa:** Las dependencias no están incluidas en el classpath.

**Solución:**
1. Ejecuta `mvn clean package` para generar el JAR con dependencias
2. Si usas IDE, asegúrate de que Maven haya descargado las dependencias
3. Verifica que `maven-shade-plugin` esté configurado correctamente

---

## 📝 Scripts de Inicio Rápido (Windows PowerShell)

Crea estos scripts para facilitar el inicio:

### `start-all-services.ps1`
```powershell
# Iniciar todos los servicios en ventanas separadas
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd discovery-service; mvn spring-boot:run"
Start-Sleep -Seconds 10
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd gateway-service; mvn spring-boot:run"
Start-Sleep -Seconds 5
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd auth-service; mvn spring-boot:run"
Start-Sleep -Seconds 5
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd user-service; mvn spring-boot:run"
```

### `start-desktop-app.ps1`
```powershell
cd presentation
mvn clean package
java -jar target/presentation-1.0-SNAPSHOT.jar
```

---

## 🧪 Prueba Rápida

### Test 1: Verificar Conexión
```bash
# Debe retornar {"status":"UP"} o similar
curl http://localhost:8080/actuator/health
```

### Test 2: Probar Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@unicauca.edu.co","password":"password123"}'
```

### Test 3: Ejecutar Ejemplo
```bash
cd presentation
mvn exec:java -Dexec.mainClass="co.unicauca.presentation.examples.MicroserviceClientExample"
```

---

## 📊 Checklist de Verificación

Antes de ejecutar la aplicación de escritorio, verifica:

- [ ] Discovery Service corriendo en puerto 8761
- [ ] Gateway Service corriendo en puerto 8080
- [ ] Auth Service corriendo y registrado en Eureka
- [ ] User Service corriendo y registrado en Eureka
- [ ] Aplicación de escritorio compilada correctamente
- [ ] Archivo `microservice.properties` configurado correctamente
- [ ] Tienes al menos un usuario creado para probar login

---

## 💡 Tips

1. **Usa múltiples terminales** - Una para cada microservicio
2. **Revisa los logs** - Te darán pistas sobre qué está fallando
3. **Eureka Dashboard** - Es tu mejor amigo para ver qué servicios están corriendo
4. **Postman/Insomnia** - Útiles para probar los endpoints antes de usar la app de escritorio
5. **Variables de entorno** - Puedes usar `GATEWAY_URL` para cambiar la URL sin editar código

---

## 🎯 Resultado Esperado

Cuando todo funcione correctamente:

1. ✅ La ventana de login aparece
2. ✅ Puedes ingresar credenciales
3. ✅ El login se comunica con el Gateway
4. ✅ El Gateway redirige a Auth Service
5. ✅ Auth Service valida con User Service
6. ✅ Recibes un token JWT
7. ✅ La aplicación muestra la vista correspondiente según el rol

¡Éxito! 🎉


# 🚀 SCRIPT RÁPIDO - Presentación Docker (5 minutos)

## ⚡ PREPARACIÓN (30 segundos)
```powershell
cd "d:\universidad\Software II\ProccesManagement-microservice\ProcessManagement\ProccesManagementMicroservices"
docker-compose up -d
Start-Sleep -Seconds 20
```

---

## 📋 DEMOSTRACIÓN

### 1️⃣ CREAR USUARIO (1 min)
```powershell
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

Invoke-WebRequest -Uri "http://localhost:8081/api/users/register" -Method POST -ContentType "application/json" -InFile "$env:TEMP\user.json"
```

**Decir:** "He creado un usuario llamado María González. Noten el ID: 1"

---

### 2️⃣ CONSULTAR USUARIO (30 seg)
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/users/maria.gonzalez@unicauca.edu.co" -Method GET
```

**Decir:** "El usuario se guardó correctamente en PostgreSQL"

---

### 3️⃣ DEMOSTRAR PERSISTENCIA (2 min) ⭐

#### Detener contenedores
```powershell
docker-compose down
```

**Decir:** "Voy a eliminar los contenedores completamente"

#### Verificar que no existen
```powershell
docker ps
```

**Decir:** "Los contenedores ya no existen"

#### Volver a levantar
```powershell
docker-compose up -d
Start-Sleep -Seconds 20
```

**Decir:** "Levanto nuevamente los servicios desde cero"

#### Consultar usuario NUEVAMENTE
```powershell
Invoke-WebRequest -Uri "http://localhost:8081/api/users/maria.gonzalez@unicauca.edu.co" -Method GET
```

**Decir:** "¡Los datos de María siguen ahí! Esto demuestra que el volumen de Docker está funcionando correctamente"

---

### 4️⃣ MOSTRAR VOLUMEN (30 seg)
```powershell
docker volume ls
docker volume inspect processmanagement_postgres-data
```

**Decir:** "Este volumen almacena todos los datos de PostgreSQL de forma persistente"

---

## 🎯 PUNTOS CLAVE A MENCIONAR

1. **Dockerización**: Servicio aislado y portable
2. **Orquestación**: Docker Compose maneja múltiples contenedores
3. **Persistencia**: Volumen garantiza que los datos sobreviven
4. **Escalabilidad**: Base para dockerizar todos los microservicios

---

## ⚠️ SI ALGO FALLA

### Puerto ocupado:
```powershell
netstat -ano | findstr :8081
taskkill /PID <numero> /F
docker-compose up -d
```

### Ver logs:
```powershell
docker logs user-service
docker logs postgres-db
```

### Reiniciar todo:
```powershell
docker-compose down
docker-compose up -d
```

---

## ✅ CHECKLIST PRE-PRESENTACIÓN

- [ ] Docker Desktop corriendo (ícono verde)
- [ ] Puerto 8081 libre
- [ ] Esta guía abierta
- [ ] Probado al menos una vez

**¡Éxito! 🎉**

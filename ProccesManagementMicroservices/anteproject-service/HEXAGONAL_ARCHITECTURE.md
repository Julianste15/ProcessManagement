# Arquitectura Hexagonal - Anteproject Service

## Descripción General

El `anteproject-service` ha sido refactorizado para implementar **Arquitectura Hexagonal (Puertos y Adaptadores)** siguiendo los principios de **Domain-Driven Design (DDD)**.

## Estructura de Capas

```
anteproject-service/
├── domain/                          # NÚCLEO DEL NEGOCIO (Sin dependencias externas)
│   ├── model/                       # Entidades y Value Objects
│   │   ├── Anteproject.java         # Entidad de dominio (POJO puro)
│   │   └── AnteprojectStatus.java   # Enum de estados
│   └── ports/                       # Interfaces (Contratos)
│       ├── in/                      # Puertos de Entrada (Casos de Uso)
│       │   ├── CreateAnteprojectUseCase.java
│       │   ├── ManageAnteprojectUseCase.java
│       │   └── GetAnteprojectUseCase.java
│       └── out/                     # Puertos de Salida (Dependencias)
│           ├── AnteprojectRepositoryPort.java
│           ├── EvaluationServicePort.java
│           └── AnteprojectEventPublisherPort.java
│
├── application/                     # LÓGICA DE APLICACIÓN
│   └── usecases/
│       └── AnteprojectApplicationService.java  # Implementa casos de uso
│
└── infrastructure/                  # ADAPTADORES (Frameworks y Tecnologías)
    ├── input/                       # Adaptadores de Entrada (Driving)
    │   └── controller/
    │       └── AnteprojectRestController.java  # REST API
    └── output/                      # Adaptadores de Salida (Driven)
        ├── persistence/
        │   ├── entity/
        │   │   └── AnteprojectEntity.java      # Entidad JPA
        │   ├── mapper/
        │   │   └── AnteprojectMapper.java      # Conversión Domain ↔ Entity
        │   ├── repository/
        │   │   └── JpaAnteprojectRepository.java  # Spring Data JPA
        │   └── AnteprojectPersistenceAdapter.java # Implementa RepositoryPort
        └── client/
            ├── EvaluationServiceAdapter.java   # Feign Client
            └── EventPublisherAdapter.java      # RabbitMQ Publisher
```

## Principios Aplicados

### 1. **Inversión de Dependencias**
- El **dominio** NO depende de la infraestructura
- La **infraestructura** depende del dominio
- Se usan **interfaces (puertos)** para definir contratos

### 2. **Separación de Responsabilidades**

#### Capa de Dominio (`domain`)
- **Responsabilidad**: Contiene la lógica de negocio pura
- **Características**:
  - Sin anotaciones de Spring (`@Service`, `@Component`)
  - Sin anotaciones de JPA (`@Entity`, `@Table`)
  - Solo clases Java puras (POJOs)
- **Ejemplo**: `Anteproject.java` es un POJO sin dependencias

#### Capa de Aplicación (`application`)
- **Responsabilidad**: Orquesta los casos de uso
- **Características**:
  - Implementa los puertos de entrada (`in`)
  - Usa los puertos de salida (`out`)
  - Contiene la lógica de coordinación
- **Ejemplo**: `AnteprojectApplicationService` coordina la creación de anteproyectos

#### Capa de Infraestructura (`infrastructure`)
- **Responsabilidad**: Implementa los detalles técnicos
- **Características**:
  - Adaptadores REST, JPA, Feign, RabbitMQ
  - Puede usar anotaciones de frameworks
  - Implementa los puertos de salida

### 3. **Puertos e Interfaces**

#### Puertos de Entrada (Input Ports)
Definen **qué puede hacer** el sistema:
```java
public interface CreateAnteprojectUseCase {
    Anteproject createAnteproject(CreateAnteprojectRequest request);
}
```

#### Puertos de Salida (Output Ports)
Definen **qué necesita** el sistema:
```java
public interface AnteprojectRepositoryPort {
    Anteproject save(Anteproject anteproject);
    Optional<Anteproject> findById(Long id);
    // ...
}
```

## Flujo de Datos

### Ejemplo: Crear un Anteproyecto

```
1. Cliente HTTP
   ↓
2. AnteprojectRestController (Input Adapter)
   ↓
3. CreateAnteprojectUseCase (Port In)
   ↓
4. AnteprojectApplicationService (Application)
   ↓
5. AnteprojectRepositoryPort (Port Out)
   ↓
6. AnteprojectPersistenceAdapter (Output Adapter)
   ↓
7. JpaAnteprojectRepository (Spring Data JPA)
   ↓
8. Base de Datos
```

## Mapeo de Datos

### Domain Model ↔ JPA Entity

El `AnteprojectMapper` convierte entre:
- **Domain Model** (`Anteproject`): Usado en la lógica de negocio
- **JPA Entity** (`AnteprojectEntity`): Usado para persistencia

```java
// Domain → Entity
AnteprojectEntity entity = mapper.toEntity(domainModel);

// Entity → Domain
Anteproject domain = mapper.toDomain(entity);
```

### Domain Model ↔ DTO

El `AnteprojectRestController` convierte entre:
- **Domain Model** (`Anteproject`): Usado internamente
- **DTO** (`AnteprojectDTO`): Expuesto en la API REST

## Ventajas de esta Arquitectura

### 1. **Testabilidad**
- El dominio se puede probar sin Spring, JPA o base de datos
- Los puertos se pueden mockear fácilmente

### 2. **Mantenibilidad**
- Cambios en la infraestructura no afectan el dominio
- Ejemplo: Cambiar de JPA a MongoDB solo requiere un nuevo adaptador

### 3. **Claridad**
- La lógica de negocio está claramente separada
- Los casos de uso son explícitos

### 4. **Flexibilidad**
- Fácil agregar nuevos adaptadores (GraphQL, gRPC, etc.)
- Fácil cambiar tecnologías (Kafka en lugar de RabbitMQ)

## Casos de Uso Implementados

### CreateAnteprojectUseCase
- **Entrada**: `CreateAnteprojectRequest`
- **Salida**: `Anteproject`
- **Lógica**: Valida que no exista un anteproyecto para el Formato A

### ManageAnteprojectUseCase
- **submitDocument**: Sube el documento del anteproyecto
- **assignEvaluators**: Asigna evaluadores (permite DRAFT o SUBMITTED)
- **updateStatus**: Actualiza el estado del anteproyecto

### GetAnteprojectUseCase
- **getAnteprojectById**: Obtiene por ID
- **getAnteprojectsByStudent**: Filtra por estudiante
- **getAnteprojectsByDirector**: Filtra por director
- **getSubmittedAnteprojectsForDepartmentHead**: Lista para jefe de departamento

## Migración desde Arquitectura en Capas

### Archivos Eliminados (Legacy)
- `model/Anteproject.java` → `domain/model/Anteproject.java`
- `model/AnteprojectStatus.java` → `domain/model/AnteprojectStatus.java`
- `service/AnteprojectService.java` → `application/usecases/AnteprojectApplicationService.java`
- `repository/AnteprojectRepository.java` → `infrastructure/output/persistence/repository/JpaAnteprojectRepository.java`
- `controller/AnteprojectController.java` → `infrastructure/input/controller/AnteprojectRestController.java`

### Cambios en DTOs
- `AnteprojectDTO` ahora usa `domain.model.AnteprojectStatus`
- Los DTOs permanecen en el paquete `dto` (capa de presentación)

## Configuración de Spring

Spring Boot automáticamente detecta:
- `@RestController` en `infrastructure.input.controller`
- `@Service` en `application.usecases`
- `@Component` en `infrastructure.output`
- `@Repository` en `infrastructure.output.persistence.repository`

## Próximos Pasos (Mejoras Futuras)

1. **Value Objects**: Crear `AnteprojectId`, `Email`, etc.
2. **Aggregates**: Definir `Anteproject` como agregado raíz
3. **Domain Events**: Publicar eventos desde el dominio
4. **CQRS**: Separar comandos de consultas
5. **Tests**: Agregar tests unitarios para el dominio

## Referencias

- [Hexagonal Architecture - Alistair Cockburn](https://alistair.cockburn.us/hexagonal-architecture/)
- [Domain-Driven Design - Eric Evans](https://www.domainlanguage.com/ddd/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)

---

**Fecha de Implementación**: 27 de Noviembre, 2025  
**Autor**: Equipo de Desarrollo - ProcessManagement Microservices

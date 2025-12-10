# Design Patterns Implementation Report

## 📋 Overview

This document provides a comprehensive overview of all design patterns implemented across the **ProcessManagement Microservices** project. Each pattern is documented with its location, purpose, and implementation details.

---

## 🎯 Patterns Summary

| Pattern | Microservice | Purpose |
|---------|--------------|---------|
| **State** | format-a-service | Manage FormatoA lifecycle states |
| **Builder** | user-service | Construct User objects with validation |
| **Adapter** | anteproject-service | Hexagonal Architecture - Adapt external services to domain ports |
| **Decorator** | notification-service | Add logging to email service |
| **Factory** | notification-service | Centralize EmailMessage creation |
| **Facade** | presentation | Simplify backend service interactions |

---

## 1. State Pattern

**Location**: `format-a-service/src/main/java/co/unicauca/formata/domain/state`

### Purpose
Manages the complex lifecycle of a FormatoA document through different states (En Evaluación, Aceptado, Rechazado, Anteproyecto Subido, etc.).

### Implementation

#### Key Components:
- **`FormatoAState`** (Interface) - Defines state behavior contract
- **`FormatoAStateContext`** - Context that holds current state
- **`AbstractFormatoAState`** - Base class for all states
- **Concrete States**:
  - `EnEvaluacionState`
  - `AceptadoState`
  - `RechazadoState`
  - `AnteproyectoSubidoState`
  - `AnteproyectoEnEvaluacionState`
  - `AnteproyectoAceptadoState`
  - `AnteproyectoRechazadoState`

### Example Usage:
```java
FormatoA formatoA = new FormatoA(...);
formatoA.getStateContext().evaluate(evaluationRequest); // State transition
```

### Benefits:
- ✅ Encapsulates state-specific behavior
- ✅ Eliminates complex if-else chains
- ✅ Easy to add new states

---

## 2. Builder Pattern

**Location**: `user-service/src/main/java/co/unicauca/user/model/UserBuilder.java`

### Purpose
Provides a fluent API for constructing `User` objects with validation.

### Implementation

```java
User user = new UserBuilder()
    .withNames("Juan")
    .withEmail("juan@unicauca.edu.co")
    .withPassword("securePass123")
    .withRole(Role.STUDENT)
    .build();
```

### Key Features:
- Email validation
- Password strength validation
- Required field validation
- Fluent interface

### Benefits:
- ✅ Readable object construction
- ✅ Validation at build time
- ✅ Immutable object creation

---

## 3. Adapter Pattern

**Location**: `anteproject-service/src/main/java/co/unicauca/anteproject/infrastructure/output`

### Purpose
Implements **Hexagonal Architecture** (Ports & Adapters) to decouple domain logic from external services.

### Implementation

#### Adapters Implemented:

##### 3.1 EvaluationServiceAdapter
**Path**: `infrastructure/output/client/EvaluationServiceAdapter.java`

```java
@Component
public class EvaluationServiceAdapter implements EvaluationServicePort {
    private final EvaluationServiceClient feignClient;
    
    @Override
    public void assignEvaluators(Long anteprojectId, ...) {
        feignClient.assignEvaluators(anteprojectId, ...);
    }
}
```

**Adapts**: Feign HTTP client → Domain port

---

##### 3.2 EventPublisherAdapter
**Path**: `infrastructure/output/EventPublisherAdapter.java`

**Adapts**: RabbitMQ/Kafka → Domain event publishing port

---

##### 3.3 AnteprojectPersistenceAdapter
**Path**: `infrastructure/output/persistence/AnteprojectPersistenceAdapter.java`

**Adapts**: JPA Repository → Domain repository port

---

### Benefits:
- ✅ Domain logic independent of frameworks
- ✅ Easy to swap implementations (e.g., Feign → RestTemplate)
- ✅ Testability (mock ports instead of external services)

---

## 4. Decorator Pattern

**Location**: `notification-service/src/main/java/co/unicauca/notification/decorators`

### Purpose
Adds logging functionality to email sending without modifying the base `EmailService`.

### Implementation

#### Key Components:
- **`EmailService`** (Interface) - Base service contract
- **`EmailServiceSimulado`** - Concrete implementation
- **`EmailServiceDecorator`** - Abstract decorator base class
- **`SimpleLoggingEmailDecorator`** - Concrete decorator that adds logging

```java
@Configuration
public class ServiceDecoratorConfig {
    @Bean
    @Primary
    public EmailService emailService(@Qualifier("baseEmailService") EmailService baseService) {
        return new SimpleLoggingEmailDecorator(baseService);
    }
}
```

### Benefits:
- ✅ Open/Closed Principle (open for extension, closed for modification)
- ✅ Single Responsibility (logging separated from email sending)
- ✅ Easy to add more decorators (e.g., retry, metrics)

---

## 5. Factory Pattern

**Location**: `notification-service/src/main/java/co/unicauca/notification/factory/NotificationFactory.java`

### Purpose
Centralizes the creation of `EmailMessage` objects for different notification types.

### Implementation

```java
@Component
public class NotificationFactory {
    @Autowired
    private TemplateService templateService;
    
    public EmailMessage createFormatoASubmittedMessage(...) {
        String subject = "Nuevo Formato A - " + titulo;
        String body = templateService.generateFormatoAEnviadoTemplate(...);
        return new EmailMessage(coordinatorEmail, subject, body);
    }
    
    public EmailMessage createEvaluatorAssignmentMessage(...) { ... }
    public EmailMessage createFormatoAEvaluatedMessage(...) { ... }
    // ... 6 factory methods total
}
```

### Factory Methods:
1. `createFormatoASubmittedMessage()`
2. `createFormatoAEvaluatedMessage()`
3. `createFormatoARetryMessage()`
4. `createEvaluatorAssignmentMessage()`
5. `createAnteprojectInEvaluationMessage()`
6. `createDepartmentHeadNotificationMessage()`

### Benefits:
- ✅ Separates object creation from business logic
- ✅ Centralized message construction
- ✅ Easy to add new notification types

---

## 6. Facade Pattern

**Location**: `presentation/src/main/java/co/unicauca/domain/services/ProcessManagementFacade.java`

### Purpose
Simplifies interactions between JavaFX controllers and multiple backend microservices.

### Implementation

```java
public class ProcessManagementFacade {
    private final FormatAService formatAService;
    private final AnteprojectService anteprojectService;
    
    // Simplified operations
    public Map<String, Object> submitFormatA(...) { ... }
    public void approveProject(...) { ... }
    public Map<String, Object> assignEvaluators(...) { ... }
    
    // Complex workflows
    public Map<String, Object> approveAndCreateAnteproject(...) {
        approveProject(formatoAId, observations);
        return createAnteproject(formatoAId, titulo, directorEmail);
    }
}
```

### Facade Methods (13 total):
- **FormatA Operations**: submit, resubmit, approve, reject, getByUser, getForCoordinator
- **Anteproject Operations**: create, upload, getSubmitted, assignEvaluators
- **Complex Workflows**: approveAndCreateAnteproject, getTeacherDashboard

### Benefits:
- ✅ Reduces coupling between UI and backend
- ✅ Centralizes integration logic
- ✅ Simplifies controller code

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                        │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Controllers (JavaFX)                                 │   │
│  │    ↓ uses                                             │   │
│  │  ProcessManagementFacade (Facade Pattern)            │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
                            ↓ HTTP
┌─────────────────────────────────────────────────────────────┐
│                    Microservices Layer                       │
│                                                              │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │ format-a-service │  │ anteproject-svc  │                │
│  │  • State Pattern │  │  • Adapter (x3)  │                │
│  └──────────────────┘  └──────────────────┘                │
│                                                              │
│  ┌──────────────────┐  ┌──────────────────┐                │
│  │ notification-svc │  │  user-service    │                │
│  │  • Decorator     │  │  • Builder       │                │
│  │  • Factory       │  │                  │                │
│  └──────────────────┘  └──────────────────┘                │
└─────────────────────────────────────────────────────────────┘
```

---

## 📊 Pattern Distribution

- **Behavioral Patterns**: State (1)
- **Creational Patterns**: Builder (1), Factory (1)
- **Structural Patterns**: Adapter (3), Decorator (1), Facade (1)

**Total**: 6 distinct patterns, 8 implementations

---

## 🎓 Best Practices Demonstrated

1. **Separation of Concerns**: Each pattern addresses a specific design problem
2. **SOLID Principles**: 
   - Single Responsibility (Factory, Decorator)
   - Open/Closed (Decorator, State)
   - Dependency Inversion (Adapter/Hexagonal Architecture)
3. **Clean Architecture**: Hexagonal Architecture in `anteproject-service`
4. **Testability**: All patterns improve unit testing capabilities

---

## 📝 Maintenance Notes

- **State Pattern**: Add new states by extending `AbstractFormatoAState`
- **Builder Pattern**: Add new fields with `withX()` methods
- **Adapter Pattern**: Follow hexagonal architecture for new external integrations
- **Decorator Pattern**: Stack multiple decorators for combined functionality
- **Factory Pattern**: Add new factory methods for new notification types
- **Facade Pattern**: Extend with new workflow methods as needed

---

## 🔗 Related Documentation

- [Hexagonal Architecture Guide](https://alistair.cockburn.us/hexagonal-architecture/)
- [Gang of Four Design Patterns](https://refactoring.guru/design-patterns)
- [Spring Framework Best Practices](https://spring.io/guides)

---

**Document Version**: 1.0  
**Last Updated**: December 2025  
**Maintained By**: Development Team

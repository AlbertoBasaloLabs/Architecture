# AstroBookings - Arquitectura

## Introducción

AstroBookings es una aplicación de reservas de viajes espaciales implementada con arquitectura en capas. Utiliza Java 21, JDK HTTP Server y Jackson para JSON. La base de datos es en memoria (HashMap).

**Características principales**:
- Gestión de cohetes, vuelos y reservas
- Control de capacidad y estados de vuelos
- Cancelación automática de vuelos con devoluciones
- Procesamiento de pagos simulado (gateway externo)
- Notificaciones por email simuladas

## Tecnologías

- **Java**: 21
- **Build**: Maven 3.x
- **HTTP Server**: JDK HTTP Server (com.sun.net.httpserver)
- **JSON**: Jackson 2.15.2
- **Database**: In-memory (HashMap)
- **External Services**: Simulated (console logs)

## Endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/rockets` | List all rockets |
| POST | `/rockets` | Create rocket |
| GET | `/flights` | List available flights (filter by status) |
| POST | `/flights` | Create flight |
| GET | `/bookings` | List bookings (filter by flight/passenger) |
| POST | `/bookings` | Create booking (processes payment) |
| POST | `/admin/cancel-flights` | Trigger cancellation check |

## Estructura de Carpetas

```
java/
├── src/main/java/com/astrobookings/
│   ├── app/            # HTTP Handlers & Config (Presentation Layer)
│   │   ├── *.java      # HTTP Handlers (Admin, Base, Booking, Flight, Rocket)
│   │   └── AppConfig.java
│   ├── business/       # Core Business Logic (Hexagon)
│   │   ├── models/     # Domain Entities, DTOs, Exceptions
│   │   ├── ports/      # Output Ports (Repository & Gateway Interfaces)
│   │   ├── *Service.java       # Input Ports (Service Interfaces)
│   │   ├── *ServiceImpl.java   # Service Implementations
│   │   └── ServiceFactory.java
│   ├── providers/      # Infrastructure Adapters
│   │   ├── InMemory*Repository.java  # Repository Implementations
│   │   ├── *Impl.java                # Gateway Implementations (Payment, Notification)
│   │   ├── RepositoryFactory.java
│   │   └── ExternalFactory.java
│   └── AstroBookingsApp.java
```

**Capas**:
- **app**: Handlers HTTP para validación de estructura y Configuración
  - Handlers: Rocket, Flight, Booking, Admin, BaseHandler
  - AppConfig: Configuración de la aplicación
- **business**: Lógica de Negocio, Modelos y Puertos
  - **models/**: Entidades de Dominio (Booking, Flight, Rocket), DTOs (Requests) y Excepciones
  - **ports/**: Interfaces para Repositorios y Servicios Externos (Output Ports)
  - **Services**: Implementación de la lógica de negocio (Input Ports)
- **providers**: Implementaciones de Infraestructura (Adaptadores)
  - Implementaciones en memoria de los repositorios
  - Implementaciones simuladas de servicios externos (Payment, Notification)
  - Factorías para la inyección de dependencias de infraestructura

## Mejoras de Responsabilidad por Capas

### Separación de Validaciones

**Validación de Estructura (Capa de Aplicación)**:
- Handlers validan que los campos requeridos existan y tengan el tipo correcto
- Ejemplo: `FlightHandler` valida que `rocketId`, `departureDate` y `basePrice` estén presentes
- Los handlers pasan objetos `CreateXxxRequest` completos a los servicios

**Validación de Negocio (Capa de Negocio)**:
- Services validan reglas de negocio y parsean datos
- Ejemplo: `FlightService` valida que la fecha sea futura, no exceda 1 año, y parsea el string a `LocalDateTime`

### Request Models en la Capa de Negocio

Los modelos de entrada están en `business/models` como **Records anémicos**:

```java
public record CreateFlightRequest(String rocketId, String departureDate, Double basePrice) {}
public record CreateBookingRequest(String flightId, String passengerName) {}
public record CreateRocketRequest(String name, Integer capacity, Double speed) {}
```

**Ventajas**:
- **Firmas limpias**: Los servicios reciben un objeto en lugar de múltiples parámetros
- **Reutilización**: Los mismos modelos se pueden usar desde cualquier interfaz (REST, CLI, tests)
- **Encapsulación**: Los servicios no dependen de detalles HTTP
- **Evolución**: Añadir campos no rompe las firmas de métodos

### Excepciones Personalizadas

Reemplazo de excepciones genéricas por excepciones específicas de negocio:

- **ValidationException** (400 Bad Request): Errores de validación de negocio
- **NotFoundException** (404 Not Found): Recursos no encontrados
- **PaymentException** (402 Payment Required): Errores de procesamiento de pago

### Manejo Centralizado de Errores

`BaseHandler.handleBusinessException()` mapea excepciones a códigos HTTP:
- `ValidationException` → 400
- `NotFoundException` → 404
- `PaymentException` → 402
- `IllegalArgumentException` (validación de estructura) → 400
- Otros → 500

### Inversión de Dependencias y Configuración

Se ha implementado un patrón de **Inversión de Dependencias** para desacoplar las capas.

#### 1. Interfaces (Puertos)
El núcleo de negocio define interfaces (puertos) para sus dependencias:
- `BookingRepository`, `PaymentGateway`, etc. (en `business/ports`)

#### 2. Implementaciones (Adaptadores)
Las implementaciones concretas residen en la capa de infraestructura (`providers`):
- `InMemoryBookingRepository`, `PaymentGatewayImpl`, etc.

#### 3. Inyección de Dependencias (AppConfig)
La clase `AppConfig` (en `app`) actúa como el "Composition Root". Es responsable de instanciar los adaptadores y los servicios, inyectando las dependencias necesarias.

- **`RepositoryFactory`** y **`ExternalFactory`** (en `providers`): Proveen las instancias de los adaptadores.
- **`ServiceFactory`** (en `business`): Crea los servicios de negocio recibiendo las dependencias por constructor.

```java
// En AppConfig.java
private static final BookingRepository bookingRepo = RepositoryFactory.getBookingRepository();
private static final PaymentGateway paymentGateway = ExternalFactory.getPaymentGateway();

private static final BookingService bookingService = ServiceFactory.createBookingService(
    bookingRepo, ..., paymentGateway, ...
);
```

Este diseño permite:
- **Desacoplamiento**: El núcleo de negocio no depende de implementaciones concretas.
- **Testabilidad**: Facilita la inyección de mocks en los tests unitarios de los servicios.
- **Flexibilidad**: Cambiar una implementación (ej. de memoria a base de datos) solo requiere cambios en la configuración (`AppConfig` y Factorías).

## Flujo de Datos

### Crear Reserva (POST /bookings)
```
Application Layer
  └─ BookingHandler
       ├─ Deserializes JSON → CreateBookingRequest
       ├─ Validates structure (fields exist)
       └─ Passes CreateBookingRequest to service
            ↓
          Business Layer
            └─ BookingService.createBooking(CreateBookingRequest)
                 ├─ Validates business rules
                 ├─ PaymentGateway (process payment)
                 └─ NotificationService (if flight confirmed)
                      ↓
                    Persistence Layer
                      ├─ BookingRepository (save booking)
                      └─ FlightRepository (update flight status)
                           ↓
                         Domain Models
                           ├─ Booking (with paymentTransactionId)
                           └─ Flight (status: SCHEDULED → CONFIRMED)
```

### Crear Vuelo (POST /flights)
```
Application Layer
  └─ FlightHandler
       ├─ Deserializes JSON → CreateFlightRequest
       ├─ Validates structure (fields exist)
       └─ Passes CreateFlightRequest to service
            ↓
          Business Layer
            └─ FlightService.createFlight(CreateFlightRequest)
                 ├─ Validates business rules
                 ├─ Parses departureDate string → LocalDateTime
                 └─ Verifies rocket exists
                      ↓
                    Persistence Layer
                      ├─ FlightRepository (save flight)
                      └─ RocketRepository (verify rocket exists)
```

### Crear Cohete (POST /rockets)
```
Application Layer
  └─ RocketHandler
       ├─ Deserializes JSON → CreateRocketRequest
       ├─ Validates structure (fields exist)
       └─ Passes CreateRocketRequest to service
            ↓
          Business Layer
            └─ RocketService.createRocket(CreateRocketRequest)
                 └─ Validates business rules (capacity <= 10)
                      ↓
                    Persistence Layer
                      └─ RocketRepository (save rocket)
```

## Ejecución

```bash
# Compilar
mvn clean package

# Ejecutar
java -jar target/astrobookings-1.0-SNAPSHOT.jar

# Server: http://localhost:8080
```

Ver [README.md](README.md) para instrucciones detalladas.

## Diagrama de Componentes

El diagrama C4 de nivel 3 se encuentra en el fichero [components-diagram.md](components-diagram.md).
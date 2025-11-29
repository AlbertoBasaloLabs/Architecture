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

| Method | Path                    | Description                                |
| ------ | ----------------------- | ------------------------------------------ |
| GET    | `/rockets`              | List all rockets                           |
| POST   | `/rockets`              | Create rocket                              |
| GET    | `/flights`              | List available flights (filter by status)  |
| POST   | `/flights`              | Create flight                              |
| GET    | `/bookings`             | List bookings (filter by flight/passenger) |
| POST   | `/bookings`             | Create booking (processes payment)         |
| POST   | `/admin/cancel-flights` | Trigger cancellation check                 |

## Estructura de Carpetas

```
java/
├── src/main/java/com/astrobookings/
│   ├── adapters/
│   │   ├── in/                       # Input Adapters (Rest Handlers)
│   │   │   ├── *.java                # HTTP Handlers
│   │   │   └── BaseHandler.java
│   │   └── out/                      # Output Adapters (Infrastructure)
│   │       ├── InMemory*Repository.java
│   │       ├── *Impl.java            # Gateways
│   │       ├── RepositoryFactory.java
│   │       └── ExternalFactory.java
│   ├── config/                       # Configuration
│   │   └── AppConfig.java
│   ├── core/
│   │   ├── application/              # Application Layer (Use Cases & Ports)
│   │   │   ├── ports/                # Output Ports
│   │   │   ├── *Service.java         # Input Ports (Interfaces)
│   │   │   ├── *ServiceImpl.java     # Application Services (Implementations)
│   │   │   └── ServiceFactory.java
│   │   └── domain/                   # Domain Layer
│   │       └── models/               # Entities, DTOs, Exceptions
│   └── AstroBookingsApp.java
```

**Capas**:
- **Adapters In**: Adaptadores de entrada (Rest Handlers) que invocan a la capa de aplicación.
- **Adapters Out**: Adaptadores de salida (Infraestructura) que implementan los puertos de salida.
- **Core Application**: Lógica de aplicación, casos de uso (Servicios) y definición de puertos.
- **Core Domain**: Lógica de dominio puro, entidades y modelos.
- **Config**: Configuración y composición de dependencias (AppConfig).

## Mejoras de Responsabilidad por Capas

### Separación de Validaciones

**Validación de Estructura (Adapters In)**:
- Handlers validan que los campos requeridos existan y tengan el tipo correcto.
- Ejemplo: `FlightHandler` valida que `rocketId`, `departureDate` y `basePrice` estén presentes.

**Validación de Negocio (Core Application)**:
- Services validan reglas de negocio y parsean datos.
- Ejemplo: `FlightService` valida que la fecha sea futura.

### Request Models en el Dominio

Los modelos de entrada están en `core/domain/models` como **Records anémicos**:

```java
public record CreateFlightRequest(String rocketId, String departureDate, Double basePrice) {}
public record CreateBookingRequest(String flightId, String passengerName) {}
public record CreateRocketRequest(String name, Integer capacity, Double speed) {}
```

### Excepciones Personalizadas

Reemplazo de excepciones genéricas por excepciones específicas de negocio:
- **ValidationException** (400 Bad Request)
- **NotFoundException** (404 Not Found)
- **PaymentException** (402 Payment Required)

### Manejo Centralizado de Errores

`BaseHandler.handleBusinessException()` mapea excepciones a códigos HTTP.

### Inversión de Dependencias y Configuración

Se ha implementado un patrón de **Inversión de Dependencias** para desacoplar las capas.

#### 1. Interfaces (Puertos)
El núcleo de aplicación define interfaces (puertos) para sus dependencias:
- `BookingRepository`, `PaymentGateway`, etc. (en `core/application/ports`)

#### 2. Implementaciones (Adaptadores)
Las implementaciones concretas residen en la capa de adaptadores de salida (`adapters/out`):
- `InMemoryBookingRepository`, `PaymentGatewayImpl`, etc.

#### 3. Inyección de Dependencias (AppConfig)
La clase `AppConfig` (en `config`) actúa como el "Composition Root".

```java
// En AppConfig.java
private static final BookingRepository bookingRepo = RepositoryFactory.getBookingRepository();
private static final PaymentGateway paymentGateway = ExternalFactory.getPaymentGateway();

private static final BookingService bookingService = ServiceFactory.createBookingService(
    bookingRepo, ..., paymentGateway, ...
);
```

## Flujo de Datos

### Crear Reserva (POST /bookings)
```
Adapters In
  └─ BookingHandler
       ├─ Deserializes JSON → CreateBookingRequest
       ├─ Validates structure
       └─ Passes CreateBookingRequest to service
            ↓
          Core Application
            └─ BookingService.createBooking(CreateBookingRequest)
                 ├─ Validates business rules
                 ├─ PaymentGateway (process payment)
                 └─ NotificationService (if flight confirmed)
                      ↓
                    Adapters Out (Persistence)
                      ├─ BookingRepository (save booking)
                      └─ FlightRepository (update flight status)
                           ↓
                         Core Domain
                           ├─ Booking (with paymentTransactionId)
                           └─ Flight (status: SCHEDULED → CONFIRMED)
```

### Crear Vuelo (POST /flights)
```
Adapters In
  └─ FlightHandler
       ├─ Deserializes JSON → CreateFlightRequest
       ├─ Validates structure
       └─ Passes CreateFlightRequest to service
            ↓
          Core Application
            └─ FlightService.createFlight(CreateFlightRequest)
                 ├─ Validates business rules
                 └─ Verifies rocket exists
                      ↓
                    Adapters Out (Persistence)
                      ├─ FlightRepository (save flight)
                      └─ RocketRepository (verify rocket exists)
```

### Crear Cohete (POST /rockets)
```
Adapters In
  └─ RocketHandler
       ├─ Deserializes JSON → CreateRocketRequest
       ├─ Validates structure
       └─ Passes CreateRocketRequest to service
            ↓
          Core Application
            └─ RocketService.createRocket(CreateRocketRequest)
                 └─ Validates business rules
                      ↓
                    Adapters Out (Persistence)
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
# Plan de Implementación: AstroBookings (Versión "Legacy/Sucia")

Este documento define la hoja de ruta para construir la versión inicial del backend de **AstroBookings**. El objetivo es crear una aplicación funcional pero con **defectos arquitectónicos intencionales** (arquitectura en capas rígida, alto acoplamiento, modelo anémico) que servirán como punto de partida para el taller de refactorización hacia Arquitectura Hexagonal y DDD.

## 1. Stack Tecnológico
Para minimizar la curva de aprendizaje y centrarse en la arquitectura, usaremos **Java estándar** sin frameworks pesados (como Spring Boot) que oculten el flujo de control.

*   **Lenguaje**: Java 21 (aprovechando `var`, `Records` si fuera necesario, pero usaremos Clases clásicas para enfatizar el estilo "legacy").
*   **Build Tool**: Maven.
*   **Servidor Web**: `com.sun.net.httpserver.HttpServer` (Incluido en el JDK).
*   **JSON**: Jackson o Gson (Única dependencia externa permitida).
*   **Base de Datos**: En memoria (Mapas estáticos/Singletons).

## 2. Estructura del Proyecto (Package by Layer)
La estructura reflejará una arquitectura en capas tradicional con dependencias directas.

```text
com.astrobookings
├── controller      # Capa de Entrada (HttpHandlers)
├── service         # Lógica de Negocio (Mezclada con orquestación)
├── data            # Acceso a Datos (Simulando DB)
├── model           # Entidades Anémicas (Solo datos, getters/setters)
└── AstroBookingsApp.java # Main class
```

## 3. Modelo de Datos (Anémico)
Clases simples con Getters y Setters. Sin lógica de negocio.

*   **`Rocket`**:
    *   `id` (String/Long)
    *   `name` (String)
    *   `capacity` (int) - Capacidad total de pasajeros.
    *   `speed` (double) - Para cálculos cosméticos.
*   **`Flight`**:
    *   `id` (String)
    *   `rocketId` (String) - Referencia por ID (Bad smell: no referencia al objeto).
    *   `departureDate` (LocalDateTime)
    *   `basePrice` (double)
    *   `minPassengers` (int) - Mínimo de pasajeros para confirmar el vuelo.
    *   `status` (Enum: SCHEDULED, CONFIRMED, SOLD_OUT)
*   **`Booking`**:
    *   `id` (String)
    *   `flightId` (String)
    *   `passengerName` (String)
    *   `bookingDate` (LocalDateTime)
    *   `finalPrice` (double) - Precio calculado tras descuentos.

## 4. "Bad Smells" a Implementar Intencionalmente
Estos son los puntos que los alumnos deberán detectar y corregir:

1.  **Controladores con Lógica**: Validaciones de negocio (ej. "el nombre no puede estar vacío") dentro del `HttpHandler`.
2.  **Servicios Acoplados**:
    *   Instanciación directa de repositorios: `private FlightRepository flightRepo = new FlightRepositoryImpl();` (Sin Inyección de Dependencias).
    *   Lógica dispersa: Calcular el precio final mezclado con la lógica de guardado.
3.  **Repositorios que filtran detalles**:
    *   Métodos que devuelven el `Map` interno o listas mutables.
    *   Consultas ineficientes (traer todo y filtrar en memoria en el servicio).
4.  **Modelo Anémico**:
    *   Las entidades no validan su estado (se puede crear un vuelo con precio negativo).
    *   Toda la lógica está en los Servicios ("Transaction Script").

## 5. Funcionalidades a Implementar

### A. Listar Vuelos Disponibles (`GET /flights`)
*   **Lógica**: Retorna todos los vuelos futuros.
*   **Defecto**: El filtro de fecha se hace en el Controlador o Servicio, no en el Repositorio.

### B. Crear Reserva (`POST /bookings`)
*   **Input JSON**: `{ "flightId": "...", "passengerName": "..." }`
*   **Flujo en `BookingService`**:
    1.  Obtener `Flight` por ID (si no existe, lanzar RuntimeException).
    2.  Obtener `Rocket` usando `flight.getRocketId()`.
    3.  **Lógica de Capacidad**:
        *   Obtener todas las `Booking` del sistema.
        *   Filtrar las que coinciden con este `flightId`.
        *   Si `count >= rocket.capacity`, lanzar error "Flight Full".
    4.  **Lógica de Precio**:
        *   Precio base = `flight.basePrice`.
        *   Si `bookingDate` es > 30 días antes de `departureDate`, aplicar 10% descuento.
    5.  Crear objeto `Booking`.
    6.  Guardar en `BookingRepository`.
    7.  **Efecto Colateral (Side Effect)**:
        *   Recuperar de nuevo todas las reservas del vuelo.
        *   Si `count >= flight.minPassengers` y el estado es `SCHEDULED`, cambiar estado a `CONFIRMED`.
        *   Guardar `Flight` actualizado (si el repositorio no lo hace por referencia).

## 6. Plan de Trabajo (Paso a Paso)

1.  **Inicialización**: Crear `pom.xml` con dependencia de Jackson.
2.  **Capa de Datos**: Crear `RocketRepository`, `FlightRepository`, `BookingRepository` usando `HashMap` estáticos para persistencia entre requests. Pre-cargar algunos datos dummy (1 Cohete, 2 Vuelos).
3.  **Capa de Modelo**: Crear las clases POJO.
4.  **Capa de Servicio**: Implementar `BookingService` con toda la lógica "sucia".
5.  **Capa Web**: Implementar `AppServer` con `HttpServer` y handlers para los endpoints.
6.  **Verificación**: Probar con `curl` o Postman que se pueden crear reservas y se respeta la capacidad.

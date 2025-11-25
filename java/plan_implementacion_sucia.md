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
    *   `speed` (double) - Campo cosmético sin uso funcional (dead code intencional).
*   **`Flight`**:
    *   `id` (String)
    *   `rocketId` (String) - Referencia por ID (Bad smell: no referencia al objeto).
    *   `departureDate` (LocalDateTime)
    *   `basePrice` (double)
    *   `minPassengers` (int) - Mínimo de pasajeros para confirmar el vuelo (hardcoded a 5 en creación).
    *   `status` (Enum: SCHEDULED, CONFIRMED, SOLD_OUT, CANCELLED)
        *   `SCHEDULED`: Estado inicial al crear el vuelo.
        *   `CONFIRMED`: Se cambia automáticamente cuando se alcanza `minPassengers` reservas.
        *   `SOLD_OUT`: Se cambia automáticamente cuando se alcanza la capacidad máxima del cohete.
        *   `CANCELLED`: Se cambia automáticamente si a falta de 1 semana no se alcanza el mínimo de pasajeros.
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
    *   La lógica de descuentos compleja (con múltiples condiciones) está dispersa en el servicio en lugar de estar encapsulada en un objeto de dominio.
5.  **Lógica de Estado Compleja en Servicio**:
    *   El cambio de estados del vuelo (CONFIRMED, SOLD_OUT) se hace en el servicio de reservas, mezclando responsabilidades.
    *   No hay un proceso automático para cambiar vuelos a CANCELLED (se debería implementar con un job/scheduler, pero se omite intencionalmente).

## 5. Funcionalidades a Implementar

### A. Listar Vuelos Disponibles (`GET /flights`)
*   **Lógica**: Retorna todos los vuelos futuros.
*   **Filtros (Query Params)**:
    *   `?status=SCHEDULED|CONFIRMED` (Opcional): Filtrar por estado.
*   **Defecto**: El filtro de fecha y estado se hace en el Controlador o Servicio, no en el Repositorio.

### B. Listar Cohetes (`GET /rockets`)
*   **Lógica**: Retorna todos los cohetes del sistema.
*   **Defecto**: Exponer directamente la entidad de base de datos.

### B. Crear Reserva (`POST /bookings`)
*   **Input JSON**: `{ "flightId": "...", "passengerName": "..." }` (no debe incluir campo `id`). El campo `flightId` debe referenciar un vuelo existente.
*   **Flujo en `BookingService`**:
    1.  Obtener `Flight` por ID (si no existe, lanzar RuntimeException).
    2.  **Validar estado del vuelo**: Si el estado es `SOLD_OUT` o `CANCELLED`, lanzar error "Cannot book this flight".
    3.  Obtener `Rocket` usando `flight.getRocketId()`.
    4.  **Lógica de Capacidad**:
        *   Obtener todas las `Booking` del sistema.
        *   Filtrar las que coinciden con este `flightId`.
        *   Si `count >= rocket.capacity`, lanzar error "Flight Full".
    5.  **Lógica de Precio con Orden de Precedencia** (Bad Smell: Lógica compleja en servicio):
        *   Precio base = `flight.basePrice`.
        *   Calcular plazas disponibles: `availableSeats = rocket.capacity - currentBookings`
        *   Calcular plazas faltantes para mínimo: `seatsToMin = flight.minPassengers - currentBookings`
        *   Calcular días hasta salida: `daysUntilDeparture = ChronoUnit.DAYS.between(now, departureDate)`
        *   **Aplicar el primer descuento que coincida**:
            1.  Si `availableSeats == 1` → Sin descuento (precio = basePrice)
            2.  Si `seatsToMin == 1` → 30% descuento (precio = basePrice * 0.7)
            3.  Si `daysUntilDeparture > 180` (6 meses) → 10% descuento (precio = basePrice * 0.9)
            4.  Si `daysUntilDeparture >= 7 && daysUntilDeparture <= 30` → 20% descuento (precio = basePrice * 0.8)
            5.  En cualquier otro caso → Sin descuento (precio = basePrice)
    6.  Crear objeto `Booking`.
    7.  Guardar en `BookingRepository`.
    8.  **Efectos Colaterales (Side Effects)**:
        *   Recuperar de nuevo todas las reservas del vuelo.
        *   Si `count >= flight.minPassengers` y el estado es `SCHEDULED`, cambiar estado a `CONFIRMED`.
        *   Si `count >= rocket.capacity`, cambiar estado a `SOLD_OUT`.
        *   Guardar `Flight` actualizado (si el repositorio no lo hace por referencia).

### C. Consultar Reservas (`GET /bookings`)
*   **Filtros (Query Params)**:
    *   `?flightId=...`: Ver todas las reservas de un vuelo.
    *   `?passengerName=...`: Ver todas las reservas de un pasajero.
    *   Ambos filtros pueden combinarse.
*   **Lógica**:
    *   Si viene `flightId`, filtrar por vuelo.
    *   Si viene `passengerName`, filtrar por nombre.
    *   Si no viene nada, listar todas las reservas.
*   **Defecto (Bad Smell)**: El filtrado se hace **en el Controlador** (`BookingHandler`), no en el servicio. El controlador obtiene todas las reservas del servicio y luego filtra con `stream().filter(...)`. Esto viola la separación de responsabilidades: el controlador tiene lógica de negocio.

### D. Crear Cohete (`POST /rockets`)
*   **Input JSON**: `{ "name": "...", "capacity": 10, "speed": ... }` (no debe incluir campo `id`).
*   **Lógica**: Crear un nuevo cohete.
*   **Validaciones**:
    *   El nombre no puede estar vacío.
    *   La capacidad máxima no puede ser mayor de 10.
*   **Defectos (Bad Smells)**:
    1.  **No existe `RocketService`**: El `RocketHandler` accede directamente al `RocketRepository`, saltándose la capa de servicio.
    2.  **Validaciones en el Repositorio**: Implementar las validaciones de nombre y capacidad **dentro del `RocketRepository.save()`**, lanzando excepciones. El repositorio conoce reglas de negocio.

### E. Crear Vuelo (`POST /flights`)
*   **Input JSON**: `{ "rocketId": "...", "departureDate": "...", "basePrice": ... }` (no debe incluir campo `id`). El campo `rocketId` debe referenciar un cohete existente.
*   **Lógica**: Programar un nuevo vuelo con estado inicial `SCHEDULED` y `minPassengers` hardcoded a 5.
*   **Validaciones**:
    *   El `rocketId` debe existir en la base de datos.
    *   La fecha debe ser futura y no más allá de 1 año.
    *   El precio base debe existir (no null) y ser positivo (> 0).
*   **Defecto (Bad Smell)**: Implementar todas estas validaciones **en el `FlightService`** (Transaction Script), en lugar de que el objeto `Flight` o un Value Object `DepartureDate` se valide a sí mismo. El servicio tiene toda la lógica de validación.

## 6. Plan de Trabajo (Paso a Paso)

1.  **Inicialización**: Crear `pom.xml` con dependencia de Jackson.
2.  **Capa de Datos**: Crear `RocketRepository`, `FlightRepository`, `BookingRepository` usando `HashMap` estáticos para persistencia entre requests. Pre-cargar algunos datos dummy (1 Cohete, 2 Vuelos).
3.  **Capa de Modelo**: Crear las clases POJO.
4.  **Capa de Servicio**: Implementar `BookingService` con toda la lógica "sucia".
5.  **Capa Web**: Implementar `AppServer` con `HttpServer` y handlers para los endpoints.
6.  **Verificación**: Probar con `curl` o Postman que se pueden crear reservas y se respeta la capacidad.

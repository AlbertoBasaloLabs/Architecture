# Plan de Implementación Sucia - AstroBookings

## Propósito Educativo

Este documento describe las decisiones arquitectónicas **intencionalmente deficientes** implementadas en el proyecto AstroBookings. El objetivo es proporcionar un código base "sucio" que sirva como punto de partida para un taller de arquitectura de software, donde los participantes aprenderán a identificar y refactorizar estos "code smells" arquitectónicos.

## Arquitectura Actual (Intencionalmente Deficiente)

### Estructura de Capas

```
com.astrobookings
├── application/          # Capa de presentación (HTTP Handlers)
├── business/            # Capa de lógica de negocio (Services)
├── persistence/         # Capa de datos (Repositories)
└── model/               # Modelos de dominio (anémicos)
```

## "Bad Smells" Arquitectónicos Implementados

### 1. **Acoplamiento Directo entre Capas**

❌ **Problema**: Las clases instancian directamente sus dependencias

```java
public class BookingService {
    private FlightRepository flightRepository = new FlightRepository();
    private PaymentGateway paymentGateway = new PaymentGateway();
    private NotificationService notificationService = new NotificationService();
    // ...
}
```

**Por qué es malo**:
- Imposible testear unitariamente (no se pueden inyectar mocks)
- Alto acoplamiento entre capas
- Violación del principio de Inversión de Dependencias (SOLID)
- Difícil cambiar implementaciones

**Solución esperada**: Inyección de dependencias mediante constructor o interfaces

---

### 2. **Lógica de Negocio en la Capa de Servicio**

❌ **Problema**: Los servicios contienen toda la lógica de negocio

```java
public Booking createBooking(String flightId, String passengerName) {
    // Validaciones
    // Cálculo de precios
    // Procesamiento de pagos
    // Actualización de estados
    // Envío de notificaciones
    // Todo mezclado en un solo método
}
```

**Por qué es malo**:
- Servicios con múltiples responsabilidades (violación SRP)
- Lógica de negocio dispersa y difícil de testear
- Modelos anémicos sin comportamiento

**Solución esperada**: Domain-Driven Design con lógica en el dominio

---

### 3. **Modelos Anémicos**

❌ **Problema**: Las entidades solo tienen getters/setters, sin comportamiento

```java
public class Booking {
    private String id;
    private double finalPrice;
    // Solo getters y setters, sin lógica
}
```

**Por qué es malo**:
- El dominio no expresa las reglas de negocio
- Lógica dispersa en servicios
- Difícil mantener invariantes del dominio

**Solución esperada**: Rich Domain Models con comportamiento encapsulado

---

### 4. **Servicios Externos sin Abstracción**

❌ **Problema**: Llamadas directas a servicios externos (PaymentGateway, NotificationService)

```java
private PaymentGateway paymentGateway = new PaymentGateway();

public Booking createBooking(...) {
    String transactionId = paymentGateway.processPayment(bookingId, price);
    // ...
}
```

**Por qué es malo**:
- Acoplamiento a implementaciones concretas
- Imposible testear sin servicios reales
- Difícil cambiar de proveedor de pagos/notificaciones
- No hay puertos/adaptadores (Hexagonal Architecture)

**Solución esperada**: Puertos (interfaces) y adaptadores

---

### 5. **Procesamiento Síncrono de Operaciones Asíncronas**

❌ **Problema**: Las devoluciones y notificaciones se procesan síncronamente

```java
for (Booking booking : bookings) {
    paymentGateway.processRefund(booking.getPaymentTransactionId(), booking.getFinalPrice());
}
notificationService.notifyFlightCancelled(flightId, bookings);
```

**Por qué es malo**:
- Bloquea el hilo principal
- Si falla una devolución, se bloquea todo el proceso
- Mala experiencia de usuario (tiempos de respuesta largos)

**Solución esperada**: Procesamiento asíncrono con colas de mensajes o eventos

---

### 6. **Falta de Manejo de Transacciones**

❌ **Problema**: No hay gestión de transacciones en operaciones críticas

```java
bookingRepository.save(booking);
// Si falla aquí, el booking se guardó pero el pago no se procesó
String transactionId = paymentGateway.processPayment(bookingId, price);
```

**Por qué es malo**:
- Posibles inconsistencias de datos
- No hay rollback automático
- Problemas de integridad referencial

**Solución esperada**: Gestión de transacciones o patrón Saga

---

### 7. **Repositorios con Lógica de Negocio**

❌ **Problema**: Los repositorios están acoplados a la implementación en memoria

```java
public class BookingRepository {
    private static List<Booking> bookings = new ArrayList<>();
    // Implementación directa sin interfaz
}
```

**Por qué es malo**:
- Imposible cambiar de base de datos
- No hay abstracción del almacenamiento
- Difícil testear

**Solución esperada**: Interfaces de repositorio + implementaciones intercambiables

---

### 8. **Handlers con Lógica de Negocio**

❌ **Problema**: Los handlers HTTP contienen validaciones y lógica

```java
private void handlePost(HttpExchange exchange) throws IOException {
    // Parsing manual de JSON
    // Validaciones mezcladas con lógica HTTP
    // Manejo de errores específico del negocio
}
```

**Por qué es malo**:
- Mezcla de responsabilidades (HTTP + negocio)
- Difícil testear la lógica sin servidor HTTP
- Violación de SRP

**Solución esperada**: Controllers delgados que delegan al dominio

---

### 9. **Falta de Eventos de Dominio**

❌ **Problema**: Los cambios de estado no emiten eventos

```java
flight.setStatus(FlightStatus.CONFIRMED);
flightRepository.save(flight);
// Notificación acoplada directamente
notificationService.notifyFlightConfirmed(flightId, updatedBookings);
```

**Por qué es malo**:
- Acoplamiento entre agregados
- Difícil auditar cambios
- No hay trazabilidad de eventos

**Solución esperada**: Domain Events + Event Sourcing (opcional)

---

### 10. **Validaciones Dispersas**

❌ **Problema**: Las validaciones están en múltiples capas

```java
// En Handler
if (passengerName == null || passengerName.isEmpty()) {
    throw new IllegalArgumentException("Passenger name is required");
}

// En Service (otra vez)
if (passengerName == null || passengerName.isEmpty()) {
    throw new IllegalArgumentException("Passenger name is required");
}
```

**Por qué es malo**:
- Duplicación de código
- Inconsistencias en validaciones
- Difícil mantener reglas de negocio

**Solución esperada**: Value Objects con validaciones encapsuladas

---

## Nuevas Funcionalidades Implementadas (Versión Sucia)

### Gateway de Pagos (Simulado)

**Clase**: `PaymentGateway`

**Implementación sucia**:
- Instanciación directa en servicios (no inyección de dependencias)
- Simulación mediante `System.out.println` (no abstracción)
- Lógica de fallo hardcodeada (monto > $10,000)
- Sin manejo de reintentos

**Uso**:
```java
String transactionId = paymentGateway.processPayment(bookingId, price);
```

---

### Servicio de Notificaciones (Simulado)

**Clase**: `NotificationService`

**Implementación sucia**:
- Instanciación directa en servicios
- Simulación mediante `System.out.println`
- Sin cola de mensajes
- Procesamiento síncrono

**Uso**:
```java
notificationService.notifyFlightConfirmed(flightId, bookings);
notificationService.notifyFlightCancelled(flightId, bookings);
```

---

### Servicio de Cancelación de Vuelos

**Clase**: `FlightCancellationService`

**Implementación sucia**:
- Lógica de negocio en servicio (no en dominio)
- Procesamiento síncrono de devoluciones
- Sin manejo de errores robusto
- Acoplamiento directo a repositorios

**Endpoint**: `POST /admin/cancel-flights`

**Uso**:
```bash
curl -X POST http://localhost:8080/admin/cancel-flights
```

---

## Ejercicios Propuestos para el Taller

### Nivel 1: Refactoring Básico
1. Extraer interfaces para repositorios
2. Implementar inyección de dependencias
3. Crear Value Objects para validaciones

### Nivel 2: Domain-Driven Design
4. Convertir modelos anémicos en Rich Domain Models
5. Implementar agregados y entidades
6. Mover lógica de negocio al dominio

### Nivel 3: Arquitectura Hexagonal
7. Definir puertos (interfaces) para servicios externos
8. Implementar adaptadores para PaymentGateway y NotificationService
9. Separar dominio de infraestructura

### Nivel 4: Event-Driven Architecture
10. Implementar Domain Events
11. Crear Event Handlers desacoplados
12. Implementar procesamiento asíncrono

---

## Conclusión

Este código representa un **anti-patrón educativo** diseñado para enseñar arquitectura de software mediante la identificación y corrección de problemas reales. Cada "bad smell" tiene una solución arquitectónica específica que los participantes del taller aprenderán a implementar.

**Recuerda**: Este código es intencionalmente malo. No lo uses como referencia para proyectos reales. 😊

# Practical Exercise Summary  
**Domain:** Astro-Booking Backend (English terms: Rocket, Flight, Booking, Passenger, Capacity, Pricing, Discount…)

This exercise spans all 8 lessons and evolves a single backend application from a poorly structured layered design into a modular, hexagonal, DDD-based system.  
All implementation is done in TypeScript (or language of choice) with simple in-memory persistence.

| Lección | Evolución de la práctica                                            |
| ------- | ------------------------------------------------------------------- |
| 1       | Detectar problemas en arquitectura por capas de la app de reservas. |
| 2       | Aplicar SRP/DI/DIP para limpiar dependencias y responsabilidades.   |
| 3       | Convertir la funcionalidad principal a Hexagonal.                   |
| 4       | Introducir casos de uso reales.                                     |
| 5       | Descubrir bounded contexts del dominio espacial.                    |
| 6       | Modelado táctico DDD aplicado a reservas y flota.                   |
| 7       | Reorganizar en módulos: Bookings, Fleet, Pricing.                   |
| 8       | Decidir qué módulo es candidato a microservicio y por qué.          |

---

## Lesson 1 — Recognizing Architectural Decay
Start with an intentionally bad layered design:

- Controllers contain business logic.  
- Repositories leak technical details.  
- Domain rules are scattered (capacity checks, minimum passengers, pricing rules).  
- No clear boundaries or abstractions.

**Goal:** Identify code smells and limitations before any refactor.

---

## Lesson 2 — Applying SRP, DI and DIP
Refactor to remove obvious violations:

- Separate responsibilities: pricing, capacity validation, booking creation.  
- Introduce interfaces for persistence.  
- Apply Dependency Injection to enable testing.  
- Keep logic out of controllers.

**Goal:** Prepare the code for a more intentional structure.

---

## Lesson 3 — Hexagonal Architecture (Ports & Adapters)
Rewrite the core Booking flow using ports/adapters:

- Domain at the center (Booking, Rocket, Flight).  
- Port: `BookingRepository`.  
- Adapter: in-memory repository.  
- HTTP adapter as a thin entry point.

**Goal:** The domain becomes independent from frameworks and infrastructure.

---

## Lesson 4 — Use Cases as the Application Core
Introduce explicit use cases:

- Create `MakeBookingUseCase`.  
- Controllers delegate to use cases.  
- Domain holds rules (capacity, minimum passengers, pricing logic).  
- Persistence only accessed through ports.

**Goal:** Business flows become clear, testable and framework-agnostic.

---

## Lesson 5 — Discovering Bounded Contexts
Analyze the domain and separate meaning:

Suggested bounded contexts:

- **Bookings** — Handling reservations.  
- **Fleet** — Managing Rockets, Flights, capacity and schedules.  
- **Pricing** — Group discounts, advance-purchase discounts.  
- **Passengers** — Passenger data and grouping.

Create a Context Map describing relationships.

**Goal:** Understand the natural domain boundaries for later modularization.

---

## Lesson 6 — Tactical DDD (Entities, Value Objects, Aggregates)
Model the domain properly:

- **Entities:** Booking, Rocket, Flight.  
- **Value Objects:** Capacity, PassengerCount, BasePrice, Discount, BookingDate.  
- **Aggregate:** `BookingAggregate`, enforcing invariants.  
- **Domain Events:** `BookingConfirmed`, `FlightCapacityUpdated`.

**Goal:** Build a robust, expressive domain model that enforces rules internally.

---

## Lesson 7 — Modular Monolith Structure
Restructure the project into functional modules (not layers):

/modules  
	/bookings  
	/fleet  
	/pricing  
	/passengers

Each module contains its own:

- Domain  
- Use cases  
- Ports  
- Adapters  
- Module API

Introduce simple enforcement rules: no cross-module imports except through public APIs.

**Goal:** A clean monolith with strict boundaries and high cohesion.

---

## Lesson 8 — Preparing for Microservice Extraction
Evaluate which module could be extracted:

- Use signals: change frequency, scaling needs, deployment cadence.  
- Likely candidate: **Pricing** (volatile, experimental).  
- Draft a migration plan:
  - Define API contracts.  
  - Prepare an Anti-Corruption Layer.  
  - Move persistence behind a dedicated adapter.

**Goal:** Understand evolutionary architecture without prematurely distributing the system.

---

## Final Result
Across the 8 lessons, the “Rocket Travel Booking” exercise progresses from:

**Big Ball of Mud → Clean Components → Hexagonal → DDD Tactical → Modular Monolith → Ready-to-Extract Microservices**

The domain remains the same, but the architecture evolves step by step, reinforcing every lesson with concrete, cumulative practice.

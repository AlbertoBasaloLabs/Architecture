# Resumen del Ejercicio Práctico
**Dominio**: Backend de Astro-Reservas (Términos en inglés: Rocket, Flight, Booking, Passenger, Capacity, Pricing, Discount…)

Este ejercicio abarca las 8 lecciones y evoluciona una única aplicación backend desde un diseño de capas mal estructurado hacia un sistema modular, hexagonal y basado en DDD (Domain-Driven Design). Toda la implementación se realiza en Java con persistencia simple en memoria.

| Lección | Evolución de la práctica                                            |
| ------- | ------------------------------------------------------------------- |
| 1       | Reconociendo el Deterioro Arquitectónico.                            |
| 2       | Aplicar SRP/ISP/DIP para limpiar dependencias y responsabilidades.   |
| 3       | Convertir la funcionalidad principal a Hexagonal.                   |
| 4       | Introducir casos de uso reales.                                     |
| 5       | Descubrir bounded contexts del dominio espacial.                    |
| 6       | Modelado táctico DDD aplicado a reservas y flota.                   |
| 7       | Reorganizar en módulos: Bookings, Fleet, Pricing.                   |
| 8       | Decidir qué módulo es candidato a microservicio y por qué.          |

---

## Lección 1 Layered Architecture — Reconociendo el Deterioro Arquitectónico
Comenzar con un diseño de capas intencionalmente deficiente:

Señalar los problemas que se encuentran en el código.
- [ ] Las reglas de validación y de negocio están dispersas.
- [ ] Llamadas a repositorio en el controlador.
- [ ] Acoplamientos por constructores de instancias.
- [ ] Las excepciones no siguen una estructura consistente.
- [ ] No hay una responsabilidad clara por capa.

## Lección 2 SOLID — Aplicando SRP, ISP y DIP para limpiar dependencias y responsabilidades
Realizar refactor para eliminar violaciones obvias:

- [ ] Usar DTOs para validar estructura, y llevar a nivel de servicio los valores y reglas de negocio
- [ ] Usar excepciones claras y consistentes. (Valorar usar una jerarquía de excepciones)
- [ ] Mover artefactos de infrastructura fuera de la capa de negocio
- [ ] Introducir interfaces para persistence.
- Aplicar Dependency Injection para habilitar testing.

Objetivo: Preparar el código para una arquitectura hexagonal.

<!-- Work in progress: -->

## Lección 3 — Arquitectura Hexagonal (Ports & Adapters)
Reescribir el flujo central de Booking usando ports/adapters:

El Domain en el centro (Booking, Rocket, Flight).

Port: BookingRepository.

Adapter: repositorio en memoria (in-memory repository).

HTTP adapter como un punto de entrada ligero (thin entry point).

Objetivo: El Domain se vuelve independiente de frameworks e infrastructure.

## Lección 4 — Casos de Uso como el Núcleo de la Aplicación
Introducir casos de uso explícitos:

Crear MakeBookingUseCase.

Los Controllers delegan en casos de uso.

El Domain contiene las reglas (capacity, minimum passengers, pricing logic).

La Persistence solo se accede a través de ports.

Objetivo: Los flujos de negocio se vuelven claros, testeables y agnósticos al framework.

## Lección 5 — Descubriendo Bounded Contexts
Analizar el dominio y separar el significado:

Bounded contexts sugeridos:

Bookings — Manejo de reservas.

Fleet — Gestión de Rockets, Flights, capacity y horarios.

Pricing — Descuentos por grupo, descuentos por compra anticipada (advance-purchase discounts).

Passengers — Datos de pasajeros y agrupación.

Crear un Context Map que describa las relaciones.

Objetivo: Comprender los límites naturales del dominio para una modularización posterior.

## Lección 6 — DDD Táctico (Entities, Value Objects, Aggregates)
Modelar el dominio correctamente:

Entities: Booking, Rocket, Flight.

Value Objects: Capacity, PassengerCount, BasePrice, Discount, BookingDate.

Aggregate: BookingAggregate, que impone invariants.

Domain Events: BookingConfirmed, FlightCapacityUpdated.

Objetivo: Construir un modelo de dominio robusto y expresivo que aplique reglas internamente.

## Lección 7 — Estructura de Monolito Modular
Reestructurar el proyecto en módulos funcionales (no capas):

/modules /bookings /fleet /pricing /passengers

Cada módulo contiene su propio:

Domain

Use cases

Ports

Adapters

Module API

Introducir reglas de aplicación simples: no se permiten importaciones cruzadas entre módulos, excepto a través de APIs públicas.

Objetivo: Un monolito limpio con límites estrictos y alta cohesión.

## 	Lección 8 — Preparación para la Extracción de Microservicios
Evaluar qué módulo podría ser extraído:

Usar señales: frecuencia de cambio, necesidades de scaling, cadencia de deployment.

Candidato probable: Pricing (volátil, experimental).

Bosquejar un plan de migración:

Definir API contracts.

Preparar una Anti-Corruption Layer.

Mover la persistence detrás de un adapter dedicado.

Objetivo: Comprender la arquitectura evolutiva sin distribuir prematuramente el sistema.

## Resultado Final
A lo largo de las 8 lecciones, el ejercicio "Rocket Travel Booking" progresa de:

Big Ball of Mud → Clean Components → Hexagonal → DDD Tactical → Modular Monolith → Ready-to-Extract Microservices

El dominio sigue siendo el mismo, pero la arquitectura evoluciona paso a paso, reforzando cada lección con práctica concreta y acumulativa.
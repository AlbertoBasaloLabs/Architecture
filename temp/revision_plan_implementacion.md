# Revisión del Plan de Implementación vs Implementación Real

## Resumen Ejecutivo

Este documento identifica las **incoherencias** y **funcionalidades faltantes** encontradas al comparar el plan de implementación (`plan_implementacion_sucia.md`) con la implementación real del código y las pruebas e2e.

---

## 1. Incoherencias Detectadas

### 1.1 Validación de Capacidad de Cohetes (Sección 5.D)

**Plan dice:**
> Validación: La capacidad máxima no puede ser mayor de 10.
> Defecto (Bad Smell): Implementar esta validación **dentro del `RocketRepository.save()`**

**Implementación real:**
- ✅ **CORRECTO**: La validación está en `RocketRepository.save()` líneas 25-27
- ✅ Lanza `IllegalArgumentException` si `capacity > 10`

**Estado:** ✅ **Coherente con el plan**

---

### 1.2 Validación de Nombre de Cohete (No documentada en el plan)

**Plan dice:**
- No menciona validación del campo `name` en cohetes

**Implementación real:**
- `RocketRepository.save()` valida que el nombre no sea nulo o vacío (líneas 21-23)

**Pruebas e2e:**
- `rockets.http` línea 20-26: Prueba crear cohete sin nombre (espera error)

**Estado:** ⚠️ **FUNCIONALIDAD NO DOCUMENTADA EN EL PLAN**

**Recomendación:** Actualizar el plan para documentar esta validación como otro "bad smell" intencional (validación en repositorio).

---

### 1.3 Validación de Precio Base en Vuelos (Sección 5.E)

**Plan dice:**
> Validación: La fecha debe ser futura y no más allá de 1 año. El precio base debe existir y ser positivo.
> Defecto (Bad Smell): Implementar esta validación **en el `FlightService`**

**Implementación real:**
- ✅ **CORRECTO**: Validaciones en `FlightService.createFlight()`:
  - Líneas 50-52: Valida que `basePrice` no sea `null`
  - Líneas 54-57: Valida que `basePrice > 0`
  - Líneas 59-62: Valida fecha futura
  - Líneas 64-68: Valida fecha no más de 1 año adelante

**Estado:** ✅ **Coherente con el plan**

---

### 1.4 Validación de RocketId en Vuelos (No documentada explícitamente)

**Plan dice:**
> Input JSON: `{ "rocketId": "...", ... }` El campo `rocketId` debe referenciar un cohete existente.

**Implementación real:**
- `FlightService.createFlight()` líneas 40-47: Valida que el cohete exista en la base de datos

**Pruebas e2e:**
- `flights.http` líneas 30-38: Prueba crear vuelo con `rocketId` inválido

**Estado:** ⚠️ **VALIDACIÓN IMPLEMENTADA PERO NO EXPLÍCITA EN EL PLAN**

**Recomendación:** Aclarar en el plan que esta validación debe hacerse en el servicio.

---

### 1.5 Filtrado de Reservas (Sección 5.C)

**Plan dice:**
> Lógica:
> - Si viene `flightId`, filtrar por vuelo.
> - Si viene `passengerName`, filtrar por nombre.
> - Si no viene nada, error o listar todo (decisión de implementación).
> Defecto: Filtrado ineficiente en memoria (`stream().filter(...)`) dentro del servicio.

**Implementación real:**
- ❌ **INCOHERENTE**: El filtrado NO está en el servicio, está en el **controlador** (`BookingHandler.handleGet()` líneas 38-43)
- El servicio solo tiene `findAllBookings()` que devuelve todo
- El controlador hace el filtrado con streams

**Pruebas e2e:**
- `bookings.http` líneas 21-35: Pruebas de filtrado por `flightId`, `passengerName` y ambos

**Estado:** ❌ **INCOHERENTE - El bad smell está en el lugar equivocado**

**Impacto:** Esto es **peor** que lo planeado desde el punto de vista arquitectónico (el controlador tiene lógica de negocio), lo cual puede ser bueno para el taller, pero debería documentarse.

**Recomendación:** 
- **Opción A:** Actualizar el plan para reflejar que el filtrado está en el controlador (bad smell aún más grave)
- **Opción B:** Mover el filtrado al servicio según el plan original

---

### 1.6 Manejo de Errores en Bookings (No documentado)

**Plan dice:**
- No especifica códigos de estado HTTP para errores

**Implementación real:**
- `BookingHandler.handlePost()` líneas 62-71:
  - `IllegalArgumentException` → 400 Bad Request
  - `RuntimeException` (Flight not found, Flight is full) → 400 Bad Request
  - Otros errores → 500 Internal Server Error

**Pruebas e2e:**
- `bookings.http` líneas 39-92: Múltiples casos de error (flightId faltante, inválido, passengerName vacío, capacidad excedida)

**Estado:** ⚠️ **FUNCIONALIDAD NO DOCUMENTADA**

**Recomendación:** Documentar en el plan el manejo de errores HTTP como parte de la implementación.

---

### 1.7 Campo `minPassengers` en Flight (Hardcoded)

**Plan dice:**
> minPassengers (int) - Mínimo de pasajeros para confirmar el vuelo.

**Implementación real:**
- `FlightService.createFlight()` línea 77: Hardcoded a `5`
- El plan no especifica si este valor debe ser configurable o hardcoded

**Pruebas e2e:**
- No hay pruebas específicas para validar el cambio de estado a `CONFIRMED` cuando se alcanza `minPassengers`

**Estado:** ⚠️ **AMBIGUO EN EL PLAN**

**Recomendación:** Aclarar en el plan si `minPassengers` debe ser:
- Un parámetro en el JSON de creación de vuelo
- Un valor hardcoded por defecto
- Un valor configurable del sistema

---

### 1.8 Ausencia de Servicio para Rockets (Sección 5.D)

**Plan dice:**
> Estructura del Proyecto:
> - controller (Capa de Entrada)
> - service (Lógica de Negocio)
> - data (Acceso a Datos)

**Implementación real:**
- ❌ **INCOHERENTE**: `RocketHandler` accede directamente a `RocketRepository` (línea 15)
- No existe `RocketService`

**Estado:** ⚠️ **INCONSISTENCIA ARQUITECTURAL NO DOCUMENTADA**

**Recomendación:** 
- **Opción A:** Documentar en el plan que Rockets NO tiene capa de servicio como otro bad smell
- **Opción B:** Crear `RocketService` para mantener consistencia arquitectural

---

## 2. Funcionalidades Faltantes

### 2.1 Pruebas e2e para `minPassengers` y cambio de estado a `CONFIRMED`

**Plan dice:**
> Efecto Colateral (Side Effect):
> - Recuperar de nuevo todas las reservas del vuelo.
> - Si `count >= flight.minPassengers` y el estado es `SCHEDULED`, cambiar estado a `CONFIRMED`.

**Implementación real:**
- ✅ Código implementado en `BookingService.createBooking()` líneas 56-62

**Pruebas e2e:**
- ❌ **FALTANTE**: No hay pruebas que verifiquen este comportamiento
- No hay prueba que cree exactamente 5 bookings (minPassengers) y verifique el cambio de estado

**Recomendación:** Agregar prueba e2e que:
1. Cree un vuelo nuevo
2. Cree 4 bookings (estado debe seguir `SCHEDULED`)
3. Cree la 5ta booking
4. Verifique que el vuelo cambió a `CONFIRMED`

---

### 2.2 Pruebas e2e para estado `SOLD_OUT`

**Plan dice:**
> status (Enum: SCHEDULED, CONFIRMED, SOLD_OUT)

**Implementación real:**
- El enum `FlightStatus` tiene estos 3 valores
- ❌ **FALTANTE**: No hay lógica que cambie el estado a `SOLD_OUT` cuando se alcanza la capacidad máxima

**Pruebas e2e:**
- No hay pruebas para `SOLD_OUT`

**Estado:** ❌ **FUNCIONALIDAD NO IMPLEMENTADA**

**Recomendación:** 
- **Opción A:** Implementar lógica en `BookingService` para cambiar estado a `SOLD_OUT` cuando `count >= rocket.capacity`
- **Opción B:** Eliminar `SOLD_OUT` del enum si no se va a usar
- **Opción C:** Documentar en el plan que `SOLD_OUT` es un estado futuro no implementado

---

### 2.3 Filtrado de vuelos por fecha futura (Sección 5.A)

**Plan dice:**
> Lógica: Retorna todos los vuelos futuros.

**Implementación real:**
- ✅ `FlightService.getAvailableFlights()` línea 24: Filtra por `departureDate.isAfter(LocalDateTime.now())`

**Pruebas e2e:**
- ⚠️ **LIMITADO**: Solo prueba GET sin filtro y con filtro de status
- No hay prueba que verifique que NO se devuelven vuelos pasados

**Recomendación:** Agregar datos dummy con vuelos pasados y verificar que no aparecen en el GET.

---

### 2.4 Validación de descuento por reserva anticipada

**Plan dice:**
> Si `bookingDate` es > 30 días antes de `departureDate`, aplicar 10% descuento.

**Implementación real:**
- ✅ `BookingService.createBooking()` líneas 38-42: Implementado correctamente

**Pruebas e2e:**
- ❌ **FALTANTE**: No hay prueba que verifique el cálculo del descuento
- No hay vuelo con fecha > 30 días en el futuro para probar esto

**Recomendación:** Agregar:
1. Vuelo dummy con fecha 60 días en el futuro
2. Prueba que cree booking y verifique que `finalPrice = basePrice * 0.9`

---

### 2.5 Validación de campo `speed` en Rocket

**Plan dice:**
> speed (double) - Para cálculos cosméticos.

**Implementación real:**
- El campo existe en el modelo
- ❌ **NO SE USA** en ninguna parte del código

**Pruebas e2e:**
- No hay pruebas relacionadas con `speed`

**Estado:** ⚠️ **CAMPO MUERTO (DEAD CODE)**

**Recomendación:** Documentar que `speed` es un campo cosmético sin validación ni uso funcional.

---

## 3. Resumen de Recomendaciones

### Prioridad Alta (Incoherencias que afectan el taller)

1. **Actualizar Sección 5.C del plan**: Documentar que el filtrado de bookings está en el controlador, no en el servicio (bad smell más grave)
2. **Decidir sobre `SOLD_OUT`**: Implementar la funcionalidad o eliminar del enum
3. **Crear pruebas e2e para `minPassengers`**: Validar el cambio de estado a `CONFIRMED`

### Prioridad Media (Mejoras de documentación)

4. **Documentar validación de nombre en Rockets** (Sección 5.D)
5. **Documentar ausencia de `RocketService`** como bad smell intencional
6. **Aclarar comportamiento de `minPassengers`** (hardcoded vs configurable)
7. **Documentar manejo de errores HTTP** en cada endpoint

### Prioridad Baja (Mejoras de cobertura de pruebas)

8. **Agregar pruebas e2e para descuento por reserva anticipada**
9. **Agregar pruebas e2e para filtrado de vuelos pasados**
10. **Documentar campo `speed` como cosmético**

---

## 4. Conclusión

La implementación está **mayormente alineada** con el plan, con los "bad smells" intencionales correctamente implementados. Sin embargo, hay algunas **incoherencias menores** y **funcionalidades no documentadas** que deberían actualizarse en el plan para reflejar la realidad del código.

El caso más significativo es el **filtrado de bookings en el controlador** en lugar del servicio, lo cual es arquitectónicamente peor (y por tanto mejor para el taller de refactorización), pero debería estar documentado explícitamente.

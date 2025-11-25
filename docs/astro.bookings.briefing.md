# AstroBookings - Briefing Funcional

## 1. Descripción General

**AstroBookings** es una aplicación de reservas para viajes espaciales. Permite a los usuarios consultar vuelos disponibles, realizar reservas de plazas en cohetes espaciales, y gestionar la información de cohetes y vuelos.

El sistema gestiona la capacidad de los cohetes, aplica políticas de precios con descuentos por reserva anticipada, y confirma automáticamente los vuelos cuando se alcanza un mínimo de pasajeros.

---

## 2. Entidades del Dominio

### 2.1 Cohete (Rocket)
Representa una nave espacial que puede transportar pasajeros.

**Atributos:**
- **Identificador único**: Código que identifica al cohete
- **Nombre**: Nombre del cohete (ej. "Falcon 9")
- **Capacidad**: Número máximo de pasajeros que puede transportar
- **Velocidad**: Velocidad del cohete (dato informativo)

**Reglas de Negocio:**
- El nombre es obligatorio
- La capacidad máxima permitida es de 10 pasajeros

---

### 2.2 Vuelo (Flight)
Representa un viaje espacial programado en un cohete específico.

**Atributos:**
- **Identificador único**: Código que identifica al vuelo
- **Cohete asignado**: Referencia al cohete que realizará el vuelo
- **Fecha de salida**: Fecha y hora programada de despegue
- **Precio base**: Precio estándar del billete
- **Mínimo de pasajeros**: Número mínimo de reservas para confirmar el vuelo (valor estándar: 5)
- **Estado**: Situación actual del vuelo

**Estados Posibles:**
- **SCHEDULED** (Programado): Estado inicial cuando se crea el vuelo
- **CONFIRMED** (Confirmado): El vuelo ha alcanzado el mínimo de pasajeros requerido
- **SOLD_OUT** (Agotado): El vuelo ha alcanzado su capacidad máxima
- **CANCELLED** (Cancelado): El vuelo ha sido cancelado automáticamente

**Reglas de Negocio:**
- La fecha de salida debe ser futura (posterior a la fecha actual)
- La fecha de salida no puede ser superior a 1 año desde la fecha actual
- El precio base debe ser un valor positivo mayor que cero
- El cohete asignado debe existir en el sistema
- El vuelo se crea inicialmente en estado SCHEDULED

**Transiciones de Estado Automáticas:**

1. **SCHEDULED → CONFIRMED**: Cuando el número de reservas alcanza el mínimo de pasajeros (5)
2. **SCHEDULED/CONFIRMED → SOLD_OUT**: Cuando se crea la última reserva que alcanza la capacidad máxima del cohete
3. **SCHEDULED → CANCELLED**: Si a falta de una semana del vuelo no se ha alcanzado el mínimo de pasajeros

**Restricciones de Reserva por Estado:**
- **SCHEDULED**: Se permiten nuevas reservas
- **CONFIRMED**: Se permiten nuevas reservas (hasta alcanzar capacidad máxima)
- **SOLD_OUT**: NO se permiten nuevas reservas (capacidad completa)
- **CANCELLED**: NO se permiten nuevas reservas (vuelo cancelado)

---

### 2.3 Reserva (Booking)
Representa la reserva de un pasajero en un vuelo específico.

**Atributos:**
- **Identificador único**: Código que identifica la reserva
- **Vuelo**: Referencia al vuelo reservado
- **Nombre del pasajero**: Nombre completo del pasajero
- **Fecha de reserva**: Fecha y hora en que se realizó la reserva
- **Precio final**: Precio calculado tras aplicar descuentos

**Reglas de Negocio:**
- El vuelo debe existir en el sistema
- El vuelo debe estar en estado SCHEDULED o CONFIRMED (no se permiten reservas en vuelos CANCELLED o SOLD_OUT)
- El nombre del pasajero es obligatorio
- No se pueden realizar más reservas que la capacidad del cohete asignado al vuelo

**Cálculo de Precio Final:**

El precio final se calcula aplicando descuentos sobre el precio base del vuelo. Las reglas de descuento se evalúan en el siguiente orden de precedencia (se aplica el primer descuento que coincida):

1. **Sin descuento** (0%): Si solo queda 1 plaza disponible en el vuelo
2. **Descuento por alcanzar mínimo** (30%): Si solo falta 1 plaza para alcanzar el mínimo de pasajeros
3. **Descuento por reserva muy anticipada** (10%): Si faltan más de 6 meses para el vuelo
4. **Descuento por reserva tardía** (20%): Si falta entre 1 mes y 1 semana para el vuelo
5. **Sin descuento** (0%): En cualquier otro caso

> **Nota**: Solo se aplica un descuento por reserva. Si se cumplen múltiples condiciones, prevalece la primera en el orden de precedencia.

---

## 3. Funcionalidades

### 3.1 Gestión de Cohetes

#### Listar Cohetes
**Endpoint:** `GET /rockets`

**Descripción:** Obtiene la lista completa de cohetes disponibles en el sistema.

**Respuesta:** Lista de cohetes con todos sus atributos.

---

#### Crear Cohete
**Endpoint:** `POST /rockets`

**Descripción:** Registra un nuevo cohete en el sistema.

**Datos de Entrada:**
- Nombre (obligatorio)
- Capacidad (obligatorio, máximo 10)
- Velocidad (opcional)

**Validaciones:**
- El nombre no puede estar vacío
- La capacidad no puede exceder 10 pasajeros

**Respuesta:** Datos del cohete creado con su identificador asignado.

---

### 3.2 Gestión de Vuelos

#### Listar Vuelos Disponibles
**Endpoint:** `GET /flights`

**Descripción:** Obtiene la lista de vuelos futuros (con fecha de salida posterior a la fecha actual).

**Filtros Opcionales:**
- `status`: Filtrar por estado del vuelo (SCHEDULED, CONFIRMED, SOLD_OUT)

**Respuesta:** Lista de vuelos que cumplen los criterios de búsqueda.

---

#### Crear Vuelo
**Endpoint:** `POST /flights`

**Descripción:** Programa un nuevo vuelo espacial.

**Datos de Entrada:**
- Identificador del cohete (obligatorio)
- Fecha de salida (obligatorio)
- Precio base (obligatorio)

**Validaciones:**
- El cohete debe existir en el sistema
- La fecha de salida debe ser futura
- La fecha de salida no puede ser superior a 1 año
- El precio base debe ser mayor que cero

**Comportamiento:**
- El vuelo se crea con estado SCHEDULED
- El mínimo de pasajeros se establece en 5 por defecto

**Respuesta:** Datos del vuelo creado con su identificador asignado.

---

### 3.3 Gestión de Reservas

#### Crear Reserva
**Endpoint:** `POST /bookings`

**Descripción:** Realiza una reserva de un pasajero en un vuelo específico.

**Datos de Entrada:**
- Identificador del vuelo (obligatorio)
- Nombre del pasajero (obligatorio)

**Validaciones:**
- El vuelo debe existir
- El nombre del pasajero no puede estar vacío
- El vuelo no debe haber alcanzado su capacidad máxima

**Lógica de Negocio:**

1. **Control de Capacidad:**
   - Se verifica el número actual de reservas del vuelo
   - Si se ha alcanzado la capacidad del cohete, se rechaza la reserva

2. **Cálculo de Precio:**
   - Se parte del precio base del vuelo
   - Si la reserva se realiza con más de 30 días de antelación respecto a la fecha de salida, se aplica un 10% de descuento

3. **Confirmación Automática del Vuelo:**
   - Después de crear la reserva, se verifica el número total de reservas
   - Si se alcanza o supera el mínimo de pasajeros (5) y el vuelo está en estado SCHEDULED, el estado cambia automáticamente a CONFIRMED

**Respuesta:** Datos de la reserva creada incluyendo el precio final calculado.

---

#### Consultar Reservas
**Endpoint:** `GET /bookings`

**Descripción:** Obtiene la lista de reservas según criterios de búsqueda.

**Filtros Opcionales:**
- `flightId`: Ver todas las reservas de un vuelo específico
- `passengerName`: Ver todas las reservas de un pasajero específico
- Ambos filtros pueden combinarse
- Sin filtros: devuelve todas las reservas del sistema

**Respuesta:** Lista de reservas que cumplen los criterios de búsqueda.

---

## 4. Reglas de Negocio Globales

### 4.1 Política de Descuentos

Los descuentos se aplican según las siguientes condiciones, evaluadas en orden de precedencia:

1. **Sin descuento (0%)**: Si solo queda 1 plaza disponible
2. **Descuento por alcanzar mínimo (30%)**: Si solo falta 1 plaza para el mínimo de pasajeros
3. **Descuento por reserva muy anticipada (10%)**: Si faltan más de 6 meses
4. **Descuento por reserva tardía (20%)**: Si falta entre 1 mes y 1 semana
5. **Sin descuento (0%)**: En cualquier otro caso

**Importante**: Solo se aplica un descuento por reserva (el primero que coincida según el orden de precedencia).

### 4.2 Gestión de Estados de Vuelo

**Estados y Transiciones:**
- Los vuelos se crean en estado **SCHEDULED**
- Cuando se alcanza el mínimo de pasajeros (5), el estado cambia a **CONFIRMED**
- Cuando se alcanza la capacidad máxima del cohete, el estado cambia a **SOLD_OUT**
- Si a falta de 1 semana del vuelo no se alcanza el mínimo de pasajeros, el estado cambia a **CANCELLED**

**Restricciones:**
- No se permiten reservas en vuelos con estado **SOLD_OUT** o **CANCELLED**
- Los vuelos en estado **SCHEDULED** o **CONFIRMED** aceptan nuevas reservas (hasta alcanzar capacidad)

### 4.3 Control de Capacidad
- No se permiten más reservas que la capacidad del cohete asignado
- El control se realiza en tiempo real al crear cada reserva

### 4.4 Restricciones Temporales
- Solo se muestran vuelos con fecha de salida futura
- Los vuelos solo pueden programarse hasta 1 año en el futuro

---

## 5. Casos de Uso Principales

### Caso de Uso 1: Reservar un Viaje Espacial
1. El usuario consulta los vuelos disponibles
2. El usuario selecciona un vuelo de interés
3. El usuario proporciona su nombre y confirma la reserva
4. El sistema verifica la disponibilidad
5. El sistema calcula el precio (aplicando descuentos si corresponde)
6. El sistema crea la reserva
7. El sistema verifica si se alcanzó el mínimo de pasajeros y actualiza el estado del vuelo si es necesario
8. El usuario recibe la confirmación con el precio final

### Caso de Uso 2: Consultar Reservas de un Vuelo
1. El administrador selecciona un vuelo
2. El sistema muestra todas las reservas asociadas a ese vuelo
3. El administrador puede ver el número de plazas ocupadas y disponibles

### Caso de Uso 3: Programar un Nuevo Vuelo
1. El administrador selecciona un cohete disponible
2. El administrador especifica la fecha de salida y el precio base
3. El sistema valida que la fecha sea válida (futura y dentro del año)
4. El sistema crea el vuelo en estado SCHEDULED
5. El vuelo queda disponible para reservas

---

## 6. Datos de Ejemplo

### Cohete Predefinido
- **ID**: r1
- **Nombre**: Falcon 9
- **Capacidad**: 10 pasajeros
- **Velocidad**: 25000 km/h

### Escenarios de Prueba Recomendados

1. **Reserva con descuento**: Crear un vuelo con fecha 60 días en el futuro y verificar que se aplica el 10% de descuento

2. **Confirmación automática**: Crear 5 reservas en un mismo vuelo y verificar que el estado cambia a CONFIRMED

3. **Control de capacidad**: Intentar crear 11 reservas en un vuelo con cohete de capacidad 10 y verificar que la última se rechaza

4. **Filtrado de vuelos**: Verificar que solo se muestran vuelos futuros, no vuelos con fecha pasada

---

## 7. Códigos de Error HTTP

### Errores de Validación (400 Bad Request)
- Campo obligatorio faltante
- Formato de datos inválido
- Violación de reglas de negocio (capacidad excedida, fecha inválida, etc.)
- Referencia a entidad inexistente (cohete o vuelo no encontrado)

### Errores del Servidor (500 Internal Server Error)
- Errores inesperados del sistema

---

## 8. Notas Adicionales

- El sistema utiliza una base de datos en memoria (los datos se pierden al reiniciar)
- Los identificadores se generan automáticamente al crear entidades
- Las fechas deben proporcionarse en formato ISO 8601 (ej. "2025-12-01T10:00:00")
- Todos los endpoints devuelven y aceptan datos en formato JSON

# AstroBookings — Briefing

AstroBookings es una aplicación de reservas para viajes espaciales. Gestiona cohetes, vuelos y reservas controlando capacidad, precios y estados de los vuelos. 

## Objetivo del sistema
- Permitir reservar viajes espaciales de forma simple.  
- Garantizar reglas de negocio de capacidad y estados.  
- Aplicar descuentos automáticos según contexto.  
- Mostrar vuelos futuros y gestionar reservas asociadas.  

## Entidades principales

### Rocket
- Nave espacial que transporta pasajeros.  
- Tiene nombre (obligatorio), capacidad (máx. 10), velocidad (opcional).  

### Flight
- Viaje espacial programado con un cohete.  
- Tiene fecha futura, precio base, mínimo de pasajeros (5 por defecto).  
- Estados: `SCHEDULED`, `CONFIRMED`, `SOLD_OUT`, `CANCELLED`.  
- Validaciones de fecha y precio.  
- Cambio automático de estado según reservas.  

### Booking
- Reserva de un pasajero en un vuelo.  
- Incluye nombre del pasajero y precio final.  
- Solo se permite reservar si el vuelo no está lleno o cancelado.  

## Lógica clave

### Capacidad
- Un vuelo no puede superar la capacidad del cohete.  
- Al llegar al límite → estado `SOLD_OUT`.  

### Confirmación automática
- Si un vuelo alcanza el mínimo de pasajeros → `CONFIRMED`.  

### Cancelación automática
- Si falta 1 semana y no se llegó al mínimo → `CANCELLED`.  

### Política de descuentos
Reglas aplicadas en orden de precedencia (solo una por reserva):  
1. Última plaza → 0%  
2. Falta 1 para el mínimo → 30%  
3. Más de 6 meses antes → 10%  
4. Entre 1 mes y 1 semana → 20%  
5. Resto → 0%  

## Funcionalidades principales

#### Gestión de Rockets
- Listar cohetes.  
- Crear cohetes con nombre y capacidad válida.  

### Gestión de Flights
- Listar vuelos futuros (filtro por estado).  
- Crear vuelos con fecha futura y precio > 0 con estado `SCHEDULED`.  

### Gestión de Bookings
- Crear reservas en vuelos válidos calculando precio con descuentos.  
- Consultar reservas por vuelo o pasajero.  


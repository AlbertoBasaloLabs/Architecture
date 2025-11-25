# Lección 6 — DDD Táctico: entidades, value objects, agregados y domain events

## 1. CONNECT (Conectar)
- Actividad inicial: plantear la pregunta:  
  “¿Qué diferencia real hay entre un objeto ‘con datos’ y un objeto que representa una regla del negocio?”
- Invitación: compartir ejemplos donde una entidad terminó siendo solo un ‘DTO gordo’.
- Objetivo: que el alumno sienta la necesidad de modelos que expresen reglas, no solo estructuras.

## 2. CONCEPT (Conceptos)
### Conceptos clave del DDD táctico:
- **Entidad**: identidad estable, ciclo de vida y reglas que dependen de su estado.
- **Value Object**: sin identidad, inmutable, expresa conceptos puros del dominio (dinero, fecha, rango, porcentaje).
- **Agregado**: agrupación lógica que garantiza invariantes; tiene una raíz que controla la consistencia.
- **Domain Events**: hechos del negocio que el modelo emite cuando ocurre algo relevante.  

### Reglas de modelado:
- El comportamiento debe vivir dentro del modelo, no fuera.
- Los invariantes se protegen desde el agregado.
- Los value objects reducen duplicación y errores conceptuales.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio paso a paso:
  - Tomar un mini-caso: “crear pedido con líneas y total”.
  - Identificar entidades reales.
  - Diseñar value objects adecuados (por ejemplo, `Precio`, `Cantidad`, `Total`).
  - Definir el agregado raíz (“Pedido”) y sus invariantes.
  - Emitir un domain event (“PedidoCreado”) desde el agregado.
- Validación: comprobar cómo el modelo evita estados inválidos sin necesidad de lógica en servicios.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: el DDD táctico crea modelos robustos que capturan reglas del negocio y reducen errores.
- Beneficio inmediato: menos lógica dispersa, más expresividad y menos bugs silenciosos.
- Pregunta final: “¿Qué regla de negocio de tu proyecto podría convertirse mañana en un value object o en un invariante de agregado?”

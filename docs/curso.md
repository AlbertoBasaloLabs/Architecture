# Arquitectura Software (20h, 4 sesiones × 2 lecciones)
De lo simple y frágil a lo modular, expresivo y escalable.

## SESIÓN 1 — Por qué romper con las capas (Problemas + Principios)
**Tema general:** Entender por qué las arquitecturas tradicionales colapsan y qué principios permiten construir sistemas más robustos.

### 1. Layered Architecture 

Señales de decadencia: problemas reales de la arquitectura por capas

Arquitectura en capas, síntomas, acoplamientos ocultos, big ball of mud, problemas de testeo y de expresividad del dominio.

### 2. Principios SOLID

Fundamentos para comenzar a invertir dependencias y separar responsabilidades.

> Por qué la arquitectura en capas falla y qué principios permiten mejorar.

<!-- Work in progress: -->

---

## SESIÓN 2 — Hexagonal en acción (Hexagonal + Use Cases)
**Tema general:** Explorar la arquitectura hexagonal como alternativa práctica a las capas y aplicarla mediante casos de uso reales.

### 3. Arquitectura Hexagonal: puertos, adaptadores y dirección de dependencias
Separación entre dominio, aplicación e infraestructura, puertos como contratos, adaptadores web y de persistencia.

### 4. Casos de uso: de controlador a dominio y persistencia
Implementación paso a paso de un caso de uso en arquitectura limpia/hexagonal.

> Cómo la arquitectura hexagonal resuelve esos problemas y se aplica a casos de uso reales.

---

## SESIÓN 3 — Dominar el dominio (Bounded Contexts + DDD Táctico)
**Tema general:** Dar forma al dominio, definir límites claros y organizar el sistema por capacidades y no por capas técnicas.

### 5. Bounded Contexts: identificación de límites y mapa de contexto
Cómo definir subdominios, contextos delimitados y relaciones entre ellos.

### 6. DDD táctico aplicado: entidades, value objects, agregados y domain events
Construcción del modelo interno dentro de cada bounded context.

> Cómo modelar y delimitar bien el dominio para evitar el caos y potenciar la modularidad.
---

## SESIÓN 4 — Evolución del sistema (Modular Monolith + Microservicios)
**Tema general:** Enseñar cómo un sistema sano puede evolucionar sin reescribirlo todo; cuándo dividir, cómo modularizar y cómo extraer servicios de forma segura.

### 7. Modular Monolith: arquitectura modular, límites estrictos y enforcement
Cómo crear módulos independientes dentro de un monolito con barreras claras, tests arquitectónicos y APIs internas estables.

### 8. Extracción a microservicios: señales, patrones y riesgos
Cuándo dividir, cómo hacerlo sin romper el dominio, patrones de extracción, cambios operativos y evaluación de coste/beneficio.

> Cómo evolucionar un sistema sano hacia modular monolith y luego microservicios sin rehacer todo.


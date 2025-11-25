# Lección 8 — Extracción a microservicios: señales, patrones y riesgos

## 1. CONNECT (Conectar)
- Actividad inicial: mostrar dos situaciones típicas:
  1. Un módulo que se ha vuelto enorme y difícil de desplegar.
  2. Un equipo bloqueado por dependencias internas.
- Pregunta detonante:  
  “¿Cuándo un módulo deja de ser manejable dentro del monolito modular?”
- Objetivo: que los alumnos conecten la necesidad de extraer microservicios con señales reales, no con moda tecnológica.

## 2. CONCEPT (Conceptos)
### Señales (disintegrators) que justifican la extracción:
- Necesidad de **escalar** de forma independiente.
- Ciclos de despliegue distintos.
- Módulos con ritmos de cambio incompatibles.
- Carga de trabajo o complejidad desproporcionada.
- Límites del Modular Monolith en consistencia o disponibilidad.

### Patrones de extracción:
- **Strangler Fig Pattern**: extraer funcionalidad poco a poco.
- **Event‑Driven Extraction**: mover capacidades guiado por eventos.
- **API Extraction**: reemplazar endpoints uno a uno.
- **Persistencia segmentada**: mover primero el adaptador y la base de datos del módulo.

### Riesgos:
- Convertir el monolito en un **distributed big ball of mud**.
- Complejidad operativa: tracing, logs, redes, resiliencia.
- Incremento de latencia y fallos por red.
- Fragmentación organizativa.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio: decidir si un módulo ficticio está listo para ser extraído.
- Tareas:
  - Identificar señales de disgregación.
  - Diseñar un plan de extracción incremental.
  - Definir qué API se extrae primero.
  - Seleccionar la estrategia de comunicación (sincronía vs eventos).
- Validación: evaluar si la extracción realmente reduce complejidad o la aumenta.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: extraer microservicios es un proceso evolutivo, no un salto brusco.
- Regla clave: “Divide solo cuando el coste de seguir unido sea mayor que el coste de separar”.
- Pregunta final: “¿Qué módulo de tu sistema sería un mal candidato para microservicio y por qué?”

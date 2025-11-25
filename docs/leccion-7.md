# Lección 7 — Modular Monolith: arquitectura modular, límites estrictos y enforcement

## 1. CONNECT (Conectar)
- Actividad inicial: pedir a los participantes que enumeren módulos de su sistema que “no deberían hablar entre sí… pero lo hacen”.
- Debate breve: por qué los monolitos acaban siendo un *Big Ball of Mud* cuando no hay límites.
- Objetivo: que el alumno identifique el problema real que resuelve un monolito modular: **separar capacidades sin caer en microservicios prematuros**.

## 2. CONCEPT (Conceptos)
### Principios del Modular Monolith:
- **Módulos con responsabilidad de dominio**, no capas técnicas.
- **Límites explícitos**: un módulo no conoce detalles internos de otro.
- **APIs internas** bien definidas para comunicación entre módulos.
- **Enforcement arquitectónico**: tests de arquitectura, convenciones, validaciones automáticas.
- **Escalabilidad organizativa** sin coste de microservicios.

### Diferencias clave:
- Monolito tradicional → un solo bloque, dependencias circulares, caos creciente.
- Modular Monolith → módulos autónomos, dependencias controladas, base futura para extraer microservicios.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio: analizar un ejemplo de aplicación “monolito clásico” y proponer una reorganización modular.
- Tareas:
  - Identificar capacidades del dominio (facturación, inventario, usuarios…).
  - Proponer límites de módulo y su API interna.
  - Localizar dependencias ilegítimas entre módulos.
  - Definir una regla de enforcement (por ejemplo: “ningún módulo puede importar del directorio interno de otro”).
- Validación: comprobar que cada módulo podría moverse o extraerse sin romper el resto del sistema.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: un Modular Monolith evita caos interno sin incurrir en la complejidad de microservicios.
- Beneficio clave: permite escalar equipos y funcionalidades sin modificar toda la arquitectura.
- Pregunta final: “Si tuvieras que modularizar hoy tu sistema, ¿qué módulo sería el primero en exigir límites estrictos?”

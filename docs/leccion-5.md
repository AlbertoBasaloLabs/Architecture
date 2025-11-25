# Lección 5 — Bounded Contexts: identificación de límites y mapa de contexto

## 1. CONNECT (Conectar)
- Actividad inicial: plantear una pregunta sencilla:  
  “¿Alguna vez un concepto en vuestro sistema significaba cosas distintas según el módulo?”
- Ejemplos típicos: “User”, “Order”, “Plan”, “Set”…  
- Mini debate: cómo estas ambigüedades generan caos, condicionales por todas partes y dependencias cruzadas.
- Objetivo: conectar la experiencia del alumno con la necesidad de establecer límites claros del dominio.

## 2. CONCEPT (Conceptos)
### Ideas clave:
- **Subdominios vs Bounded Contexts**: no siempre 1 a 1, pero cada contexto debe tener su propio modelo coherente.
- **Ubiquitous Language**: consistente dentro del contexto, no fuera.
- **Mapa de contexto**: representación visual de relaciones entre bounded contexts (upstream, downstream, colaboración, conformidad, ACLs).
- **Riesgos del modelo único**: “big ball of mud”, filología, modelos contradictorios.

### Objetivo conceptual:
Comprender por qué el dominio no puede modelarse de forma uniforme y cómo los límites arquitectónicos estabilizan la evolución del sistema.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio guiado: descomponer un dominio conocido (por ejemplo, e‑commerce o suscripción) en subdominios.
- Tareas:
  - Identificar actores y procesos clave.
  - Detectar conceptos con significados múltiples.
  - Proponer candidatos a bounded contexts.
  - Dibujar un primer **context map** con relaciones básicas.
- Validación: comprobar que cada contexto tiene un lenguaje propio y un propósito claro.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: sin límites claros, el dominio se degrada; con bounded contexts, se organiza.
- Beneficio clave: cada contexto puede evolucionar de forma independiente sin romper al resto.
- Pregunta final: “¿Cuál es el primer contexto de tu sistema que necesita ser aislado ya mismo para evitar más confusión?”

# Lección 2 — Principios que cambian la arquitectura: SRP, DI e Inversión de Dependencias

## 1. CONNECT (Conectar)
- Actividad inicial: pedir a los participantes que escriban en un post‑it qué entienden por “responsabilidad” en una clase o módulo.
- Debate rápido: ¿qué pasa cuando una pieza del sistema hace “demasiadas cosas”?
- Objetivo: conectar la experiencia previa con la necesidad real de aplicar SRP y dependencia invertida.

## 2. CONCEPT (Conceptos)
### Principios clave:
- **SRP (Single Responsibility Principle)**: una pieza debe cambiar por una sola razón.
- **Dependecy Injection (DI)**: separar creación de uso, mejorar testabilidad y desacoplar decisiones técnicas.
- **Inversión de Dependencias (DIP)**: el código de alto nivel no depende de detalles, sino de abstracciones.

### Ideas fundamentales:
- El flujo tradicional en capas empuja dependencias hacia dentro, creando rigidez.
- Invertir dependencias abre la puerta a arquitecturas como Hexagonal, Clean y Modular Monolith.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio guiado: refactorizar un componente donde la creación de dependencias está acoplada.
- Tareas:
  - Identificar violaciones de SRP.
  - Extraer interfaces y aplicar DIP.
  - Reescribir con DI para permitir sustitución en tests.
- Validación: comprobar cómo cambian los tests al aplicar estos principios.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: cómo SRP, DI y DIP permiten romper las capas y preparar un diseño orientado al dominio.
- Regla práctica: “toda dependencia debe apuntar hacia el comportamiento estable”.
- Pregunta final: “¿Qué dependencia concreta en tu proyecto sería la primera candidata a invertir mañana?”

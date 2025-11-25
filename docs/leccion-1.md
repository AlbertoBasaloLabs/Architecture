# Lección 1 — Señales de decadencia: problemas reales de la arquitectura por capas

## 1. CONNECT 
- Actividad inicial: Enumeren síntomas vistos en proyectos envejecidos (deuda técnica, caos en dependencias, dificultad para testear).

> “¿Qué señales aparecen en un sistema cuando la arquitectura deja de sostener el crecimiento?”

## 2. CONCEPT 
### Problemas típicos de una arquitectura por capas:
- Acoplamientos ocultos.
- Flujo de dependencias mal definido.
- Lógica dispersa entre controladores, servicios y repositorios.
- “Big Ball of Mud”.
- Pruebas frágiles o imposibles.
- Falta de expresividad del dominio.

### Objetivo conceptual:
Entender por qué el enfoque por capas tiende a degradarse y preparar el terreno para alternativas como Hexagonal y Clean Architecture.

## 3. CONCRETE PRACTICE 
- Ejercicio guiado: analizar AstroBookings y detectar problemas reales.
- Tareas:
  - [ ] Identificar dependencias ilegítimas.
  - [ ] Detectar lógica de dominio en la capa incorrecta.
  - [ ] Marcar puntos donde la capa de infraestructura filtra detalles al dominio.
- Puesta en común: cada grupo comparte un síntoma y una posible causa.

## 4. CONCLUSIONS
- La arquitectura por capas tiende a degradarse con el tiempo.
- Existen principios que permiten mejorar la arquitectura.
> “¿Qué cambiarías mañana mismo en tu código para evitar que se convierta en un ‘big ball of mud’?”

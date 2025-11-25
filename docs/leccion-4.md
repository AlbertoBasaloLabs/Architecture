# Lección 4 — Casos de uso: del controlador al dominio y la persistencia

## 1. CONNECT (Conectar)
- Actividad inicial: mostrar un endpoint típico de un controlador “gordo” y preguntar:  
  “¿Qué lógica de negocio se está ejecutando aquí que no debería estar en este sitio?”
- Mini debate: cuál es la “unidad mínima” de arquitectura que debería representar la intención del sistema.
- Objetivo: que los asistentes identifiquen que un *caso de uso* es la pieza que debe orquestar la lógica, no el controlador ni el repositorio.

## 2. CONCEPT (Conceptos)
### Ideas clave:
- **Caso de uso**: orquestador de un flujo concreto de aplicación.
- **Controlador**: entrada al sistema, sin lógica de negocio.
- **Dominio**: reglas de negocio puras y consistentes.
- **Adaptadores de persistencia**: implementan puertos, nunca exponen detalles internos.

### Flujo recomendado:
Controlador → Caso de Uso → Dominio → Persistencia (vía puerto)  
Siempre con dependencia invertida hacia el dominio.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio guiado: transformar un endpoint existente en un caso de uso limpio.
- Tareas:
  - Extraer la lógica del controlador.
  - Implementar una clase “interactor” o “use case handler”.
  - Introducir un puerto para persistencia.
  - Sustituir la llamada directa al repositorio por la interfaz.
- Validación: comprobar que el caso de uso es testeable sin infraestructura.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: un caso de uso bien definido reduce acoplamiento y facilita testing y evolución de la arquitectura.
- Beneficio inmediato: claridad en responsabilidad y facilidad para integrar DI, mocks y adaptadores.
- Pregunta final: “¿Qué caso de uso concreto podrías aislar mañana en tu proyecto para mejorar la claridad del flujo?”

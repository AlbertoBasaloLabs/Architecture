# Lección 3 — Arquitectura Hexagonal: puertos, adaptadores y dirección de dependencias

## 1. CONNECT (Conectar)
- Actividad inicial: mostrar un diagrama sencillo de arquitectura por capas y preguntar:  
  “¿Dónde suele empezar a romperse este modelo en proyectos reales?”
- Discusión breve: experiencias con controladores que hacen demasiado, repositorios que filtran detalles técnicos o lógica mezclada.
- Objetivo: que el alumno reconozca la necesidad de una arquitectura que haga explícitas las dependencias.

## 2. CONCEPT (Conceptos)
### Puntos clave de la Arquitectura Hexagonal:
- **Domino como centro**: el modelo y los casos de uso no dependen de la infraestructura.
- **Puertos**: contratos que definen cómo el dominio habla con el exterior.
- **Adaptadores**: implementaciones que conectan el sistema con tecnologías reales (HTTP, DB, colas, etc.).
- **Dirección de dependencias**: todo apunta hacia el dominio.

### Intención arquitectónica:
Separar capacidades de negocio de detalles técnicos para mejorar testabilidad, flexibilidad y resistencia a cambios.

## 3. CONCRETE PRACTICE (Práctica concreta)
- Ejercicio guiado: redibujar un mini-caso de uso real en formato hexagonal.
- Tareas:
  - Identificar qué partes del ejemplo son dominio, qué son puertos y qué son adaptadores.
  - Crear un puerto (interface) para la persistencia.
  - Proponer un adaptador de base de datos y otro mock para testing.
- Validación: comprobar que el dominio no conoce ningún detalle técnico.

## 4. CONCLUSIONS (Conclusiones)
- Resumen: Hexagonal hace explícitas las dependencias y elimina el acoplamiento estructural típico de las capas.
- Beneficio inmediato: tests más sencillos y lógica de negocio independiente de cambios de frameworks.
- Pregunta final: “Si mañana cambiaras de base de datos, ¿qué debería mantenerse intacto?”


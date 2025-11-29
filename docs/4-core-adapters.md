# 4 Core and Adapters

<! -- TODO: Solo para poder definir la práctica -->

## 1. CONNECT

## 2. CONCEPT 

## 3. CONCRETE PRACTICE 

Partimos de una solución con adaptadores de infraestructura para puertos de salida del la capa de negocio 

Implementar la **Arquitectura Hexagonal** completa usando puertos de entrada y agrupando dominio y aplicación.

- Capa core, que incluye dominio y aplicación
    - [ ] Renombrar la actual carpeta business a core/domain.
    - [ ] Crear una nueva carpeta core/application.
- Capa adapters, que incluye entrada y salida
    - [ ] Mover los adaptadores actuales a la carpeta adapters/out.
    - [ ] Crear una nueva carpeta adapters/in para los adaptadores de entrada.
- Rest handlers como adaptadores de entrada
    - [ ] Mover los handlers REST a adapters/in que implementan los puertos de entrada.
    - [ ] Los handlers usan los servicios de la capa core/application.

## 4. CONCLUSIONS 
# 3 Ports Adapters

<! -- TODO: Valorar renombrar a Driven Ports -->

La conectividad que cambia la dependencia: puertos y adaptadores

## 1. CONNECT 

- ¿De quién depende la lógica de negocio?

- ¿Qué consecuencias tiene esta dependencia?

> Objetivo: entender la necesidad de proteger la lógica de negocio.

## 2. CONCEPT 

### Principios clave:

- **Domain**: núcleo con la lógica de negocio de la aplicación.
- **Details**: el resto de la solución.
- **Ports**: interfaces de comunicación.
- **Adapters**: implementaciones concretas.
- **Driving Adapters**: adaptadores que vienen desde el exterior.
- **Driven Adapters**: adaptadores que vienen desde el interior.

### Ideas fundamentales:

- El flujo de dependencias va de fuera (detalles) hacia dentro (dominio).
- La lógica de negocio se encuentra en el núcleo (dominio) y es independiente de los detalles.

## 3. CONCRETE PRACTICE 
Partimos de un diseño flexible con interfaces y factorías.

Aplicar el patrón **ports y adapters** en la capa de infraestructura (Driven Adapters).

- Driven Adapters (capas de negocio e infraestructura)
    - [ ] Las interfaces de infraestructura pasan a ser ports expuestos desde el núcleo.
    - [ ] Las implementaciones de infraestructura quedan como adapters de los ports.
    - [ ] Los servicios exponen sus necesidad de uso de ports.
- Inversión del control de dependencias, configurando la capa de aplicación
    - [ ] La capa de negocio no depende de ninguna otra capa.
    - [ ] La capa de aplicación depende de la capa de negocio y de la capa de infraestructura.
    - [ ] La capa de aplicación usa las factorías de infraestructura (Configuración) para obtener adaptadores.
    - [ ] La capa de aplicación envía esos adaptadores a la capa de negocio.
    - [ ] La capa de negocio usa sus ports sin conocer detalles de implementación.
- No es necesario implementar Driving ports por el momento
- Renombra la carpeta providers a adapters

> Objetivo: patrón ports y adapters y familiaridad con hexagonal architecture.

## 4. CONCLUSIONS 

- La lógica de negocio es lo más estable de la aplicación.
- Los detalles puede cambiar con el tiempo.
- La inversión del control permite la independencia de la lógica de negocio.

- ¿Cómo de independiente es tu lógica de negocio?
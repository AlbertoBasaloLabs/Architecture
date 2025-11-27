# 1 Layered Architecture 

Señales de decadencia: problemas reales de la arquitectura por capas

## 1. CONNECT 

- ¿Cómo se organiza el código?

- ¿Qué impacto tiene las decisiones de diseño?

### Objetivo conceptual:
Entender por qué el enfoque por capas tiende a degradarse y preparar el terreno para alternativas como Hexagonal y Clean Architecture.

## 2. CONCEPT 

### Ventajas de la arquitectura por capas:

- Sencillez.
- Organización.
- Separación de responsabilidades.

### Problemas típicos de una arquitectura por capas:

- Acoplamientos.
- Flujo de dependencias.
- Lógica dispersa.
- Dominio inexpressivo.
- Pruebas frágiles o complejas.
- Potencialmente un _Big Ball of Mud_.

## 3. CONCRETE PRACTICE 
Comenzar con un diseño de capas intencionalmente deficiente.

Señalar mediante comentarios `To Do:` los problemas que se encuentran en el código.
- [ ] Las reglas de validación y de negocio están dispersas.
- [ ] Llamadas a repositorio en el controlador.
- [ ] Acoplamientos por constructores de instancias.
- [ ] Las excepciones no siguen una estructura consistente.
- [ ] No hay una responsabilidad clara por capa.

Objetivo: familiarizarse con el código y sus deficiencias actuales

## 4. CONCLUSIONS

- La arquitectura por capas tiende a degradarse con el tiempo.
- Existen principios que permiten mejorar la arquitectura.

> ¿Qué cambiarías mañana mismo en tu código para evitar que se convierta en un ‘big ball of mud’?

# AstroBookings (Legacy Backend)

Este proyecto es la base para el taller de Arquitectura de Software. Representa una aplicación backend funcional pero con una arquitectura "sucia" (Layered, Anemic Model, Coupled) que iremos refactorizando durante el curso.

## 📋 Prerrequisitos

Necesitas tener instalado lo siguiente en tu máquina:

1.  **Java JDK 21** (o superior).
2.  **Maven 3.8+** (Herramienta de construcción).
3.  Un IDE (IntelliJ, Eclipse, VsCode o cualquier variante).
4.  Un cliente HTTP o `curl` en terminal.

## 🚀 Compilación y Ejecución

```bash
cd java
mvn -B clean package
java -jar target/astrobookings-1.0-SNAPSHOT.jar
```

## 🏗️ Arquitectura

Ver [ARCHITECTURE.md](ARCHITECTURE.md) para más detalles.

---

- Author: [Alberto Basalo](https://albertobasalo.dev)
- Academy: [Academy Organization](https://aicode.academy)
- Laboratory: [Source Repository](https://github.com/AlbertoBasaloLabs/Architecture)    
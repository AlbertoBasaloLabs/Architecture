# AstroBookings (Legacy Backend)

Este proyecto es la base para el taller de Arquitectura de Software. Representa una aplicación backend funcional pero con una arquitectura "sucia" (Layered, Anemic Model, Coupled) que iremos refactorizando durante el curso.

## 📋 Prerrequisitos

Necesitas tener instalado lo siguiente en tu máquina:

1.  **Java JDK 21** (o superior).
2.  **Maven 3.8+** (Herramienta de construcción).
3.  Un IDE (IntelliJ IDEA, o VS Code, Eclipse).
4.  Un cliente HTTP (Postman, Insomnia o `curl` en terminal).

### Verificación de herramientas

Abre una terminal y ejecuta:

```bash
java -version
mvn -version
```

Deberías ver `java version "21..."` y `Apache Maven 3...`.

---

## 🚀 Compilación y Ejecución

El flujo es idéntico para **Windows (PowerShell/CMD)**, **macOS** y **Linux/WSL**.

### 1. Compilar el proyecto

Desde la raíz de la carpeta `java` (donde está el `pom.xml`):

```bash
mvn clean package
```

Si todo va bien, verás un mensaje de `BUILD SUCCESS` y se generará una carpeta `target`.

### 2. Ejecutar la aplicación

El comando anterior genera un "fat jar" (un ejecutable con todas las dependencias incluidas) en `target/`. Ejecútalo con:

```bash
java -jar target/astrobookings-legacy-1.0-SNAPSHOT.jar
```

Verás el mensaje:
> Server started on port 8080

---

## 🧪 Cómo probar la API

La aplicación no tiene interfaz gráfica (todavía). Usaremos la terminal o Postman.

### Listar vuelos disponibles

**Request:**
```bash
curl -v http://localhost:8080/flights
```

**Respuesta esperada (JSON):**
Una lista de vuelos programados para el futuro.

### Crear una reserva

**Request:**
Necesitas un `flightId` válido (obtenido del paso anterior, por defecto hay uno llamado `f1`).

**Windows (PowerShell):**
```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/bookings" `
-ContentType "application/json" `
-Body '{"flightId": "f1", "passengerName": "Elon"}'
```

**macOS / Linux / WSL (curl):**
```bash
curl -X POST http://localhost:8080/bookings \
-H "Content-Type: application/json" \
-d '{"flightId": "f1", "passengerName": "Elon"}'
```

---

## 🛠️ Solución de Problemas Comunes

### "Java not found" o "mvn not found"
*   **Windows**: Asegúrate de que las variables de entorno `JAVA_HOME` y `MAVEN_HOME` están configuradas y añadidas al `PATH`.
*   **Mac/Linux**: Usa [SDKMAN!](https://sdkman.io/) para instalar versiones fácilmente:
    ```bash
    sdk install java 21.0.2-open
    sdk install maven
    ```

### Puerto 8080 ocupado
Si al arrancar dice `Address already in use`, edita el fichero `src/main/java/com/astrobookings/AstroBookingsApp.java` y cambia el puerto `8080` por otro (ej. `8081`).

### Caracteres extraños en Windows
Si ves símbolos raros en la consola, ejecuta esto antes en PowerShell:
```powershell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
```

# 🎬 TFG Cine

Aplicación web desarrollada como **Proyecto Final de Grado (DAM)** que consume la API de **The Movie Database (TMDB)** para mostrar información actualizada sobre películas y series: tendencias, rankings y contenido destacado.

El objetivo del proyecto es construir una **plataforma de exploración de cine y series**, con una interfaz moderna y una arquitectura backend sólida.

---

## 🚀 Funcionalidades principales

- 🎥 **Película destacada (HERO)**  
  Muestra la película más relevante del día según TMDB (trending diario), con:
    - Imagen de fondo en alta resolución
    - Título
    - Géneros
    - Valoración media y número de votos
    - Sinopsis

- 🔥 **Tendencias · Top Películas**  
  Carrusel horizontal con las películas más populares del momento.

- 📺 **Tendencias · Top Series**  
  Carrusel independiente con las series más populares.

- 🎞️ **Componentes reutilizables**
    - Cards de películas/series
    - Secciones tipo carrusel
    - Layout alineado y consistente en toda la home

- 🌍 **Soporte multidioma**
    - Español como idioma principal
    - Fallback automático a inglés cuando el título contiene caracteres no latinos

---

## 🧱 Arquitectura del proyecto

### Backend
- **Java 21**
- **Spring Boot**
- **RestTemplate** para consumo de la API de TMDB
- **Mustache** como motor de plantillas
- Separación clara por capas:
    - `controller`
    - `service`
    - `model`

### Frontend
- HTML + Mustache
- CSS modularizado (`hero`, `cards`, `sections`, etc.)
- JavaScript vanilla para carruseles
- Diseño responsive

### Infraestructura
- Despliegue en **servidor Linux**
- **NGINX** como reverse proxy
- Aplicación ejecutada como **servicio systemd**
- Variables sensibles gestionadas por **variables de entorno**

---

## 🔐 Seguridad y configuración

La clave de la API de TMDB **no se incluye en el repositorio**.

Se gestiona mediante variable de entorno:

```bash
TMDB_API_KEY=tu_api_key
```

El proyecto está preparado para ejecutarse en:

 - entorno local
 - entorno de producción
 - sin exponer credenciales sensibles.

Ejecución:

```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`.

---

## 📂 Estructura del proyecto simplificada

```
tfg-cine/
├── src/main/
│   ├── java/com/tfgcine/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── service/
│   │   └── TfgCineApplication.java
│   ├── resources/
│   │   ├── static/
│   │   │   ├── css/
│   │   │   ├── js/
│   │   │   └── images/
│   │   ├── templates/
│   │   └── application.properties
├── pom.xml
└── README.md
```

## Archivo application.properties (configuración básica)

```properties
server.port=8080
tmdb.api.key=${TMDB_API_KEY}
tmdb.api.url=https://api.themoviedb.org/3
tmdb.image.base.url=https://image.tmdb.org/t/p/w500
```

TMDB_API_KEY debe definirse en las variables de entorno del sistema operativo o en el entorno de ejecución.
Para definirla en Linux:

```bash
export TMDB_API_KEY=tu_api_key
```

Para definirla en Windows (PowerShell):

```powershell
$env:TMDB_API_KEY="tu_api_key"
```

---

## Estado del proyecto

- ✅ Despliegue en servidor Linux configurado
- ✅ Consumo de la API de TMDB implementado
- ✅ Interfaz de usuario básica funcional
- 🚧 Funcionalidades adicionales en desarrollo:
  - Usuarios y autenticación
  - Reseñas y valoraciones
  - Búsqueda avanzada
  - Favoritos y listas personalizadas

- 🚧 Mejoras de UI/UX en proceso
- 🚧 Documentación pendiente

---

## Autoría
- **Proyecto Final de Grado (DAM) - 2026**
- **Desarrollador:** Tamara Martínez Vargas
- **Centro Educativo**: Campus FP, Alcalá de Henares, Madrid.

## Licencia
Este proyecto ha sido desarrollado con fines educativos y no está destinado a uso comercial. La API de TMDB es propiedad de The Movie Database y su uso está sujeto a sus términos y condiciones.
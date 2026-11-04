# RutaIA

Sistema de recomendación de cursos para la institución. El estudiante escribe lo que quiere aprender en lenguaje natural y recibe cursos reales del catálogo, junto con los cursos que se usaron como fuente.

## Tecnologías

- Backend: Java 21, Spring Boot 4, Spring Data JPA y Gradle
- Base de datos relacional: PostgreSQL 16
- Búsqueda vectorial: Qdrant
- Automatización: n8n
- IA: OpenRouter (embeddings y chat)
- Frontend: HTML, CSS y JavaScript

## Arquitectura

```
Frontend -> Spring Boot -> PostgreSQL
Spring Boot -> n8n -> Qdrant y OpenRouter
```

El frontend solo se comunica con Spring Boot.

## Requisitos

- JDK 21
- Docker Desktop
- Git
- Cuenta y API key de OpenRouter

## Instalación

1. Clona el repositorio y cambia a la rama `dev`.
2. Copia `.env.example` como `.env` y completa los valores. El archivo `.env` no se sube a Git.
3. Crea `src/main/resources/application-local.properties` con la conexión a la base:

```
spring.datasource.url=jdbc:postgresql://localhost:5434/rutaia
spring.datasource.username=rutaia
spring.datasource.password=tu_contraseña
```

4. Levanta PostgreSQL y Qdrant:

```
docker compose up -d
```

5. Crea las tablas:

```
docker cp db/schema.sql rutaia-postgres:/tmp/schema.sql
docker exec rutaia-postgres psql -U rutaia -d rutaia -f /tmp/schema.sql
```

6. Crea la colección de vectores:

```
.\scripts\qdrant-coleccion.ps1
```

7. Opcional: carga datos de prueba:

```
docker cp db/datos_prueba.sql rutaia-postgres:/tmp/datos_prueba.sql
docker exec rutaia-postgres psql -U rutaia -d rutaia -f /tmp/datos_prueba.sql
.\scripts\qdrant-relleno.ps1
```

8. Arranca la aplicación:

```
$env:OPENROUTER_API_KEY = "tu_key"
$env:SPRING_PROFILES_ACTIVE = "local"
.\gradlew.bat bootRun
```

9. Abre `http://localhost:8080` en el navegador.

## Endpoints

| Método | Ruta | Descripción |
|---|---|---|
| POST | /api/estudiantes | Registra un estudiante |
| GET | /api/estudiantes | Lista estudiantes |
| GET | /api/estudiantes/{id} | Consulta un estudiante |
| GET | /api/estudiantes/{id}/historial | Historial de consultas |
| GET | /api/cursos | Lista todos los cursos |
| POST | /api/cursos | Crea un curso |
| PUT | /api/cursos/{id} | Actualiza un curso |
| PATCH | /api/cursos/{id}/desactivar | Desactiva un curso sin borrarlo |
| GET | /api/catalogo | Cursos activos, con filtros `categoria` y `nivel` |
| POST | /api/consultas | Crea una consulta y obtiene una recomendación |
| GET | /api/consultas/{id} | Consulta una búsqueda guardada |
| POST | /api/recomendaciones/{id}/calificacion | Califica una recomendación de 1 a 5 |
| GET | /api/estadisticas | Totales, promedio y curso más recomendado |
| GET | /api/health | Verifica que la aplicación responde |

## Flujo de una consulta

1. El estudiante envía una pregunta.
2. Spring guarda la consulta en estado Pendiente y llama a n8n.
3. n8n genera el embedding de la pregunta, busca los cursos más parecidos en Qdrant y pide la recomendación a OpenRouter.
4. Spring valida que las fuentes existan y estén activas en PostgreSQL, y guarda la recomendación.

Estados posibles: Pendiente, Respondida, Sin resultados y Error.

## Pendiente

- Los flujos de n8n (indexación de cursos y consulta-rag) no están en el repositorio. Sin ellos, las consultas terminan en estado Error.

## Errores conocidos

- Los vectores de prueba son aleatorios. La búsqueda semántica real requiere indexar el catálogo con OpenRouter.
- PostgreSQL usa el puerto 5434 porque el 5432 estaba ocupado en el equipo de desarrollo.

## Estructura

- `src/main/java/com/rutaia/rutaia/`: código del backend, separado por módulo
- `src/main/resources/static/`: frontend
- `db/`: esquema y datos de prueba
- `scripts/`: colección de Qdrant y carga de vectores

## Autor

Iker Mauricio Dahinten Galan
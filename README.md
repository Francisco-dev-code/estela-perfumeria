# Estela — Perfumería online

Trabajo de la Evaluación 2 de ISY1102 (Seguridad y Calidad en el Desarrollo de Software).
Negocio simulado: perfumería online con catálogo, carrito, pedidos, cancelaciones,
devoluciones y administración de productos.

No es un sitio público ni un servicio real: es un proyecto académico que corre en local.

## Estructura del repositorio

| Carpeta | Contenido |
|---|---|
| `estela-api/` | Backend Spring Boot. Reglas de negocio, seguridad y pruebas unitarias |
| `estela-web/` | Frontend estático: `index.html` y `assets/`. Sin build |
| `estela-infra/` | `docker-compose.yml`, `sonar-project.properties` y plan de JMeter |
| `estela-docs/` | ERS, plan de pruebas, casos, matriz de evidencias e informe de certificación |
| `evidencias/` | Capturas y reportes de ejecución |

## Requisitos previos

- JDK 17 o superior
- Docker Desktop (solo si se usa la opción con contenedores)
- Python 3 (para servir el frontend)
- Node con `npx` (solo para Lighthouse)

No hace falta instalar Maven: el backend trae el wrapper `mvnw`.

## Cómo levantarlo en local

Backend, en una terminal:

```bash
cd estela-api
./mvnw spring-boot:run
```

En Windows: `mvnw.cmd spring-boot:run`

Frontend, en otra terminal:

```bash
cd estela-web
python -m http.server 8081
```

Luego abrir http://localhost:8081

La API queda en http://localhost:8080

## Cómo levantarlo con Docker

```bash
cd estela-infra
docker compose up -d --build
```

Servicios:

| Servicio | URL |
|---|---|
| Web | http://localhost:8081 |
| API | http://localhost:8080 |
| SonarQube | http://localhost:9000 |

## Usuarios de prueba

| Rol | Correo | Clave |
|---|---|---|
| Cliente | cliente@estela.cl | Cliente123! |
| Administradora | admin@estela.cl | Admin123! |

Son cuentas de desarrollo cargadas al iniciar. No corresponden a personas reales.

## Endpoints principales

| Método y ruta | Acceso | Uso |
|---|---|---|
| `GET /api/products` | Visitante | Catálogo y búsqueda |
| Rutas de pedido | Cliente autenticado | Crear, listar, cancelar y devolver |
| `/api/admin/**` | Solo rol ADMIN | Gestión de productos y pedidos |

La lista completa, con códigos de requerimiento asociados, está en `estela-docs/ERS.md`.

## Documentación de pruebas

| Archivo | Para qué sirve |
|---|---|
| `estela-docs/ERS.md` | 31 requerimientos: 12 funcionales, 7 no funcionales, 6 de seguridad y 6 legales |
| `estela-docs/PlanPruebas.md` | Estrategia, tipos de prueba y criterios de aceptación |
| `estela-docs/CasosPrueba.md` | Casos CP-01 a CP-09 con sus campos |
| `estela-docs/MatrizEvidencias.csv` | Requerimiento, caso, evidencia y estado |
| `estela-docs/InformeCertificacion.md` | Conclusiones y dictamen |

## Ejecutar las pruebas

Unitarias y cobertura:

```bash
cd estela-api
./mvnw test
./mvnw verify
```

El reporte de cobertura queda en `estela-api/target/site/jacoco/index.html`.

Análisis estático:

```bash
cd estela-infra
docker compose up -d sonarqube
cd ../estela-api
./mvnw verify sonar:sonar -Dsonar.host.url=http://localhost:9000
```

Rendimiento:

```bash
cd estela-infra
jmeter -n -t jmeter/estela-carga.jmx -l resultados.jtl -e -o reporte
```

Accesibilidad, con el frontend levantado:

```bash
npx lighthouse http://localhost:8081 --view
```

## Estado del trabajo

| Elemento | Situación |
|---|---|
| ERS | Cerrado en versión 1.0, 31 requerimientos con código |
| Casos diseñados | CP-01 a CP-09 |
| Cobertura de la matriz | 13 de 31 requerimientos con caso y evidencia asignados |
| Ejecuciones registradas | Pendientes de completar y adjuntar |

Los estados de la matriz se actualizan después de cada ejecución. Un caso ejecutado no
puede seguir marcado como pendiente.

## Limitaciones conocidas

- El pago es simulado: no existe pasarela real ni cobro efectivo.
- La autenticación usa HTTP Basic sobre HTTP local. En producción exigiría HTTPS.
- El frontend guarda las credenciales en `sessionStorage`. Es una decisión consciente del ejercicio, no una práctica recomendada.
- H2 se usa en desarrollo y PostgreSQL en Docker.
- No hay recuperación de contraseña ni verificación de correo.

## Uso

Material académico. No está pensado para despliegue productivo ni para procesar datos reales de clientes.

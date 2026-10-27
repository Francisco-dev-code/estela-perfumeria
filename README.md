# Estela - Perfumeria online

Proyecto fullstack creado para la Evaluacion 2 de ISY1102. El negocio es una perfumeria
online con catalogo, carrito, pedidos, cancelaciones, devoluciones y administracion.

## Repositorios y orden

| Repositorio | Contenido | Puerto |
|---|---|---|
| `estela-api` | Backend Spring Boot, reglas de negocio, seguridad y pruebas unitarias | 8080 |
| `estela-web` | Frontend estatico accesible, sin build obligatorio | 8081 |
| `estela-infra` | Docker Compose, SonarQube y plan JMeter | 9000 / 8080 / 8081 |
| `estela-docs` | ERS, plan de pruebas, matriz de evidencia e informe de certificacion | - |
| `estela-qa` | Evidencias, capturas y reportes generados | - |

## Inicio rapido local

1. Backend:
   ```bash
   cd estela-api
   mvn spring-boot:run
   ```
2. Frontend en otra terminal:
   ```bash
   cd estela-web
   python -m http.server 8081
   ```
3. Abrir `http://localhost:8081`.

Usuarios de prueba:

- Cliente: `cliente@estela.cl` / `Cliente123!`
- Administradora: `admin@estela.cl` / `Admin123!`

## Inicio con Docker

```bash
cd estela-infra
docker compose up -d --build
```

- Web: `http://localhost:8081`
- API: `http://localhost:8080/api/products`
- SonarQube: `http://localhost:9000` (`admin` / `admin` en el primer ingreso)

## Pruebas unitarias

```bash
cd estela-api
mvn test
mvn verify
```

El reporte de cobertura queda en `target/site/jacoco/index.html`.

## Analisis estatico

```bash
cd estela-infra
docker compose up -d sonarqube
cd ../estela-api
mvn verify sonar:sonar -Dsonar.host.url=http://localhost:9000
```

## Rendimiento

```bash
cd estela-infra
jmeter -n -t jmeter/estela-carga.jmx -l resultados.jtl -e -o reporte
```

## Accesibilidad y vulnerabilidades web

Con el frontend levantado:

```bash
npx lighthouse http://localhost:8081 --view
```

## Alcance y limites

- El pago es simulado: no se conecta a pasarela real.
- Las credenciales se envian con HTTP Basic sobre HTTP local; en produccion debe usarse HTTPS.
- La sesion del frontend guarda credenciales en `sessionStorage`; es una limitacion consciente para el ejercicio.
- H2 se usa en desarrollo y PostgreSQL en Docker.

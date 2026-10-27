# Plan de pruebas - Estela

## 1. Objetivo

Verificar que el negocio de perfumeria online funcione con calidad, seguridad y cumplimiento
normativo, y entregar evidencia trazable para el informe de certificacion.

## 2. Alcance

Incluye registro, login, catalogo, busqueda, pedido, stock, historial, cancelacion, devolucion,
administracion de productos, rendimiento, accesibilidad y analisis estatico.

Excluye pasarela de pago real, facturacion electronica, app movil nativa y despliegue productivo.

## 3. Estrategia

Enfoque mixto:

- Manual y exploratorio sobre flujos de negocio.
- Automatizado para pruebas unitarias de servicios.
- Herramientas para rendimiento, accesibilidad y analisis estatico.
- Priorizacion por riesgo: pedidos y stock primero, luego seguridad y administracion.

## 4. Tipos de prueba

| Tipo | Proposito | Herramienta |
|---|---|---|
| Funcional | Verificar reglas de negocio y flujos | Navegador + API |
| Unitaria | Validar calculos, stock y permisos | JUnit 5 + Mockito |
| Integracion | Verificar API con base de datos | Spring Boot Test |
| Seguridad | Roles, validacion y exposicion de datos | Revision manual + SonarQube |
| Rendimiento | Medir latencia y errores bajo carga | JMeter |
| Accesibilidad | Verificar uso con teclado y contraste | Lighthouse + revision manual |
| Compatibilidad | Verificar navegadores | Chrome, Edge, Firefox |
| Estatica | Detectar bugs, vulnerabilidades y duplicacion | SonarQube |

## 5. Criterios transversales de aceptacion

- Funcional: resultado observado igual al esperado y sin error 5xx.
- Seguridad: acceso denegado con 401 o 403 cuando corresponde.
- Rendimiento: p95 menor o igual a 2 s y errores menores o iguales a 1%.
- Cobertura: mayor o igual a 60% en servicios.
- Estatica: cero vulnerabilidades criticas abiertas.
- Accesibilidad: Lighthouse Accessibility mayor o igual a 90.

## 6. Criterios de entrada y salida

Entrada:

- Codigo compilable y aplicacion levantada.
- Datos semilla cargados.
- Herramientas instaladas.

Salida:

- Todos los casos ejecutados o justificados como bloqueados.
- Evidencia asociada a cada caso.
- Incidentes clasificados como Critico, Grave, Moderado o Leve.
- Informe de certificacion emitido.

## 7. Recursos

- 1 analista de pruebas.
- 1 desarrollador de apoyo.
- Equipo con Java 17, Maven, Docker y JMeter.
- Navegador Chromium para Lighthouse.
- Entorno local con API 8080, web 8081 y SonarQube 9000.

## 8. Cronograma sugerido

| Etapa | Duracion |
|---|---|
| Preparacion de entorno y datos | 1 hora |
| Pruebas unitarias y estaticas | 2 horas |
| Pruebas funcionales y de seguridad | 3 horas |
| Rendimiento y accesibilidad | 2 horas |
| Evidencias e informe | 2 horas |

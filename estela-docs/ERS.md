# ERS - Estela Perfumeria Online

Version 1.0. Negocio: venta online de perfumes con stock, pedidos, cancelaciones y devoluciones.

## Actores

- Visitante: navega catalogo y busca productos.
- Cliente: se registra, compra, revisa historial, cancela o devuelve.
- Administrador: gestiona productos y revisa pedidos.

## Requerimientos funcionales

| Codigo | Requerimiento | Prioridad |
|---|---|---|
| RF-01 | Registrar cuenta con correo unico y clave minima de 8 caracteres | Alta |
| RF-02 | Autenticar usuario y diferenciar rol CLIENTE / ADMIN | Alta |
| RF-03 | Listar productos activos con nombre, marca, precio y stock | Alta |
| RF-04 | Buscar productos por nombre o marca sin distinguir mayusculas | Media |
| RF-05 | Crear pedido con uno o mas productos y calcular total | Alta |
| RF-06 | Descontar stock al confirmar pedido | Alta |
| RF-07 | Impedir pedido si no hay stock suficiente | Alta |
| RF-08 | Listar pedidos del cliente autenticado | Alta |
| RF-09 | Cancelar pedido pagado y reintegrar stock | Alta |
| RF-10 | Solicitar devolucion de pedido pagado y reintegrar stock | Media |
| RF-11 | Crear, editar y desactivar productos como administrador | Alta |
| RF-12 | Listar todos los pedidos como administrador | Media |

## Requerimientos no funcionales

| Codigo | Requerimiento | Criterio de aceptacion |
|---|---|---|
| RNF-01 | Rendimiento de catalogo | p95 menor o igual a 2 s con 20 usuarios concurrentes |
| RNF-02 | Tasa de error bajo carga | Menor o igual a 1% en escenario de catalogo |
| RNF-03 | Cobertura de pruebas unitarias | Mayor o igual a 60% en servicios |
| RNF-04 | Calidad estatica | Sin vulnerabilidades criticas ni bloqueantes abiertas |
| RNF-05 | Accesibilidad web | Lighthouse Accessibility mayor o igual a 90 |
| RNF-06 | Compatibilidad | Correcto en Chrome, Edge y Firefox actuales |
| RNF-07 | Usabilidad movil | Sin desbordes horizontales a 375 px |

## Requerimientos de seguridad

| Codigo | Requerimiento | Criterio |
|---|---|---|
| SEG-01 | Claves almacenadas con hash BCrypt | Ninguna clave en texto plano |
| SEG-02 | Control de acceso por rol | Cliente recibe 403 en /api/admin/** |
| SEG-03 | Aislamiento de pedidos | Cliente no puede cancelar pedido ajeno |
| SEG-04 | Validacion de entradas | Correo invalido, stock negativo y cantidades invalidas rechazadas con 400 |
| SEG-05 | Mensajes de error sin datos sensibles | Sin trazas ni SQL en respuesta |
| SEG-06 | Sesion sin estado | Sin cookie de sesion; autenticacion por credenciales en cada solicitud |

## Requerimientos legales y normativos

| Codigo | Requerimiento | Referencia |
|---|---|---|
| LEG-01 | Tratar solo datos necesarios para la compra | Ley 21.719, principio de finalidad y minimizacion |
| LEG-02 | Permitir acceso y rectificacion de datos del cliente | Ley 21.719, derechos del titular |
| LEG-03 | Registrar y reportar incidentes de seguridad | Ley 21.663, gestion de incidentes |
| LEG-04 | Aplicar seguridad desde el diseno | Ley 21.663 y OWASP ASVS |
| LEG-05 | Documentar medidas de disponibilidad y respaldo | ISO 25010, fiabilidad y disponibilidad |
| LEG-06 | Asegurar accesibilidad conforme a estandares | WCAG 2.2 AA e ISO 25010 usabilidad |

## Trazabilidad

Cada caso de prueba debe citar un codigo RF, RNF, SEG o LEG. La matriz `MatrizEvidencias.csv`
conecta requerimiento, caso, evidencia, resultado y estado.

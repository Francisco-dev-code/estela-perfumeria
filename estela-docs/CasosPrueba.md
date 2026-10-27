# Casos de prueba representativos

## CP-01 - Registro de cliente

- Requerimiento: RF-01, SEG-04
- Precondicion: correo no registrado
- Datos: `nuevo@estela.cl` / `Perfume2026!`
- Pasos: abrir login, completar registro, enviar.
- Resultado esperado: 201 y cuenta creada.
- Criterio: respuesta 201; correo repetido devuelve 409.
- Tipo: funcional y seguridad.

## CP-02 - Busqueda de catalogo

- Requerimiento: RF-04, RNF-01
- Precondicion: productos semilla cargados.
- Datos: `ambar`
- Pasos: escribir en buscador y enviar.
- Resultado esperado: solo productos cuyo nombre o marca contiene `ambar`.
- Criterio: al menos 1 resultado y ningun producto inactivo.
- Tipo: funcional y rendimiento.

## CP-03 - Pedido con stock suficiente

- Requerimiento: RF-05, RF-06
- Precondicion: cliente autenticado y producto con stock.
- Datos: producto 1, cantidad 2.
- Resultado esperado: pedido PAID con total correcto y stock descontado.
- Criterio: total igual a precio por cantidad; stock baja en 2.
- Tipo: funcional e integracion.

## CP-04 - Pedido sin stock

- Requerimiento: RF-07
- Datos: producto con stock 5, cantidad 6.
- Resultado esperado: rechazo 409 y stock intacto.
- Criterio: mensaje de stock insuficiente, sin pedido creado.
- Tipo: funcional y negativa.

## CP-05 - Acceso de cliente a administracion

- Requerimiento: SEG-02
- Precondicion: sesion de cliente.
- Pasos: llamar `POST /api/admin/products`.
- Resultado esperado: 403.
- Criterio: no se crea el producto.
- Tipo: seguridad.

## CP-06 - Cancelacion de pedido ajeno

- Requerimiento: SEG-03
- Precondicion: dos clientes con pedidos.
- Pasos: cliente A intenta cancelar pedido de cliente B.
- Resultado esperado: 403.
- Criterio: estado del pedido no cambia.
- Tipo: seguridad.

## CP-07 - Carga de catalogo

- Requerimiento: RNF-01, RNF-02
- Datos: 20 usuarios, rampa 10 s, 5 iteraciones.
- Resultado esperado: p95 menor o igual a 2 s y errores menores o iguales a 1%.
- Tipo: rendimiento.

## CP-08 - Accesibilidad de catalogo

- Requerimiento: RNF-05, LEG-06
- Datos: Lighthouse en `http://localhost:8081`.
- Resultado esperado: Accessibility mayor o igual a 90 y navegacion por teclado.
- Tipo: accesibilidad.

## CP-09 - Analisis estatico

- Requerimiento: RNF-04, LEG-04
- Datos: `mvn verify sonar:sonar`.
- Resultado esperado: cero vulnerabilidades criticas abiertas.
- Tipo: estatica.

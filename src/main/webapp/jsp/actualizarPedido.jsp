<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Actualizar Pedido #${pedido.idPedido}</title>

    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/actualizarPedido.css">
</head>

<body>
<div class="contenedor_panel">
    <main class="contenido_principal_actualizar">
        <div class="panel_actualizacion">
            <div class="header_actualizacion">
                <h2><i class="fas fa-edit"></i> Actualizar Pedido #${pedido.idPedido}</h2>
                <button class="btn_volver" onclick="location.href='GestionarPedido?ruta=verDetalle&idPedido=${pedido.idPedido}'">
                    <i class="fas fa-arrow-left"></i> Volver
                </button>
            </div>

            <form id="formActualizarPedido" action="GestionarPedido" method="post">
                <input type="hidden" name="ruta" value="aceptarCambios">
                <input type="hidden" name="idPedido" value="${pedido.idPedido}">

                <div class="seccion_formulario">
                    <h3><i class="fas fa-info-circle"></i> Información del Pedido</h3>

                    <div class="form_grid">
                        <div class="form_grupo">
                            <label for="numMesa">
                                <i class="fas fa-table"></i> Número de Mesa:
                            </label>
                            <input type="number" id="numMesa" name="numMesa"
                                   value="${pedido.numMesa}" required min="1" max="50">
                        </div>

                        <div class="form_grupo">
                            <label for="invitados">
                                <i class="fas fa-users"></i> Número de Invitados:
                            </label>
                            <input type="number" id="invitados" name="invitados"
                                   value="${pedido.invitados}" required min="1" max="20">
                        </div>

                        <div class="form_grupo">
                            <label for="formaPago">
                                <i class="fas fa-credit-card"></i> Forma de Pago:
                            </label>
                            <select id="formaPago" name="formaPago" required>
                                <option value="Efectivo" ${pedido.formaPago == 'Efectivo' ? 'selected' : ''}>
                                    💵 Efectivo
                                </option>
                                <option value="Tarjeta" ${pedido.formaPago == 'Tarjeta' ? 'selected' : ''}>
                                    💳 Tarjeta
                                </option>
                                <option value="Transferencia" ${pedido.formaPago == 'Transferencia' ? 'selected' : ''}>
                                    🏦 Transferencia
                                </option>
                            </select>
                        </div>

                        <div class="form_grupo">
                            <label for="nombreCliente">
                                <i class="fas fa-user"></i> Nombre del Cliente:
                            </label>
                            <input type="text" id="nombreCliente" name="nombreCliente"
                                   value="${pedido.nombreCliente}" required>
                        </div>
                    </div>
                </div>


                <div class="seccion_formulario">
                    <h3><i class="fas fa-utensils"></i> Platos del Pedido</h3>

                    <div class="platos_container">
                        <c:forEach var="detalle" items="${detallesPedido}" varStatus="status">
                            <div class="plato_card" data-detalle-id="${detalle.idDetallePedido}">
                                <div class="plato_imagen_container">
                                    <img src="${detalle.plato.imagen}" alt="${detalle.plato.nombre}" class="plato_imagen">
                                </div>

                                <div class="plato_detalles">
                                    <h4 class="plato_nombre">${detalle.plato.nombre}</h4>
                                    <p class="plato_precio">
                                        $<fmt:formatNumber value="${detalle.plato.precio}" minFractionDigits="2" maxFractionDigits="2"/> c/u
                                    </p>

                                    <div class="cantidad_container">
                                        <label>Cantidad:</label>
                                        <div class="cantidad_controls_avanzados">
                                            <input type="number"
                                                   name="cantidades[${detalle.idDetallePedido}]"
                                                   id="cantidad_${detalle.idDetallePedido}"
                                                   value="${detalle.cantidad}"
                                                   min="1"
                                                   max="20"
                                                   class="input_cantidad_avanzado"
                                                   onchange="actualizarSubtotal(${detalle.idDetallePedido}, ${detalle.plato.precio})">
                                        </div>
                                    </div>

                                    <div class="subtotal_container">
                                        <strong>Subtotal: $<span id="subtotal_${detalle.idDetallePedido}">
                                                <fmt:formatNumber value="${detalle.precio}" minFractionDigits="2" maxFractionDigits="2"/>
                                            </span></strong>
                                    </div>
                                </div>

                                <button type="button" class="btn_eliminar_plato_avanzado"
                                        onclick="eliminarPlatoDirecto(${detalle.idDetallePedido}, ${pedido.idPedido})"
                                        title="Eliminar plato del pedido">
                                    <i class="fas fa-trash-alt"></i>
                                </button>
                            </div>
                        </c:forEach>

                        <c:if test="${empty detallesPedido}">
                            <div class="sin_platos">
                                <i class="fas fa-utensils"></i>
                                <p>No hay platos en este pedido</p>
                            </div>
                        </c:if>
                    </div>

                    <div class="total_actualizable">
                        <div class="total_info">
                            <span class="total_label">Total del Pedido:</span>
                            <span class="total_valor" id="totalPedido">
                                    $<fmt:formatNumber value="${pedido.cantidadPagar}" minFractionDigits="2" maxFractionDigits="2"/>
                                </span>
                        </div>
                    </div>
                </div>

                <div class="botones_formulario">
                    <button type="button" class="btn_cancelar_form"
                            onclick="confirmarCancelar()">
                        <i class="fas fa-times"></i> Cancelar
                    </button>

                    <button type="submit" class="btn_aceptar_cambios">
                        <i class="fas fa-save"></i> Aceptar Cambios
                    </button>
                </div>
            </form>
        </div>
    </main>
</div>

<script>
    // Función para cambiar cantidad
    function cambiarCantidad(idDetalle, incremento) {
        const input = document.getElementById(`cantidad_${idDetalle}`);
        let nuevaCantidad = parseInt(input.value) + incremento;

        if (nuevaCantidad >= 1 && nuevaCantidad <= 20) {
            input.value = nuevaCantidad;
            // Disparar evento change para actualizar subtotal
            input.dispatchEvent(new Event('change'));
        }
    }

    // Función para actualizar subtotal
    function actualizarSubtotal(idDetalle, precioUnitario) {
        const cantidadInput = document.getElementById(`cantidad_${idDetalle}`);
        const subtotalSpan = document.getElementById(`subtotal_${idDetalle}`);

        const cantidad = parseInt(cantidadInput.value) || 1;
        const subtotal = cantidad * precioUnitario;

        subtotalSpan.textContent = subtotal.toFixed(2);
        actualizarTotal();
    }

    // Función para actualizar el total general
    function actualizarTotal() {
        let total = 0;
        document.querySelectorAll('[id^="subtotal_"]').forEach(span => {
            if (span.closest('.plato_card').style.display !== 'none') {
                total += parseFloat(span.textContent) || 0;
            }
        });

        document.getElementById('totalPedido').textContent = '$' + total.toFixed(2);
    }

    function eliminarPlatoDirecto(idDetalle, idPedido) {
        Swal.fire({
            title: '¿Eliminar este plato?',
            text: "Se quitará del pedido permanentemente",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#F67F20',
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                // Redirigir directamente al controlador
                window.location.href = 'GestionarPedido?ruta=eliminarPlato&idPedido=' + idPedido + '&idDetalle=' + idDetalle;
            }
        });
    }

    // Función para confirmar cancelación
    function confirmarCancelar() {
        Swal.fire({
            title: '¿Cancelar cambios?',
            text: "Se perderán todos los cambios realizados",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#6c757d',
            cancelButtonColor: '#F67F20',
            confirmButtonText: 'Sí, cancelar',
            cancelButtonText: 'Continuar editando'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = 'GestionarPedido?ruta=verDetalle&idPedido=${pedido.idPedido}';
            }
        });
    }

    // Validación del formulario
    document.getElementById('formActualizarPedido').addEventListener('submit', function(e) {
        const platosVisibles = document.querySelectorAll('.plato_card:not([style*="display: none"])');

        if (platosVisibles.length === 0) {
            e.preventDefault();
            Swal.fire({
                title: 'Error',
                text: 'El pedido debe tener al menos un plato',
                icon: 'error',
                confirmButtonColor: '#F67F20'
            });
            return;
        }

        // Confirmación final
        e.preventDefault();
        Swal.fire({
            title: '¿Guardar cambios?',
            text: "Los cambios se aplicarán al pedido",
            icon: 'question',
            showCancelButton: true,
            confirmButtonColor: '#F67F20',
            cancelButtonColor: '#6c757d',
            confirmButtonText: 'Sí, guardar',
            cancelButtonText: 'Cancelar'
        }).then((result) => {
            if (result.isConfirmed) {
                this.submit();
            }
        });
    });
</script>

<script src="${pageContext.request.contextPath}/js/gestionPedidos.js"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</body>
</html>

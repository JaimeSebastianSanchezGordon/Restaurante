<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Pedidos</title>

    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/gestionPedidos.css">
</head>

<body>
<div class="contenedor_panel">
    <aside class="barra_lateral">
        <div class="logo">
            <i class="fas fa-utensils"></i>
        </div>

        <nav class="menu_navegacion">
        	<a href="${pageContext.request.contextPath}/ListarPlatos" class="elemento_navegacion">
                <i class="fas fa-home fa_navegacion_activo fa_tam"></i>
            </a>
            <a href="${pageContext.request.contextPath}/Dashboard" class="elemento_navegacion">
                    <i class="fas fa-th-large"></i>
                </a>
            <a href="${pageContext.request.contextPath}/GestionarPedido" class="elemento_navegacion activo">
                <i class="fas fa-file-alt"></i>
            </a>
            <a href="${pageContext.request.contextPath}/platos" class="elemento_navegacion">
                <i class="fas fa-hamburger"></i>
            </a>
        </nav>
    </aside>

    <main class="contenido_principal">
        <div class="panel_pedidos">
            <div class="encabezado_pedidos">
                <h2>
                    <c:if test="${not empty pedidoSeleccionado}">
                        Pedido ${pedidoSeleccionado.estado}
                    </c:if>
                    <c:if test="${empty pedidoSeleccionado}">
                        No hay pedidos
                    </c:if>
                </h2>
                <c:if test="${not empty pedidoSeleccionado}">
                    <span class="numero_pedido">Pedido #${pedidoSeleccionado.idPedido}</span>
                    <span class="estado_pago ${pedidoSeleccionado.estado == 'Pagado' ? 'estado_pagado' : 'estado_no_pagado'}">
                            ${pedidoSeleccionado.estado}
                    </span>
                </c:if>
            </div>

            <div class="lista_pedidos">
                <div class="titulo_lista">Todos los Pedidos</div>

                <c:forEach var="pedido" items="${pedidos}">
                    <div class="pedido_item <c:if test='${pedido.idPedido == pedidoSeleccionado.idPedido}'>activo</c:if>"
                         onclick="location.href='GestionarPedido?ruta=verDetalle&idPedido=${pedido.idPedido}'">
                        <div class="pedido_header">
                            <span class="pedido_id">Pedido #${pedido.idPedido}</span>
                            <span class="pedido_estado ${pedido.estado == 'Pagado' ? 'estado_pagado' : 'estado_no_pagado'}">
                                    ${pedido.estado}
                            </span>
                        </div>
                        <div class="pedido_info">
                            <span>Mesa • ${pedido.numMesa} • Invitados • ${pedido.invitados}</span>
                            <span class="pedido_precio">$<fmt:formatNumber value="${pedido.cantidadPagar}" minFractionDigits="2" maxFractionDigits="2"/></span>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty pedidos}">
                    <div class="pedido_item">
                        <div class="pedido_info">
                            <span>No hay pedidos disponibles</span>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="panel_detalles">
            <c:if test="${not empty pedidoSeleccionado}">
                <div class="detalles_header">
                    <h3>Detalles</h3>

                    <div class="info_mesa">
                        <div class="info_item">
                            <div class="info_label">N.º de Mesa</div>
                            <div class="info_value">${pedidoSeleccionado.numMesa}</div>
                        </div>
                        <div class="info_item">
                            <div class="info_label">Invitados</div>
                            <div class="info_value">${pedidoSeleccionado.invitados}</div>
                        </div>
                        <div class="info_item">
                            <div class="info_label">Cliente</div>
                            <div class="info_value">${pedidoSeleccionado.nombreCliente}</div>
                        </div>
                        <div class="info_item">
                            <div class="info_label">Pago</div>
                            <div class="info_value">${pedidoSeleccionado.formaPago}</div>
                        </div>
                    </div>
                </div>

                <div class="ordenes_titulo">Órdenes</div>

                <c:forEach var="detalle" items="${detallesPedido}">
                    <div class="orden_item">
                        <img class="orden_imagen" src="${detalle.plato.imagenUrl != null ? detalle.plato.imagenUrl : '/images/default-food.png'}">

                        <div class="orden_info">
                            <div class="orden_nombre">${detalle.plato.nombrePlato}</div>
                            <div class="orden_cantidad">x ${detalle.cantidad}</div>
                        </div>
                        <div class="orden_precio">$<fmt:formatNumber value="${detalle.precio}" minFractionDigits="2" maxFractionDigits="2"/></div>
                    </div>
                </c:forEach>

                <c:if test="${empty detallesPedido}">
                    <div class="orden_item">
                        <div class="orden_info">
                            <div class="orden_nombre">No hay elementos en este pedido</div>
                        </div>
                    </div>
                </c:if>

                <div class="total_pedido">
                    <strong>Total: $<fmt:formatNumber value="${pedidoSeleccionado.cantidadPagar}" minFractionDigits="2" maxFractionDigits="2"/></strong>
                </div>

                <div class="botones_accion">
                    <c:choose>
                        <c:when test='${pedidoSeleccionado.estado != "Pagado"}'>
                            <button class="boton_imprimir" onclick="location.href='GestionarPedido?ruta=ejecutar&idPedido=${pedidoSeleccionado.idPedido}'">
                                Ejecutar pedido
                            </button>
                            <button class="boton_actualizar"
                                    onclick="location.href='GestionarPedido?ruta=actualizarPedido&idPedido=${pedidoSeleccionado.idPedido}'">
                                Actualizar Pedido
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="boton_imprimir" disabled style="background-color: #ccc;">
                                Pedido Pagado
                            </button>
                        </c:otherwise>
                    </c:choose>

                    <button class="boton_eliminar" onclick="eliminarPedido('GestionarPedido?ruta=eliminar&idPedido=${pedidoSeleccionado.idPedido}')">
                        Eliminar Pedido
                    </button>
                </div>
            </c:if>

            <c:if test="${empty pedidoSeleccionado}">
                <div class="detalles_header">
                    <h3>Selecciona un pedido para ver los detalles</h3>
                </div>
            </c:if>
        </div>
    </main>
</div>

<script>
    function eliminarPedido(url) {
        Swal.fire({
            title: '¿Estás seguro?',
            text: "¡No podrás deshacer esta acción!",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#d33',
            cancelButtonColor: '#3085d6',
            confirmButtonText: 'Sí, eliminar'
        }).then((result) => {
            if (result.isConfirmed) {
                window.location.href = url;
            }
        });
    }
</script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

</body>
</html>
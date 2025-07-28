<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Gestión de Pedidos</title>
    <script src="https://kit.fontawesome.com/80cfa4399f.js" crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estadoPedido.css">
</head>

<body>
<div class="contenedor_panel">
    <!-- SIDEBAR EXACTAMENTE IGUAL QUE TU ORIGINAL -->
    <aside class="barra_lateral">
        <div class="logo">
            <i class="fa-brands fa-slack"></i>
        </div>
        <nav class="navegacion">
            <a href="ListarPlatos" class="elemento_navegacion"> 
                <i class="fas fa-home"></i>
                <span class="tooltip">Inicio</span>
            </a> 
            <a href="${pageContext.request.contextPath}/EstadoPedido" class="elemento_navegacion active"> 
                <i class="fas fa-users"></i>
                <span class="tooltip">Pedidos</span>
            </a> 
            
        </nav>
        <a href="jsp/login.jsp" class="elemento_navegacion logout">
            <i class="fa-solid fa-person-walking-arrow-right"></i>
            <span class="tooltip">Salir</span>
        </a>
    </aside>

    <main class="contenido_principal">
        <div class="panel_pedidos">
            <section class="encabezado">
                <div class="titulo">
                    <h1>Estado de Pedidos</h1>
                </div>
                <div class="busqueda_pedido">
                    <form action="${pageContext.request.contextPath}/EstadoPedido" method="post" class="d-flex">
                        <input type="hidden" name="ruta" value="verEstadoPedidoPorId">
                        <input type="text" name="idPedido" class="form-control me-2" placeholder="Buscar por ID de pedido">
                        <button type="submit" class="btn btn-primary">
                            <i class="fas fa-search me-1"></i>
                            Buscar
                        </button>
                    </form>
                </div>
            </section>

            <!-- Mensaje de error si existe -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger mb-4" role="alert">
                    <i class="fas fa-exclamation-circle me-2"></i>${error}
                </div>
            </c:if>

            <!-- Vista cuando se busca un pedido específico -->
            <c:if test="${not empty estadoPedido}">
                <div class="pedido_item mb-4">
                    <div class="pedido_header">
                        <span class="pedido_id">
                            <i class="fas fa-receipt me-2"></i>
                            Pedido #${estadoPedido.idPedido}
                        </span>
                        <c:choose>
                            <c:when test="${estadoPedido.estadoPreparacion == 'listo'}">
                                <span class="pedido_estado estado_preparado">
                                    <i class="fas fa-check me-1"></i>
                                    Pedido listo
                                </span>
                            </c:when>
                            <c:when test="${estadoPedido.estadoPreparacion == 'en preparacion'}">
                                <span class="pedido_estado estado_preparando">
                                    <i class="fas fa-clock me-1"></i>
                                    En preparación
                                </span>
                            </c:when>
                        </c:choose>
                    </div>
                </div>
            </c:if>

            <!-- Vista del listado general (solo cuando no hay búsqueda específica) -->
            <c:if test="${empty estadoPedido}">
                <div class="lista_pedidos">
                    <div class="titulo_lista">
                        <i class="fas fa-list me-2"></i>
                        Todos los Pedidos
                    </div>

                    <c:choose>
                        <c:when test="${not empty estadoPedidos}">
                            <c:forEach items="${estadoPedidos}" var="pedido">
                                <div class="pedido_item">
                                    <div class="pedido_header">
                                        <span class="pedido_id">
                                            <i class="fas fa-receipt me-2"></i>
                                            Pedido #${pedido.idPedido}
                                        </span>
                                        <c:choose>
                                            <c:when test="${pedido.estadoPreparacion == 'listo'}">
                                                <span class="pedido_estado estado_preparado">
                                                    <i class="fas fa-check me-1"></i>
                                                    Pedido listo
                                                </span>
                                            </c:when>
                                            <c:when test="${pedido.estadoPreparacion == 'en preparacion'}">
                                                <span class="pedido_estado estado_preparando">
                                                    <i class="fas fa-clock me-1"></i>
                                                    En preparación
                                                </span>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="alert alert-info">
                                <i class="fas fa-info-circle me-2"></i>
                                No hay pedidos registrados actualmente.
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </c:if>
        </div>
    </main>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
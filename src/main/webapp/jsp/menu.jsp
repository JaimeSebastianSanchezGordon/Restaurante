<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Panel de Control - PointSell</title>
    <script src="https://kit.fontawesome.com/80cfa4399f.js" crossorigin="anonymous"></script>
    <link href="https://fonts.googleapis.com/css2?family=Lexend:wght@300;400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>

<body>
    <div class="contenedor_panel">
        <!-- Barra lateral -->
        <aside class="barra_lateral">
            <div class="logo">
                <i class="fa-brands fa-slack"></i>
            </div>
            <nav class="navegacion">
                <a href="ListarPlatos" class="elemento_navegacion active"> 
                    <i class="fas fa-home"></i>
                    <span class="tooltip">Inicio</span>
                </a> 
                <a href="${pageContext.request.contextPath}/EstadoPedido" class="elemento_navegacion"> 
                    <i class="fas fa-users"></i>
                    <span class="tooltip">Pedidos</span>
                </a> 
                
            </nav>
            <a href="jsp/login.jsp" class="elemento_navegacion logout">
                <i class="fa-solid fa-person-walking-arrow-right"></i>
                <span class="tooltip">Salir</span>
            </a>
        </aside>

        <!-- Contenido principal -->
        <main class="contenido_principal">
            <!-- Sección menú -->
            <section class="menu">
                <header class="barra_superior">
                    <div class="titulo_seccion">
                        <h1>Point<span class="resaltado">sell</span></h1>
                        <p class="subtitulo">Sistema de punto de venta</p>
                    </div>
                    
                </header>

                <div class="menu_contenido">
                    <div class="encabezado_menu">
                        <div class="info_menu">
                            <h2 class="titulo_menu">Menú Especial</h2>
                            <p class="descripcion_menu">Selecciona los productos para tu pedido</p>
                        </div>
                        <form id="formCrearPedido" class="boton_crear_pedido">
                            <input type="hidden" name="ruta" value="registrar" />
                            <button type="submit" class="btn_realizar_pedido">
                                <i class="fas fa-plus"></i>
                                Realizar Pedido
                            </button>
                        </form>
                    </div>

                    <div class="contenedor_platos">
                        <c:forEach var="plato" items="${platos}">
                            <article class="tarjeta_plato">
                                <div class="imagen_contenedor">
                                    <img src="${plato.imagenUrl != null ? plato.imagenUrl : '/images/default-food.png'}" 
                                         alt="${plato.nombrePlato}" 
                                         class="imagen_plato">
                                    <div class="overlay_precio">
                                        <span class="precio">$${plato.precio}</span>
                                    </div>
                                </div>
                                <div class="contenido_tarjeta">
                                    <div class="info_plato">
                                        <h3 class="nombre_plato">${plato.nombrePlato}</h3>
                                        <p class="descripcion_plato">${plato.descripcionPlato}</p>
                                    </div>
                                    <div class="accion_tarjeta">
                                        <button onclick="agregarProducto(${plato.id})" class="btn_agregar">
                                            <i class="fas fa-plus"></i>
                                            Agregar
                                        </button>
                                    </div>
                                </div>
                            </article>
                        </c:forEach>
                    </div>
                </div>
            </section>

            <!-- Sección registro pedido -->
            <section class="registro_pedido">
                <header class="encabezado_pedido">
                    <div class="info_pedido">
                        <h3>Pedido Actual</h3>
                        <span class="numero_pedido" id="pedidoActual">#001</span>
                    </div>
                    <button class="btn_limpiar" onclick="limpiarPedido()">
                        <i class="fa-solid fa-trash"></i>
                    </button>
                </header>

                <div class="lista_productos" id="listaPedido">
                    <!-- Los productos se agregarán dinámicamente aquí con la nueva estructura -->
                </div>

                <footer class="resumen_pedido">
                    <div class="totales" id="totalPago">
                        <div class="linea_total">
                            <span>Subtotal</span>
                            <span id="subtotal">$0.00</span>
                        </div>
                        <div class="linea_total">
                            <span>IVA (19%)</span>
                            <span id="iva">$0.00</span>
                        </div>
                        <div class="linea_total total_final">
                            <span>Total</span>
                            <span id="total">$0.00</span>
                        </div>
                    </div>
                    
                    <a href="#" class="enlace_ordenar">
                        <button class="btn_ordenar" type="button">
                            <i class="fas fa-shopping-cart"></i>
                            Procesar Pedido
                        </button>
                    </a>
                </footer>
            </section>
        </main>
    </div>

    <!-- Scripts -->
    <script>
        const contextPath = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/js/registroPedido.js"></script>
</body>
</html>
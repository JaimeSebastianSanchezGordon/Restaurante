<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<!DOCTYPE html>
<html lang="es">

<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Panel de Control</title>
<script src="https://kit.fontawesome.com/80cfa4399f.js"
	crossorigin="anonymous"></script>
<link
	href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link rel="stylesheet" href="css/menu.css">
</head>

<body>
	<div class="contenedor_panel">
		<aside class="barra_lateral">
			<div class="logo fa_borde_b">
				<i class="fa-brands fa-slack fa_navegacion_activo fa_tam_principal"></i>
			</div>
			<a href="ListarPlatos" class="elemento_navegacion"> <i
				class="fas fa-home fa_navegacion_activo fa_tam"></i>
			</a> <a href="" class="elemento_navegacion"> <i
				class="fas fa-users fa_navegacion_inactivo fa_tam"></i>
			</a> <a href="" class="elemento_navegacion"> <i
				class="fas fa-file-alt fa_navegacion_inactivo fa_tam"></i>
			</a> <a href="" class="elemento_navegacion"> <i
				class="fas fa-hamburger fa_navegacion_inactivo fa_tam"></i>
			</a> <a href="jsp/login.jsp" class="elemento_navegacion fa_navegacion_inactivo fa_tam">
				<i class="fa-solid fa-person-walking-arrow-right"></i>
			</a>
		</aside>
		<section class="menu">
			<div class="barraBusqueda">
				<h1>
					Point<span class="resaltado">sell</span>
				</h1>
				<div class="campoBusqueda color_gris">
					<i class="fas fa-search"></i> <input class="busqueda" type="text"
						placeholder="Search Anything Here">
				</div>
				<div class="notificacion">
					<i class="fa-solid fa-bell color_blanco"></i>
				</div>
			</div>
			<div class="menuContenido">
				<h2 class="paraTi">Special Menu For You</h2>
				<div class="platos">
					<c:forEach var="plato" items="${platos}">
						<div class="tarjeraPlato">
							<div class="imagenPlato">
								<img src="${plato.imagenUrl != null ? plato.imagenUrl : '/images/default-food.png'}" alt="">
							</div>
							<div class="nombrePrecio">
								<h3>${plato.nombrePlato}</h3>
								<h3 class="color_naranja">$ ${plato.precio}</h3>
							</div>
							<div class="contenidoPlato">
								<p class="color_gris">${plato.descripcionPlato}</p>
							</div>
							<div class="notaBotton">
								<button class="btnAgregar">+ Add Product</button>
							</div>
						</div>
					</c:forEach>
				</div>
		</section>

		<section class="registroPedido">
            <!-- Número pedido -->
            <div class="numeroPedido" id="pedidoActual">
                <div>
                    <i class="fa-solid fa-xmark"></i>
                </div>
            </div>
            <!-- Lista pedidos -->
            <div class="Lista-pedidos">
            	<div id="listaPedido">
            
            	</div>
            </div>
            <!-- Pago -->
            <div class="pago">
            	<div id="totalPago">
            	
            	</div>
                
                <!-- Botón Ordenar -->
                <a href="factura.html">
                    <div class="ordenarBoton">
                        <button class="btnOrdenar">Place Order</button>
                    </div>
                </a>
            </div>
        </section>

	</div>
	<!-- Script -->
    <script>
    	const contextPath = '${pageContext.request.contextPath}';
    </script>
	<script src="${pageContext.request.contextPath}/js/registroPedido.js"></script>
</body>

</html>
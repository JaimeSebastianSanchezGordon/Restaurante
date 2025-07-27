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
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/menu.css">
</head>

<body>
	<div class="contenedor_panel">
		<aside class="barra_lateral">
			<div class="logo fa_borde_b">
				<i class="fa-brands fa-slack fa_navegacion_activo fa_tam_principal"></i>
			</div>
			<a href="ListarPlatos" class="elemento_navegacion"> <i
				class="fas fa-home fa_navegacion_activo fa_tam"></i>
			</a> <a href="${pageContext.request.contextPath}/EstadoPedido" class="elemento_navegacion"> <i
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
			<div class="numeroPedido">
				<h3>Order #256482</h3>
				<div>
					<i class="fa-solid fa-xmark"></i>
				</div>
			</div>
			<!-- Lista pedidos -->
			<div class="Lista-pedidos">
				<!-- Pedido 1 -->
				<div class="Pedido">
					<!-- Imagen -->
					<div>
						<img src="/imagenes/burger.jpg" alt="">
					</div>
					<!-- Nombre y precio -->
					<div class="nombrePrecio-derecha">
						<!-- Nombre -->
						<div>
							<p>Grill Sandwich</p>
						</div>
						<!-- Precio -->
						<div>
							<p>$30.00</p>
						</div>
					</div>
					<!-- Selección cantidad -->
					<div>
						<!--  Boton menos -->
						<div>
							<i class="fa-solid fa-minus"></i>
						</div>
						<!-- Cantidad -->
						<div class="aparte">
							<p>1</p>
						</div>
						<!-- Boton mas -->
						<div>
							<i class="fa-solid fa-plus" style="color: #ffffff;"></i>
						</div>
					</div>
				</div>
				<!-- Pedido 2 -->
				<div class="Pedido">
					<!-- Imagen -->
					<div>
						<img src="/imagenes/popeyes.jpg" alt="">
					</div>
					<!-- Nombre y precio -->
					<div class="nombrePrecio-derecha">
						<!-- Nombre -->
						<div>
							<p>Chicken Popeyes</p>
						</div>
						<!-- Precio -->
						<div>
							<p>$20.00</p>
						</div>
					</div>
					<!-- Selección cantidad -->
					<div>
						<!--  Boton menos -->
						<div>
							<i class="fa-solid fa-minus"></i>
						</div>
						<!-- Cantidad -->
						<div>
							<p>1</p>
						</div>
						<!-- Boton mas -->
						<div>
							<i class="fa-solid fa-plus" style="color: #ffffff;"></i>
						</div>
					</div>
				</div>
			</div>
			<!-- Pago -->
			<div class="pago">
				<!-- Subtotal -->
				<div>
					<p>Subtotal</p>
					<p>$50.00</p>
				</div>
				<!-- Impuestos -->
				<div>
					<p class="color_gris">Tax</p>
					<p class="color_gris">$4.00</p>
				</div>
				<!-- Charges -->
				<div>
					<p class="color_gris">Charges</p>
					<p class="color_gris">$16.00</p>
				</div>
				<!-- Línea -->
				<hr>
				<div>
					<p>Total</p>
					<p>$70.00</p>
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
</body>

</html>
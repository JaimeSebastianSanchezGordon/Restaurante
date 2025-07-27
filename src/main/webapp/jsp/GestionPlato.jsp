<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Gestión de Platos</title>
<link
	href="https://fonts.googleapis.com/css2?family=Lexend:wght@400;500;600;700&display=swap"
	rel="stylesheet">
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/gestionPlato.css">
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
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
                </a> <a href="${pageContext.request.contextPath}/GestionarPedido" class="elemento_navegacion"> <i
					class="fas fa-file-alt"></i>
				</a> <a href="#" class="elemento_navegacion activo"> <i
					class="fas fa-hamburger"></i>
				</a>
			</nav>
		</aside>

		<main class="contenido_principal">
			<section class="encabezado">
				<div class="titulo">
					<h1>Gestión de Platos</h1>
				</div>
				<div>
					<button class="btnAgregar" id="anadirPlatoBtn"
						data-bs-toggle="modal" data-bs-target="#modalPlato">+
						Añadir Plato</button>
				</div>
			</section>

			<section class="tabla_productos">
				<div class="contenedor_tabla">
					<table>
						<thead>
							<tr>
								<th>Producto</th>
								<th>Descripción</th>
								<th>Tipo de Plato</th>
								<th>ID Producto</th>
								<th>Precio</th>
								<th>Acciones</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${platos}" var="plato">
								<tr>
									<td class="producto_detalle"><img
										src="${plato.imagenUrl != null ? plato.imagenUrl : '/images/default-food.png'}"
										alt="${plato.nombrePlato}" class="imagen_producto" />
										<div class="info_producto">
											<div class="nombre_producto">${plato.nombrePlato}</div>
										</div>
									</td>
									<td class="descripcion_plato">
										<c:choose>
											<c:when test="${not empty plato.descripcionPlato}">
												<span class="descripcion_texto">${plato.descripcionPlato}</span>
											</c:when>
											<c:otherwise>
												<span class="descripcion_vacia">Sin descripción</span>
											</c:otherwise>
										</c:choose>
									</td>
									<td><c:choose>
											<c:when test="${plato.tipoPlato == 'Comida'}">
												<span class="tipo comida">Comida</span>
											</c:when>
											<c:when test="${plato.tipoPlato == 'Bebida'}">
												<span class="tipo bebida">Bebida</span>
											</c:when>
											<c:when test="${plato.tipoPlato == 'Otros'}">
												<span class="tipo otros">Otros</span>
											</c:when>
											<c:otherwise>
												<span class="tipo otros">${plato.tipoPlato}</span>
											</c:otherwise>
										</c:choose></td>
									<td>${plato.codigoProducto}</td>
									<td>${plato.precio}</td>
									<td class="acciones"><a
										href="${pageContext.request.contextPath}/platos?ruta=editar&id=${plato.id}"
										class="btn_editar"> <i class="fas fa-edit"></i>
											Editar
									</a>
										<button class="btn_eliminar" data-id="${plato.id}"
											data-nombre="${plato.nombrePlato}" data-bs-toggle="modal"
											data-bs-target="#modalEliminarPlato">
											<i class="fas fa-trash-alt"></i> Eliminar
										</button></td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</section>
		</main>
	</div>

	<!-- Modal para añadir nuevo plato -->
	<div class="modal fade" id="modalPlato" tabindex="-1"
		aria-hidden="true" data-bs-backdrop="static">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Registrar Nuevo Plato</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form
					action="${pageContext.request.contextPath}/platos?ruta=guardar"
					method="post" enctype="multipart/form-data">
					<div class="modal-body">
						<!-- Mostrar error si existe -->
						<c:if test="${not empty sessionScope.errorRegistroPlato}">
							<div class="alert alert-danger" role="alert">
								<i class="fas fa-exclamation-triangle me-2"></i>
								${sessionScope.errorRegistroPlato}
							</div>
							<c:remove var="errorRegistroPlato" scope="session" />
						</c:if>

						<!-- Campos del formulario con valores preservados -->
						<div class="row">
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Nombre del Plato</label> <input
										type="text" class="form-control" name="nombrePlato"
										value="${sessionScope.datosPlato.nombrePlato}" maxlength="100"
										required>
								</div>
							</div>
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Código de Producto</label> <input
										type="text" class="form-control" name="codigoProducto"
										value="${sessionScope.datosPlato.codigoProducto}" maxlength="20"
										required>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Precio ($)</label> <input
										type="number" class="form-control" name="precio"
										value="${sessionScope.datosPlato.precio}" min="0" step="0.01"
										required>
								</div>
							</div>
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Tipo de Plato</label> <select
										class="form-select" name="tipoPlato" required>
										<option value="">Seleccione un tipo</option>
										<option value="Comida"
											${sessionScope.datosPlato.tipoPlato eq 'Comida' ? 'selected' : ''}>Comida</option>
										<option value="Bebida"
											${sessionScope.datosPlato.tipoPlato eq 'Bebida' ? 'selected' : ''}>Bebida</option>
										<option value="Otros"
											${sessionScope.datosPlato.tipoPlato eq 'Otros' ? 'selected' : ''}>Otros</option>
									</select>
								</div>
							</div>
						</div>

						<div class="mb-3">
							<label class="form-label">Descripción</label>
							<textarea class="form-control" name="descripcionPlato" rows="3">${sessionScope.datosPlato.descripcionPlato}</textarea>
						</div>

						<!-- Campo para subir imagen del plato -->
						<div class="mb-3">
							<label class="form-label">Imagen del Plato</label>
							<div class="campo_imagen_plato" id="campo_imagen_plato">
								<input type="file" id="entrada_imagen_plato" name="imagenPlato"
									class="entrada_imagen_plato" accept="image/*">

								<!-- Contenido cuando no hay imagen -->
								<div class="contenido_subida_plato" id="contenido_subida_plato">
									<svg class="icono_subida_plato" viewBox="0 0 24 24" fill="none"
										xmlns="http://www.w3.org/2000/svg">
                                        <path
											d="M3 16.5V18.75C3 19.993 4.007 21 5.25 21H18.75C19.993 21 21 19.993 21 18.75V16.5M16.5 12L12 16.5M12 16.5L7.5 12M12 16.5V3"
											stroke="currentColor" stroke-width="2" stroke-linecap="round"
											stroke-linejoin="round" />
                                    </svg>
									<div class="texto_subida_plato">Arrastra una imagen aquí</div>
									<div class="texto_subida_secundario_plato">PNG, JPG, GIF
										hasta 5MB</div>
									<button type="button" class="boton_subida_plato"
										id="boton_subida_plato">Seleccionar archivo</button>
								</div>

								<!-- Preview cuando hay imagen -->
								<div class="preview_imagen_plato" id="preview_imagen_plato">
									<img class="imagen_preview_plato" id="imagen_preview_plato"
										src="" alt="Preview">
									<div class="info_imagen_plato">
										<div class="nombre_imagen_plato" id="nombre_imagen_plato"></div>
										<div class="tamano_imagen_plato" id="tamano_imagen_plato"></div>
									</div>
									<div class="acciones_imagen_plato">
										<button type="button" class="boton_cambiar_plato"
											id="boton_cambiar_plato">Cambiar</button>
										<button type="button" class="boton_eliminar_plato"
											id="boton_eliminar_plato">Eliminar</button>
									</div>
								</div>
							</div>
							<div class="error_imagen_plato" id="error_imagen_plato"></div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancelar</button>
						<button type="submit" class="btn btn-primary">Guardar</button>
					</div>
				</form>
			</div>
		</div>
	</div>


	<!-- Modal para editar plato -->
	<div class="modal fade" id="modalEditarPlato" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog modal-lg">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Editar Plato</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form
					action="${pageContext.request.contextPath}/platos?ruta=modificar"
					method="post" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${platoEdicion.id}">
					<div class="modal-body">
						<!-- Mostrar error si existe -->
						<c:if test="${not empty sessionScope.errorEdicionPlato}">
							<div class="alert alert-danger" role="alert">
								<i class="fas fa-exclamation-triangle me-2"></i>
								${sessionScope.errorEdicionPlato}
							</div>
							<c:remove var="errorEdicionPlato" scope="session" />
						</c:if>

						<!-- Mostrar foto actual del plato -->
						<div class="text-center mb-4">
							<img
								src="${pageContext.request.contextPath}/${not empty platoEdicion.imagenUrl ? platoEdicion.imagenUrl : '/images/default-food.png'}"
								alt="Foto actual del plato" class="rounded-circle"
								style="width: 150px; height: 150px; object-fit: cover; border: 3px solid #dee2e6;">
							<p class="text-muted mt-2">Imagen actual del plato ${platoEdicion.nombrePlato}</p>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Nombre del Plato</label> <input
										type="text" class="form-control" name="nombrePlato"
										value="${platoEdicion.nombrePlato}" maxlength="100" required>
								</div>
							</div>
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Código de Producto</label> <input
										type="text" class="form-control" name="codigoProducto"
										value="${platoEdicion.codigoProducto}" maxlength="20" readonly>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Precio ($)</label> <input
										type="number" class="form-control" name="precio"
										value="${platoEdicion.precio}" min="0" step="0.01" required>
								</div>
							</div>
							<div class="col-md-6">
								<div class="mb-3">
									<label class="form-label">Tipo de Plato</label> <select
										class="form-select" name="tipoPlato" required>
										<option value="Comida"
											${platoEdicion.tipoPlato eq 'Comida' ? 'selected' : ''}>Comida</option>
										<option value="Bebida"
											${platoEdicion.tipoPlato eq 'Bebida' ? 'selected' : ''}>Bebida</option>
										<option value="Otros"
											${platoEdicion.tipoPlato eq 'Otros' ? 'selected' : ''}>Otros</option>
									</select>
								</div>
							</div>
						</div>

						<div class="mb-3">
							<label class="form-label">Descripción</label>
							<textarea class="form-control" name="descripcionPlato" rows="3">${platoEdicion.descripcionPlato}</textarea>
						</div>

						<!-- Campo para actualizar la imagen -->
						<div class="mb-3">
							<label class="form-label">Actualizar Imagen</label>
							<div class="campo_imagen_plato" id="campo_imagen_edicion">
								<input type="file" id="entrada_imagen_edicion"
									name="imagenPlato" class="entrada_imagen_plato"
									accept="image/*">

								<!-- Contenido cuando no hay imagen -->
								<div class="contenido_subida_plato"
									id="contenido_subida_edicion">
									<svg class="icono_subida_plato" viewBox="0 0 24 24" fill="none"
										xmlns="http://www.w3.org/2000/svg">
                                    <path
											d="M3 16.5V18.75C3 19.993 4.007 21 5.25 21H18.75C19.993 21 21 19.993 21 18.75V16.5M16.5 12L12 16.5M12 16.5L7.5 12M12 16.5V3"
											stroke="currentColor" stroke-width="2" stroke-linecap="round"
											stroke-linejoin="round" />
                                </svg>
									<div class="texto_subida_plato">Arrastra una nueva imagen
										aquí</div>
									<div class="texto_subida_secundario_plato">PNG, JPG, GIF
										hasta 5MB</div>
									<button type="button" class="boton_subida_plato"
										id="boton_subida_edicion">Seleccionar archivo</button>
								</div>

								<!-- Preview cuando hay imagen -->
								<div class="preview_imagen_plato" id="preview_imagen_edicion">
									<img class="imagen_preview_plato" id="imagen_preview_edicion"
										src="" alt="Preview">
									<div class="info_imagen_plato">
										<div class="nombre_imagen_plato" id="nombre_imagen_edicion"></div>
										<div class="tamano_imagen_plato" id="tamano_imagen_edicion"></div>
									</div>
									<div class="acciones_imagen_plato">
										<button type="button" class="boton_cambiar_plato"
											id="boton_cambiar_edicion">Cambiar</button>
										<button type="button" class="boton_eliminar_plato"
											id="boton_eliminar_edicion">Eliminar</button>
									</div>
								</div>
							</div>
							<div class="form-text">Deja en blanco si no deseas cambiar
								la imagen actual</div>
							<div class="error_imagen_plato" id="error_imagen_edicion"></div>
						</div>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancelar</button>
						<button type="submit" class="btn btn-primary">Actualizar</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Modal para confirmar eliminación -->
	<div class="modal fade" id="modalEliminarPlato" tabindex="-1"
		aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Confirmar Eliminación</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<form
					action="${pageContext.request.contextPath}/platos?ruta=eliminar"
					method="post">
					<input type="hidden" id="eliminarId" name="id">
					<div class="modal-body">
						<p id="mensajeEliminacion">¿Estás seguro que deseas eliminar
							este plato? Esta acción no se puede deshacer.</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-secondary"
							data-bs-dismiss="modal">Cancelar</button>
						<button type="submit" class="btn btn-danger">Eliminar</button>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- Scripts -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/gestionPlato.js"></script>
</body>
</html>
document.addEventListener('DOMContentLoaded', function() {
	// ====================== CONFIGURACIÓN SUBIDA DE IMÁGENES ======================
	const configuraciones = [
		{
			// Configuración para modal de registro
			prefijo: 'plato',
			campoImagen: document.getElementById('campo_imagen_plato'),
			entradaImagen: document.getElementById('entrada_imagen_plato'),
			botonSubida: document.getElementById('boton_subida_plato'),
			contenidoSubida: document.getElementById('contenido_subida_plato'),
			previewImagen: document.getElementById('preview_imagen_plato'),
			imagenPreview: document.getElementById('imagen_preview_plato'),
			nombreImagen: document.getElementById('nombre_imagen_plato'),
			tamanoImagen: document.getElementById('tamano_imagen_plato'),
			botonCambiar: document.getElementById('boton_cambiar_plato'),
			botonEliminar: document.getElementById('boton_eliminar_plato'),
			errorImagen: document.getElementById('error_imagen_plato')
		},
		{
			// Configuración para modal de edición
			prefijo: 'edicion',
			campoImagen: document.getElementById('campo_imagen_edicion'),
			entradaImagen: document.getElementById('entrada_imagen_edicion'),
			botonSubida: document.getElementById('boton_subida_edicion'),
			contenidoSubida: document.getElementById('contenido_subida_edicion'),
			previewImagen: document.getElementById('preview_imagen_edicion'),
			imagenPreview: document.getElementById('imagen_preview_edicion'),
			nombreImagen: document.getElementById('nombre_imagen_edicion'),
			tamanoImagen: document.getElementById('tamano_imagen_edicion'),
			botonCambiar: document.getElementById('boton_cambiar_edicion'),
			botonEliminar: document.getElementById('boton_eliminar_edicion'),
			errorImagen: document.getElementById('error_imagen_edicion')
		}
	];

	// ====================== CONSTANTES Y CONFIGURACIONES ======================
	const TAMANO_MAXIMO_ARCHIVO = 5 * 1024 * 1024; // 5MB
	const TIPOS_IMAGEN_PERMITIDOS = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
	const TIMEOUT_ALERTAS = 5000; // 5 segundos

	// Extracción del contextPath una sola vez
	const contextPath = window.location.pathname.split('/')[1] || '';
	const baseUrl = `/${contextPath}`;
	const urlParams = new URLSearchParams(window.location.search);

	console.log("Base URL:", baseUrl);

	// ====================== FUNCIONES UTILITARIAS ======================
	
	/**
	 * Formatea el tamaño de archivo en bytes a formato legible
	 * @param {number} bytes - Tamaño en bytes
	 * @returns {string} Tamaño formateado
	 */
	function formatearTamano(bytes) {
		if (bytes === 0) return '0 Bytes';
		const k = 1024;
		const tamanos = ['Bytes', 'KB', 'MB', 'GB'];
		const i = Math.floor(Math.log(bytes) / Math.log(k));
		return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + tamanos[i];
	}

	/**
	 * Valida si el archivo es una imagen válida
	 * @param {File} archivo - Archivo a validar
	 * @returns {Object} {valido: boolean, error: string}
	 */
	function validarArchivo(archivo) {
		if (!TIPOS_IMAGEN_PERMITIDOS.includes(archivo.type)) {
			return {
				valido: false,
				error: 'Por favor seleccione un archivo de imagen válido (JPG, PNG, GIF, WebP).'
			};
		}

		if (archivo.size > TAMANO_MAXIMO_ARCHIVO) {
			return {
				valido: false,
				error: 'El archivo es demasiado grande. Máximo 5MB permitido.'
			};
		}

		return { valido: true, error: null };
	}

	/**
	 * Redirige a la página de platos
	 */
	function redirigirAPlatos() {
		window.location.href = baseUrl + '/platos';
	}

	/**
	 * Configura un modal para cerrar y redirigir
	 * @param {HTMLElement} modal - Elemento del modal
	 * @param {Object} config - Configuración de imagen (opcional)
	 */
	function configurarCierreModal(modal, config = null) {
		if (!modal) return;

		const btnCerrar = modal.querySelector('.btn-close');
		const btnCancelar = modal.querySelector('.btn-secondary');

		// Evento principal de cierre del modal
		modal.addEventListener('hidden.bs.modal', function() {
			if (config) {
				limpiarImagen(config);
			}
			redirigirAPlatos();
		});

		// Manejar botón de cerrar (X)
		if (btnCerrar) {
			btnCerrar.addEventListener('click', function(e) {
				e.preventDefault();
			});
		}

		// Manejar botón cancelar
		if (btnCancelar) {
			btnCancelar.addEventListener('click', function(e) {
				e.preventDefault();
			});
		}
	}

	// ====================== FUNCIONES DE MANEJO DE IMÁGENES ======================

	/**
	 * Muestra el preview de la imagen
	 * @param {Object} config - Configuración del modal
	 * @param {File} archivo - Archivo de imagen
	 * @param {string} src - URL de la imagen
	 */
	function mostrarPreview(config, archivo, src) {
		if (!config.imagenPreview || !config.nombreImagen || !config.tamanoImagen) return;

		config.imagenPreview.src = src;
		config.nombreImagen.textContent = archivo.name;
		config.tamanoImagen.textContent = formatearTamano(archivo.size);

		if (config.contenidoSubida && config.previewImagen) {
			config.contenidoSubida.style.display = 'none';
			config.previewImagen.style.display = 'flex';
		}

		config.campoImagen.style.cursor = 'default';
	}

	/**
	 * Limpia la imagen seleccionada
	 * @param {Object} config - Configuración del modal
	 */
	function limpiarImagen(config) {
		config.entradaImagen.value = '';
		
		if (config.imagenPreview) config.imagenPreview.src = '';
		if (config.nombreImagen) config.nombreImagen.textContent = '';
		if (config.tamanoImagen) config.tamanoImagen.textContent = '';

		if (config.previewImagen && config.contenidoSubida) {
			config.previewImagen.style.display = 'none';
			config.contenidoSubida.style.display = 'flex';
		}

		config.campoImagen.style.cursor = 'pointer';
		ocultarErrorImagen(config);
	}

	/**
	 * Muestra error de imagen
	 * @param {Object} config - Configuración del modal
	 * @param {string} mensaje - Mensaje de error
	 */
	function mostrarErrorImagen(config, mensaje) {
		if (!config.errorImagen) return;
		
		config.errorImagen.textContent = mensaje;
		config.errorImagen.style.display = 'block';
		config.campoImagen.classList.add('error');
	}

	/**
	 * Oculta error de imagen
	 * @param {Object} config - Configuración del modal
	 */
	function ocultarErrorImagen(config) {
		if (!config.errorImagen) return;
		
		config.errorImagen.style.display = 'none';
		config.campoImagen.classList.remove('error');
	}

	/**
	 * Procesa la imagen seleccionada
	 * @param {Object} config - Configuración del modal
	 * @param {File} archivo - Archivo de imagen
	 * @param {string} origen - Origen de la selección ('input' o 'drop')
	 * @param {File} archivoActual - Referencia al archivo actual
	 */
	function procesarImagen(config, archivo, origen, setArchivoActual) {
		const validacion = validarArchivo(archivo);
		
		if (!validacion.valido) {
			mostrarErrorImagen(config, validacion.error);
			return;
		}

		// Actualizar el input file si viene de drag & drop
		if (origen === 'drop') {
			const dataTransfer = new DataTransfer();
			dataTransfer.items.add(archivo);
			config.entradaImagen.files = dataTransfer.files;
		}

		setArchivoActual(archivo);

		// Usar FileReader para mostrar preview
		const reader = new FileReader();
		reader.onload = function(e) {
			mostrarPreview(config, archivo, e.target.result);
		};
		reader.onerror = function() {
			mostrarErrorImagen(config, 'Error al leer el archivo. Por favor, intente nuevamente.');
		};
		reader.readAsDataURL(archivo);
		
		ocultarErrorImagen(config);
	}

	// ====================== INICIALIZACIÓN DE MODALES ======================

	/**
	 * Inicializa la configuración de un modal
	 * @param {Object} config - Configuración del modal
	 */
	function inicializarModal(config) {
		if (!config.campoImagen || !config.entradaImagen) {
			console.error(`Elementos no encontrados para modal ${config.prefijo}`);
			return;
		}

		let archivoActual = null;

		// Función para actualizar archivoActual
		const setArchivoActual = (archivo) => { archivoActual = archivo; };

		// Configurar eventos de botones
		const eventos = [
			{ elemento: config.botonSubida, accion: () => config.entradaImagen.click() },
			{ elemento: config.botonCambiar, accion: () => config.entradaImagen.click() },
			{ elemento: config.botonEliminar, accion: () => limpiarImagen(config) }
		];

		eventos.forEach(({ elemento, accion }) => {
			if (elemento) {
				elemento.addEventListener('click', function(e) {
					e.stopPropagation();
					accion();
				});
			}
		});

		// Click en el área de imagen
		config.campoImagen.addEventListener('click', function(e) {
			if (e.target === config.campoImagen ||
				(e.target.closest('.contenido_subida_plato') && !e.target.closest('button'))) {
				config.entradaImagen.click();
			}
		});

		// Cambio en el input de archivo
		config.entradaImagen.addEventListener('change', function(e) {
			const archivo = e.target.files[0];
			if (archivo) {
				procesarImagen(config, archivo, 'input', setArchivoActual);
			}
		});

		// Configurar Drag and Drop
		const dragEvents = {
			dragover: (e) => {
				e.preventDefault();
				config.campoImagen.classList.add('dragover');
			},
			dragleave: (e) => {
				e.preventDefault();
				config.campoImagen.classList.remove('dragover');
			},
			drop: (e) => {
				e.preventDefault();
				config.campoImagen.classList.remove('dragover');
				
				const archivo = e.dataTransfer.files[0];
				if (archivo) {
					procesarImagen(config, archivo, 'drop', setArchivoActual);
				}
			}
		};

		Object.entries(dragEvents).forEach(([event, handler]) => {
			config.campoImagen.addEventListener(event, handler);
		});

		// Verificar archivo antes de enviar formulario
		const formulario = config.campoImagen.closest('form');
		if (formulario) {
			formulario.addEventListener('submit', function(e) {
				if (archivoActual && config.entradaImagen.files.length === 0) {
					const dataTransfer = new DataTransfer();
					dataTransfer.items.add(archivoActual);
					config.entradaImagen.files = dataTransfer.files;
				}
			});
		}
	}

	// Inicializar todos los modales
	configuraciones.forEach(inicializarModal);

	// ====================== CONFIGURACIÓN DE MODALES ======================

	// Modal de registro
	const modalRegistro = document.getElementById('modalPlato');
	configurarCierreModal(modalRegistro, configuraciones[0]);

	// Modal de edición
	const modalEdicion = document.getElementById('modalEditarPlato');
	configurarCierreModal(modalEdicion);

	// Modal de eliminación
	const modalEliminar = document.getElementById('modalEliminarPlato');
	if (modalEliminar) {
		modalEliminar.addEventListener('show.bs.modal', function(event) {
			const button = event.relatedTarget;
			const platoId = button.getAttribute('data-id');
			const nombrePlato = button.getAttribute('data-nombre');

			const eliminarId = document.getElementById('eliminarId');
			const mensajeEliminacion = document.getElementById('mensajeEliminacion');

			if (eliminarId) eliminarId.value = platoId;
			if (mensajeEliminacion) {
				mensajeEliminacion.textContent = 
					`¿Estás seguro que deseas eliminar el plato "${nombrePlato}"? Esta acción no se puede deshacer.`;
			}
		});
	}

	// ====================== MANEJO DE BOTONES PRINCIPALES ======================

	// Botón para abrir modal de registro
	const btnNuevoPlato = document.getElementById('anadirPlatoBtn');
	if (btnNuevoPlato) {
		btnNuevoPlato.addEventListener('click', function(e) {
			e.preventDefault();
			window.location.href = baseUrl + '/platos?ruta=registrar';
		});
	}

	// Botones de eliminar
	const botonesEliminar = document.querySelectorAll('.btn-eliminar');
	botonesEliminar.forEach(btn => {
		btn.addEventListener('click', function() {
			const platoId = this.getAttribute('data-id');
			const nombrePlato = this.getAttribute('data-nombre');

			// Configurar el modal de confirmación
			const eliminarId = document.getElementById('eliminarId');
			const mensajeEliminacion = document.getElementById('mensajeEliminacion');

			if (eliminarId) eliminarId.value = platoId;
			if (mensajeEliminacion) {
				mensajeEliminacion.textContent = 
					`¿Estás seguro que deseas eliminar el plato "${nombrePlato}"? Esta acción no se puede deshacer.`;
			}

			// Mostrar el modal
			const modal = new bootstrap.Modal(document.getElementById('modalEliminarPlato'));
			modal.show();
		});
	});

	// ====================== MOSTRAR MODALES SEGÚN PARÁMETRO URL ======================

	const mostrarModal = urlParams.get('mostrarModal');

	if (mostrarModal === 'registro' && modalRegistro) {
		const modal = new bootstrap.Modal(modalRegistro);
		modal.show();
	} else if (mostrarModal === 'edicion' && modalEdicion) {
		const modal = new bootstrap.Modal(modalEdicion);
		modal.show();
	} else {
		// Limpiar el parámetro de la URL sin recargar
		const cleanPath = baseUrl + window.location.pathname.substring(window.location.pathname.indexOf('/', 1));
		history.replaceState({}, document.title, cleanPath);
	}

	// ====================== LIMPIAR SESIÓN AL CARGAR ======================
	
	/**
	 * Oculta alertas después del timeout especificado
	 */
	function configurarAlertas() {
		const alertas = document.querySelectorAll('.alert');
		alertas.forEach(alerta => {
			setTimeout(() => {
				if (alerta.parentNode) {
					alerta.style.transition = 'opacity 0.3s ease-out';
					alerta.style.opacity = '0';
					setTimeout(() => {
						alerta.style.display = 'none';
					}, 300);
				}
			}, TIMEOUT_ALERTAS);
		});
	}

	/**
	 * Limpia parámetros de URL si es necesario
	 */
	function limpiarURL() {
		if (!urlParams.has('mostrarModal') && !urlParams.has('ruta')) {
			history.replaceState({}, document.title, window.location.pathname);
		}
	}

	// Ejecutar funciones de limpieza al cargar
	window.addEventListener('load', function() {
		configurarAlertas();
		limpiarURL();
	});

	// ====================== MANEJO DE ERRORES GLOBALES ======================
	
	// Manejo básico de errores para operaciones asíncronas
	window.addEventListener('error', function(e) {
		console.error('Error en la aplicación:', e.error);
	});

	// Prevenir errores de elementos no encontrados
	window.addEventListener('unhandledrejection', function(e) {
		console.error('Promise rechazada:', e.reason);
	});
});
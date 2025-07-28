// Actualiza tu archivo registroPedido.js

// Carrito local en memoria
let carritoLocal = [];
let numeroLibrePedido = 1;

document.addEventListener("DOMContentLoaded", function() {
    // Obtener el siguiente número de pedido desde la base de datos
    obtenerSiguienteNumeroPedido()
        .then(numero => {
            numeroLibrePedido = numero;
            document.getElementById("pedidoActual").textContent = `#${String(numeroLibrePedido).padStart(3, '0')}`;
            console.log("Número de pedido inicializado:", numeroLibrePedido);
        })
        .catch(error => {
            console.error("Error al obtener siguiente número de pedido:", error);
            // Si falla, mantener el valor por defecto
            document.getElementById("pedidoActual").textContent = `#${String(numeroLibrePedido).padStart(3, '0')}`;
        });
    
    // Inicializar vista
    actualizarVistaCarrito();
    actualizarTotalesLocales();
});

// Nueva función para obtener el siguiente número de pedido
function obtenerSiguienteNumeroPedido() {
    return fetch(contextPath + "/registrarPedido?ruta=obtenerSiguienteNumero", {
        method: "GET"
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al obtener siguiente número: " + response.status);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            return data.siguienteNumero;
        } else {
            throw new Error("Error en la respuesta del servidor");
        }
    });
}

// Actualizar la función procesarPedido para incrementar correctamente el número
function procesarPedido() {
    if (carritoLocal.length === 0) {
        alert("No hay productos en el carrito para procesar.");
        return;
    }
    
    if (!confirm("¿Confirmar el procesamiento del pedido? Una vez procesado no se puede modificar.")) {
        return;
    }
    
    console.log("Iniciando procesamiento del pedido...");
    
    // Paso 1: Crear pedido
    crearPedidoEnBD()
        .then(idPedido => {
            console.log("Pedido creado con ID:", idPedido);
            // Paso 2: Agregar todos los productos del carrito
            return agregarProductosAlPedido(idPedido);
        })
        .then(idPedido => {
            console.log("Todos los productos agregados correctamente");
            // Paso 3: Calcular totales finales
            return calcularTotalesFinales(idPedido);
        })
        .then(() => {
            alert("¡Pedido procesado exitosamente!");
            // Limpiar carrito local
            carritoLocal = [];
            
            // Obtener el siguiente número de pedido desde la BD
            return obtenerSiguienteNumeroPedido();
        })
        .then(nuevoNumero => {
            numeroLibrePedido = nuevoNumero;
            document.getElementById("pedidoActual").textContent = `#${String(numeroLibrePedido).padStart(3, '0')}`;
            actualizarVistaCarrito();
            actualizarTotalesLocales();
        })
        .catch(error => {
            console.error("Error al procesar pedido:", error);
            alert("Error al procesar el pedido. Inténtalo de nuevo.");
        });
}

// El resto de las funciones permanecen igual...
// (agregarProducto, actualizarVistaCarrito, aumentarCantidad, etc.)

// Función para agregar producto al carrito local
function agregarProducto(idPlato) {
    // Buscar información del plato desde el DOM
    const tarjetaPlato = event.target.closest('.tarjeta_plato');
    const nombrePlato = tarjetaPlato.querySelector('.nombre_plato').textContent;
    const precioTexto = tarjetaPlato.querySelector('.precio').textContent;
    const precio = parseFloat(precioTexto.replace('$', ''));
    const imagenUrl = tarjetaPlato.querySelector('.imagen_plato').src;
    
    console.log("Agregando producto al carrito local:", {idPlato, nombrePlato, precio});
    
    // Buscar si el producto ya existe en el carrito
    const productoExistente = carritoLocal.find(item => item.idPlato === idPlato);
    
    if (productoExistente) {
        // Si existe, aumentar cantidad
        productoExistente.cantidad++;
    } else {
        // Si no existe, agregarlo
        carritoLocal.push({
            idPlato: idPlato,
            nombrePlato: nombrePlato,
            precio: precio,
            cantidad: 1,
            imagenUrl: imagenUrl
        });
    }
    
    // Actualizar vista y totales
    actualizarVistaCarrito();
    actualizarTotalesLocales();
    
    console.log("Carrito actual:", carritoLocal);
}

// Función para actualizar la vista del carrito
function actualizarVistaCarrito() {
    const lista = document.getElementById("listaPedido");
    lista.innerHTML = "";
    
    if (carritoLocal.length === 0) {
        lista.innerHTML = "<p style='text-align: center; color: var(--color-gris); padding: 20px;'>No hay productos aún</p>";
        return;
    }
    
    carritoLocal.forEach((producto, index) => {
        const item = document.createElement("div");
        item.classList.add("item_pedido");
        item.innerHTML = `
            <div class="info_item">
                <p class="nombre_item">${producto.nombrePlato}</p>
                <div class="detalles_item">
                    <span class="precio_item">$${producto.precio.toFixed(2)}</span>
                    <div class="controles_cantidad">
                        <button class="btn_cantidad btn-minus" type="button" onclick="disminuirCantidad(${index})">
                            <i class="fas fa-minus"></i>
                        </button>
                        <span class="cantidad_numero">${producto.cantidad}</span>
                        <button class="btn_cantidad btn-plus" type="button" onclick="aumentarCantidad(${index})">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                </div>
            </div>
            <button class="btn_eliminar btn-trash" type="button" onclick="eliminarProducto(${index})">
                <i class="fas fa-times"></i>
            </button>
        `;
        
        lista.appendChild(item);
    });
}

// Función para aumentar cantidad
function aumentarCantidad(index) {
    carritoLocal[index].cantidad++;
    actualizarVistaCarrito();
    actualizarTotalesLocales();
    console.log("Cantidad aumentada:", carritoLocal[index]);
}

// Función para disminuir cantidad
function disminuirCantidad(index) {
    if (carritoLocal[index].cantidad > 1) {
        carritoLocal[index].cantidad--;
        actualizarVistaCarrito();
        actualizarTotalesLocales();
        console.log("Cantidad disminuida:", carritoLocal[index]);
    }
}

// Función para eliminar producto
function eliminarProducto(index) {
    if (confirm("¿Estás seguro de eliminar este producto?")) {
        const productoEliminado = carritoLocal.splice(index, 1)[0];
        actualizarVistaCarrito();
        actualizarTotalesLocales();
        console.log("Producto eliminado:", productoEliminado);
    }
}

// Función para limpiar carrito
function limpiarPedido() {
    if (confirm("¿Estás seguro de limpiar todo el pedido?")) {
        carritoLocal = [];
        actualizarVistaCarrito();
        actualizarTotalesLocales();
        console.log("Carrito limpiado");
    }
}

// Función para calcular totales locales
function actualizarTotalesLocales() {
    let subtotal = 0;
    
    carritoLocal.forEach(producto => {
        subtotal += producto.precio * producto.cantidad;
    });
    
    const IVA_RATE = 0.12;
    const impuesto = subtotal * IVA_RATE;
    const total = subtotal + impuesto;
    
    // Actualizar DOM
    document.getElementById("totalPago").innerHTML = `
        <div class="linea_total">
            <span>Subtotal</span>
            <span>$${subtotal.toFixed(2)}</span>
        </div>
        <div class="linea_total">
            <span>IVA (12%)</span>
            <span>$${impuesto.toFixed(2)}</span>
        </div>
        <div class="linea_total total_final">
            <span>Total</span>
            <span>$${total.toFixed(2)}</span>
        </div>
    `;
}

// Función auxiliar para crear pedido en BD
function crearPedidoEnBD() {
    const formData = new FormData();
    formData.append("ruta", "registrar");
    
    return fetch(contextPath + "/registrarPedido?ruta=registrar", {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al crear pedido: " + response.status);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            return data.idPedido;
        } else {
            throw new Error("Error en la respuesta del servidor");
        }
    });
}

// Función auxiliar para agregar productos al pedido
function agregarProductosAlPedido(idPedido) {
    const promesas = carritoLocal.map(producto => {
        const formData = new FormData();
        formData.append("idPedido", idPedido);
        formData.append("idPlato", producto.idPlato);
        formData.append("cantidad", producto.cantidad);
        
        return fetch(contextPath + "/registrarPedido?ruta=ordenar", {
            method: "POST",
            body: formData
        })
        .then(response => {
            if (!response.ok) {
                throw new Error(`Error al agregar producto ${producto.nombrePlato}: ${response.status}`);
            }
            console.log(`Producto ${producto.nombrePlato} agregado correctamente`);
        });
    });
    
    return Promise.all(promesas).then(() => idPedido);
}

// Función auxiliar para calcular totales finales
function calcularTotalesFinales(idPedido) {
    const formData = new FormData();
    formData.append("idPedido", idPedido);
    
    return fetch(contextPath + "/registrarPedido?ruta=sumaTotal", {
        method: "POST",
        body: formData
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Error al calcular totales: " + response.status);
        }
        return response.json();
    })
    .then(data => {
        if (data.success) {
            console.log("Totales calculados:", data);
            return data;
        } else {
            throw new Error("Error al calcular totales");
        }
    });
}

// Event listener para el botón "Procesar Pedido"
document.addEventListener("DOMContentLoaded", function() {
    // Cambiar el comportamiento del botón de procesar pedido
    const btnOrdenar = document.querySelector('.btn_ordenar');
    if (btnOrdenar) {
        btnOrdenar.addEventListener('click', function(e) {
            e.preventDefault();
            procesarPedido();
        });
    }
});
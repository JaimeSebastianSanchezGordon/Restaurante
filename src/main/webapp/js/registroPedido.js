document.addEventListener("DOMContentLoaded", function() {
document.getElementById("formCrearPedido").addEventListener("submit", function(event) {
    event.preventDefault(); // Evita recarga
console.log("JS")
    const formData = new FormData(this);

    fetch(contextPath + "/registrarPedido?ruta=registrar", {
        method: "POST",
        body: formData
    })
    .then(response => 	{
	    if (!response.ok) {
	        throw new Error("Error en la solicitud: " + response.status);
	    }
	    return response.json();
	}) // Espera un JSON que contenga el ID
    .then(data => {
        if (data.success) {
            // Mostrar ID en el DOM
            document.getElementById("pedidoActual").innerHTML = 
                "<h3>ID Pedido:</h3> " + data.idPedido;

            // Guardar el ID en una variable global para usarlo después
            window.pedidoId = data.idPedido;
			console.log("Pedido registrado:", data);
        } else {
            alert("Error al crear pedido");
        }
    })
    .catch(error => console.error("Error:", error));
});
});

function agregarProducto(idPlato){
	if(!window.pedidoId){
		alert("Primero debes crear un pedido");
		return;
	}
	console.log("ID a enviar:", window.pedidoId);

	const formData = new FormData();
	formData.append("idPedido", window.pedidoId);
	formData.append("idPlato", idPlato);
	formData.append("cantidad", 1);
	
	fetch(contextPath + "/registrarPedido?ruta=ordenar", {
	        method: "POST",
	        body: formData
	})
	.then(response => 	{
		if (!response.ok) {
			throw new Error("Error en la solicitud: " + response.status);
		}
		console.log("Plato agregado correctamente");
		actualizarListaPedido();
		actualizarSumaTotal();
	})
	.catch(error => {
			console.error("Error al agregar plato:", error);
			alert("Error al agregar plato");
	});
	/*
	.then(data => {
		if (data.success) {
		    // Mostrar ID en el DOM
		    document.getElementById(" ").innerHTML = 
		        "<h3>Nombre:</h3> " + data.nombre + "<br>" +
				"<p>Precio: </p>" + data.precio + "<br>" +
				"<p>Cantidad: </p>" + data.cantidad;

		      // Guardar el ID en una variable global para usarlo después
		    window.pedidoId = data.idPedido;
			console.log("Pedido registrado:", data);
		} else {
		    alert("Error al crear pedido");
		}
	});
	*/
}

function actualizarListaPedido(){
	if(!window.pedidoId) return;
	
	const formData = new FormData();
	formData.append("idPedido", window.pedidoId);
	
	fetch(contextPath + "/registrarPedido?ruta=listarPedidos", {
		   method: "POST",
		   body: formData
	})
	.then(response => 	{
		if (!response.ok) {
			throw new Error("Error en la solicitud: " + response.status);
		}
			return response.json();
	})
	.then(detallesPedido => {
		const lista = document.getElementById("listaPedido");
		lista.innerHTML = "";
		
		if(detallesPedido.length === 0){
			lista.innerHTML = "<p>No hay productos aun </p>";
			return
		}
		
		detallesPedido.forEach(d => {
			const item = document.createElement("div");
			item.innerHTML = `
			<div class="Pedido">
				<div class="nombrePrecio-derecha">
					<div>
						<p>${d.nombrePlato}</p>
					</div>
					<div>
						<p>$${d.precioPlato}</p>
					</div>
					<div class="cantidad-control">
						<div class="btn-minus" style="cursor:pointer;"><i class="fa-solid fa-minus"></i></div>
						<div class="aparte"><p class="cantidad">x${d.cantidad}</p></div>
						<div class="btn-plus" style="cursor:pointer;"><i class="fa-solid fa-plus" style="color: #ffffff;"></i></div>
					</div>
				</div>
			</div>
		 `;
	 	const plusBtn = item.querySelector(".btn-plus");
	 	const minusBtn = item.querySelector(".btn-minus");
	 	const cantidadText = item.querySelector(".cantidad");

	 	plusBtn.addEventListener("click", () => {
	 		d.cantidad++;
	 		cantidadText.textContent = `x${d.cantidad}`;
			console.log("Boton preionado")
			
			const formData = new FormData();
			formData.append("idDetallePedido", d.id);
			formData.append("accion", "incrementar");
				
			fetch(contextPath + "/registrarPedido?ruta=modificarCantidad", {
				   method: "POST",
				   body: formData
			})
			.then(response => 	{
					if (!response.ok) {
						throw new Error("Error en la solicitud: " + response.status);
					}
					console.log("Cantidad incrementada correctamente");
					actualizarSumaTotal();
			})
			.catch(error => {
					console.error("Error al incrementar cantidad:", error);
					alert("Error al agregar cantidad");
			});
	 	});

	 	minusBtn.addEventListener("click", () => {
	 		if (d.cantidad > 1) {
	 			d.cantidad--;
	 			cantidadText.textContent = `x${d.cantidad}`;
				const formData = new FormData();
				formData.append("idDetallePedido", d.id);
				formData.append("accion", "disminuir");
								
				fetch(contextPath + "/registrarPedido?ruta=modificarCantidad", {
					method: "POST",
					body: formData
				})
				.then(response => 	{
					if (!response.ok) {
						throw new Error("Error en la solicitud: " + response.status);
					}
					console.log("Cantidad Decrementada correctamente");
					actualizarSumaTotal();
				})
				.catch(error => {
					console.error("Error al decrementar cantidad:", error);
					alert("Error al restar cantidad");
				});
	 		}
	 	});
		
			lista.appendChild(item);
		});
	})
	.catch(err => console.error("Error al listar detalles:", err));
}

function actualizarSumaTotal(){
	if(!window.pedidoId) return;
	
	const formData = new FormData();
		formData.append("idPedido", window.pedidoId);
		
		fetch(contextPath + "/registrarPedido?ruta=sumaTotal", {
			   method: "POST",
			   body: formData
		})
		.then(response => 	{
			if (!response.ok) {
				throw new Error("Error en la solicitud: " + response.status);
			}
				return response.json();
		})
		.then(data => {
		   	if (data.success) {
		   		// Mostrar ID en el DOM
				document.getElementById("totalPago").innerHTML = `
				  <div>
				    	<p><strong>Subtotal:</strong> $${data.subtotal.toFixed(2)}</p>
				    	<p><strong>Impuesto:</strong> $${data.impuesto.toFixed(2)}</p> 
				    	<p><strong>Total:</strong> $${data.total.toFixed(2)}</p>
				  </div>
				`;

		   } else {
	            alert("Error al sumar pedido");
	       }
		    })
		    .catch(error => console.error("Error:", error));
}
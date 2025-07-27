package controlador;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.Gson;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.JPA.JPADetallePedidoDAO;
import modelo.JPA.JPAPedidoDAO;
import modelo.JPA.JPAPlatoDAO;
import modelo.dao.DetallePedidoDAO;
import modelo.dao.PedidoDAO;
import modelo.dao.PlatoDAO;
import modelo.dto.DetallePedidoDTO;
import modelo.entity.DetallePedido;
import modelo.entity.Pedido;
import modelo.entity.Plato;


@MultipartConfig
@WebServlet(name = "RegistroPedidoController", urlPatterns = {"/registrarPedido"})
public class RegistrarPedidoController extends HttpServlet{
	private static final Logger LOGGER = Logger.getLogger(GestorPlatosController.class.getName());
	private PedidoDAO pedidoDAO = new JPAPedidoDAO();
	private PlatoDAO platoDAO = new JPAPlatoDAO();
	
	private DetallePedidoDAO detallePedidoDAO = new JPADetallePedidoDAO();

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		this.ruteador(req, resp);
	}
	
	private void ruteador(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
		String ruta = req.getParameter("ruta");
        if (ruta == null) ruta = "listarPedidos";
		
        switch (ruta) {
        case "registrar" -> registrarPedido(req, resp);
        case "ordenar" -> ordenar(req, resp);
        case "listarPedidos" -> listarPedidos(req, resp);
        case "modificarCantidad" -> modificarCantidad(req, resp);
        case "sumaTotal" -> sumaTotal(req, resp);
        default -> {
            LOGGER.warning("Ruta no reconocida: " + ruta);
            listarPedidos(req, resp);
        }
    }
	}
	
	private void registrarPedido(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		
		Pedido pedido = new Pedido();
		pedido.setEstado("En proceso");
		pedido.setFormaPago("Pendiente");
		System.out.println("Paso por registrar pedido");
		Long idPedido = pedidoDAO.registrarPedido(pedido);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
	    out.print("{\"success\": true, \"idPedido\": " + idPedido + "}");
	    out.flush();
		
	}
	
	private void ordenar(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		int idPedido = Integer.parseInt(req.getParameter("idPedido"));
		int idPlato = Integer.parseInt(req.getParameter("idPlato"));
		int cantidad = Integer.parseInt(req.getParameter("cantidad"));
		
		System.out.println("Valores: " + idPedido + ", " + idPlato + ", " + cantidad);
		
		DetallePedido detallePedido = new DetallePedido();
		detallePedido.setIdPed(idPedido);
		detallePedido.setIdPla(idPlato);
		detallePedido.setCantidad(cantidad);
		
		detallePedidoDAO.agregarDetallePedido(detallePedido);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	private void listarPedidos(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		int idPedido = Integer.parseInt(req.getParameter("idPedido"));
		
		System.out.println("idPedido: " + idPedido);
		
		List<DetallePedido> detallesPedido = detallePedidoDAO.obtenerDetallesPorPedidoPed(Long.valueOf(idPedido));
		if(detallesPedido.get(0) == null) {
			System.out.println("No hay lista");
		}
		
		List<DetallePedidoDTO> listaDTO = new ArrayList<>();
		
		for(DetallePedido detalle : detallesPedido) {
			Plato plato = platoDAO.obtenerPlato(Long.valueOf(detalle.getIdPla()));
			DetallePedidoDTO detalleDTO = new DetallePedidoDTO(plato.getNombrePlato(), plato.getPrecio(), detalle.getCantidad(), Long.valueOf(detalle.getId()));
			listaDTO.add(detalleDTO);
		}
	
		// Convierte la lista en JSON )
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        resp.getWriter().write(gson.toJson(listaDTO));
	}
	
	private void modificarCantidad(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		int idPedido = Integer.parseInt(req.getParameter("idDetallePedido"));
        String accion = req.getParameter("accion");
        
        DetallePedido detallePedido = detallePedidoDAO.obtenerDetallePedido(Long.valueOf(idPedido));
        
        if (detallePedido != null) {
            int cantidadActual = detallePedido.getCantidad();
            
            if ("incrementar".equals(accion)) {
                detallePedido.setCantidad(cantidadActual + 1);
            } else if ("disminuir".equals(accion) && cantidadActual > 1) {
                detallePedido.setCantidad(cantidadActual - 1);
            }

            detallePedidoDAO.actualizarDetallePedido(detallePedido);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("Cantidad actualizada");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write("Detalle no encontrado");
        }
        
        
	}
	
	private void sumaTotal(HttpServletRequest req, HttpServletResponse resp) 
			throws ServletException, IOException{
		double subtotal = 0.0, impuesto = 0.0, total = 0.0, IVA = 0.12;
		int idPedido = Integer.parseInt(req.getParameter("idPedido"));
		
		List<DetallePedido> detallesPedido = detallePedidoDAO.obtenerDetallesPorPedidoPed(Long.valueOf(idPedido));
		
		for(DetallePedido detalle : detallesPedido) {
			Plato plato = platoDAO.obtenerPlato(Long.valueOf(detalle.getIdPla()));
			double precioPlato = plato.getPrecio();
			int cantidad = detalle.getCantidad();
			
			subtotal += precioPlato * cantidad;
		}
		
		impuesto = subtotal * IVA;
		total = subtotal + impuesto;
		Pedido pedido = pedidoDAO.getPedidoById(Long.valueOf(idPedido));
		pedidoDAO.actualizarPedidoConTotal(pedido, total);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
	    out.print("{\"success\": true, \"subtotal\": " + subtotal + ", \"impuesto\": " + impuesto + ", \"total\": " + total + "}");
	    out.flush();
	}
	
}

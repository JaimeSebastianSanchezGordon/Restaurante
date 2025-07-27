package controlador;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.JPA.JPAPedidoDAO;
import modelo.JPA.JPADetallePedidoDAO;
import modelo.dao.PedidoDAO;
import modelo.dao.DetallePedidoDAO;
import modelo.entity.Pedido;
import modelo.entity.DetallePedido;
import modelo.entity.Usuario;

@WebServlet("/GestionarPedido")
public class gestionPedidosController extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private PedidoDAO pedidoDAO;
    private DetallePedidoDAO detallePedidoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        pedidoDAO = new JPAPedidoDAO();
        detallePedidoDAO = new JPADetallePedidoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.rutear(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.rutear(request, response);
    }

    private void rutear(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutorizado");

        if (usuario == null) {
        String ruta = (request.getParameter("ruta") == null) ? "listar" : request.getParameter("ruta");

            switch (ruta) {
                case "listar":
                    this.listarPedidos(request, response);
                    break;
                case "verDetalle":
                    this.verDetallePedido(request, response);
                    break;
                case "actualizarPedido":
                    this.mostrarFormularioActualizar(request, response);
                    break;
                case "aceptarCambios":
                    this.aceptarCambios(request, response);
                    break;
                case "eliminar":
                    this.eliminarPedido(request, response);
                    break;
                case "ejecutar":
                    this.ejecutarPedido(request, response);
                    break;
                case "eliminarPlato":
                    this.eliminarPlato(request, response);
                    break;
                default:
                    this.listarPedidos(request, response);
                    break;
            }
        } else {
            response.sendRedirect("ingreso");
        }
    }

    private void listarPedidos(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Pedido> pedidos = pedidoDAO.obtenerTodosLosPedidos();
        request.setAttribute("pedidos", pedidos);

        if (!pedidos.isEmpty()) {
            Long idPedidoSeleccionado = obtenerIdPedidoSeleccionado(request, pedidos);
            cargarDetallePedido(request, idPedidoSeleccionado);
        }

        getServletContext().getRequestDispatcher("/jsp/gestionPedidos.jsp").forward(request, response);
    }

    private Long obtenerIdPedidoSeleccionado(HttpServletRequest request, List<Pedido> pedidos) {
        String idPedidoParam = request.getParameter("idPedido");
        try {
            return (idPedidoParam != null && !idPedidoParam.isEmpty()) ?
                    Long.parseLong(idPedidoParam) : pedidos.get(0).getIdPedido();
        } catch (NumberFormatException e) {
            return pedidos.get(0).getIdPedido();
        }
    }

    private void verDetallePedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Pedido> pedidos = pedidoDAO.obtenerTodosLosPedidos();
        request.setAttribute("pedidos", pedidos);

        String idPedidoParam = request.getParameter("idPedido");
        if (idPedidoParam != null && !idPedidoParam.isEmpty()) {
            try {
                cargarDetallePedido(request, Long.parseLong(idPedidoParam));
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de pedido inválido");
            }
        }

        getServletContext().getRequestDispatcher("/jsp/gestionPedidos.jsp").forward(request, response);
    }

    private void cargarDetallePedido(HttpServletRequest request, Long idPedidoSeleccionado) {
        Pedido pedidoSeleccionado = pedidoDAO.getPedidoById(idPedidoSeleccionado);
        List<DetallePedido> detallesPedido = detallePedidoDAO.obtenerDetallesPorPedido(idPedidoSeleccionado);

        request.setAttribute("pedidoSeleccionado", pedidoSeleccionado);
        request.setAttribute("detallesPedido", detallesPedido);
    }

    private void ejecutarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoParam = request.getParameter("idPedido");
        if (idPedidoParam != null && !idPedidoParam.isEmpty()) {
            try {
                pedidoDAO.marcarPedidoComoPagado(Long.parseLong(idPedidoParam));
                request.setAttribute("mensaje", "Pedido ejecutado correctamente");
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de pedido inválido");
            }
        }
        this.listarPedidos(request, response);
    }

    private void mostrarFormularioActualizar(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoParam = request.getParameter("idPedido");
        if (idPedidoParam == null || idPedidoParam.isEmpty()) {
            request.setAttribute("error", "ID de pedido no proporcionado");
            this.listarPedidos(request, response);
            return;
        }

        try {
            Long idPedido = Long.parseLong(idPedidoParam);
            Pedido pedido = pedidoDAO.getPedidoById(idPedido);
            if (pedido == null) {
                request.setAttribute("error", "Pedido no encontrado");
                this.listarPedidos(request, response);
                return;
            }

            request.setAttribute("pedido", pedido);
            request.setAttribute("detallesPedido", detallePedidoDAO.obtenerDetallesPorPedido(idPedido));
            getServletContext().getRequestDispatcher("/jsp/actualizarPedido.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID de pedido inválido");
            this.listarPedidos(request, response);
        }
    }

    private void aceptarCambios(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoParam = request.getParameter("idPedido");
        if (idPedidoParam == null || idPedidoParam.isEmpty()) {
            request.setAttribute("error", "ID de pedido no proporcionado");
            this.mostrarFormularioActualizar(request, response);
            return;
        }

        try {
            Long idPedido = Long.parseLong(idPedidoParam);
            Pedido pedido = pedidoDAO.getPedidoById(idPedido);

            if (pedido == null) {
                request.setAttribute("error", "Pedido no encontrado");
            } else if (pedido.getEstado().equals("Pagado")) {
                request.setAttribute("error", "No se puede actualizar un pedido que ya está pagado");
            } else {
                actualizarPedidoYDetalles(request, idPedido);
                response.sendRedirect("GestionarPedido?ruta=verDetalle&idPedido=" + idPedido);
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error al actualizar el pedido: " + e.getMessage());
            e.printStackTrace();
        }

        this.mostrarFormularioActualizar(request, response);
    }

    private void actualizarPedidoYDetalles(HttpServletRequest request, Long idPedido) {
        Pedido pedido = new Pedido();
        pedido.setIdPedido(idPedido);
        pedido.setNumMesa(Integer.parseInt(request.getParameter("numMesa")));
        pedido.setInvitados(Integer.parseInt(request.getParameter("invitados")));
        pedido.setFormaPago(request.getParameter("formaPago"));
        pedido.setNombreCliente(request.getParameter("nombreCliente"));

        Map<String, String[]> parametros = request.getParameterMap();
        for (String nombreParam : parametros.keySet()) {
            if (nombreParam.startsWith("cantidades[")) {
                try {
                    String idDetalleStr = nombreParam.substring(nombreParam.indexOf('[') + 1, nombreParam.indexOf(']'));
                    Long idDetalle = Long.parseLong(idDetalleStr);
                    int nuevaCantidad = Integer.parseInt(request.getParameter(nombreParam));

                    detallePedidoDAO.actualizarCantidadDetalle(idDetalle, nuevaCantidad);
                } catch (Exception e) {
                    System.err.println("Error procesando cantidad: " + nombreParam);
                    e.printStackTrace();
                }
            }
        }
        double nuevoTotal = detallePedidoDAO.calcularTotalPedido(idPedido);
        pedido.setCantidadPagar(nuevoTotal);
        pedidoDAO.actualizarPedido(pedido);
    }

    private void eliminarPlato(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Long idPedido = Long.parseLong(request.getParameter("idPedido"));
            Long idDetalle = Long.parseLong(request.getParameter("idDetalle"));

            detallePedidoDAO.eliminarDetallePedido(idDetalle);

            response.sendRedirect("GestionarPedido?ruta=actualizarPedido&idPedido=" + idPedido);
        } catch (Exception e) {
            request.setAttribute("error", "Error al eliminar el plato: " + e.getMessage());
            this.mostrarFormularioActualizar(request, response);
        }
    }

    private void eliminarPedido(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idPedidoParam = request.getParameter("idPedido");
        if (idPedidoParam != null && !idPedidoParam.isEmpty()) {
            try {
                pedidoDAO.eliminarPedido(Long.parseLong(idPedidoParam));
                request.setAttribute("mensaje", "Pedido eliminado correctamente");
            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID de pedido inválido");
            }
        } else {
            request.setAttribute("error", "ID de pedido no proporcionado");
        }
        this.listarPedidos(request, response);
    }
}
package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.JPA.JPAEstadoPedidoDAO;
import modelo.dao.EstadoPedidoDAO;
import modelo.dto.EstadoPedidoDTO;

import java.io.IOException;
import java.util.List;

@WebServlet("/EstadoPedido")
public class EstadoPedidoController extends HttpServlet{
    private static final long serialVersionUID = 1L;
    private EstadoPedidoDAO estadoPedidoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        estadoPedidoDAO = new JPAEstadoPedidoDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.rutear(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        this.rutear(request, response);
    }

    private void rutear(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String ruta = request.getParameter("ruta") != null ? request.getParameter("ruta") : "verEstadoOrden";

        switch (ruta) {
            case "verEstadoOrden":
                this.verEstadoOrden(request, response);
                break;
            case "verEstadoPedidoPorId":
                this.verEstadoPedidoPorId(request, response);
                break;
            default:
                this.verEstadoOrden(request, response);
                break;
        }
    }

    private void verEstadoOrden(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<EstadoPedidoDTO> estadoPedidos = estadoPedidoDAO.obtenerPedidos();
        request.setAttribute("estadoPedidos", estadoPedidos);

        getServletContext().getRequestDispatcher("/jsp/estadoPedido.jsp").forward(request, response);
    }

    private void verEstadoPedidoPorId(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idPedido = request.getParameter("idPedido");
        if (idPedido != null && !idPedido.isEmpty()) {
            try {
                int idPedidoInt = Integer.parseInt(idPedido);
                EstadoPedidoDTO estadoPedido = estadoPedidoDAO.obtenerPedidoPorId(idPedidoInt);

                if (estadoPedido == null) {
                    request.setAttribute("error", "No se encontró ningún pedido: " + idPedido);
                    // Opcional: puedes mantener el listado de pedidos aunque la búsqueda falle
                    verEstadoOrden(request, response);
                } else {
                    request.setAttribute("estadoPedido", estadoPedido);
                }

            } catch (NumberFormatException e) {
                request.setAttribute("error", "ID inválido. Debe ser un número.");
                verEstadoOrden(request, response);
            }
        } else {
            request.setAttribute("error", "Debe ingresar un ID de pedido.");
            verEstadoOrden(request, response);
        }

        getServletContext().getRequestDispatcher("/jsp/estadoPedido.jsp").forward(request, response);
    }
}



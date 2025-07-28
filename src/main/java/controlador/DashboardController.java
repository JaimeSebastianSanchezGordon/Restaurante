package controlador;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.JPA.JPADashboardDAO;
import modelo.dao.DashboardDAO;
import modelo.entity.Pedido;
import modelo.entity.Plato;
import modelo.entity.Usuario;

@WebServlet("/Dashboard")
public class DashboardController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private DashboardDAO dashboardDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        dashboardDAO = new JPADashboardDAO();
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

        HttpSession sesion = request.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutorizado");

        if (usuario != null) {
        String ruta = (request.getParameter("ruta") == null) ? "dashboard" : request.getParameter("ruta");

            switch (ruta) {
                case "dashboard":
                    this.mostrarDashboard(request, response);
                    break;
                case "filtrarHoy":
                    this.filtrarPorHoy(request, response);
                    break;
                case "filtrarSemana":
                    this.filtrarPorSemana(request, response);
                    break;
                case "filtrarMes":
                    this.filtrarPorMes(request, response);
                    break;
                case "filtrarAno":
                    this.filtrarPorAno(request, response);
                    break;
                case "obtenerDatosGrafico":
                    this.obtenerDatosGrafico(request, response);
                    break;
                default:
                    this.mostrarDashboard(request, response);
                    break;
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/ingreso");
        }
    }

    private void mostrarDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Por defecto mostrar datos del mes actual
        this.filtrarPorMes(request, response);
    }

    private void filtrarPorHoy(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LocalDate hoy = LocalDate.now();
        cargarDatosDashboard(request, hoy, hoy, "Hoy");
        getServletContext().getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }

    private void filtrarPorSemana(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioSemana = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
        cargarDatosDashboard(request, inicioSemana, hoy, "Esta Semana");
        getServletContext().getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }

    private void filtrarPorMes(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        cargarDatosDashboard(request, inicioMes, hoy, "Este Mes");
        getServletContext().getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }

    private void filtrarPorAno(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LocalDate hoy = LocalDate.now();
        LocalDate inicioAno = hoy.withDayOfYear(1);
        cargarDatosDashboard(request, inicioAno, hoy, "Este Año");
        getServletContext().getRequestDispatcher("/jsp/dashboard.jsp").forward(request, response);
    }

 // En el método cargarDatosDashboard, agregar validaciones:
    private void cargarDatosDashboard(HttpServletRequest request, LocalDate fechaInicio, 
                                     LocalDate fechaFin, String periodo) {
        try {
            // Obtener datos con valores por defecto
            Double ingresoTotal = dashboardDAO.obtenerIngresoTotal(fechaInicio, fechaFin);
            ingresoTotal = (ingresoTotal != null) ? ingresoTotal : 0.0;
            
            Integer totalPedidos = dashboardDAO.obtenerTotalPedidos(fechaInicio, fechaFin);
            totalPedidos = (totalPedidos != null) ? totalPedidos : 0;
            
            Double porcentajeCrecimiento = dashboardDAO.obtenerPorcentajeCrecimiento(fechaInicio, fechaFin);
            porcentajeCrecimiento = (porcentajeCrecimiento != null) ? porcentajeCrecimiento : 0.0;
            
            // Calcular promedio
            Double promedioVenta = totalPedidos > 0 ? ingresoTotal / totalPedidos : 0.0;
            
            // Obtener listas con verificación
            List<Object[]> ventasDiarias = dashboardDAO.obtenerVentasDiarias(fechaInicio, fechaFin);
            if (ventasDiarias == null) ventasDiarias = new ArrayList<>();
            
            List<Object[]> platosPopulares = dashboardDAO.obtenerPlatosPopulares(fechaInicio, fechaFin);
            if (platosPopulares == null) platosPopulares = new ArrayList<>();
            
            Map<String, Double> distribucionCategorias = dashboardDAO.obtenerDistribucionCategorias(fechaInicio, fechaFin);
            if (distribucionCategorias == null) {
                distribucionCategorias = new HashMap<>();
                distribucionCategorias.put("Comida", 0.0);
                distribucionCategorias.put("Bebida Fría", 0.0);
                distribucionCategorias.put("Otros", 0.0);
            }
            
            // Setear atributos
            request.setAttribute("ingresoTotal", ingresoTotal);
            request.setAttribute("totalPedidos", totalPedidos);
            request.setAttribute("promedioVenta", promedioVenta);
            request.setAttribute("porcentajeCrecimiento", porcentajeCrecimiento);
            request.setAttribute("ventasDiarias", ventasDiarias);
            request.setAttribute("platosPopulares", platosPopulares);
            request.setAttribute("distribucionCategorias", distribucionCategorias);
            request.setAttribute("periodoSeleccionado", periodo);
            request.setAttribute("fechaInicio", fechaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            request.setAttribute("fechaFin", fechaFin.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            
            // Debug en consola
            System.out.println("=== DEBUG DASHBOARD ===");
            System.out.println("Período: " + periodo);
            System.out.println("Ingreso Total: " + ingresoTotal);
            System.out.println("Total Pedidos: " + totalPedidos);
            System.out.println("Ventas Diarias count: " + ventasDiarias.size());
            System.out.println("Platos Populares count: " + platosPopulares.size());
            System.out.println("Distribución: " + distribucionCategorias);
            System.out.println("=====================");
            
        } catch (Exception e) {
            request.setAttribute("error", "Error al cargar los datos del dashboard: " + e.getMessage());
            e.printStackTrace();
            
            // Setear valores por defecto en caso de error
            request.setAttribute("ingresoTotal", 0.0);
            request.setAttribute("totalPedidos", 0);
            request.setAttribute("promedioVenta", 0.0);
            request.setAttribute("porcentajeCrecimiento", 0.0);
            request.setAttribute("ventasDiarias", new ArrayList<>());
            request.setAttribute("platosPopulares", new ArrayList<>());
            
            Map<String, Double> defaultDistribucion = new HashMap<>();
            defaultDistribucion.put("Comida", 0.0);
            defaultDistribucion.put("Bebida Fría", 0.0);
            defaultDistribucion.put("Otros", 0.0);
            request.setAttribute("distribucionCategorias", defaultDistribucion);
        }
    }

    private void obtenerDatosGrafico(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Endpoint para obtener datos vía AJAX si es necesario
        String periodo = request.getParameter("periodo");
        LocalDate fechaInicio, fechaFin;
        LocalDate hoy = LocalDate.now();

        switch (periodo) {
            case "hoy":
                fechaInicio = fechaFin = hoy;
                break;
            case "semana":
                fechaInicio = hoy.minusDays(hoy.getDayOfWeek().getValue() - 1);
                fechaFin = hoy;
                break;
            case "ano":
                fechaInicio = hoy.withDayOfYear(1);
                fechaFin = hoy;
                break;
            default: // mes
                fechaInicio = hoy.withDayOfMonth(1);
                fechaFin = hoy;
                break;
        }

        List<Object[]> ventasDiarias = dashboardDAO.obtenerVentasDiarias(fechaInicio, fechaFin);
        
        // Convertir a JSON y enviar
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < ventasDiarias.size(); i++) {
            Object[] venta = ventasDiarias.get(i);
            json.append("{\"fecha\":\"").append(venta[0]).append("\",\"total\":").append(venta[1]).append("}");
            if (i < ventasDiarias.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        
        response.getWriter().write(json.toString());
    }
}
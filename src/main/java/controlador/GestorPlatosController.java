package controlador;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import modelo.JPA.JPAPlatoDAO;
import modelo.entity.Plato;
import modelo.entity.Usuario;
import service.ImagenService;

@WebServlet(name = "PlatoController", urlPatterns = {"/platos"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 5,       // 5MB
        maxRequestSize = 1024 * 1024 * 10    // 10MB
)
public class GestorPlatosController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(GestorPlatosController.class.getName());

    // Atributos de sesión para manejo de errores
    private static final String ATTR_DATOS_PLATO = "datosPlato";
    private static final String ATTR_ERROR_REGISTRO = "errorRegistroPlato";
    private static final String ATTR_PLATO_EDICION = "platoEdicion";
    private static final String ATTR_ERROR_EDICION = "errorEdicionPlato";

    private final JPAPlatoDAO platoDAO = new JPAPlatoDAO();
    private final ImagenService imagenService = new ImagenService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        this.ruteador(req, resp);
    }

    private void ruteador(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        HttpSession sesion = req.getSession();
        Usuario usuario = (Usuario) sesion.getAttribute("usuarioAutorizado");
       if (usuario != null){
            String ruta = req.getParameter("ruta");
            if (ruta == null) ruta = "listar";

            switch (ruta) {
                case "registrar" -> presentarFormularioRegistroPlato(req, resp);
                case "editar" -> presentarFormularioEdicionPlato(req, resp);
                case "listar" -> listarPlatos(req, resp);
                case "guardar" -> guardarPlato(req, resp);
                case "eliminar" -> eliminarPlato(req, resp);
                case "modificar" -> modificarPlato(req, resp);
                default -> {
                    LOGGER.warning("Ruta no reconocida: " + ruta);
                    listarPlatos(req, resp);
                }
            }
       } else {
           resp.sendRedirect(req.getContextPath() + "/ingreso");
       }
    }

    private void presentarFormularioRegistroPlato(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + "/jsp/GestionPlato.jsp?mostrarModal=registro");
    }

    private void presentarFormularioEdicionPlato(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            String id = req.getParameter("id");
            if (id == null || id.trim().isEmpty()) {
                throw new IllegalArgumentException("ID de plato requerido");
            }

            Plato plato = platoDAO.obtenerPlato(Long.parseLong(id));
            if (plato == null) {
                throw new IllegalArgumentException("Plato no encontrado");
            }

            HttpSession session = req.getSession();
            session.setAttribute(ATTR_PLATO_EDICION, plato);

            resp.sendRedirect(req.getContextPath() + "/jsp/GestionPlato.jsp?mostrarModal=edicion");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error al presentar formulario de edición", e);
            resp.sendRedirect(req.getContextPath() + "/platos?ruta=listar");
        }
    }

    private void listarPlatos(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Limpiar atributos específicos de la sesión
        limpiarAtributosSesion(req.getSession(false));

        // Obtener platos
        List<Plato> platos = platoDAO.obtenerTodosPlatos();
        req.setAttribute("platos", platos);
        req.getRequestDispatcher("/jsp/GestionPlato.jsp").forward(req, resp);
    }

    private void guardarPlato(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            // Extraer y validar datos del formulario
            DatosPlato datos = extraerDatosFormulario(req);

            // Procesar imagen
            String rutaImagen = procesarImagen(req.getPart("imagenPlato"),
                    datos.codigoProducto(), req);

            // Crear objeto Plato
            Plato plato = crearPlatoDesdeData(datos, rutaImagen);

            // Guardar en BD
            if (platoDAO.agregarPlato(plato)) {
                limpiarAtributosSesion(req.getSession());
                resp.sendRedirect(req.getContextPath() + "/platos?ruta=listar");
            } else {
                manejarErrorRegistro(req, resp, "Error al guardar el plato en la base de datos", datos);
            }

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error de formato numérico", e);
            manejarErrorConDatos(req, resp, "Datos numéricos inválidos", true);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al guardar plato", e);
            manejarErrorConDatos(req, resp, "Error interno: " + e.getMessage(), true);
        }
    }

    private void modificarPlato(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        try {
            // Extraer datos incluyendo ID
            long id = Long.parseLong(req.getParameter("id"));
            DatosPlato datos = extraerDatosFormulario(req);

            // Obtener plato existente para conservar imagen actual
            Plato platoExistente = platoDAO.obtenerPlato(id);
            if (platoExistente == null) {
                throw new IllegalArgumentException("Plato no encontrado");
            }

            // Procesar nueva imagen si existe, sino mantener la actual
            String rutaImagen = procesarImagenEdicion(req.getPart("imagenPlato"),
                    datos.codigoProducto(),
                    platoExistente.getImagenUrl(), req);

            // Crear objeto Plato actualizado
            Plato plato = crearPlatoDesdeData(datos, rutaImagen);
            plato.setId(id);

            // Actualizar en BD
            if (platoDAO.actualizarPlato(plato)) {
                limpiarAtributosSesion(req.getSession());
                resp.sendRedirect(req.getContextPath() + "/platos");
            } else {
                manejarErrorEdicion(req, resp, "Error al actualizar el plato en la base de datos",
                        id, datos);
            }

        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "Error de formato numérico en edición", e);
            manejarErrorConDatos(req, resp, "Datos numéricos inválidos", false);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error al modificar plato", e);
            manejarErrorConDatos(req, resp, "Error interno: " + e.getMessage(), false);
        }
    }

    private void eliminarPlato(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            long id = Long.parseLong(req.getParameter("id"));

            // Obtener plato para eliminar su imagen
            Plato plato = platoDAO.obtenerPlato(id);

            if (platoDAO.eliminarPlato(id)) {
                // Eliminar imagen asociada
                if (plato != null && plato.getImagenUrl() != null) {
                    imagenService.eliminarImagen(plato.getImagenUrl(),
                            req.getServletContext().getRealPath("/"));
                }
                resp.sendRedirect(req.getContextPath() + "/platos?ruta=listar");
            } else {
                req.setAttribute("error", "Error al eliminar el plato");
                listarPlatos(req, resp);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.WARNING, "ID inválido para eliminación", e);
            req.setAttribute("error", "ID de plato inválido");
            listarPlatos(req, resp);
        }
    }

    // Métodos auxiliares optimizados

    private DatosPlato extraerDatosFormulario(HttpServletRequest req) {
        return new DatosPlato(
                req.getParameter("nombrePlato"),
                req.getParameter("codigoProducto"),
                Double.parseDouble(req.getParameter("precio")),
                req.getParameter("descripcionPlato"),
                req.getParameter("tipoPlato") // Cambiado de "estado" a "tipoPlato"
        );
    }

    private String procesarImagen(Part filePart, String codigoProducto, HttpServletRequest req)
            throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return imagenService.getImagenPorDefecto();
        }

        if (!imagenService.validarImagen(filePart)) {
            throw new IllegalArgumentException(imagenService.getMensajeErrorValidacion());
        }

        return imagenService.guardarImagen(filePart, codigoProducto,
                req.getServletContext().getRealPath("/"));
    }

    private String procesarImagenEdicion(Part filePart, String codigoProducto,
                                         String rutaImagenActual, HttpServletRequest req)
            throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return rutaImagenActual; // Mantener imagen actual
        }

        if (!imagenService.validarImagen(filePart)) {
            throw new IllegalArgumentException(imagenService.getMensajeErrorValidacion());
        }

        return imagenService.guardarImagen(filePart, codigoProducto,
                req.getServletContext().getRealPath("/"));
    }

    private Plato crearPlatoDesdeData(DatosPlato datos, String rutaImagen) {
        Plato plato = new Plato();
        plato.setNombrePlato(datos.nombrePlato());
        plato.setCodigoProducto(datos.codigoProducto());
        plato.setPrecio(datos.precio());
        plato.setDescripcionPlato(datos.descripcionPlato());
        plato.setTipoPlato(datos.tipoPlato()); // Cambiado de setEstado a setTipoPlato
        plato.setImagenUrl(rutaImagen);
        return plato;
    }

    private void limpiarAtributosSesion(HttpSession session) {
        if (session != null) {
            session.removeAttribute(ATTR_DATOS_PLATO);
            session.removeAttribute(ATTR_ERROR_REGISTRO);
            session.removeAttribute(ATTR_PLATO_EDICION);
            session.removeAttribute(ATTR_ERROR_EDICION);
        }
    }

    private void manejarErrorRegistro(HttpServletRequest req, HttpServletResponse resp,
                                      String error, DatosPlato datos) throws IOException {
        HttpSession session = req.getSession();

        Plato datosPlato = crearPlatoDesdeData(datos, null);
        session.setAttribute(ATTR_DATOS_PLATO, datosPlato);
        session.setAttribute(ATTR_ERROR_REGISTRO, error);

        resp.sendRedirect(req.getContextPath() + "/jsp/GestionPlato.jsp?mostrarModal=registro");
    }

    private void manejarErrorEdicion(HttpServletRequest req, HttpServletResponse resp,
                                     String error, long id, DatosPlato datos) throws IOException {
        HttpSession session = req.getSession();

        Plato datosPlato = crearPlatoDesdeData(datos, null);
        datosPlato.setId(id);

        session.setAttribute(ATTR_PLATO_EDICION, datosPlato);
        session.setAttribute(ATTR_ERROR_EDICION, error);

        resp.sendRedirect(req.getContextPath() + "/jsp/GestionPlato.jsp?mostrarModal=edicion");
    }

    private void manejarErrorConDatos(HttpServletRequest req, HttpServletResponse resp,
                                      String error, boolean esRegistro) throws IOException {
        try {
            if (esRegistro) {
                DatosPlato datos = extraerDatosFormulario(req);
                manejarErrorRegistro(req, resp, error, datos);
            } else {
                long id = Long.parseLong(req.getParameter("id"));
                DatosPlato datos = extraerDatosFormulario(req);
                manejarErrorEdicion(req, resp, error, id, datos);
            }
        } catch (Exception e) {
            // Si no se pueden extraer los datos, redirigir a listado
            resp.sendRedirect(req.getContextPath() + "/platos?ruta=listar");
        }
    }

    // Record actualizado para encapsular datos del formulario
    private record DatosPlato(
            String nombrePlato,
            String codigoProducto,
            double precio,
            String descripcionPlato,
            String tipoPlato // Cambiado de "estado" a "tipoPlato", removido "cantidad"
    ) {}
}
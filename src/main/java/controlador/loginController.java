package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import modelo.JPA.JPAPlatoDAO;
import modelo.JPA.JPAUsuarioDAO;
import modelo.dao.UsuarioDAO;
import modelo.entity.Usuario;

@WebServlet("/ingreso")
public class loginController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
	}
	
	private void ruteador(HttpServletRequest request, HttpServletResponse response) {
		
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String usuario = request.getParameter("usuario");
		System.out.println(usuario + " " + email + " " + password);
		
		UsuarioDAO usuarioDAO = new JPAUsuarioDAO();
		
		Usuario usuarioAutorizado = usuarioDAO.autorizar(email, password, usuario);
		System.out.println(usuarioAutorizado);
		
		if	(usuarioAutorizado != null) {
			
			HttpSession sesion = request.getSession();
			sesion.setAttribute("usuarioAutorizado", usuarioAutorizado);
			
			if	(usuarioAutorizado.getTipo().equals("cliente")) {	
				System.out.println("LISTADO DE LOS PLATOS");
				response.sendRedirect(request.getContextPath() + "/ListarPlatos");
			} else {
				response.sendRedirect(request.getContextPath() + "/Dashboard");
			}
		} else {
			System.out.println("USUARIO NO REGISTRADO");
			response.sendRedirect(request.getContextPath() + "/ingreso");
			
		}
		
	}
}

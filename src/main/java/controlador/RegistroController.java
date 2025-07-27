package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.JPA.JPAUsuarioDAO;
import modelo.dao.UsuarioDAO;
import modelo.entity.Usuario;

@WebServlet("/registroCliente")
public class RegistroController extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		System.out.println(email + " " + password + " Cliente");
		
		UsuarioDAO usuarioDAO = new JPAUsuarioDAO();
		
		Usuario usuario = new Usuario(email, password, "cliente");
		
		usuarioDAO.crear(usuario);
		
		response.sendRedirect("jsp/login.jsp");
		
		
	}
}
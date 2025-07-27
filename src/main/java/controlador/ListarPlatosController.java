package controlador;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import modelo.JPA.JPAPlatoDAO;
import modelo.dao.PlatoDAO;

@WebServlet("/ListarPlatos")
public class ListarPlatosController extends HttpServlet{

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		JPAPlatoDAO platoDAO = new JPAPlatoDAO();

		request.setAttribute("platos", platoDAO.obtenerTodosPlatos());

		getServletContext().getRequestDispatcher("/jsp/menu.jsp").forward(request, response);
	}

}

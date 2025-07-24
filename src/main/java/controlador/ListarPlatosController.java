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
		
		PlatoDAO platoDAO = new JPAPlatoDAO();
		
		request.setAttribute("platos", platoDAO.getPlatos());
		
		getServletContext().getRequestDispatcher("/jsp/menu2.jsp").forward(request, response);
	}

}

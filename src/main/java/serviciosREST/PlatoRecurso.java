package serviciosREST;

import java.util.List;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import modelo.JPA.JPAPlatoDAO;
import modelo.dao.PlatoDAO;
import modelo.entity.Plato;

@Path("/platos")
public class PlatoRecurso {

	private PlatoDAO platoDAO;
	
	public PlatoRecurso() {
		platoDAO = new JPAPlatoDAO();
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Plato> getPlatos(){
		return platoDAO.getPlatos();
	}
	
	@GET
	@Path("/{nombre}")
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	public List<Plato> getPlatosPorNombre(@PathParam("nombre") String nombre){
		return platoDAO.getPlatosPorNombre(nombre);
	}
	
}

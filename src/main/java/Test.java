import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import modelo.JPA.JPAPlatoDAO;
import modelo.entity.Plato;
import modelo.entity.Usuario;
import modelo.dao.PlatoDAO;

public class Test {

	public static void main(String[] args) {
			EntityManager em = Persistence.createEntityManagerFactory("Restaurante").createEntityManager();
			Usuario u = new Usuario("1", "1", "1");
		
		em.getTransaction().begin();
		em.persist(u);
		em.getTransaction().commit();


	}

}

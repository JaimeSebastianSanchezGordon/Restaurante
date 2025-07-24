package modelo.JPA;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import modelo.dao.PlatoDAO;
import modelo.entity.Plato;

public class JPAPlatoDAO implements PlatoDAO {

	EntityManager em;

	public JPAPlatoDAO() {
		em = Persistence.createEntityManagerFactory("Restaurante").createEntityManager();
	}

	@Override
	public List<Plato> getPlatos() {

		String sentenciaJPQL = "SELECT p FROM Plato p";
		Query query = em.createQuery(sentenciaJPQL);

		return (List<Plato>) query.getResultList();
	}

	@Override
	public List<Plato> getPlatosPorNombre(String nombre) {
		String sentenciaJPQL = "SELECT p FROM Plato p WHERE p.nombre = :nombre";
		Query query = em.createQuery(sentenciaJPQL);
		query.setParameter("nombre", nombre);

		return (List<Plato>) query.getResultList();
	}

}

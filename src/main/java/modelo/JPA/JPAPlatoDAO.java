package modelo.JPA;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import modelo.dao.PlatoDAO;
import modelo.entity.Plato;
import util.JPAUtil;

public class JPAPlatoDAO implements PlatoDAO {

	@Override
	public List<Plato> getPlatosPorNombre(String nombre) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			return em.createQuery("SELECT p FROM Plato p WHERE p.nombrePlato = :nombre", Plato.class)
					.setParameter("nombre", nombre)
					.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public boolean agregarPlato(Plato platoNuevo) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(platoNuevo);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean actualizarPlato(Plato plato) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(plato);
			em.getTransaction().commit();
			return true;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public boolean eliminarPlato(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			Plato plato = em.find(Plato.class, id);
			if (plato != null) {
				em.remove(plato);
				em.getTransaction().commit();
				return true;
			}
			return false;
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return false;
		} finally {
			em.close();
		}
	}

	@Override
	public List<Plato> obtenerTodosPlatos() {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			TypedQuery<Plato> query = em.createQuery("SELECT p FROM Plato p", Plato.class);
			return query.getResultList();
		} finally {
			em.close();
		}
	}

	@Override
	public Plato obtenerPlato(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			return em.find(Plato.class, id);
		} finally {
			em.close();
		}
	}

}

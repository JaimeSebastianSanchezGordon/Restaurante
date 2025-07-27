package modelo.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import modelo.dao.UsuarioDAO;
import modelo.entity.Usuario;
import util.JPAUtil;

public class JPAUsuarioDAO implements UsuarioDAO {

	@Override
	public void crear(Usuario usuario) {

		EntityManager em = JPAUtil.getEntityManager();
		
		em.getTransaction().begin();
		em.persist(usuario);
		em.getTransaction().commit();
		
	}

	@Override
	public Usuario autorizar(String email, String password) {
		EntityManager em = JPAUtil.getEntityManager();
		String sentenciaJPQL = "SELECT u FROM Usuario u WHERE u.email = :email AND u.password = :password";
		Query query = em.createQuery(sentenciaJPQL);
		query.setParameter("email", email);
		query.setParameter("password", password);

	    try {
	        return (Usuario) query.getSingleResult();
	    } catch (NoResultException e) {
	        return null;
	    } finally {
	        em.close();
	    }
	}

}

package modelo.JPA;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import modelo.dao.DetallePedidoDAO;
import modelo.entity.DetallePedido;
import modelo.entity.Plato;
import util.JPAUtil;
import modelo.dao.PlatoDAO;

public class JPADetallePedidoDAO implements DetallePedidoDAO {

	private PlatoDAO platoDAO = new JPAPlatoDAO();
	
    @Override
    public List<DetallePedido> obtenerDetallesPorPedido(Long idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT d FROM DetallePedido d WHERE d.pedido.idPedido = :idPedido";
            Query query = em.createQuery(jpql, DetallePedido.class);
            query.setParameter("idPedido", idPedido);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public void agregarDetallePedido(DetallePedido detalle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(detalle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al agregar detalle pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarDetallePedido(DetallePedido detalle) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(detalle);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar detalle pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarCantidadDetalle(Long idDetalle, int nuevaCantidad) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            DetallePedido detalle = em.find(DetallePedido.class, idDetalle);
            if (detalle != null && nuevaCantidad > 0) {
                detalle.setCantidad(nuevaCantidad);
                detalle.setPrecio(detalle.getPlato().getPrecio() * nuevaCantidad);
                em.merge(detalle);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar cantidad del detalle", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarDetallePedido(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            DetallePedido detalle = em.find(DetallePedido.class, id);
            if (detalle != null) {
                em.remove(detalle);
                em.getTransaction().commit();
            } else {
                em.getTransaction().rollback();
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar detalle pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public double calcularTotalPedido(Long idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            Query query = em.createQuery(
                    "SELECT SUM(d.precio) FROM DetallePedido d WHERE d.pedido.idPedido = :idPedido");
            query.setParameter("idPedido", idPedido);
            Double total = (Double) query.getSingleResult();
            return total != null ? total : 0.0;
        } finally {
            em.close();
        }
    }
    
    @Override
	public List<String> adjuntarDetallePedido(DetallePedido detalle) {
		EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(detalle);
            em.getTransaction().commit();
            List<String> datos = new ArrayList<>();
            Plato plato = new Plato();
            plato = platoDAO.obtenerPlato(Long.valueOf(detalle.getIdPlato()));
            datos.add(plato.getNombrePlato());
            datos.add(String.valueOf(plato.getPrecio()));
            datos.add(String.valueOf(detalle.getCantidad()));
            return datos;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al agregar detalle pedido", e);
        } finally {
            em.close();
        }
	}

	@Override
	public DetallePedido obtenerDetallePedido(Long id) {
		EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(DetallePedido.class, id);
        } finally {
            em.close();
        }
	}
}
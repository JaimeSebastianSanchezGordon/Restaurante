package modelo.JPA;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import modelo.dao.DetallePedidoDAO;
import modelo.entity.DetallePedido;
import util.JPAUtil;

public class JPADetallePedidoDAO implements DetallePedidoDAO {

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
}
package modelo.JPA;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import modelo.dao.PedidoDAO;
import modelo.entity.Pedido;
import util.JPAUtil;

public class JPAPedidoDAO implements PedidoDAO {

    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT p FROM Pedido p";
            Query query = em.createQuery(jpql, Pedido.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Pedido getPedidoById(int idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Pedido.class, idPedido);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarPedido(Pedido pedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pedido existingPedido = em.find(Pedido.class, pedido.getIdPedido());
            if (existingPedido != null) {
                existingPedido.setNumMesa(pedido.getNumMesa());
                existingPedido.setFormaPago(pedido.getFormaPago());
                existingPedido.setInvitados(pedido.getInvitados());
                existingPedido.setNombreCliente(pedido.getNombreCliente());
                existingPedido.setCantidadPagar(pedido.getCantidadPagar());
                em.merge(existingPedido);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void actualizarPedidoConTotal(Pedido pedido, double total) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pedido existingPedido = em.find(Pedido.class, pedido.getIdPedido());
            if (existingPedido != null) {
                existingPedido.setNumMesa(pedido.getNumMesa());
                existingPedido.setFormaPago(pedido.getFormaPago());
                existingPedido.setInvitados(pedido.getInvitados());
                existingPedido.setNombreCliente(pedido.getNombreCliente());
                existingPedido.setCantidadPagar(total);
                em.merge(existingPedido);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al actualizar pedido con total", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void eliminarPedido(int idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            
            // Eliminar detalles del pedido primero
            Query deleteDetalles = em.createQuery("DELETE FROM DetallePedido d WHERE d.pedido.idPedido = :idPedido");
            deleteDetalles.setParameter("idPedido", idPedido);
            deleteDetalles.executeUpdate();

            // Eliminar el pedido
            Pedido pedido = em.find(Pedido.class, idPedido);
            if (pedido != null) {
                em.remove(pedido);
            }

            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al eliminar el pedido", e);
        } finally {
            em.close();
        }
    }

    @Override
    public void marcarPedidoComoPagado(int idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pedido pedido = em.find(Pedido.class, idPedido);
            if (pedido != null) {
                pedido.setEstado("Pagado");
                em.merge(pedido);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Error al marcar pedido como pagado", e);
        } finally {
            em.close();
        }
    }

    @Override
    public double calcularTotalPedido(int idPedido) {
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
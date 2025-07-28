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
    public Pedido getPedidoById(Long idPedido) {
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
    public void eliminarPedido(Long idPedido) {
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
    public void marcarPedidoComoPagado(Long idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Pedido pedido = em.find(Pedido.class, idPedido);
            if (pedido != null) {
                pedido.setEstado("Pagado");
                pedido.setEstadoPreparacion("listo");
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
	public Long registrarPedido(Pedido pedido) {
		EntityManager em = JPAUtil.getEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(pedido);
			em.getTransaction().commit();
			System.out.println("Guardado");
			Long idGenerado = pedido.getIdPedido();
			System.out.println("{ \"success\": true, \"idPedido\": " + idGenerado + " }");
			return idGenerado;
		} catch (Exception e) {
			System.out.println("No guardado");
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
			return (long) 0;
		}finally {
			em.close();
		}
	}
	@Override
	public Long obtenerUltimoIdPedido() {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        String jpql = "SELECT MAX(p.idPedido) FROM Pedido p";
	        Query query = em.createQuery(jpql);
	        Long ultimoId = (Long) query.getSingleResult();
	        return ultimoId != null ? ultimoId : 0L;
	    } finally {
	        em.close();
	    }
	}

	@Override
	public Long obtenerSiguienteNumeroPedido() {
	    EntityManager em = JPAUtil.getEntityManager();
	    try {
	        // Obtener el último número de pedido + 1
	        String jpql = "SELECT COALESCE(MAX(p.idPedido), 0) + 1 FROM Pedido p";
	        Query query = em.createQuery(jpql);
	        Long siguienteNumero = (Long) query.getSingleResult();
	        return siguienteNumero != null ? siguienteNumero : 1L;
	    } finally {
	        em.close();
	    }
	}
}
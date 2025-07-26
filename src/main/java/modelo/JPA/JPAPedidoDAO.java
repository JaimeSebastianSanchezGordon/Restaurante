package modelo.JPA;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import modelo.dao.PedidoDAO;
import modelo.entity.Pedido;

public class JPAPedidoDAO implements PedidoDAO {

    private EntityManager em;

    public JPAPedidoDAO() {
        em = Persistence.createEntityManagerFactory("ProyectoPedidos").createEntityManager();
    }
    @Override
    public List<Pedido> obtenerTodosLosPedidos() {
        String jpql = "SELECT p FROM Pedido p";
        Query query = em.createQuery(jpql, Pedido.class);
        return (List<Pedido>) query.getResultList();
    }

    @Override
    public Pedido getPedidoById(int idPedido) {
        return em.find(Pedido.class, idPedido);
    }

    @Override
    public void actualizarPedido(Pedido pedido) {
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
    }

    @Override
    public void actualizarPedidoConTotal(Pedido pedido, double total) {
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
    }


    @Override
    public void eliminarPedido(int idPedido) {
        try {
            em.getTransaction().begin();

            Query deleteDetalles = em.createQuery("DELETE FROM DetallePedido d WHERE d.pedido.idPedido = :idPedido");
            deleteDetalles.setParameter("idPedido", idPedido);
            deleteDetalles.executeUpdate();

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
        }
    }

    @Override
    public void marcarPedidoComoPagado(int idPedido) {
        em.getTransaction().begin();
        Pedido pedido = em.find(Pedido.class, idPedido);
        if (pedido != null) {
            pedido.setEstado("Pagado");
            em.merge(pedido);
        }
        em.getTransaction().commit();
    }

    @Override
    public double calcularTotalPedido(int idPedido) {
        Query query = em.createQuery(
                "SELECT SUM(d.precio) FROM DetallePedido d WHERE d.pedido.idPedido = :idPedido");
        query.setParameter("idPedido", idPedido);
        Double total = (Double) query.getSingleResult();
        return total != null ? total : 0.0;
    }
}

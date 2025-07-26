package modelo.JPA;

import java.util.List;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import modelo.dao.DetallePedidoDAO;
import modelo.entity.DetallePedido;

public class JPADetallePedidoDAO implements DetallePedidoDAO {

    private EntityManager em;

    public JPADetallePedidoDAO() {
        em = Persistence.createEntityManagerFactory("ProyectoPedidos").createEntityManager();
    }

    @Override
    public List<DetallePedido> obtenerDetallesPorPedido(int idPedido) {
        String jpql = "SELECT d FROM DetallePedido d WHERE d.idPedido = :idPedido";
        Query query = em.createQuery(jpql, DetallePedido.class);
        query.setParameter("idPedido", idPedido);
        return query.getResultList();
    }

    @Override
    public void agregarDetallePedido(DetallePedido detalle) {
        em.getTransaction().begin();
        em.persist(detalle);
        em.getTransaction().commit();
    }

    @Override
    public void actualizarDetallePedido(DetallePedido detalle) {
        em.getTransaction().begin();
        em.merge(detalle);
        em.getTransaction().commit();
    }

    @Override
    public void actualizarCantidadDetalle(int idDetalle, int nuevaCantidad) {
        em.getTransaction().begin();
        DetallePedido detalle = em.find(DetallePedido.class, idDetalle);
        if (detalle != null && nuevaCantidad > 0) {
            detalle.setCantidad(nuevaCantidad);
            detalle.setPrecio(detalle.getPlato().getPrecio() * nuevaCantidad);
            em.merge(detalle);
        }
        em.getTransaction().commit();
    }

    @Override
    public void eliminarDetallePedido(int id) {
        try {
            em.getTransaction().begin();
            DetallePedido detalle = em.find(DetallePedido.class, id);
            if (detalle != null) {
                System.out.println("Eliminando detalle: " + detalle.getId());
                em.remove(detalle);
                em.getTransaction().commit();
                System.out.println("Detalle eliminado exitosamente");
            } else {
                em.getTransaction().rollback();
                System.out.println("No se encontró detalle con ID: " + id);
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error al eliminar detalle: " + e.getMessage());
            throw new RuntimeException("Error al eliminar detalle pedido", e);
        }
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
package modelo.JPA;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import modelo.dto.EstadoPedidoDTO;
import modelo.dao.EstadoPedidoDAO;
import modelo.entity.Pedido;
import util.JPAUtil;

import java.util.List;

public class JPAEstadoPedidoDAO implements EstadoPedidoDAO {
    @Override
    public List<EstadoPedidoDTO> obtenerPedidos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT new modelo.dto.EstadoPedidoDTO(p.idPedido, p.estadoPreparacion) " +
                    "FROM Pedido p ";
            TypedQuery<EstadoPedidoDTO> query = em.createQuery(jpql, EstadoPedidoDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public EstadoPedidoDTO obtenerPedidoPorId(int idPedido) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT new modelo.dto.EstadoPedidoDTO(p.idPedido, p.estadoPreparacion) " +
                    "FROM Pedido p WHERE p.idPedido = :id";
            TypedQuery<EstadoPedidoDTO> query = em.createQuery(jpql, EstadoPedidoDTO.class);
            query.setParameter("id", idPedido);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}

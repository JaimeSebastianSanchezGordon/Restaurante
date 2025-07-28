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
            // CORREGIDO: Incluir todos los estados de preparación relevantes
            String jpql = "SELECT new modelo.dto.EstadoPedidoDTO(p.idPedido, p.estadoPreparacion) " +
                    "FROM Pedido p " +
                    "WHERE p.estadoPreparacion IN ('listo', 'en preparacion', 'No iniciado') " +
                    "ORDER BY p.idPedido DESC";
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
            // CORREGIDO: Agregar espacio después de :id y incluir más estados
            String jpql = "SELECT new modelo.dto.EstadoPedidoDTO(p.idPedido, p.estadoPreparacion) " +
                    "FROM Pedido p WHERE p.idPedido = :id " +
                    "AND p.estadoPreparacion IN ('listo', 'en preparacion', 'No iniciado')";
            TypedQuery<EstadoPedidoDTO> query = em.createQuery(jpql, EstadoPedidoDTO.class);
            query.setParameter("id", (long) idPedido);

            // Usar getResultList() en lugar de getSingleResult() para mejor manejo
            List<EstadoPedidoDTO> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);

        } finally {
            em.close();
        }
    }
    
    // MÉTODO ADICIONAL: Obtener todos los pedidos sin filtro para debug
    public List<EstadoPedidoDTO> obtenerTodosLosPedidos() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT new modelo.dto.EstadoPedidoDTO(p.idPedido, p.estadoPreparacion) " +
                    "FROM Pedido p ORDER BY p.idPedido DESC";
            TypedQuery<EstadoPedidoDTO> query = em.createQuery(jpql, EstadoPedidoDTO.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
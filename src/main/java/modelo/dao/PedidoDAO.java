package modelo.dao;

import java.util.List;
import modelo.entity.Pedido;

public interface PedidoDAO {
    List<Pedido> obtenerTodosLosPedidos();
    Pedido getPedidoById(Long idPedido);
    void actualizarPedido(Pedido pedido);
    void actualizarPedidoConTotal(Pedido pedido, double total);
    void eliminarPedido(Long idPedido);
    void marcarPedidoComoPagado(Long idPedido);
    double calcularTotalPedido(Long idPedido);
    Long registrarPedido(Pedido pedido);
}
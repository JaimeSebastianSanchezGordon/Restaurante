package modelo.dao;

import java.util.List;
import modelo.entity.Pedido;

public interface PedidoDAO {
    List<Pedido> obtenerTodosLosPedidos();
    Pedido getPedidoById(int idPedido);
    void actualizarPedido(Pedido pedido);
    void actualizarPedidoConTotal(Pedido pedido, double total);
    void eliminarPedido(int idPedido);
    void marcarPedidoComoPagado(int idPedido);
    double calcularTotalPedido(int idPedido);
}
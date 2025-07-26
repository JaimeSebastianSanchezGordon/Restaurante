package modelo.dao;

import java.util.List;
import modelo.entity.DetallePedido;

public interface DetallePedidoDAO {
    List<DetallePedido> obtenerDetallesPorPedido(int idPedido);
    void agregarDetallePedido(DetallePedido detalle);
    void actualizarDetallePedido(DetallePedido detalle);
    void actualizarCantidadDetalle(int idDetalle, int nuevaCantidad);
    void eliminarDetallePedido(int id);
    double calcularTotalPedido(int idPedido);
}
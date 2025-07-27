package modelo.dao;

import java.util.List;
import modelo.entity.DetallePedido;

public interface DetallePedidoDAO {
    List<DetallePedido> obtenerDetallesPorPedido(Long idPedido);
    void agregarDetallePedido(DetallePedido detalle);
    void actualizarDetallePedido(DetallePedido detalle);
    void actualizarCantidadDetalle(Long idDetalle, int nuevaCantidad);
    void eliminarDetallePedido(Long id);
    double calcularTotalPedido(Long idPedido);
}
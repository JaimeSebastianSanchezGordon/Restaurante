package modelo.dao;

import modelo.dto.EstadoPedidoDTO;

import java.util.List;

public interface EstadoPedidoDAO {
    List<EstadoPedidoDTO> obtenerPedidos();
    EstadoPedidoDTO obtenerPedidoPorId(int idPedido);
}

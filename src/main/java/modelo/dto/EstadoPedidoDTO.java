package modelo.dto;

public class EstadoPedidoDTO {
    private int idPedido;
    private String estadoPreparacion;

    public EstadoPedidoDTO(int idPedido, String estadoPreparacion) {
        this.idPedido = idPedido;
        this.estadoPreparacion = estadoPreparacion;
    }

    public String getEstadoPreparacion() {
        return estadoPreparacion;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    public void setEstadoPreparacion(String estadoPreparacion) {
        this.estadoPreparacion = estadoPreparacion;
    }
}

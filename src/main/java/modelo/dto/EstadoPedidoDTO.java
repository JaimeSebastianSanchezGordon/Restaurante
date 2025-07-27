package modelo.dto;

public class EstadoPedidoDTO {
    private long idPedido;
    private String estadoPreparacion;

    public EstadoPedidoDTO(long idPedido, String estadoPreparacion) {
        this.idPedido = idPedido;
        this.estadoPreparacion = estadoPreparacion;
    }

    public String getEstadoPreparacion() {
        return estadoPreparacion;
    }

    public long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }
    public void setEstadoPreparacion(String estadoPreparacion) {
        this.estadoPreparacion = estadoPreparacion;
    }
}

// ==================== PEDIDO.JAVA ====================
package modelo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPedido;

    @Column(name = "estado")
    private String estado;

    @Column(name = "numMesa")
    private int numMesa;

    @Column(name = "formaPago")
    private String formaPago;

    @Column(name = "cantidadPagar")
    private double cantidadPagar;

    @Column(name = "nombreCliente")
    private String nombreCliente;

    @Column(name = "invitados")
    private int invitados;

    // Relación OneToMany con DetallePedido
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles = new ArrayList<>();

    // Constructores
    public Pedido() {
    }

    public Pedido(String estado, int numMesa, String formaPago, double cantidadPagar,
                  String nombreCliente, int invitados) {
        this.estado = estado;
        this.numMesa = numMesa;
        this.formaPago = formaPago;
        this.cantidadPagar = cantidadPagar;
        this.nombreCliente = nombreCliente;
        this.invitados = invitados;
    }

    // Getters y Setters
    public Long getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Long idPedido) {
        this.idPedido = idPedido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getNumMesa() {
        return numMesa;
    }

    public void setNumMesa(int numMesa) {
        this.numMesa = numMesa;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public double getCantidadPagar() {
        return cantidadPagar;
    }

    public void setCantidadPagar(double cantidadPagar) {
        this.cantidadPagar = cantidadPagar;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getInvitados() {
        return invitados;
    }

    public void setInvitados(int invitados) {
        this.invitados = invitados;
    }

    public List<DetallePedido> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetallePedido> detalles) {
        this.detalles = detalles;
    }

    // Métodos de utilidad para manejar la relación bidireccional
    public void addDetalle(DetallePedido detalle) {
        detalles.add(detalle);
        detalle.setPedido(this);
    }

    public void removeDetalle(DetallePedido detalle) {
        detalles.remove(detalle);
        detalle.setPedido(null);
    }

    @Override
    public String toString() {
        return "Pedido [idPedido=" + idPedido + ", estado=" + estado + ", numMesa=" + numMesa + 
               ", formaPago=" + formaPago + ", cantidadPagar=" + cantidadPagar + 
               ", nombreCliente=" + nombreCliente + ", invitados=" + invitados + "]";
    }
}
package modelo.entity;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pedido")
public class Pedido implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idPedido;

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

    private String estadoPreparacion;

    public Pedido(int idPedido, String estado, int numMesa, String formaPago, float cantidadPagar,
                  String nombreCliente, int invitados, String estadoPreparacion) {
        this.idPedido = idPedido;
        this.estado = estado;
        this.numMesa = numMesa;
        this.formaPago = formaPago;
        this.cantidadPagar = cantidadPagar;
        this.nombreCliente = nombreCliente;
        this.invitados = invitados;
        this.estadoPreparacion = estadoPreparacion;
    }

    public Pedido() {

    }


    // Getters y Setters
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
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

    public void setCantidadPagar(double total) {
        this.cantidadPagar = total;
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

    public String getEstadoPreparacion() {
        return estadoPreparacion;
    }
    public void setEstadoPreparacion(String estadoPreparacion) {
        this.estadoPreparacion = estadoPreparacion;
    }

    @Override
    public String toString() {
        return "Pedido [idPedido=" + idPedido + ", estado=" + estado + ", numMesa=" + numMesa + ", formaPago="
                + formaPago + ", cantidadPagar=" + cantidadPagar + ", nombreCliente=" + nombreCliente + ", invitados="
                + invitados + "]";
    }
}

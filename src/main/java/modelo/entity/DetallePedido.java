// ==================== DETALLEPEDIDO.JAVA ====================
package modelo.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "detallepedido")
public class DetallePedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "idPedido")
    private int idPedido;

    @Column(name = "idPlato")
    private int idPlato;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "precio")
    private double precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPedido")
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPlato")
    private Plato plato;

    // Constructores
    public DetallePedido() {
    }

    public DetallePedido(Pedido pedido, Plato plato, int cantidad, double precio) {
        this.pedido = pedido;
        this.plato = plato;
        this.cantidad = cantidad;
        this.precio = precio;
    }
    
    public DetallePedido(int idPedido, int idPlato, int cantidad, float precio) {
        this.idPedido = idPedido;
        this.idPlato = idPlato;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        this.plato = plato;
    }

    // Métodos de utilidad
    public Long getIdPedido2() {
        return pedido != null ? pedido.getIdPedido() : null;
    }

    public Long getIdPlato2() {
        return plato != null ? plato.getId() : null;
    }

    @Override
    public String toString() {
        return "DetallePedido [id=" + id + ", cantidad=" + cantidad + ", precio=" + precio + 
               ", pedidoId=" + getIdPedido2() + ", platoId=" + getIdPlato2() + "]";
    }
}
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
public class DetallePedido  implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "idPedido")
    private int idPedido;

    @Column(name = "idPlato")
    private int idPlato;

    @Column(name = "cantidad")
    private int cantidad;

    @Column(name = "precio")
    private double precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idPedido", insertable = false, updatable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "idPlato", insertable = false, updatable = false)
    private Plato plato;

    public DetallePedido() {

    }

    public DetallePedido(int idPedido, int idPlato, int cantidad, float precio) {
        super();
        this.idPedido = idPedido;
        this.idPlato = idPlato;
        this.cantidad = cantidad;
        this.precio = precio;
    }

    public int getIdDetallePedido() {
        return this.id;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
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

}

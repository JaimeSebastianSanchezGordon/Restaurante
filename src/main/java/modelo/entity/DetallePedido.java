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

    @Column(name = "idPed")
    private int idPed;

    @Column(name = "idPla")
    private int idPla;
    
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

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
    public Long getIdPedido() {
        return pedido != null ? pedido.getIdPedido() : null;
    }

    public Long getIdPlato() {
        return plato != null ? plato.getId() : null;
    }

    

	public int getIdPed() {
		return idPed;
	}

	public void setIdPed(int idPed) {
		this.idPed = idPed;
	}

	public int getIdPla() {
		return idPla;
	}

	public void setIdPla(int idPla) {
		this.idPla = idPla;
	}

	@Override
    public String toString() {
        return "DetallePedido [id=" + id + ", cantidad=" + cantidad + ", precio=" + precio + 
               ", pedidoId=" + getIdPedido() + ", platoId=" + getIdPlato() + "]";
    }
}
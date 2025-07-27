package modelo.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "plato")
public class Plato implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "nombre", nullable = false, length = 100)
	private String nombrePlato;

	@Column(name = "estado", nullable = false, length = 20)
	private String estado; // "en_stock", "fuera_stock", "limitado"

	@Column(name = "codigo_producto", unique = true, length = 20)
	private String codigoProducto;

	@Column(name = "descripcion")
	private String descripcionPlato;

	@Column(name = "cantidad", nullable = false)
	private Integer cantidad;

	@Column(name = "precio", nullable = false, precision = 10, scale = 2)
	private double precio;

	@Column(name = "imagen_url", length = 255)
	private String imagenUrl;

	// Constructores
	public Plato() {
	}

	public Plato(String nombre, String estado, String codigoProducto,String descripcionProducto,
				 Integer cantidad, double precio, String imagenUrl) {
		this.nombrePlato = nombre;
		this.estado = estado;
		this.codigoProducto = codigoProducto;
		this.descripcionPlato = descripcionProducto;
		this.cantidad = cantidad;
		this.precio = precio;
		this.imagenUrl = imagenUrl;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getImagenUrl() {
		return imagenUrl;
	}

	public void setImagenUrl(String imagenUrl) {
		this.imagenUrl = imagenUrl;
	}

	public void setDescripcionPlato (String descripcionPlato) {
		this.descripcionPlato = descripcionPlato;
	}

	public String getNombrePlato() {
		return nombrePlato;
	}

	public void setNombrePlato(String nombrePlato) {
		this.nombrePlato = nombrePlato;
	}

	public String getDescripcionPlato() {
		return descripcionPlato;
	}

	// Método para obtener el texto del estado
	public String getEstadoTexto() {
		switch (estado) {
			case "in_stock": return "En Stock";
			case "out_stock": return "Agotado";
			case "limited": return "Últimas Unidades";
			default: return estado;
		}
	}

	@Override
	public String toString() {
		return "Plato{" +
				"id=" + id +
				", nombre='" + nombrePlato + '\'' +
				", estado='" + estado + '\'' +
				", codigoProducto='" + codigoProducto + '\'' +
				", cantidad=" + cantidad +
				", precio=" + precio +
				", imagenUrl='" + imagenUrl + '\'' +
				'}';
	}
}

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

	@Column(name = "tipo_plato", length = 255)
	private String tipoPlato; // Comida, Bebebida, otros.
	
	@Column(name = "codigo_producto", unique = true, length = 20)
	private String codigoProducto;

	@Column(name = "descripcion")
	private String descripcionPlato;

	@Column(name = "precio", nullable = false, precision = 10, scale = 2)
	private double precio;

	@Column(name = "imagen_url", length = 255)
	private String imagenUrl;

	// Constructores
	public Plato() {
	}

	public Plato(String nombre, String codigoProducto,String descripcionProducto,
				   double precio, String imagenUrl, String tipo) {
		this.nombrePlato = nombre;
		this.codigoProducto = codigoProducto;
		this.descripcionPlato = descripcionProducto;
		this.precio = precio;
		this.imagenUrl = imagenUrl;
		this.tipoPlato = tipo;
	}

	// Getters y Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
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
		switch (tipoPlato) {
			case "Comida": return "Comida";
			case "Bebida": return "Bebida";
			case "Otros": return "Otros";
			default: return tipoPlato;
		}
	}

	public String getTipoPlato() {
		return tipoPlato;
	}

	public void setTipoPlato(String tipoPlato) {
		this.tipoPlato = tipoPlato;
	}
}

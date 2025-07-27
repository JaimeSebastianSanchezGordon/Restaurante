package modelo.dto;

public class DetallePedidoDTO {
	private String nombrePlato;
	private double precioPlato;
	private int cantidad;
	private int id;
	
	public DetallePedidoDTO(String nombrePlato, double precioPlato, int cantidad, int id) {
		this.nombrePlato = nombrePlato;
		this.precioPlato = precioPlato;
		this.cantidad = cantidad;
		this.id = id;
	}

	public String getNombrePlato() {
		return nombrePlato;
	}

	public void setNombrePlato(String nombrePlato) {
		this.nombrePlato = nombrePlato;
	}

	public double getPrecioPlato() {
		return precioPlato;
	}

	public void setPrecioPlato(double precioPlato) {
		this.precioPlato = precioPlato;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	
	
	
	
	
	
}

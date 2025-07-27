package modelo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Usuario {
	@Id
	private String email;
	@Column
	private String password;
	@Column
	private String tipo;
	
	public Usuario(String email, String password, String tipo) {
		super();
		this.email = email;
		this.password = password;
		this.tipo = tipo;
	}
	
	

	public Usuario() {
		super();
	}



	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@Override
	public String toString() {
		return "Usuario [email=" + email + ", password=" + password + ", tipo=" + tipo + "]";
	}
	
}

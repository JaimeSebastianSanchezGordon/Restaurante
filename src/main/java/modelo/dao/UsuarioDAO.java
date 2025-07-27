package modelo.dao;

import modelo.entity.Usuario;

public interface UsuarioDAO {

	public void crear(Usuario usuario);
	
	public Usuario autorizar(String email, String password, String tipo);
}

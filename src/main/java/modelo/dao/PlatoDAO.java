package modelo.dao;

import java.util.List;

import modelo.entity.Plato;

public interface PlatoDAO {

	boolean agregarPlato(Plato platoNuevo);
	boolean actualizarPlato(Plato plato);
	boolean eliminarPlato(Long id);
	List<Plato> obtenerTodosPlatos();
	Plato obtenerPlato(Long id);

}

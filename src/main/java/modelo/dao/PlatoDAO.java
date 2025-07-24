package modelo.dao;

import java.util.List;

import modelo.entity.Plato;

public interface PlatoDAO {
	
	public List<Plato> getPlatos();
	public List<Plato> getPlatosPorNombre(String nombre);

}

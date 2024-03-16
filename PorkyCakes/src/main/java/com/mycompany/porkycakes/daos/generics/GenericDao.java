package com.mycompany.porkycakes.daos.generics;

import java.util.List;

public interface GenericDao<T> {
	boolean existePorId(String id);

	void cargar(T entidad);
	boolean eliminar(String id);

	T buscarPorId(String id);
	List<T> buscarPorNombre(String nombre);
	List<T> buscarTodos();
}

package com.mycompany.porkycakes.daos.generics;

import java.util.List;

import com.mycompany.porkycakes.model.Categorias;
import com.mycompany.porkycakes.model.Producto;

public interface ProductoDao extends GenericDao<Producto> {
    List<Producto> buscarPorCantidad(int cantidad);
    List<Producto> buscarPorPrecio(int min, int max);
    List<Producto> buscarPorCategoria(Categorias categoria);
    List<Producto> buscarPorRango(int min, int max);
}

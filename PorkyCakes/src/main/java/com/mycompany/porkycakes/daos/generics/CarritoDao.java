package com.mycompany.porkycakes.daos.generics;

import java.util.List;

import com.mycompany.porkycakes.model.Producto;

public interface CarritoDao {
    boolean agregarProducto(String productoId);
    boolean vaciarCarrito();
    List<Producto> verCarrito();
}

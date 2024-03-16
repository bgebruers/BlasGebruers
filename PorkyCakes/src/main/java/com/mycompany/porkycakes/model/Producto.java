package com.mycompany.porkycakes.model;
import lombok.Data;

@Data
public class Producto{
    private String nombre;
    private int idProducto;
    private int stock;
    private String precio;
    private int calificacion;
    private String descripcion;
    private String tiempoElab;
    private String imagen; // ruta
    private Categorias categoria;
}

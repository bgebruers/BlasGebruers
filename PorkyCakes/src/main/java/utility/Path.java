/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import lombok.Getter;

/**
 *
 * @author usuario
 */

public class Path {


    public static class Web {
       // @Getter public static final String PRODUCTO = "/api/producto/";
        @Getter public static final String PRODUCTO_PRECIO = "/api/producto/buscarxPrecio";
        @Getter public static final String PRODUCTO_NOMBRE= "/api/producto/buscarxNombre";
        @Getter public static final String PRODUCTO_CANTIDAD = "/api/producto/buscarxCantidad";
        @Getter public static final String PRODUCTO_CATEGORIA = "/api/producto/buscarxCategoria";
        @Getter public final static String VER_CARRITO = "/api/producto/verCarrito";
        @Getter public static final String AGREGAR_CARRITO = "/api/producto/agregarCarrito";
        @Getter public static final String VACIAR_CARRITO = "/api/producto/vaciarCarrito";
        @Getter public static final String ELIMINAR_DE_CARRITO = "/api/producto/eliminar";
        @Getter public static final String PRODUCTO = "/api/producto";
    }
    
    public static class Template {
        public final static String LAYOUT = "templates/layouts.vsl";
        public final static String PRODUCTO = "templates/producto.vsl";
        public final static String PRINCIPAL = "templates/paginaPrincipal.vsl";
        public final static String MOSTRAR = "templates/mostrar.vsl";
        public final static String INIT = "templates/init.vsl";
        public final static String SIN_RESULTADO = "templates/sinResultados.vsl";
        public final static String SUGERENCIA = "templates/sugerencia.vsl";
        public final static String VER_CARRITO = "templates/verCarrito.vsl";
        public final static String CARRITO_VACIO = "templates/carritoVacio.vsl";
        public final static String RESPUESTA_CARRITO = "templates/respuestaCarrito.vsl";
        public final static String PRODUCTO_ELIMINADO = "templates/productoEliminado.vsl";
        
    }
}

package com.mycompany.porkycakes;

import static spark.Spark.*;

import com.mycompany.porkycakes.controllers.AdminController;
import com.mycompany.porkycakes.controllers.CarritoController;
import com.mycompany.porkycakes.controllers.PresupuestoController;
import com.mycompany.porkycakes.controllers.PrincipalController;
import com.mycompany.porkycakes.controllers.ProductoController;
import com.mycompany.porkycakes.daos.factories.DAOAbstractFact;
import com.mycompany.porkycakes.daos.factories.DAOSqlFact;
import com.mycompany.porkycakes.daos.generics.AdminDao;
import com.mycompany.porkycakes.daos.generics.CarritoDao;
import com.mycompany.porkycakes.daos.generics.GenericDao;
import com.mycompany.porkycakes.daos.generics.ProductoDao;
import com.mycompany.porkycakes.model.Presupuesto;

import utility.Path;

public class porkyMain {
    public static boolean loggedIn = false;

    public static void main(String[] args) {
        staticFiles.location("/public");

        // idealmente esto se haria con un framework p inyectar dependencias
        DAOAbstractFact DAOsFactory = new DAOSqlFact();

		ProductoDao productoDao = DAOsFactory.getProductoDAO();
		AdminDao adminDao = DAOsFactory.getAdminDAO();
        CarritoDao carritoDao = DAOsFactory.getCarritoDAO();
        GenericDao<Presupuesto> presupuestoDAO = DAOsFactory.getPresupuestoDAO();

		PrincipalController principalC = new PrincipalController(productoDao);
		ProductoController productoC = new ProductoController(productoDao);
		AdminController adminC = new AdminController(adminDao);
        CarritoController carritoC = new CarritoController(carritoDao);
        PresupuestoController presupuestoC = new PresupuestoController(presupuestoDAO);

        // Route para el cliente a pagina principal
        get("/porkycakes", principalC.principal);
        get(Path.Template.MOSTRAR, principalC.mostrar);

        get(Path.Web.PRODUCTO, productoC.producto);
        get(Path.Web.PRODUCTO_NOMBRE, productoC.buscarxNombre);
        get(Path.Web.PRODUCTO_CANTIDAD, productoC.buscarxCantiadad);
        get(Path.Web.PRODUCTO_PRECIO, productoC.buscarxPrecio);
        get(Path.Web.PRODUCTO_CATEGORIA, productoC.buscarxCategoria);

        get(Path.Web.AGREGAR_CARRITO, carritoC.agregarCarrito);
        get(Path.Web.VER_CARRITO, carritoC.verCarrito);
        get(Path.Web.VACIAR_CARRITO, carritoC.vaciarCarrito);
        get(Path.Web.ELIMINAR_DE_CARRITO, productoC.eliminar);

        // Route para administrador iniciar sesion
        // No hay sign up. Se cargo un admi a la db a mano p probar
        // datos de log in: cuil: 01234567890, password: marta123
        get("/porkyAdmin", adminC.logPag); // visual
        post("/porkyAdmin", adminC.logging); // logica
        
        get("/cargarProd", productoC.cargarProdForm); // entrar al form
        post("/cargarProd", productoC.cargarProd); // enviar form (al apretar el boton)
        
        get("/cargarPresup", presupuestoC.cargarPresupuestoForm);
        post("/cargarPresup", presupuestoC.cargarPresupuesto); 
        get("/mostrarPresup", presupuestoC.mostrarPresupuesto); // Ta muy feo xq no se pedia. Es solo para ver lo cargado
   }
}

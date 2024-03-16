package com.mycompany.porkycakes.daos.sqlImplementations;

import java.util.List;

import org.sql2o.Connection;

import com.mycompany.porkycakes.daos.generics.CarritoDao;
import com.mycompany.porkycakes.model.Producto;

import utility.Sql2oDAO;


public class CarritoDAOImpl implements CarritoDao {
        
	public boolean agregarProducto(String idProducto){
        String sql = "INSERT INTO carrito (idProducto) VALUES (:idProducto);";
        try (Connection con = Sql2oDAO.getSql2o().open()) {
            con.createQuery(sql)
               .addParameter("idProducto", idProducto)
               .executeUpdate();
            return true;
        }catch(Exception e){
            return false;
        }
    }
    
    public List<Producto> verCarrito(){
        String sql = "SELECT * FROM producto inner join carrito WHERE producto.idProducto = carrito.idProducto";   
        
        try (Connection con = Sql2oDAO.getSql2o().open()) {
            return con.createQuery(sql).executeAndFetch(Producto.class);
        }
    }    
    
    public boolean vaciarCarrito(){
        String sql = "DELETE FROM carrito;";
        
        try (Connection con = Sql2oDAO.getSql2o().open()) {
            con.createQuery(sql)
            .executeUpdate();
            return true;
        }catch(Exception e){
            System.out.println(e);
            return false;
        }
    }
}

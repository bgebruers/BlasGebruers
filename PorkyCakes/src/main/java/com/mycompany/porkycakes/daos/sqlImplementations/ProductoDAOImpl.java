package com.mycompany.porkycakes.daos.sqlImplementations;

import com.mycompany.porkycakes.daos.generics.ProductoDao;
import com.mycompany.porkycakes.model.Categorias;
import com.mycompany.porkycakes.model.Producto;
import org.sql2o.Connection;
import org.sql2o.Query;
import utility.Sql2oDAO;
import java.util.List;

public class ProductoDAOImpl implements ProductoDao {
	@Override
	public boolean existePorId(String id) {
		return false;
	}

	@Override
	public Producto buscarPorId(String id) {
		String sql = "SELECT * FROM producto WHERE idProducto like :id";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Producto> prod = con.createQuery(sql)
					.addParameter("id", id)
					.executeAndFetch(Producto.class);

			if(prod.size() == 1) {
				return prod.get(0);
			}

			return null;
		}
	}

	public List<Producto> buscarPorNombre(String nombre) {
		String sql = "SELECT * FROM producto WHERE upper(nombre) like :nombre";
		try (Connection con = Sql2oDAO.getSql2o().open()) {

			List<Producto> res = con
				.createQuery(sql)
				.addParameter("nombre", "%"+nombre.toUpperCase()+"%")
				.executeAndFetch(Producto.class);
			return res;
		}
	}

	public List<Producto> buscarPorCantidad(int stock) {
		String sql = "SELECT * FROM producto WHERE stock >= :stock";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Producto> res = con
					.createQuery(sql)
					.addParameter("stock", stock)
					.executeAndFetch(Producto.class);
			return res;
		}
	}

	public List<Producto> buscarPorPrecio(int precioMin, int precioMax) {
		String sql = "SELECT * FROM producto WHERE precio >= :precioMin and precio <= :precioMax";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Producto> res = con
					.createQuery(sql)
					.addParameter("precioMin", precioMin)
					.addParameter("precioMax", precioMax)
					.executeAndFetch(Producto.class);
			return res;
		}
	}
	public List<Producto> buscarPorCategoria(Categorias categoria) {
		String sql = "SELECT * FROM producto WHERE categoria = :categoria";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Producto> res = con
					.createQuery(sql)
					.addParameter("categoria", categoria.name())
					.executeAndFetch(Producto.class);
			return res;
		}
	}
	public List<Producto> buscarPorRango(int idProductoMin, int idProductoMax) {
		String sql = "SELECT * FROM producto WHERE idProducto >= :idProductoMin and idProducto <= :idProductoMax";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Producto> res = con
					.createQuery(sql)
					.addParameter("idProductoMin", idProductoMin)
					.addParameter("idProductoMax", idProductoMax)
					.executeAndFetch(Producto.class);
			return res;
		}
	}

	@Override
	public void cargar(Producto producto) {
		String sql = "INSERT into producto (nombre, stock, precio, calificacion, descripcion, tiempoElab, imagen, categoria) values (:nombre, :stock, :precio, :calificacion, :descripcion, :tiempoElab, :imagen, :categoria)";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			Query query = con.createQuery(sql)
					.addParameter("nombre", producto.getNombre())
					.addParameter("stock", 4)
					.addParameter("precio", producto.getPrecio())
					.addParameter("calificacion", 4)
					.addParameter("descripcion", producto.getDescripcion())
					.addParameter("tiempoElab", producto.getTiempoElab())
					.addParameter("imagen", producto.getImagen())
					.addParameter("categoria", producto.getCategoria().toString());


			query.executeUpdate();
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public List<Producto> buscarTodos() {
		String sql = "SELECT * FROM producto;";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Producto>  res = con
					.createQuery(sql)
					.executeAndFetch(Producto.class);
			return res;
		}
	}
    public boolean eliminar(String idProducto){
        String sql = "DELETE FROM carrito WHERE idProducto = :idProducto";
        try (Connection con = Sql2oDAO.getSql2o().open()) {
            con.createQuery(sql)
               .addParameter("idProducto", idProducto)
               .executeUpdate();
            return true;
        }catch(Exception e){
            return false;
        }
    };
}

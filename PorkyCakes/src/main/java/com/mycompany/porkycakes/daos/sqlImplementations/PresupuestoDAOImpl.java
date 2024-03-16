package com.mycompany.porkycakes.daos.sqlImplementations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.data.Row;
import org.sql2o.data.Table;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.porkycakes.daos.generics.GenericDao;
import com.mycompany.porkycakes.model.Presupuesto;

import utility.Sql2oDAO;

public class PresupuestoDAOImpl implements GenericDao<Presupuesto> {

	public Presupuesto buscarPorId(String titulo) {
		String sql = "SELECT * FROM presupuesto WHERE titulo like :titulo";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			Table temp = con.createQuery(sql)
					.addParameter("titulo", titulo)
					.executeAndFetchTable();

			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> json = new HashMap<>();

			Row row = temp.rows().get(0);
			json.put("titulo", row.getString("titulo"));
			json.put("descripcion", row.getString("descripcion"));
			json.put("productos", mapper.readValue(row.getString("productos"), Map.class));
			json.put("fechaEntrega", row.getString("fechaEntrega"));

			Presupuesto presup = mapper.readValue(mapper.writeValueAsString(json), Presupuesto.class);
			return presup;
		} catch(Exception e) {
			e.printStackTrace();
		}

		return null;
	}

    @Override
    public void cargar(Presupuesto presupuesto) {
		ObjectMapper mapper = new ObjectMapper();
		String sql = "INSERT into presupuesto (titulo, descripcion, productos, fechaEntrega) values (:titulo, :descripcion, :productos, :fechaEntrega)";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			Query query = con.createQuery(sql)
					.addParameter("titulo", presupuesto.getTitulo())
					.addParameter("descripcion", presupuesto.getDescripcion())
					.addParameter("productos", mapper.writeValueAsString(presupuesto.getProductos()))
					.addParameter("fechaEntrega", presupuesto.getFechaEntrega());

			query.executeUpdate();
		} catch(Exception e) {
            e.printStackTrace();
		}
    }

    @Override
    public List<Presupuesto> buscarTodos() {
		String sql = "SELECT * FROM presupuesto";

		try (Connection con = Sql2oDAO.getSql2o().open()) {
			return con.createQuery(sql)
                .executeAndFetch(Presupuesto.class);
		} catch(Exception e) {
            e.printStackTrace();
		}

        return null;
    }

    @Override
    public boolean existePorId(String id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'existePorId'");
    }

    @Override
    public List<Presupuesto> buscarPorNombre(String titulo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'buscarPorId'");
    }

	@Override
	public boolean eliminar(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
	}
}

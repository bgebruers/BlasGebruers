package com.mycompany.porkycakes.daos.sqlImplementations;

import com.mycompany.porkycakes.daos.generics.AdminDao;
import com.mycompany.porkycakes.model.Admin;
import org.sql2o.Connection;
import org.sql2o.Query;

import utility.Sql2oDAO;

import utility.PasswordAuthentication;
import java.util.List;

public class AdminDAOImpl implements AdminDao {
	private PasswordAuthentication authenticator = new PasswordAuthentication();

	@Override
	public boolean existePorId(String id) {
		return true;
	}

	public boolean credencialesValidas(String cuil, String password) {
		String sql = "SELECT * FROM admin WHERE cuil like :cuil";

		try (Connection con = Sql2oDAO.getSql2o().open()) {
		    List<Admin> matches = con.createQuery(sql)
		            .addParameter("cuil", cuil)
					.executeAndFetch(Admin.class);

			if(matches.size() == 1) {
				return authenticator.authenticate(password.toCharArray(), matches.get(0).getPassword());
			}

			return false;
		}
	}

	@Override
	public Admin buscarPorId(String cuil) {
		String sql = "SELECT * FROM admin WHERE cuil like :cuil";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			List<Admin> admin = con.createQuery(sql)
					.addParameter("cuil", cuil)
					.executeAndFetch(Admin.class);

			if(admin.size() == 1) {
				return admin.get(0);
			}

			return null;
		}
	}

	@Override
	public void cargar(Admin admin) {
		admin.setPassword(
			authenticator.hash(
				admin.getPassword().toCharArray()
			)
		);

		String sql = "INSERT into admin values (:cuil, :nombre, :mail, :telefono, :password)";
		try (Connection con = Sql2oDAO.getSql2o().open()) {
			Query query = con.createQuery(sql)
					.addParameter("cuil", admin.getCuil())
					.addParameter("nombre", admin.getNombre())
					.addParameter("mail", admin.getMail())
					.addParameter("telefono", admin.getTelefono())
					.addParameter("password", admin.getPassword());

			query.executeUpdate();
		} catch(Exception e) {
			throw e;
		}
	}

	@Override
	public List<Admin> buscarTodos() {
		return null;
	}

	@Override
	public boolean eliminar(String id) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'eliminar'");
	}

	@Override
	public List<Admin> buscarPorNombre(String nombre) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Unimplemented method 'buscarPorNombre'");
	}
}

package com.mycompany.porkycakes.daos.factories;

import com.mycompany.porkycakes.daos.generics.AdminDao;
import com.mycompany.porkycakes.daos.generics.CarritoDao;
import com.mycompany.porkycakes.daos.generics.GenericDao;
import com.mycompany.porkycakes.daos.generics.ProductoDao;
import com.mycompany.porkycakes.daos.sqlImplementations.AdminDAOImpl;
import com.mycompany.porkycakes.daos.sqlImplementations.CarritoDAOImpl;
import com.mycompany.porkycakes.daos.sqlImplementations.PresupuestoDAOImpl;
import com.mycompany.porkycakes.daos.sqlImplementations.ProductoDAOImpl;
import com.mycompany.porkycakes.model.Presupuesto;

public class DAOSqlFact implements DAOAbstractFact {

    @Override
    public AdminDao getAdminDAO() {
        return new AdminDAOImpl();
    }

    @Override
    public ProductoDao getProductoDAO() {
        return new ProductoDAOImpl();
    }

    @Override
    public GenericDao<Presupuesto> getPresupuestoDAO() {
        return new PresupuestoDAOImpl();
    }

    @Override
    public CarritoDao getCarritoDAO() {
        return new CarritoDAOImpl();
    }
    
}

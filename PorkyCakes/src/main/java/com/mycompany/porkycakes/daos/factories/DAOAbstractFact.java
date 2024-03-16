package com.mycompany.porkycakes.daos.factories;

import com.mycompany.porkycakes.daos.generics.AdminDao;
import com.mycompany.porkycakes.daos.generics.CarritoDao;
import com.mycompany.porkycakes.daos.generics.GenericDao;
import com.mycompany.porkycakes.daos.generics.ProductoDao;
import com.mycompany.porkycakes.model.Presupuesto;

public interface DAOAbstractFact {
    AdminDao getAdminDAO();
    ProductoDao getProductoDAO();
    GenericDao<Presupuesto> getPresupuestoDAO();
    CarritoDao getCarritoDAO();
}

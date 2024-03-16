package com.mycompany.porkycakes.daos.generics;

import com.mycompany.porkycakes.model.Admin;

public interface AdminDao extends GenericDao<Admin> {
    boolean credencialesValidas(String user, String password);
}

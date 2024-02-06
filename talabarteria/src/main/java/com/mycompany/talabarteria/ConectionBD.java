/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.talabarteria;

import java.sql.Connection;
import java.sql.*;

/**
 *
 * @author usuario
 */
public class ConectionBD {
    
    public static Statement conect() throws SQLException {
        String sqlCon = "jdbc:postgresql://localhost:5432/TalabarteriaLuiggi";
        Connection conn = DriverManager.getConnection(sqlCon, "postgres", "blas43483");
        Statement stmt = conn.createStatement();
        return stmt;
    } 
}

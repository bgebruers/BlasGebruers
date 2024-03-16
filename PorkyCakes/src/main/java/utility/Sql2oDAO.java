/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;
import org.sql2o.Sql2o;

/**
 *
 * @author usuario
 */
public class Sql2oDAO {
    static Sql2o sql2o;

    public static Sql2o getSql2o() {
        if (sql2o == null) {
            sql2o = new Sql2o("jdbc:mysql://localhost:3306/porkys", "porky", "porky2023");
        }
                
        return sql2o;
    }
    
}

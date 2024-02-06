/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.talabarteria;

import javafx.application.Application;
import javafx.stage.Stage;
import interfaz.Login;
import com.mycompany.talabarteria.ConectionBD;

/**
 *
 * @author usuario
 */
public class Main extends Application {
    ConectionBD conn;
    @Override
    public void start(Stage primaryStage) throws Exception {
       conn = new ConectionBD();
       Login lg = new Login(primaryStage, conn);
      // lg.start(primaryStage);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;



import com.mycompany.talabarteria.ConectionBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import interfaz.Inicio;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import com.mycompany.talabarteria.Alertas;
/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author usuario
 */
public class Login extends Application {
    private Statement stmt;
    private TextField txUsuario = new TextField();
    private PasswordField txContra = new PasswordField();
    private Button btLogin = new Button("Iniciar Sesion");
    private Stage primaryStage;
    private ConectionBD connBD;      
    private String user, password;
    private Alertas alertas = new Alertas();
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
        this.primaryStage = primaryStage;
      
        stmt = connBD.conect();                    
        

        Label lbPrin = new Label("Talabarteria Luiggi");
        lbPrin.setFont(new Font(30));
        
        Label lbUsuario = new Label("Ingrese un usuario");
        lbUsuario.setFont(new Font(17));
        
        Label lbContra = new Label("Ingrese su contraseña");
        lbContra.setFont(new Font(17));
        
        
        txUsuario.setPrefHeight(30);
        txUsuario.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
        
       
        txContra.setPrefHeight(30);
        txContra.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
        
       
        btLogin.setCursor(Cursor.HAND);
        btLogin.setStyle("-fx-background-color: yellow; -fx-font-size: 16; -fx-border-radius: 5"); // Cambiar color a amarillo
        btLogin.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE); // Hacer que el botón se expanda
        VBox.setMargin(btLogin, new Insets(10, 0, 0, 0));
        
        //componente que contiene al otro, este esta para que se pueda pintar de color claro
        VBox bxPrincipal = new VBox();
        bxPrincipal.setSpacing(10);
        bxPrincipal.setPadding(new Insets(10));
        
        //este componente es el que contiene todos los demas, es el rectangulo del centro
        VBox root = new VBox();
        root.setSpacing(10);
        root.setPadding(new Insets(10));
        root.setMinSize(250, 250); 
        root.setMaxSize(250, 250); 
        bxPrincipal.setAlignment(Pos.CENTER);
        
        
        bxPrincipal.getChildren().addAll(lbPrin, root);
        
        //color claro
        BackgroundFill backgroundFill = new BackgroundFill(Color.web("#D9FFC1"), null, null);
        Background background = new Background(backgroundFill);
        bxPrincipal.setBackground(background);
        
        //color verde oscuro
        BackgroundFill bk = new BackgroundFill(Color.web("#64AE49"), null, null);
        Background background2 = new Background(bk);
        root.setBackground(background2);
        
        root.getChildren().addAll(lbUsuario, txUsuario, lbContra, txContra, btLogin);
             
        Scene scene = new Scene(bxPrincipal, 1200, 600);
        
        btLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    // Llamar a la función cuando se hace clic en el botón
                    onLoginButtonClick(event);
                } catch (Exception ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        

        primaryStage.setTitle("Talabarteria Luiggi");
        primaryStage.setScene(scene);
        primaryStage.show();  
        
    }
    
    public Login(Stage primaryStage,ConectionBD conn){
       connBD = conn;
        try {
            this.start(primaryStage);
        } catch (SQLException ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    //agregar la accion al boton
    private void onLoginButtonClick(ActionEvent evt) throws Exception {                                            
        user = txUsuario.getText();
        password = txContra.getText();
        boolean acceso;
       
        acceso =  this.checkDatos(user, password);

//Este if lo que hace es, si coinciden los datos llamo a la clase inicio, pido su scena y luego seteo como primaria la de ella.        
        if(acceso){
          //le paso la primaryStage para poder usarla en Inicio.
            Inicio ini = new Inicio(primaryStage, connBD);
            Scene scene = ini.getScene();
            primaryStage.setScene(scene);
            //le paso la stage para que pueda armar las pantallas que le siguen.
            ini.setStage(primaryStage);
            
        }else{
            alertas.mostrarAlerta("Error", "Ha ingresado datos incorrectos", "ERROR");
        }
            
                
    }                                           

     //checkea que los datos ingresados para login esten almacenados como empleados en la base de datos
    public boolean checkDatos(String usuario, String contra){
        //variables para checkear el estado del usuario y contaseña, son true si estan en la bd
        boolean userCorr = false;
        boolean passCorr = false;
        boolean res;
        
        String query = "SELECT \"user\", \"password\" FROM \"Empleados\"";
        try{
            ResultSet rs = stmt.executeQuery(query);
            while(rs.next()){
                //estos datos son los traidos desde la bd
                String user = rs.getString("user");
                String password = rs.getString("password");

                if(usuario.equals(user)){
                    userCorr = true;
                }else{
                    userCorr = false;
                }
                if(contra.equals(password)){
                    passCorr = true;
                }else{
                    passCorr = false;
                }
                
                res = permitirAcceso(userCorr, passCorr);
                if(res){
                    return true;
                }
            }         
        }catch(SQLException e){
            System.out.println("Hubo un error de coneccion del tipo: " + e);
        }
        return false;
                      
    }
    private boolean permitirAcceso(boolean user, boolean pass){
        boolean res;
        if(user && pass){
            res = true;
        }else{        
            res = false;
        }
        return res;
    }
    
     
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.mycompany.talabarteria.Alertas;
import com.mycompany.talabarteria.ConectionBD;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author usuario
 */
public class AgregarEmpleado extends Application{
    private VBox empleadoForm;
    private Stage primaryLocalStage;
    private Label title ;
    private Button btConfirmar;
    private TextField txNombre, txPassword;
    private String usuario, password;
    private ConectionBD connBD;
    private Alertas alerta = new Alertas();
    private Statement stmt;
    String btStyle = "-fx-min-width: 150px; " +
                "-fx-min-height: 35px; " +
                "-fx-max-width: 20px; " +
                "-fx-max-height: 15px; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;" +
                "-fx-border-radius: 15;";
    Insets insets = new Insets(13, 0, 0, 0);
        
    @Override
    public void start(Stage empleadoStage) throws Exception {
        empleadoForm = new VBox(); 

        title = new Label("Registro de nueva empleado");
        title.setFont(new Font(25));
        VBox.setMargin(title, insets);
         
        txNombre = new TextField("Ingrese un usuario");
        txNombre.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txNombre.setPrefHeight(30);        
        VBox.setMargin(txNombre, insets);
        
        txNombre.setOnMouseClicked(e -> {
            if (txNombre.getText().equals("Ingrese un usuario")) {
                txNombre.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txNombre.clear();
            }
        });
        //chequea si cambio de texto para poder cambiar el color de la letra
        txNombre.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.equals("Ingrese un usuario")) {
                txNombre.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });

        
        
        txPassword = new TextField("Ingrese una contraseña");
        txPassword.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txPassword.setPrefHeight(30);
        VBox.setMargin(txPassword, insets);
               
        txPassword.setOnMouseClicked(e -> {
            if (txPassword.getText().equals("Ingrese una contraseña")) {
                txPassword.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txPassword.clear(); // Limpiar el texto al hacer clic si es el texto de marcador de posición
            }
        });
        txPassword.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Ingrese una contraseña")) {
                txPassword.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
    
        btConfirmar = new Button("Confirmar");
        btConfirmar.setStyle(btStyle);
        VBox.setMargin(btConfirmar, insets);
        btConfirmar.setCursor(Cursor.HAND);
        
        btConfirmar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    onConfirmarButtonClick(event);
                } catch (Exception ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
         
    
        //color verde oscuro
        BackgroundFill bk = new BackgroundFill(Color.web("#64AE49"), null, null);
        Background background = new Background(bk);
        empleadoForm.setBackground(background);
        empleadoForm.setAlignment(Pos.TOP_CENTER);
        empleadoForm.setPadding(new Insets(10));
               
        empleadoForm.getChildren().addAll(title, txNombre, txPassword, btConfirmar);
        
        empleadoStage.setTitle("Registro de Nuevo Empleado");
       
        Scene nuevaVentanaScene = new Scene(empleadoForm, 350, 350);
        empleadoStage.setScene(nuevaVentanaScene);
        empleadoStage.show();
    }
    
    public AgregarEmpleado(ConectionBD conn){
        connBD = conn;
        primaryLocalStage = new Stage();
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarEmpleado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void onConfirmarButtonClick(ActionEvent evt) throws SQLException{
        usuario = txNombre.getText();
        System.out.println("usuario: " + usuario);
        password = txPassword.getText();
        System.out.println("password: " + password);
        stmt = connBD.conect();
        
       if (!usuario.isEmpty() && !usuario.equals("Ingrese un usuario")  
        && !password.isEmpty() && !password.equals("Ingrese una contraseña")) {
            String query = "INSERT INTO \"Empleados\" (\"user\", \"password\") VALUES (?, ?)";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                pstmt.setString(1, usuario);
                pstmt.setString(2, password);

                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    alerta.mostrarAlerta("Exito", "Nuevo empleado registrado con exito", "INFORMATION");
                }else{
                    alerta.mostrarAlerta("Error", "Algo salio mal", "ERROR");
                }
            }
        }else{
            alerta.mostrarAlerta("Error", "Ha ingresados nulos o invalidos", "ERROR");
        }
        
       
        
    
    }
}

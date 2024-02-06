/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.mycompany.talabarteria.ConectionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import com.mycompany.talabarteria.Alertas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author usuario
 */
public class AgregarProveedor extends Application {
    private Label titleAgregar;
    private TextField txNombre, txCelular, txEmail;
    private ConectionBD connBD;
    private Statement stmt;
    private VerProveedores vp;
    private Stage agStage;
    private Alertas alerta = new Alertas();
     //Estilos globales
    String btStyle = "-fx-min-width: 150px; " +
                "-fx-min-height: 28px; " +
                "-fx-max-width: 7px; " +
                "-fx-max-height: 15px; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;" +
                "-fx-border-radius: 15;";
    Insets insets = new Insets(13, 0, 0, 0); 
      //color verde oscuro
    BackgroundFill backgroundFillOscuro = new BackgroundFill(Color.web("#64AE49"), null, null);
    Background bkOscuro = new Background(backgroundFillOscuro);

    
    @Override
    public void start(Stage agregarStage) throws Exception {
        this.agStage = agregarStage;
        VBox agregarVBox = new VBox();
        Scene agregarScene = new Scene(agregarVBox, 400, 400);
        
        titleAgregar = new Label("Agregar Proveedor");
        titleAgregar.setFont(new Font(25));
        
        txNombre = new TextField("Ingrese el nombre");
        txNombre.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        
        txNombre.setPrefSize(230, 28);
        txNombre.setOnMouseClicked(e -> {
            if (txNombre.getText().equals("Ingrese el nombre")) {
                txNombre.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txNombre.clear();
            }
        });
        //chequea si cambio de texto para poder cambiar el color de la letra
        txNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                txNombre.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
        
        txCelular = new TextField("Ingrese el celular");
        txCelular.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txCelular.setOnMouseClicked(e -> {
            if (txCelular.getText().equals("Ingrese el celular")) {
                txCelular.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txCelular.clear();
            }
        });
         //chequea si cambio de texto para poder cambiar el color de la letra
        txCelular.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Ingrese el celular")) {
                txCelular.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
        
        txEmail = new TextField("Ingrese el email");
        txEmail.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txEmail.setOnMouseClicked(e -> {
            if (txEmail.getText().equals("Ingrese el email")) {
                txEmail.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txEmail.clear();
            }
        });
         //chequea si cambio de texto para poder cambiar el color de la letra
        txEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Ingrese el email")) {
                txEmail.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
         
                         
        Button btGuardar = new Button("Agregar");
        btGuardar.setStyle(btStyle);

        agregarVBox.getChildren().addAll(titleAgregar, txNombre, txCelular, txEmail, btGuardar);
        agregarVBox.setAlignment(Pos.CENTER);
        //agregarVBox.setSpacing(5);
        agregarVBox.setBackground(bkOscuro);
        
        Insets txInsets = new Insets(5, 10, 10, 10);
        VBox.setMargin(titleAgregar, txInsets);
        VBox.setMargin(txNombre, txInsets );
        VBox.setMargin(txCelular, txInsets);
        VBox.setMargin(txEmail, txInsets);
        VBox.setMargin(btGuardar, txInsets);
        
        agregarStage.setScene(agregarScene);
        agregarStage.setTitle("Agregar Proveedor");

        btGuardar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                 try {
                    onGuardarButtonClick(event);
                } catch (Exception ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        });
      
        agregarStage.show();
    }
    
    public AgregarProveedor(ConectionBD conn, VerProveedores verproveedor){
        connBD = conn;
        this.vp = verproveedor;
        Stage agregarStage = new Stage();
        try {
            this.start(agregarStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public AgregarProveedor(ConectionBD conn) {
        connBD = conn;
        Stage agregarStage = new Stage();
        try {
            this.start(agregarStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void onGuardarButtonClick(ActionEvent evt) throws SQLException{
        String nombre = txNombre.getText();
        System.out.println("el nombre del cliente ingresado es: "+ nombre);
        String celular = txCelular.getText();
        if(celular.equals("Ingrese el celular") || celular.equals("")){
            celular = "";
        }
        String email = txEmail.getText();
        if(email.equals("Ingrese el email") || email.equals("")){
            email = "";
        }
        
        stmt = connBD.conect();
        
        String qyNombreProveedor = "SELECT \"nombre\" FROM \"Proveedores\"";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyNombreProveedor)) {     
            try (ResultSet rs = pstmt.executeQuery()) { 
                while(rs.next()){
                    if(rs.getString("nombre").equals(nombre)){
                        alerta.mostrarAlerta("Error", "Ya existe un proveedor con ese nombre", "ERROR");
                        return;
                    }
                } 


            }
        }
        
        
        if(nombre.equals("Ingrese el nombre") || nombre.equals("")){
            alerta.mostrarAlerta("Error", "No puedes ingresar un proveedor sin nombre", "ERROR");
        }else{
            String query = "INSERT INTO \"Proveedores\" (\"nombre\", \"celular\", \"email\")"
                    + " VALUES (?, ?, ?)";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                 
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, nombre);
                pstmt.setString(2, celular);
                pstmt.setString(3, email); 
 
                // Ejecutar la consulta
                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    alerta.mostrarAlerta("Éxito", "Proveedor cargado correctamente.", "INFORMATION");
                    txNombre.setText("Ingrese el nombre");
                    txCelular.setText("Ingrese el celular");
                    txEmail.setText("Ingrese el email");
                    vp.updateTable();
                }
            }  
        }
    }
    
}
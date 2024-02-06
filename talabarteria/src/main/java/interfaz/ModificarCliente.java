/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.mycompany.talabarteria.Alertas;
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
public class ModificarCliente extends Application{
      private ConectionBD connBD;
    private Label titleModificar;
    private TextField txNombre, txCelular, txEmail;
    private Statement stmt;
    private VerClientes vc;
    private Stage agStage;
    private Alertas alerta = new Alertas();
    private String nombreCliente, celularCliente, emailCliente;
    private int idCliente;
    private boolean cambioNombre ,cambioCelular, cambioEmail;
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

    
    public ModificarCliente(ConectionBD conn, VerClientes verCliente, String nombreCliente) {
        this.connBD = conn;
        this.vc = verCliente;
        consultaCliente(nombreCliente);
        Stage modificarStage = new Stage();
        try{   
            this.start(modificarStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage modificarStage) throws Exception {
        
        this.agStage = modificarStage;
        VBox moficarVBox = new VBox();
        Scene agregarScene = new Scene(moficarVBox, 400, 400);
        
        titleModificar = new Label("Modificar datos de Cliente");
        titleModificar.setFont(new Font(25));
        
        txNombre = new TextField(nombreCliente);
        txNombre.setPrefSize(230, 28);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                cambioNombre = true;
            }
        });
        
        txCelular = new TextField(String.valueOf(celularCliente));
        //chequea si cambio de texto para poder cambiar el color de la letra
        txCelular.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(celularCliente)) {
                cambioCelular = true;
            }
        });
             
        txEmail = new TextField(emailCliente);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(emailCliente)) {
                cambioEmail = true;
            }
        });  
                       
        Button btGuardar = new Button("Modificar");
        btGuardar.setStyle(btStyle);

        moficarVBox.getChildren().addAll(titleModificar, txNombre, txCelular, txEmail, btGuardar);
        moficarVBox.setAlignment(Pos.CENTER);
        moficarVBox.setBackground(bkOscuro);
        
        Insets txInsets = new Insets(5, 10, 10, 10);
        VBox.setMargin(titleModificar, txInsets);
        VBox.setMargin(txNombre, txInsets );
        VBox.setMargin(txCelular, txInsets);
        VBox.setMargin(txEmail, txInsets);
        VBox.setMargin(btGuardar, txInsets);
        
        modificarStage.setScene(agregarScene);
        modificarStage.setTitle("Modificar Proveedor");

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
      
        modificarStage.show();
    }
    
     private void consultaCliente(String nombreClienteSelected) {
        try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(VerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT \"idCliente\", \"nombre\", \"celular\", \"email\" "
                + "  FROM \"Clientes\" WHERE \"Clientes\".\"nombre\" = ?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nombreClienteSelected);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("idCliente");
                    nombreCliente = rs.getString("nombre");
                    celularCliente = rs.getString("celular");
                    emailCliente = rs.getString("email");                    
                } 
            }
          
        }catch(SQLException e){
            System.out.println("Error del tipo: " + e);
        }
       
    }
     
    private void onGuardarButtonClick(ActionEvent evt) throws SQLException{
        String query;
        int filasAfectadas = 0;
        try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(VerStock.class.getName()).log(Level.SEVERE, null, ex);
        } 
        if(cambioNombre){
            nombreCliente = txNombre.getText();
            query = "UPDATE \"Clientes\" SET \"nombre\" = ? WHERE \"Clientes\".\"idCliente\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, nombreCliente);
                pstmt.setInt(2, idCliente);
                filasAfectadas = pstmt.executeUpdate();
                
            } catch (SQLException ex) {
                System.out.println("error del tipo: "+  ex);
                //Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioCelular){
            celularCliente = txCelular.getText();
            query = "UPDATE \"Clientes\" SET \"celular\" = ? WHERE \"Clientes\".\"idCliente\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, celularCliente);
                pstmt.setInt(2, idCliente);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioEmail){
            emailCliente = txEmail.getText();
            query = "UPDATE \"Clientes\" SET \"email\" = ? WHERE \"Clientes\".\"idCliente\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, emailCliente);
                pstmt.setInt(2, idCliente);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        if (filasAfectadas > 0) {
            alerta.mostrarAlerta("Exito", "Cliente actualizado correctamente", "INFORMATION");
            vc.updateTable();
        }
        
    }
}

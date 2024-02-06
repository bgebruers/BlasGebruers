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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mycompany.talabarteria.Alertas;

/**
 *
 * @author usuario
 */
public class ModificarProveedor extends Application {
    private ConectionBD connBD;
    private Label titleModificar;
    private TextField txNombre, txCelular, txEmail;
    private Statement stmt;
    private VerProveedores vp;
    private Stage agStage;
    private Alertas alerta = new Alertas();
    private String nombreProveedor, celularProveedor, emailProveedor;
    private int idProveedor;
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

    
    public ModificarProveedor(ConectionBD conn, VerProveedores verProveedor, String nombreProveedor) {
        this.connBD = conn;
        this.vp = verProveedor;
        consultaProveedor(nombreProveedor);
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
        
        titleModificar = new Label("Modificar Producto");
        titleModificar.setFont(new Font(25));
        
        txNombre = new TextField(nombreProveedor);
        txNombre.setPrefSize(230, 28);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                cambioNombre = true;
            }
        });
        
        txCelular = new TextField(String.valueOf(celularProveedor));
        //chequea si cambio de texto para poder cambiar el color de la letra
        txCelular.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(celularProveedor)) {
                cambioCelular = true;
            }
        });
             
        txEmail = new TextField(emailProveedor);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(emailProveedor)) {
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
    
    private void onGuardarButtonClick(ActionEvent evt) throws SQLException{
        String query;
        int filasAfectadas = 0;
        try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(VerStock.class.getName()).log(Level.SEVERE, null, ex);
        } 
        if(cambioNombre){
            nombreProveedor = txNombre.getText();
            query = "UPDATE \"Proveedores\" SET \"nombre\" = ? WHERE \"Proveedores\".\"idProveedor\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, nombreProveedor);
                pstmt.setInt(2, idProveedor);
                filasAfectadas = pstmt.executeUpdate();
                
            } catch (SQLException ex) {
                System.out.println("error del tipo: "+  ex);
                //Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioCelular){
            celularProveedor = txCelular.getText();
            query = "UPDATE \"Proveedores\" SET \"celular\" = ? WHERE \"Proveedores\".\"idProveedor\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, celularProveedor);
                pstmt.setInt(2, idProveedor);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioEmail){
            emailProveedor = txEmail.getText();
            query = "UPDATE \"Proveedores\" SET \"email\" = ? WHERE \"Proveedores\".\"idProveedor\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, emailProveedor);
                pstmt.setInt(2, idProveedor);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
            
        if (filasAfectadas > 0) {
            alerta.mostrarAlerta("Exito", "Proveedor actualizado correctamente", "INFORMATION");
            vp.updateTable();
        }
        
    }
    
    private void consultaProveedor(String nombreProductoSelected) {
        try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(VerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        String query = "SELECT \"idProveedor\", \"nombre\", \"celular\", \"email\""
                + " FROM \"Proveedores\" WHERE \"Proveedores\".\"nombre\" = ?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nombreProductoSelected);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idProveedor = rs.getInt("idProveedor");
                    nombreProveedor = rs.getString("nombre");
                    celularProveedor = rs.getString("celular");
                    emailProveedor = rs.getString("email");
                } 
            }
        }catch(SQLException e){
            System.out.println("Error del tipo: " + e);
        }
       
    }
  
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.mycompany.talabarteria.ConectionBD;
import javafx.application.Application;
import javafx.stage.Stage;
import com.mycompany.talabarteria.ConectionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import com.mycompany.talabarteria.Alertas;

/**
 *
 * @author usuario
 */
public class ModificarProducto extends Application {
    private ConectionBD connBD;
    private Label titleModificar;
    private TextField txNombre, txPrecio, txTipo, txStockActual, txProveedorVende;
    private Statement stmt;
    private VerStock vs;
    private Stage agStage;
    private Alertas alerta = new Alertas();
    private String nombreProducto, nombreProveedor, tipoProducto;
    private int stockProducto, idProducto;
    private double precioProducto;
    private boolean cambioNombre ,cambioPrecio, cambioTipo, cambioStock, cambioProveedor;
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

    
    ModificarProducto(ConectionBD conn, VerStock verStock, String nombreProducto) {
        this.connBD = conn;
        this.vs = verStock;
        consultaProducto(nombreProducto);
        Stage modificarStage = new Stage();
        try{   
            this.start(modificarStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void start(Stage modificarStage) throws Exception {
        System.out.println("el proveedor es: " + nombreProveedor);
        
        this.agStage = modificarStage;
        VBox moficarVBox = new VBox();
        Scene agregarScene = new Scene(moficarVBox, 400, 400);
        
        titleModificar = new Label("Modificar Producto");
        titleModificar.setFont(new Font(25));
        
        txNombre = new TextField(nombreProducto);
        
        txNombre.setPrefSize(230, 28);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txNombre.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                cambioNombre = true;
            }
        });
        txPrecio = new TextField(String.valueOf(precioProducto));
        //chequea si cambio de texto para poder cambiar el color de la letra
        txPrecio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(precioProducto)) {
                cambioPrecio = true;
            }
        });
             
        txTipo = new TextField(tipoProducto);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txTipo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(tipoProducto)) {
                cambioTipo = true;
            }
        });  
        
        txStockActual = new TextField(Integer.toString(stockProducto));
        //chequea si cambio de texto para poder cambiar el color de la letra
        txStockActual.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(stockProducto)) {
                cambioStock = true;
            }
        });
        
        txProveedorVende = new TextField(nombreProveedor);
        //chequea si cambio de texto para poder cambiar el color de la letra
        txProveedorVende.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals(nombreProveedor)) {
                cambioProveedor = true;
            }
        });
                 
        Button btGuardar = new Button("Modificar");
        btGuardar.setStyle(btStyle);

        moficarVBox.getChildren().addAll(titleModificar, txNombre, txPrecio, txTipo, txStockActual, txProveedorVende ,btGuardar);
        moficarVBox.setAlignment(Pos.CENTER);
        //agregarVBox.setSpacing(5);
        moficarVBox.setBackground(bkOscuro);
        
        Insets txInsets = new Insets(5, 10, 10, 10);
        VBox.setMargin(titleModificar, txInsets);
        VBox.setMargin(txNombre, txInsets );
        VBox.setMargin(txPrecio, txInsets);
        VBox.setMargin(txTipo, txInsets);
        VBox.setMargin(txStockActual, txInsets);
        VBox.setMargin(txProveedorVende, txInsets);
        VBox.setMargin(btGuardar, txInsets);
        
        modificarStage.setScene(agregarScene);
        modificarStage.setTitle("Modificar Producto");

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
        modificarStage.setResizable(false);
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
            nombreProducto = txNombre.getText();
            query = "UPDATE \"Productos\" SET \"nombre\" = ? WHERE \"Productos\".\"idProducto\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, nombreProducto);
                pstmt.setInt(2, idProducto);
                filasAfectadas = pstmt.executeUpdate();
                
            } catch (SQLException ex) {
                System.out.println("error del tipo: "+  ex);
                //Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioPrecio){
            precioProducto = Double.parseDouble(txPrecio.getText());
            query = "UPDATE \"Productos\" SET \"precio\" = ? WHERE \"Productos\".\"idProducto\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setDouble(1, precioProducto);
                pstmt.setInt(2, idProducto);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioTipo){
            tipoProducto = txTipo.getText();
            query = "UPDATE \"Productos\" SET \"tipo\" = ? WHERE \"Productos\".\"idProducto\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setString(1, tipoProducto);
                pstmt.setInt(2, idProducto);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(cambioStock){
            stockProducto = Integer.parseInt(txStockActual.getText());
            query = "UPDATE \"Productos\" SET \"stock\" = ? WHERE \"Productos\".\"idProducto\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setInt(1, stockProducto);
                pstmt.setInt(2, idProducto);
                filasAfectadas = pstmt.executeUpdate();
            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        }
        if(cambioProveedor){
            nombreProveedor = txProveedorVende.getText();
            System.out.println("nombre del proveedor que cambia: "+ nombreProveedor);
            int idProveedor = 0;
            String qyIdProveedor = "SELECT \"idProveedor\" FROM \"Proveedores\" WHERE \"Proveedores\".\"nombre\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyIdProveedor)) {
                pstmt.setString(1, nombreProveedor);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        idProveedor = rs.getInt("idProveedor");
                        System.out.println("id proveedor nuevo: " + idProveedor);
                    } else {
                        alerta.mostrarAlerta("Error", "Ingresaste un nombre vacio o inexistente", "ERROR");
                    }
                }
            }
            
            //actualizo la tabla de producto cambiando el proveedor
            query = "UPDATE \"Productos\" SET \"idProveedor\" = ? WHERE \"Productos\".\"idProducto\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                // Establecer valores de parámetros de la consulta preparada
                pstmt.setInt(1, idProveedor);
                pstmt.setInt(2, idProveedor);
                filasAfectadas = pstmt.executeUpdate();

            } catch (SQLException ex) {
                Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
       
        if (filasAfectadas > 0) {
            alerta.mostrarAlerta("Exito", "Producto actualizado correctamente", "INFORMATION");
            vs.updateTable();
        }
        
    }
    
    private void consultaProducto(String nombreProductoSelected) {
        try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(VerStock.class.getName()).log(Level.SEVERE, null, ex);
        }
        int idProveedor = 0;
        String query = "SELECT \"idProducto\", \"nombre\", \"precio\", \"tipo\", \"stock\", \"idProveedor\" "
                + "  FROM \"Productos\" WHERE \"Productos\".\"nombre\" = ?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nombreProductoSelected);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idProducto = rs.getInt("idProducto");
                    nombreProducto = rs.getString("nombre");
                    precioProducto = rs.getDouble("precio");
                    tipoProducto = rs.getString("tipo");
                    stockProducto = rs.getInt("stock");
                    idProveedor = rs.getInt("idProveedor");
                } 
            }
            if(idProveedor != 0){
                String qyNombreProveedor = "SELECT \"nombre\" FROM \"Proveedores\" WHERE \"Proveedores\".\"idProveedor\" = ?";
                try (PreparedStatement pstmtProv = stmt.getConnection().prepareStatement(qyNombreProveedor)) {
                    pstmtProv.setInt(1, idProveedor);
                    try (ResultSet rsProv = pstmtProv.executeQuery()) {
                        if (rsProv.next()) {
                            nombreProveedor = rsProv.getString("nombre");
                        }
                    }
                } catch (SQLException e) {
                    System.out.println("Error al obtener el nombre del proveedor: " + e.getMessage());
               }
            }
        }catch(SQLException e){
            System.out.println("Error del tipo: " + e);
        }
       
    }
}

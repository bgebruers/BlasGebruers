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
 * Simple Preloader Using the ProgressBar Control
 *
 * @author usuario
 */
public class AgregarProducto extends Application {
    private Label titleAgregar;
    private TextField txNombre, txPrecio, txTipo, txStockActual, txProveedorVende;
    private ConectionBD connBD;
    private Statement stmt;
    private VerStock vs;
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
        
        titleAgregar = new Label("Agregar Producto");
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
        
        txPrecio = new TextField("Ingrese el precio");
        txPrecio.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txPrecio.setOnMouseClicked(e -> {
            if (txPrecio.getText().equals("Ingrese el precio")) {
                txPrecio.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txPrecio.clear();
            }
        });
         //chequea si cambio de texto para poder cambiar el color de la letra
        txPrecio.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Ingrese el precio")) {
                txPrecio.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
        
        txTipo = new TextField("Ingrese el tipo");
        txTipo.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txTipo.setOnMouseClicked(e -> {
            if (txTipo.getText().equals("Ingrese el tipo")) {
                txTipo.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txTipo.clear();
            }
        });
         //chequea si cambio de texto para poder cambiar el color de la letra
        txTipo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Ingrese el tipo")) {
                txTipo.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
         
        txStockActual = new TextField("Ingrese el stock actual");
        txStockActual.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txStockActual.setOnMouseClicked(e -> {
            if (txStockActual.getText().equals("Ingrese el stock actual")) {
                txStockActual.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txStockActual.clear();
            }
        });
          //chequea si cambio de texto para poder cambiar el color de la letra
        txStockActual.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                txStockActual.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
         
        txProveedorVende = new TextField("Ingrese el nombre del proveedor");
        txProveedorVende.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txProveedorVende.setOnMouseClicked(e -> {
            if (txProveedorVende.getText().equals("Ingrese el nombre del proveedor")) {
                txProveedorVende.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txProveedorVende.clear();
            }
        });
            //chequea si cambio de texto para poder cambiar el color de la letra
        txProveedorVende.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                txProveedorVende.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
         
        Button btGuardar = new Button("Agregar");
        btGuardar.setStyle(btStyle);

        agregarVBox.getChildren().addAll(titleAgregar, txNombre, txPrecio, txTipo, txStockActual, txProveedorVende ,btGuardar);
        agregarVBox.setAlignment(Pos.CENTER);
        //agregarVBox.setSpacing(5);
        agregarVBox.setBackground(bkOscuro);
        
        Insets txInsets = new Insets(5, 10, 10, 10);
        VBox.setMargin(titleAgregar, txInsets);
        VBox.setMargin(txNombre, txInsets );
        VBox.setMargin(txPrecio, txInsets);
        VBox.setMargin(txTipo, txInsets);
        VBox.setMargin(txStockActual, txInsets);
        VBox.setMargin(txProveedorVende, txInsets);
        VBox.setMargin(btGuardar, txInsets);
        
        agregarStage.setScene(agregarScene);
        agregarStage.setTitle("Agregar Producto");

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
    
    public AgregarProducto(ConectionBD conn, VerStock verstock){
        connBD = conn;
        this.vs = verstock;
        Stage agregarStage = new Stage();
        try {
            this.start(agregarStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public AgregarProducto(ConectionBD conn) {
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
        double precio;
        String tipo = txTipo.getText();
        int stock ;
        String nombreProveedor = txProveedorVende.getText();
        int idProveedor = 0;
        
        stmt = connBD.conect();
        
        String qyNombreProducto = "SELECT \"nombre\" FROM \"Productos\"";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyNombreProducto)) {     
            try (ResultSet rs = pstmt.executeQuery()) { 
                while(rs.next()){
                    if(rs.getString("nombre").equals(nombre)){
                        alerta.mostrarAlerta("Error", "Ya existe un producto con ese nombre", "ERROR");
                        return;
                    }
                } 


            }
        }
        
        
        
        if(nombre.equals("Ingrese el nombre") || nombre.equals("") ||
            tipo.equals("Ingrese el tipo") || tipo.equals("") || 
            nombreProveedor.equals("Ingrese el proveedor") || nombreProveedor.equals("") ||
            txPrecio.getText().equals("Ingrese el precio") || txPrecio.getText().equals("") ||
            txStockActual.getText().equals("Ingrese el stock atual") || txStockActual.getText().equals("")){
            
            alerta.mostrarAlerta("Error", "Asegurese de que todos los campos esten completos", "ERROR");
        }else{
            precio = Double.parseDouble(txPrecio.getText());
            stock = Integer.parseInt(txStockActual.getText());    
            String qyIdProducto = "SELECT \"idProveedor\" FROM \"Proveedores\" WHERE \"Proveedores\".\"nombre\" = ?";

            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyIdProducto)) {
                pstmt.setString(1, nombreProveedor);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        idProveedor = rs.getInt("idProveedor");
                    } else {
                        alerta.mostrarAlerta("Error", "Ingresaste un nombre vacio o inexistente", "ERROR");
                    }
                }
            }
            if(idProveedor != 0){
                String query = "INSERT INTO \"Productos\" (\"nombre\", \"tipo\", \"precio\", \"stock\", \"idProveedor\")"
                        + " VALUES (?, ?, ?, ?, ?)";
                 try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {

                    // Establecer valores de parámetros de la consulta preparada
                    pstmt.setString(1, nombre);
                    pstmt.setString(2, tipo);
                    pstmt.setDouble(3, precio); 
                    pstmt.setInt(4, stock);
                    pstmt.setInt(5, idProveedor);

                    // Ejecutar la consulta
                    int filasAfectadas = pstmt.executeUpdate();

                    if (filasAfectadas > 0) {
                        alerta.mostrarAlerta("Éxito", "Producto cargado correctamente.", "INFORMATION");
                        txNombre.setText("Ingrese el nombre");
                        txTipo.setText("Ingrese el precio");
                        txPrecio.setText("Ingrese el tipo");
                        txStockActual.setText("Cantidad stock actual");
                        txProveedorVende.setText("Ingrese el nombre del proveedor");
                        vs.updateTable();
                    }
                 } 
            }
        }
        
    }
  
  
}

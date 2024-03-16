/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

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
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mycompany.talabarteria.ConectionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import interfaz.AgregarProducto;
import com.mycompany.talabarteria.Alertas;
import java.time.LocalDateTime;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyEvent;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author usuario
 */
public class NuevaVenta extends Application {
    private VBox ventaForm;
    private HBox hbRadioButtons;
    private Statement stmt;
    private Stage primaryLocalStage;
    private Label title ;
    private Button btConfirmar;
    private TextField txCliente, txCantidad;
    private String producto, cliente;
    private int cantidad;
    private RadioButton pago, noPago;
    private ConectionBD connBD;
    private Alertas alerta = new Alertas();
    private AgregarProducto agregarProducto;
    private AgregarCliente agregarCliente;
    ComboBox<String> txProducto = new ComboBox<>();
    String btStyle = "-fx-min-width: 150px; " +
                "-fx-min-height: 35px; " +
                "-fx-max-width: 20px; " +
                "-fx-max-height: 15px; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;" +
                "-fx-border-radius: 15;";
    Insets insets = new Insets(13, 0, 0, 0);
        
    @Override
    public void start(Stage ventaStage) throws Exception {
        ventaForm = new VBox(); 

        title = new Label("Registro de nueva venta");
        title.setFont(new Font(25));
        VBox.setMargin(title, insets);
        
               
        Label placeholder = new Label("Ingrese un producto");
        txProducto = new ComboBox<>();
        txProducto.setEditable(true);
        txProducto.setStyle("-fx-background-radius: 5;");
        txProducto.setPrefWidth(1300);  
        txProducto.setPrefHeight(30);        
        VBox.setMargin(txProducto, insets);
        txProducto.setPlaceholder(placeholder);
                
        //este va a buscar a la base de datos con lo que se le ingresa
        txProducto.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            actualizarSugerencias(newValue);
        });
        txProducto.getEditor().setOnKeyPressed(event -> {
            if (!txProducto.isShowing()) {
                txProducto.show();
            }
        });
     
        txCantidad = new TextField("Cantidad vendida");
        txCantidad.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txCantidad.setPrefHeight(30);
        VBox.setMargin(txCantidad, insets);
        txCantidad.setOnMouseClicked(e -> {
            if (txCantidad.getText().equals("Cantidad vendida")) {
                txCantidad.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txCantidad.clear();
            }
        });
        txCantidad.textProperty().addListener((observable, oldValue, newValue) -> {
        // Verificar si el texto cambia de "Producto"
            if (!newValue.equals("Producto")) {
                txCantidad.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
        
        txCliente = new TextField("Ingrese un cliente");
        txCliente.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txCliente.setPrefHeight(30);
        VBox.setMargin(txCliente, insets);
        txCliente.setDisable(true);
        
        txCliente.setOnMouseClicked(e -> {
            if (txCliente.getText().equals("Ingrese un cliente")) {
                txCliente.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txCliente.clear(); // Limpiar el texto al hacer clic si es el texto de marcador de posición
            }
        });
        txCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Producto")) {
                txCliente.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
        
        hbRadioButtons = new HBox(7);
        pago = new RadioButton("Pago");
        noPago = new RadioButton("No pago");
        pago.setSelected(true);
                
       // Configurar acciones para los radio buttons
        noPago.setOnAction(event -> {
            txCliente.setDisable(false);
            pago.setSelected(false);
        });

        pago.setOnAction(event -> {
            txCliente.setDisable(true);
            noPago.setSelected(false);
        });
        
        
        hbRadioButtons.getChildren().addAll(pago, noPago);
        hbRadioButtons.setAlignment(Pos.CENTER);
 
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
        ventaForm.setBackground(background);
        ventaForm.setAlignment(Pos.TOP_CENTER);
        ventaForm.setPadding(new Insets(10));
               
        ventaForm.getChildren().addAll(title, txProducto, txCantidad, txCliente, hbRadioButtons, btConfirmar);
        
        ventaStage.setTitle("Registro Nueva Venta");
       
        Scene nuevaVentanaScene = new Scene(ventaForm, 350, 350);
        ventaStage.setScene(nuevaVentanaScene);
        ventaStage.show();
    }
      
 
    private void actualizarSugerencias(String texto) {
         try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
          String query = "SELECT \"nombre\" FROM \"Productos\" WHERE \"Productos\".\"nombre\" LIKE ? AND \"Productos\".\"nombre\" <> 'ENTREGA'";

          try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
              String textoBusqueda = "%" + texto + "%";
              pstmt.setString(1, textoBusqueda);
              
              ObservableList<String> sugerenciasList = FXCollections.observableArrayList();
              try (ResultSet rs = pstmt.executeQuery()) {
                  while (rs.next()) {
                      String nombreProducto = rs.getString("nombre");
                      sugerenciasList.add(nombreProducto);
                  }
              } catch (SQLException e) {
                  // Manejar excepciones si ocurren problemas con la consulta
                  e.printStackTrace();
              }
    
              txProducto.setItems(sugerenciasList);
              
            } catch (SQLException ex) {
              Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    public NuevaVenta(ConectionBD conn){
       connBD = conn;
       primaryLocalStage = new Stage();
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void onConfirmarButtonClick(ActionEvent evt) throws SQLException, ParseException {
        stmt = connBD.conect();
        int idProducto = 0;
        int idCliente = 0;
        double precioProducto = 0;
        //producto = txProducto.getText();
        producto = txProducto.getEditor().getText();
        cliente = txCliente.getText().toUpperCase();
        if(txCantidad.getText().isEmpty() || txCantidad.getText().equals("Cantidad vendida")){
            cantidad = 0;
        }else{
            cantidad = Integer.parseInt(txCantidad.getText());
        }
        //se inserta la fecha con hora para hacer el cancelar deuda.
        LocalDateTime fechaActual = LocalDateTime.now();
           
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String fechaFormateada = fechaActual.format(formato);
           
        LocalDateTime fechaHoraParseada = LocalDateTime.parse(fechaFormateada, formato);

        java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(fechaHoraParseada);

        String qyIdProducto = "SELECT \"idProducto\", \"precio\" FROM \"Productos\" WHERE \"Productos\".\"nombre\" = ?";

        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyIdProducto)) {
            pstmt.setString(1, producto);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idProducto = rs.getInt("idProducto");
                    precioProducto = rs.getDouble("precio");
                } else {
                    boolean okAgregar = alerta.mostrarAlertaConfirmacion("Agregar Producto", 
                        "No existe el producto ingresado, ¿Desea agregar nuevo producto?", "CONFIRMATION");
                    if(okAgregar){
                        agregarProducto = new AgregarProducto(connBD);
                        try {
                            //reinicio el boton para que se me pueda volver a ejecutar el onCOnfirmarButtonClick
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
                        } catch (Exception ex) {
                            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    
                }
            }
        }

        if(!cliente.isEmpty() || !cliente.equals("Ingrese un cliente")){
            String qyIdCliente = "SELECT \"idCliente\" FROM \"Clientes\" WHERE \"Clientes\".\"nombre\" = ?";


           try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyIdCliente)) {
            pstmt.setString(1, cliente.toUpperCase());

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("idCliente");
                } else {
                    if (noPago.isSelected()) {
                        boolean agregarCli = alerta.mostrarAlertaConfirmacion("Error", "No existe un cliente en la base de datos con ese nombre. ¿Desea Agregar un nuevo cliente?", "ERROR");
                        if (agregarCli) {
                            agregarCliente = new AgregarCliente(connBD);
                            try {
                                // Reinicio el botón para que se pueda volver a ejecutar el onConfirmarButtonClick
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
                            } catch (Exception ex) {
                                Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            // Si el usuario decide agregar un nuevo cliente, salir del método
                            return;
                            
                        }else{   
                            alerta.mostrarAlerta("Error", "Si no pago el producto debe ingresar un cliente", "ERROR");
                            return;
                        }
                    }
                }
            }
        }
           
           
        String qyStock = "SELECT \"stock\" FROM \"Productos\" WHERE \"Productos\".\"idProducto\" = ?";
        try (PreparedStatement pstmtConsultaStock = stmt.getConnection().prepareStatement(qyStock)) {
            pstmtConsultaStock.setInt(1, idProducto);
            try {
                ResultSet resultSet = pstmtConsultaStock.executeQuery(); // Corregido aquíblas
                if (resultSet.next()) {
                    int stockActual = resultSet.getInt("stock");
                    if (cantidad > stockActual) {
                        alerta.mostrarAlerta("Error", "No puedes vender mas del stock: " + stockActual, "ERROR");
                        return;
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error al obtener el nuevo stock: " + e.getMessage());
            }
        }


        String query = "INSERT INTO \"Ventas\" (\"cantidad\", \"fecha\", \"idCliente\", \"idProducto\")"
                    + " VALUES (?, ?, ?, ?)";
        if (cantidad == 0) {
            alerta.mostrarAlerta("Error", "Debe ingresar una cantidad vendida", "ERROR");    
        }else if(idProducto != 0){
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {

                // Establecer valores de parámetros de la consulta preparada
                pstmt.setInt(1, cantidad);
                pstmt.setTimestamp(2, timestamp);
                pstmt.setInt(3, idCliente); 
                pstmt.setInt(4, idProducto);

                // Ejecutar la consulta
                int filasAfectadas = pstmt.executeUpdate();

                if (filasAfectadas > 0) {
                    alerta.mostrarAlerta("Éxito", "Venta insertada correctamente.", "INFORMATION");
                    // Resetear los TextField después del éxito
                   // txProducto.setText("Producto");
                    
                    txCliente.setText("Ingrese un cliente");
                    txCantidad.setText("Cantidad vendida");
                    pago.setSelected(true);
                    noPago.setSelected(false);
                    txCliente.setDisable(true);
                    //actualizacion del stock en productos
                    String queryActualizarStock = "UPDATE \"Productos\" SET \"stock\" = \"stock\" - ? "
                            + "WHERE \"Productos\".\"idProducto\" = ?";
                    try (PreparedStatement pstmtActualizarStock = stmt.getConnection().prepareStatement(queryActualizarStock)) {
                       pstmtActualizarStock.setInt(1, cantidad);
                       pstmtActualizarStock.setInt(2, idProducto);

                       pstmtActualizarStock.executeUpdate();

                       String qyNuevoStock = "SELECT \"stock\" FROM \"Productos\" WHERE \"Productos\".\"idProducto\" = ?";
                        try (PreparedStatement pstmtConsultaStock = stmt.getConnection().prepareStatement(qyNuevoStock)) {
                            pstmtConsultaStock.setInt(1, idProducto);
                            try {
                                ResultSet resultSet = pstmtConsultaStock.executeQuery(); // Corregido aquíblas
                                if (resultSet.next()) {
                                    int nuevoStock = resultSet.getInt("stock");
                                    System.out.println("nuevo stock: " + nuevoStock);
                                    if (nuevoStock <= 10) {
                                        alerta.mostrarAlerta("¡Advertencia!", "El stock del producto: " + producto + " es de: " + nuevoStock, "INFORMATION");
                                    }
                                }
                            } catch (SQLException e) {
                                System.out.println("Error al obtener el nuevo stock: " + e.getMessage());
                            }
                        }
                       
                    } catch (SQLException e) {
                       System.out.println("Error al actualizar el stock: " + e.getMessage());
                    }

                    String queryActualizarSaldoCliente = "UPDATE \"Clientes\" SET \"saldo\" = ? "
                            + "WHERE \"Clientes\".\"idCliente\" = ?";
                    try (PreparedStatement pstmtActualizarSaldo = stmt.getConnection().prepareStatement(queryActualizarSaldoCliente)) {
                       pstmtActualizarSaldo.setDouble(1, (precioProducto*cantidad));
                       pstmtActualizarSaldo.setInt(2, idCliente);
                       pstmtActualizarSaldo.executeUpdate();
                    } catch (SQLException e) {
                       System.out.println("Error al actualizar el stock: " + e.getMessage());
                    }



                }
            } catch(SQLException e) {
                System.out.println("Error: " + e.getMessage());
            }

        }
    }
    }

}
        


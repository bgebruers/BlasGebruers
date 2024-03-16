/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mycompany.talabarteria.ConectionBD;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import interfaz.AgregarProducto;
import java.sql.PreparedStatement;
import interfaz.ModificarProducto;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.TableRow;
import javafx.scene.input.MouseButton;
import org.postgresql.util.PSQLException;
import com.mycompany.talabarteria.Alertas;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author usuario
 */
public class VerStock extends Application {
    private Stage primaryLocalStage;
    private VBox bxStock, bxTable;
    private HBox hxButtons;
    private Button btAgregar, btEliminar, btModificar;
    private Label title;
    private TextField txSearch;
    private double anchoEscena; 
    private int tamañoColumna = 250;
    private Alertas alerta = new Alertas();
    private ConectionBD connBD;
    Statement stmt;
    TableView<Producto> tabla = new TableView<>();
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    //Estilos globales
    String btStyle = "-fx-min-width: 150px; " +
                "-fx-min-height: 28px; " +
                "-fx-max-width: 7px; " +
                "-fx-max-height: 15px; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;" +
                "-fx-border-radius: 15;";
    Insets insets = new Insets(13, 0, 0, 0); 
        
    //color claro
    BackgroundFill backgroundFillClaro = new BackgroundFill(Color.web("#D9FFC1"), null, null);
    Background bkClaro = new Background(backgroundFillClaro);

    public VerStock() {}
    
    @Override
    public void start(Stage stockStage) throws Exception {
        bxStock = new VBox();
        hxButtons = new HBox(10);
        bxTable = new VBox();
                  
        Scene stockScene = new Scene(bxStock, 1000, 600);
        anchoEscena = stockScene.getWidth();

        title = new Label("Listado de Productos");
        title.setFont(new Font(25));
        VBox.setMargin(title, insets);
        
               
        btAgregar = new Button("Agregar");
        btAgregar.setStyle(btStyle);
        
        btAgregar.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    onAgregarButtonClick(event);
                } catch (Exception ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        btEliminar = new Button("Eliminar");
        btEliminar.setStyle(btStyle);
        btEliminar.setDisable(true);
        
       
        btModificar = new Button("Modificar");
        btModificar.setStyle(btStyle);
        btModificar.setDisable(true);
       
        txSearch = new TextField("Busca un producto");
        txSearch.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txSearch.setPrefSize(230, 28);
        txSearch.setOnMouseClicked(e -> {
            if (txSearch.getText().equals("Busca un producto")) {
                txSearch.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txSearch.clear();
            }
        });
        txSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Si el campo de búsqueda está vacío, mostrar todos los productos
                tabla.setItems(listaProductos);
            } else {
                // Utilizar FilteredList para realizar la búsqueda predictiva
                FilteredList<Producto> filteredList = new FilteredList<>(listaProductos, p ->
                        p.getNombre().toLowerCase().contains(newValue.toLowerCase()));

                tabla.setItems(filteredList);
            }
        });
        hxButtons.getChildren().addAll(btAgregar, btEliminar, btModificar, txSearch);
        hxButtons.setPadding(new Insets(10));
        hxButtons.setAlignment(Pos.CENTER_LEFT);
        
        // Crear columnas
        TableColumn<Producto, String> columnaProducto = new TableColumn<>("Producto");
        columnaProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaProducto.setPrefWidth(tamañoColumna);

        TableColumn<Producto, Double> columnaPrecio = new TableColumn<>("Precio");
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaPrecio.setPrefWidth(tamañoColumna);

        TableColumn<Producto, Integer> columnaStock = new TableColumn<>("Stock");
        columnaStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        columnaStock.setPrefWidth(tamañoColumna);

        TableColumn<Producto, String> columnaProveedor = new TableColumn<>("Proveedor");
        columnaProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        columnaProveedor.setPrefWidth(tamañoColumna);
        
        //este codigo me permite darle el click a la fila y sacar el nombre del producto
        tabla.setRowFactory(tv -> {
            TableRow<Producto> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    Producto productoSeleccionado = row.getItem();
                    btEliminar.setDisable(false);
                    btEliminar.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                onEliminarButtonClick(event, productoSeleccionado.getNombre());
                            } catch (Exception ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                   
                    btModificar.setDisable(false);
                    btModificar.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                             
                            try {
                                onModificarButtonClick(event, productoSeleccionado.getNombre());
                            } catch (Exception ex) {
                                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        }
                    });
                 
                }
            });
            return row;
        });
        // Agregar columnas a la tabla
        tabla.getColumns().addAll(columnaProducto, columnaPrecio, columnaStock, columnaProveedor);
         
        //captura el tamaño de la pantalla y setea el tamaño de las columnas
        stockScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                anchoEscena = (double) newValue;
                tamañoColumna = (int) (anchoEscena / 4);
                columnaProducto.setPrefWidth(tamañoColumna); 
                columnaPrecio.setPrefWidth(tamañoColumna); 
                columnaStock.setPrefWidth(tamañoColumna); 
                columnaProveedor.setPrefWidth(tamañoColumna);
            }
        });
        
        // Conectar a la base de datos y obtener los productos
        cargarProductosDesdeBD(tabla);
        
        bxTable.getChildren().add(tabla);
        
        bxStock.setAlignment(Pos.TOP_CENTER);
        bxStock.getChildren().addAll(title, hxButtons, bxTable);
        
        stockStage.setTitle("Listado de Productos");
         
        bxStock.setBackground(bkClaro);
        
        stockStage.setScene(stockScene);
        stockStage.show();
    }
    
    public VerStock(ConectionBD conn){
       connBD = conn;
       primaryLocalStage = new Stage();
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
       
        }
    }
    public static class Producto {
        private String nombre;
        private double precio;
        private int stock;
        private String proveedor;

        public Producto(String nombre, double precio, int stock, String nombreProv) {
            this.nombre = nombre;
            this.precio = precio;
            this.stock = stock;
            this.proveedor = nombreProv;
        }

        public String getNombre() {
            return nombre;
        }

        public double getPrecio() {
            return precio;
        }

        public int getStock() {
            return stock;
        }
        public String getProveedor() {
            return proveedor;
        }
    }
    private void cargarProductosDesdeBD(TableView<Producto> tabla) throws SQLException {
        stmt = connBD.conect();
        String nombreProveedor = null;
        String nombre = null;
        String query = "SELECT \"nombre\", \"precio\", \"stock\", \"idProveedor\" FROM \"Productos\"";
        try {
            ResultSet resultSet = stmt.executeQuery(query);

            // Limpiar la lista antes de agregar nuevos productos
            listaProductos.clear();

            while (resultSet.next()) {
                nombre = resultSet.getString("nombre");

                if (!nombre.equals("ENTREGA")) {
                    double precio = resultSet.getDouble("precio");
                    int stock = resultSet.getInt("stock");
                    int idProveedor = resultSet.getInt("idProveedor");

                    String qyIdProducto = "SELECT \"nombre\" FROM \"Proveedores\" WHERE \"Proveedores\".\"idProveedor\" = ?";
                    nombreProveedor = "";

                    try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(qyIdProducto)) {
                        pstmt.setInt(1, idProveedor);
                        try (ResultSet rs = pstmt.executeQuery()) {
                            if (rs.next()) {
                                nombreProveedor = rs.getString("nombre");
                                
                            }
                        }
                    }

                    listaProductos.add(new Producto(nombre, precio, stock, nombreProveedor.toUpperCase()));
                }
            }

            // Establecer la lista de productos en la tabla
            tabla.setItems(listaProductos);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    
    }

    private void onAgregarButtonClick(ActionEvent evt) {
        AgregarProducto ap = new AgregarProducto(connBD, this);
    }
    
    public void updateTable() throws SQLException{
        try {
            if (tabla != null) {
                cargarProductosDesdeBD(tabla);
                tabla.refresh();
            } else {
                System.out.println("La tabla es nula");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    
    
    private void onEliminarButtonClick(ActionEvent evt, String nombreProductoSelected) throws SQLException{
        boolean ok = alerta.mostrarAlertaConfirmacion("¡Advertencia!", "¿Desea eliminar este producto?", "CONFIRMATION");
        if(ok && nombreProductoSelected != ""){
            String query = "DELETE FROM \"Productos\" WHERE \"Productos\".\"nombre\" = ?";
             try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                pstmt.setString(1, nombreProductoSelected);
                
                int filasAfectadas = pstmt.executeUpdate();
                if(filasAfectadas > 0){
                    alerta.mostrarAlerta("Exito", "Producto eliminado correctamente", "INFORMATION");
                    updateTable();
                }
            }catch (PSQLException e) {
                if ("23503".equals(e.getSQLState())) {
                    // Manejar la violación de clave foránea aquí
                    alerta.mostrarAlerta("Error", 
                            "No es posible eliminar este producto ya que hay una venta registrada, "
                                    + "¿Desea eliminar el producto y la venta?", 
                            "ERROR");
                   
                } else {
                    // Otro manejo de excepciones
                    System.err.println("Error inesperado: " + e.getMessage());
                }
                    }    
                }
    }
    
    private void onModificarButtonClick(ActionEvent evt, String nombreProductoSelected){
        ModificarProducto mp = new ModificarProducto(connBD, this, nombreProductoSelected);
    }
    
    

    
}
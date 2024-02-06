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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import com.mycompany.talabarteria.Alertas;        
/**
 *
 * @author usuario
 */

public class VerProveedores extends Application {
    private ConectionBD connBD;
    private Statement stmt;
    private Stage primaryLocalStage;
    private Label titleProveedores;
    private VBox bxProveedores, bxTable;
    private HBox hxButtons;
    private double anchoEscena; 
    private int tamañoColumna = 333;
    private Alertas alerta = new Alertas();
    private Button btAgregar, btEliminar, btModificar;
    private TextField txSearch;
    TableView<VerProveedores.Proveedor> tabla = new TableView<>();
    private ObservableList<VerProveedores.Proveedor> listaProveedores = FXCollections.observableArrayList();
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
    
    @Override
    public void start(Stage proveedoresStage) throws Exception {
        bxProveedores = new VBox();
        bxTable = new VBox();
        
        titleProveedores = new Label("Lista de Proveedores");
        titleProveedores.setFont(new Font(25));
        VBox.setMargin(titleProveedores, insets);
        Scene proveedoresScene = new Scene(bxProveedores, 1000, 600);
        
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
        
        txSearch = new TextField("Buscar un proveedor");
        txSearch.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txSearch.setPrefSize(230, 28);
        txSearch.setOnMouseClicked(e -> {
            if (txSearch.getText().equals("Buscar un proveedor")) {
                txSearch.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txSearch.clear();
            }
        });
        txSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Si el campo de búsqueda está vacío, mostrar todos los productos
                tabla.setItems(listaProveedores);
            } else {
                // Utilizar FilteredList para realizar la búsqueda predictiva
                FilteredList<VerProveedores.Proveedor> filteredList = new FilteredList<>(listaProveedores, p ->
                        p.getNombre().toLowerCase().contains(newValue.toLowerCase()));

                tabla.setItems(filteredList);
            }
        });
        
        
        hxButtons = new HBox(10);
        hxButtons.getChildren().addAll(btAgregar, btEliminar, btModificar, txSearch);
        hxButtons.setPadding(new Insets(10));
        hxButtons.setAlignment(Pos.CENTER_LEFT);
        
        
        // Crear columnas
        TableColumn<VerProveedores.Proveedor, String> columnaProveedor = new TableColumn<>("Nombre");
        columnaProveedor.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaProveedor.setPrefWidth(tamañoColumna);

        TableColumn<VerProveedores.Proveedor, Double> columnaCelular = new TableColumn<>("Celular");
        columnaCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        columnaCelular.setPrefWidth(tamañoColumna);

        TableColumn<VerProveedores.Proveedor, Integer> columnaEmail = new TableColumn<>("Email");
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaEmail.setPrefWidth(tamañoColumna);

        
        //este codigo me permite darle el click a la fila y sacar el nombre del producto
        tabla.setRowFactory(tv -> {
            TableRow<VerProveedores.Proveedor> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 1) {
                    VerProveedores.Proveedor proveedorSeleccionado = row.getItem();
                    btEliminar.setDisable(false);
                    btEliminar.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent event) {
                            try {
                                onEliminarButtonClick(event, proveedorSeleccionado.getNombre());
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
                                onModificarButtonClick(event, proveedorSeleccionado.getNombre());
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
        tabla.getColumns().addAll(columnaProveedor, columnaCelular, columnaEmail);
         
        proveedoresScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                anchoEscena = (double) newValue;
                tamañoColumna = (int) (anchoEscena / 3);
                columnaProveedor.setPrefWidth(tamañoColumna); 
                columnaCelular.setPrefWidth(tamañoColumna); 
                columnaEmail.setPrefWidth(tamañoColumna); 
                
            }
        });
        // Conectar a la base de datos y obtener los productos
        cargarProveedoresDesdeBD(tabla);
        
        bxTable.getChildren().add(tabla);
        
        
        proveedoresStage.setTitle("Listado de Productos");
             
        
        bxProveedores.getChildren().addAll(titleProveedores, hxButtons, bxTable);
        bxProveedores.setBackground(bkClaro);
        bxProveedores.setAlignment(Pos.TOP_CENTER);
        
        
        anchoEscena = proveedoresScene.getWidth();
        
        proveedoresStage.setScene(proveedoresScene);
        proveedoresStage.show();
    }
    
    private void onEliminarButtonClick(ActionEvent event, String nombreProveedorSelected) throws SQLException {
        boolean ok = alerta.mostrarAlertaConfirmacion("¡Advertencia!", "¿Desea eliminar este producto?", "CONFIRMATION");
        if(ok && nombreProveedorSelected != ""){
            String query = "DELETE FROM \"Proveedores\" WHERE \"Proveedores\".\"nombre\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                pstmt.setString(1, nombreProveedorSelected);
                
                int filasAfectadas = pstmt.executeUpdate();
                if(filasAfectadas > 0){
                    alerta.mostrarAlerta("Exito", "Proveedor eliminado correctamente", "INFORMATION");
                    updateTable();
                }
            } 
        }
    }
    private void onModificarButtonClick(ActionEvent event, String nombreProveedorSelected) {
        ModificarProveedor mp = new ModificarProveedor(connBD, this, nombreProveedorSelected);
    }
    private void onAgregarButtonClick(ActionEvent event) {
        AgregarProveedor ap = new AgregarProveedor(connBD, this);
    }
    
    public VerProveedores(ConectionBD conn){
        connBD = conn;
        primaryLocalStage = new Stage();
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
       
        }
    }
    
    public static class Proveedor {
        private String nombre;
        private String celular;
        private String email;
        
        public Proveedor(String nombre, String celular, String email) {
            this.nombre = nombre;
            this.celular = celular;
            this.email = email;
        }

        public String getNombre() {
            return nombre;
        }

        public String getCelular() {
            return celular;
        }

        public String getEmail() {
            return email;
        }
       
    }
    private void cargarProveedoresDesdeBD(TableView<VerProveedores.Proveedor> tabla) throws SQLException {
        stmt = connBD.conect();
         
        String query = "SELECT \"nombre\", \"celular\", \"email\" FROM \"Proveedores\"";
        try {
            ResultSet resultSet = stmt.executeQuery(query);

            // Limpiar la lista antes de agregar nuevos productos
            listaProveedores.clear();

            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String celular = resultSet.getString("celular");
                String email = resultSet.getString("email");
                

                listaProveedores.add(new VerProveedores.Proveedor(nombre, celular, email));
            }

            // Establecer la lista de productos en la tabla
            tabla.setItems(listaProveedores);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void updateTable() throws SQLException{
        if (tabla != null) {
            cargarProveedoresDesdeBD(tabla);
            tabla.refresh();
        } else {
            System.out.println("La tabla es nula");
        }
    }
   

}


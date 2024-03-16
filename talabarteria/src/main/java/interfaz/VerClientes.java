/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.mycompany.talabarteria.Alertas;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;
import com.mycompany.talabarteria.ConectionBD;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.animation.PauseTransition;
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
import javafx.util.Duration;

/**
 *
 * @author usuario
 */
public class VerClientes extends Application{
    private ConectionBD connBD;
    private Stage primaryLocalStage;
    private Label titleClientes;
    private Button btAgregar, btEliminar, btModificar, btAgregarEntrega;
    private TextField txSearch;
    private VBox bxClientes, bxTable;
    private HBox hxButtons;
    private double anchoEscena; 
    private int tamañoColumna = 250;
    private Alertas alerta = new Alertas();
    Statement stmt;
    TableView<VerClientes.Cliente> tabla = new TableView<>();
    private ObservableList<VerClientes.Cliente> listaClientes = FXCollections.observableArrayList();
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
    
    //color claro
    BackgroundFill backgroundFillClaro = new BackgroundFill(Color.web("#D9FFC1"), null, null);
    Background bkClaro = new Background(backgroundFillClaro);
    
    @Override
    public void start(Stage clientesStage) throws Exception {
        bxClientes = new VBox();
        bxTable = new VBox();
        hxButtons = new HBox(10);
        Scene clientesScene = new Scene(bxClientes, 1000, 600);
        anchoEscena = clientesScene.getWidth();
        
        titleClientes = new Label("Listado de Clientes");
        titleClientes.setFont(new Font(25));
        VBox.setMargin(titleClientes, insets);
        
               
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
       
        btAgregarEntrega = new Button("Agregar entrega");
        btAgregarEntrega.setStyle(btStyle);
        btAgregarEntrega.setDisable(true);
        
        txSearch = new TextField("Busca un cliente");
        txSearch.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        txSearch.setPrefSize(230, 28);
        txSearch.setOnMouseClicked(e -> {
            if (txSearch.getText().equals("Busca un cliente")) {
                txSearch.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txSearch.clear();
            }
        });
        txSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.trim().isEmpty()) {
                // Si el campo de búsqueda está vacío, mostrar todos los productos
                tabla.setItems(listaClientes);
            } else {
                // Utilizar FilteredList para realizar la búsqueda predictiva
                FilteredList<VerClientes.Cliente> filteredList = new FilteredList<>(listaClientes, p ->
                        p.getNombre().toLowerCase().contains(newValue.toLowerCase()));

                tabla.setItems(filteredList);
            }
        });
        hxButtons.getChildren().addAll(btAgregar, btEliminar, btModificar, btAgregarEntrega, txSearch);
        hxButtons.setPadding(new Insets(10));
        hxButtons.setAlignment(Pos.CENTER_LEFT);
          // Crear columnas
        TableColumn<VerClientes.Cliente, String> columnaNombre = new TableColumn<>("Nombre");
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaNombre.setPrefWidth(tamañoColumna);

        TableColumn<VerClientes.Cliente, Double> columnaCelular = new TableColumn<>("Celular");
        columnaCelular.setCellValueFactory(new PropertyValueFactory<>("celular"));
        columnaCelular.setPrefWidth(tamañoColumna);

        TableColumn<VerClientes.Cliente, Integer> columnaEmail = new TableColumn<>("Email");
        columnaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnaEmail.setPrefWidth(tamañoColumna);

        TableColumn<VerClientes.Cliente, Integer> columnaCuit = new TableColumn<>("CUIT");
        columnaCuit.setCellValueFactory(new PropertyValueFactory<>("cuit"));
        columnaCuit.setPrefWidth(tamañoColumna);

        
        //este codigo me permite darle el click a la fila y sacar el nombre del producto
        tabla.setRowFactory(tv -> {
            TableRow<VerClientes.Cliente> row = new TableRow<>();
            PauseTransition pause = new PauseTransition(Duration.millis(300)); // Ajusta el tiempo según tus necesidades
            pause.setOnFinished(event -> {
                // Acciones para un clic único
                VerClientes.Cliente clienteSeleccionado = row.getItem();
                btEliminar.setDisable(false);
                btEliminar.setOnAction(e -> {
                    try {
                        onEliminarButtonClick(e, clienteSeleccionado.getNombre());
                    } catch (Exception ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });

                btModificar.setDisable(false);
                btModificar.setOnAction(e -> {
                    try {
                        onModificarButtonClick(e, clienteSeleccionado.getNombre());
                    } catch (Exception ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                btAgregarEntrega.setDisable(false);
                btAgregarEntrega.setOnAction(e -> {
                    try {
                        onAgregarEntregaButtonClick(e, clienteSeleccionado.getNombre());
                    } catch (Exception ex) {
                        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
            });
             row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    if (event.getClickCount() == 2) {
                        // Acciones para doble clic
                        VerClientes.Cliente clienteSeleccionado = row.getItem();
                        // Agrega aquí la lógica para el doble clic
                        onDobleClickAction(clienteSeleccionado.getNombre());   
                        
                    } else {
                        // Inicia el temporizador para un clic único
                        pause.playFromStart();
                    }
                }
            });
            return row;
        });
         //captura el tamaño de la pantalla y setea el tamaño de las columnas
        clientesScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                anchoEscena = (double) newValue;
                tamañoColumna = (int) (anchoEscena / 4);
                columnaNombre.setPrefWidth(tamañoColumna); 
                columnaCelular.setPrefWidth(tamañoColumna); 
                columnaEmail.setPrefWidth(tamañoColumna); 
                columnaCuit.setPrefWidth(tamañoColumna);
               
            }
        });
        // Agregar columnas a la tabla
        tabla.getColumns().addAll(columnaNombre, columnaCelular, columnaEmail, columnaCuit);
        cargarClientesDesdeBD(tabla);
        bxTable.getChildren().add(tabla);
        
        bxClientes.setAlignment(Pos.TOP_CENTER);
        bxClientes.getChildren().addAll(titleClientes, hxButtons, bxTable);
        
        clientesStage.setTitle("Listado de Clientes");
         
        bxClientes.setBackground(bkClaro);
        
        clientesStage.setScene(clientesScene);
        clientesStage.show();
    }
    
    public VerClientes(ConectionBD conn){
        connBD = conn;
        primaryLocalStage = new Stage();
        
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
       
        }
    }
     public static class Cliente {
        private String nombre;
        private String celular;
        private String email;
        private String cuit;


        public Cliente(String nombre, String celular, String email, String cuit) {
            this.nombre = nombre;
            this.celular = celular;
            this.email = email;
            this.cuit = cuit;
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
        public String getCuit(){
            return cuit;
        }
    } 
     
    private void onEliminarButtonClick(ActionEvent evt, String nombreClienteSelected){
        boolean ok = alerta.mostrarAlertaConfirmacion("¡Advertencia!", "¿Desea eliminar este producto?", "CONFIRMATION");
        if(ok ){
            String query = "DELETE FROM \"Clientes\" WHERE \"Clientes\".\"nombre\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                pstmt.setString(1, nombreClienteSelected.toUpperCase());
                
                int filasAfectadas = pstmt.executeUpdate();
                if(filasAfectadas > 0){
                    alerta.mostrarAlerta("Exito", "Cliente eliminado correctamente", "INFORMATION");
                    updateTable();
                }
            } catch (SQLException ex) {
                Logger.getLogger(VerClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    private void onModificarButtonClick(ActionEvent evt, String nombreClienteSeleccionado){
        ModificarCliente mc = new ModificarCliente(connBD, this, nombreClienteSeleccionado);
    }
    private void onAgregarButtonClick(ActionEvent evt){
        AgregarCliente ac = new AgregarCliente(connBD, this);
    }
    private void onAgregarEntregaButtonClick(ActionEvent evt, String nombreClienteSeleccionado){
        AgregarEntregaCliente agc = new AgregarEntregaCliente(connBD, this ,nombreClienteSeleccionado);
    }
    private void onDobleClickAction(String clienteSelected){
        CuentaCorriente cc = new CuentaCorriente(connBD, clienteSelected);
    }
    
    public void updateTable() throws SQLException{
        try {
            if (tabla != null) {
                cargarClientesDesdeBD(tabla);
                tabla.refresh();
            } else {
                System.out.println("La tabla es nula");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void cargarClientesDesdeBD(TableView<VerClientes.Cliente> tabla) throws SQLException {
        stmt = connBD.conect();
        
        String query = "SELECT \"idCliente\", \"nombre\", \"celular\", \"email\", \"cuit\" FROM \"Clientes\"";
        try {
            ResultSet resultSet = stmt.executeQuery(query);

            // Limpiar la lista antes de agregar nuevos productos
            listaClientes.clear();

            while (resultSet.next()) {
                int idCliente = resultSet.getInt("idCliente");
                String nombre = resultSet.getString("nombre").toUpperCase();
                String celular = resultSet.getString("celular");
                String email = resultSet.getString("email");
                String cuit = resultSet.getString("cuit");
                if(idCliente != 0){
                   listaClientes.add(new VerClientes.Cliente(nombre, celular, email, cuit));
                }   
            }

            // Establecer la lista de productos en la tabla
            tabla.setItems(listaClientes);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

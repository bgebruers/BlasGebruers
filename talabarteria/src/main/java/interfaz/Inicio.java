/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;


import com.mycompany.talabarteria.ConectionBD;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import interfaz.NuevaVenta;
import javafx.application.Application;
/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author usuario
 */
public class Inicio extends Application {
    private Scene scene;
    private Stage primaryStage;
    private Label title, menu;
    private VBox inicioRoot, bxTitle, bxMenu; 
    private HBox hxBody;    //el logo va en este
    private Button btVenta, btStock, btProveedor, btResumen, btClientes, btEmpleado, btAgregarProducto, btCerrarSesion;
    private ConectionBD connBD;
    @Override
    public void start(Stage primaryStage) throws Exception {
        //principal, el que ocupara toda la scene de fondo  
        inicioRoot = new VBox();
        inicioRoot.setSpacing(10);
        
        bxTitle = new VBox();
        title = new Label("Talabarteria Luiggi");
        title.setFont(new Font(30));
        bxTitle.setMinSize(1000, 100); 
        bxTitle.setMaxSize(Double.MAX_VALUE, 250); 
        bxTitle.setAlignment(Pos.CENTER);
        
        //color verde oscuro
        BackgroundFill bk = new BackgroundFill(Color.web("#64AE49"), null, null);
        Background background2 = new Background(bk);
        bxTitle.setBackground(background2);
        bxTitle.getChildren().addAll(title);
        
        //el que va al medio, ocupa el resto de la pantalla.
        hxBody = new HBox();
        
        bxMenu = new VBox();
        bxMenu.setBackground(background2);
                
        //color claro
        BackgroundFill backgroundFill = new BackgroundFill(Color.web("#D9FFC1"), null, null);
        Background background = new Background(backgroundFill);
     
        hxBody.getChildren().add(bxMenu);
        
        bxMenu.setMinSize(200, 1000); 
        bxMenu.setMaxSize(Double.MAX_VALUE, 2500); 
        
        String btStyle = "-fx-min-width: 150px; " +
                "-fx-min-height: 35px; " +
                "-fx-max-width: 20px; " +
                "-fx-max-height: 15px; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;" +
                "-fx-border-radius: 15;";
        Insets insets = new Insets(10, 0, 0, 0);
        

        btVenta = createButton("Venta", btStyle, this::onVentaButtonClick);
        VBox.setMargin(btVenta, insets);
        btVenta.setCursor(Cursor.HAND);
        
        btStock = createButton("Productos", btStyle, this::onStockButtonClick);
        VBox.setMargin(btStock, insets);
        btStock.setCursor(Cursor.HAND);
        
        btProveedor = createButton("Proveedor", btStyle, this::onProveedorButtonClick);
        VBox.setMargin(btProveedor, insets);
        btProveedor.setCursor(Cursor.HAND);
        
        btResumen = createButton("Resumen", btStyle, this::onResumenButtonClick);
        VBox.setMargin(btResumen, insets);
        btResumen.setCursor(Cursor.HAND);
        
        btClientes = createButton("Clientes", btStyle, this::onClientesButtonClick);
        VBox.setMargin(btClientes, insets);
        btClientes.setCursor(Cursor.HAND);
        
        btEmpleado = createButton("Empleados", btStyle, this::onEmpleadoButtonClick);
        VBox.setMargin(btEmpleado, insets);
        btEmpleado.setCursor(Cursor.HAND);
        
        btAgregarProducto = createButton("Agregar Producto", btStyle, this::onAregarButtonClick);
        VBox.setMargin(btAgregarProducto, insets);
        btAgregarProducto.setCursor(Cursor.HAND);
        
        btCerrarSesion = createButton("Cerrar Sesion", btStyle, this::onCerrarSesionButtonClick);
        
        menu = new Label("MENÚ");
        menu.setFont(new Font(20));
        
        bxMenu.getChildren().addAll(menu,
                btVenta, 
                btStock, 
                btProveedor, 
                btClientes, 
                btResumen, 
                btEmpleado);
        bxMenu.setAlignment(Pos.TOP_CENTER);
      
        
        inicioRoot.getChildren().addAll(bxTitle, hxBody);
        inicioRoot.setBackground(background);
        scene = new Scene(inicioRoot,  1200, 600);
        
        primaryStage.setTitle("Talabarteria Luiggi");
        primaryStage.setScene(scene);
        primaryStage.show(); 
    }
    
    
    public Inicio(Stage primaryStage, ConectionBD conn) throws Exception {
       connBD = conn;
       start(primaryStage);
    }

    public Scene getScene() {
        return scene;
    }
    public void setStage(Stage primaryStage){
        this.primaryStage = primaryStage;
    }
    
    private Button createButton(String text, String btStyle, EventHandler<ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(btStyle);
        button.setOnAction(handler);
        return button;
    }

    private void onVentaButtonClick(ActionEvent event){   
       NuevaVenta nv = new NuevaVenta(connBD);      
    }
    
    private void onStockButtonClick(ActionEvent event) {   
        VerStock vs = new VerStock(connBD);
    }
    private void onProveedorButtonClick(ActionEvent event) {   
        VerProveedores vr = new VerProveedores(connBD);
    }
    private void onResumenButtonClick(ActionEvent event) {   
        System.out.println("Accion del boton resumen");
    }
    private void onClientesButtonClick(ActionEvent event) {   
        VerClientes vc = new VerClientes(connBD);
    }
    private void onEmpleadoButtonClick(ActionEvent event) {   
        System.out.println("Accion del boton empleado");
    }
    
    private void onAregarButtonClick(ActionEvent event){
        System.out.println("Accion del boton agregar producto");
    }
    
    private void onCerrarSesionButtonClick(ActionEvent event) {   
        System.out.println("Accion del boton cerrar sesion");
    }
    
    
    
}

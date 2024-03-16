/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;


import com.mycompany.talabarteria.Alertas;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 * Simple Preloader Using the ProgressBar Control
 *
 * @author usuario
 */
public class Inicio extends Application {
    private Scene scene;
    private Stage primaryStage;
    private Label title, menu;
    private VBox bxTitle, bxMenu; 
    private BorderPane inicioRoot;
    private HBox hxBody;    //el logo va en este
    private Button btVenta, btStock, btProveedor, btResumen, btClientes, btEmpleado, btCerrarSesion;
    private ConectionBD connBD;
    private Alertas alerta = new Alertas();
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        inicioRoot = new BorderPane();
        
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
        
        Image img = new Image(getClass().getResourceAsStream("/images/logo.png"));
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(420);
        imgView.setFitWidth(520);
        HBox.setMargin(imgView, new Insets(70, 0, 0, 150));
   
        hxBody.getChildren().addAll(bxMenu, imgView);
        
        bxMenu.setMinSize(250, 1000); 
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
        
               
        btCerrarSesion = createButton("Cerrar Sesion", btStyle, this::onCerrarSesionButtonClick);
        btCerrarSesion.setCursor(Cursor.HAND);
        VBox.setMargin(btCerrarSesion, new Insets(120, 0, 0, 0));
        
        menu = new Label("MENÚ");
        menu.setFont(new Font(20));
        VBox.setMargin(menu, new Insets(70, 0, 0, 0));
        bxMenu.getChildren().addAll(menu,
                btVenta, 
                btStock, 
                btProveedor, 
                btClientes, 
                btResumen, 
                btEmpleado,
                btCerrarSesion);
        bxMenu.setAlignment(Pos.TOP_CENTER);
      
        
        
                    //color negro
        BackgroundFill backgroundFillNegro = new BackgroundFill(Color.web("#36373b"), null, null);
        Background bkNegro = new Background(backgroundFillNegro);
        
        //inicioRoot.setTop(bxTitle);
        inicioRoot.setLeft(hxBody);

        inicioRoot.setBackground(bkNegro);
        scene = new Scene(inicioRoot, 1100, 600);
        
        primaryStage.setTitle("Talabarteria Luiggi");
        primaryStage.setScene(scene);
        primaryStage.show(); 
        primaryStage.setResizable(true);
        primaryStage.centerOnScreen();
    }
    
      
    public Inicio(Stage primaryStage, ConectionBD conn) throws Exception {
       connBD = conn;
       primaryStage.centerOnScreen();
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
        Resumen r = new Resumen(connBD);
    }
    private void onClientesButtonClick(ActionEvent event) {   
        VerClientes vc = new VerClientes(connBD);
    }
    private void onEmpleadoButtonClick(ActionEvent event) {   
        new AgregarEmpleado(connBD);
    }

    private void onCerrarSesionButtonClick(ActionEvent event) {   
        boolean ok = alerta.mostrarAlertaConfirmacion("¡Advertencia!", "¿Desea cerrar sesion?", "CONFIRMATION");
        if(ok){
            primaryStage.close();
        }

    }
    
    
    
}

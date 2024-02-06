/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.mycompany.talabarteria.ConectionBD;
import java.sql.PreparedStatement;
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
import java.sql.ResultSet;
/**
 *
 * @author usuario
 */
public class AgregarEntregaCliente extends Application {
    private ConectionBD connBD;
    private Label nombreCliente, titleEntrega;
    private TextField txEntrega;
    private Button btConfirmar;
    private VBox bxEntrega, bxNombreCliente;
    private String nombre;
    private VerClientes vc;
    private Statement stmt;
    private Alertas alerta = new Alertas();
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
    public void start(Stage entregaStage) throws Exception {
        bxEntrega = new VBox();
        bxNombreCliente = new VBox();
        Scene entregaScene = new Scene(bxEntrega, 300, 300);
        
        titleEntrega = new Label("Agregar entrega de Cliente");
        titleEntrega.setFont(new Font(20));
        VBox.setMargin(titleEntrega, insets);
        
        nombreCliente = new Label("Cliente: " + nombre);
        nombreCliente.setFont(new Font(15));
        VBox.setMargin(nombreCliente, insets);
        
        txEntrega = new TextField("Ingrese el monto entregado");
        txEntrega.setStyle("-fx-background-radius: 5; -fx-border-radius: 5; -fx-text-fill: #B1B1B1;");
        VBox.setMargin(txEntrega, insets);
        
        txEntrega.setPrefSize(90, 28);
        txEntrega.setOnMouseClicked(e -> {
            if (txEntrega.getText().equals("Ingrese el monto entregado")) {
                txEntrega.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
                txEntrega.clear();
            }
        });
         //chequea si cambio de texto para poder cambiar el color de la letra
        txEntrega.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.equals("Ingrese el monto entregado")) {
                txEntrega.setStyle("-fx-background-radius: 5; -fx-border-radius: 5;");
            }
        });
        
        btConfirmar = new Button("Confirmar");
        btConfirmar.setStyle(btStyle);
        VBox.setMargin(btConfirmar, insets);
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
        
        bxNombreCliente.getChildren().add(nombreCliente);
        bxNombreCliente.setAlignment(Pos.TOP_LEFT);
        bxNombreCliente.setBackground(bkOscuro);
        
        bxEntrega.getChildren().addAll(titleEntrega,bxNombreCliente, txEntrega, btConfirmar);
        bxEntrega.setBackground(bkOscuro);
        bxEntrega.setAlignment(Pos.TOP_CENTER);
        
        entregaStage.setScene(entregaScene);
        entregaStage.setTitle("Agregar Entrega de Cliente");
        entregaStage.show();
        
    }
    
    public AgregarEntregaCliente(ConectionBD conn, VerClientes vercliente, String nombreClienteSelected){
        this.nombre = nombreClienteSelected;
        this.vc = vercliente;
        Stage entregaStage = new Stage();
        try {
            this.start(entregaStage);
        } catch (Exception ex) {
            Logger.getLogger(AgregarProducto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void onConfirmarButtonClick(ActionEvent evt) throws SQLException{
        int filasAfectadas = 0;
        int filasAfectadasXSaldo;
        try {
            stmt = connBD.conect();
        } catch (SQLException ex) {
            Logger.getLogger(VerStock.class.getName()).log(Level.SEVERE, null, ex);
        } 
         
            double entrega = Double.parseDouble(txEntrega.getText());
            String query = "UPDATE \"Clientes\" SET \"entrega\" = ? WHERE \"Clientes\".\"nombre\" = ?";
            try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
                pstmt.setDouble(1, entrega);
                pstmt.setString(2, nombre);
                filasAfectadas = pstmt.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("error del tipo: "+  ex);
                //Logger.getLogger(ModificarProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (filasAfectadas > 0) {
               String qySaldo = "SELECT \"saldo\" FROM \"Clientes\" WHERE \"Clientes\".\"nombre\" = ?";
            try {
                try (PreparedStatement pstmtSaldo = stmt.getConnection().prepareStatement(qySaldo)) {
                    pstmtSaldo.setString(1, nombre);
                    ResultSet resultSet = pstmtSaldo.executeQuery();

                    while (resultSet.next()) {
                        double saldo = resultSet.getDouble("saldo");
                        double nuevoSaldo = saldo - entrega;
                        System.out.println("Nuevo saldo es: " + nuevoSaldo);

                        String updateQuery = "UPDATE \"Clientes\" SET \"saldo\" = ? WHERE \"Clientes\".\"nombre\" = ?";
                        try (PreparedStatement pstmtUpdate = stmt.getConnection().prepareStatement(updateQuery)) {
                            pstmtUpdate.setDouble(1, nuevoSaldo);
                            pstmtUpdate.setString(2, nombre);

                            filasAfectadasXSaldo = pstmtUpdate.executeUpdate();
                            if (filasAfectadasXSaldo > 0) {
                                alerta.mostrarAlerta("Éxito", "Entrega actualizada correctamente", "INFORMATION");
                                vc.updateTable();
                            }
                        }
                    }
                }
            } catch (SQLException e) {
                System.out.println("Error del tipo: " + e);
            }

            }
          

           
        }
}
    


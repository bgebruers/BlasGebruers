/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.itextpdf.text.BaseColor;
import javafx.application.Application;
import javafx.stage.Stage;
import com.mycompany.talabarteria.ConectionBD;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import com.mycompany.talabarteria.Alertas;
/**
 *
 * @author usuario
 */
public class CuentaCorriente extends Application {
    private ConectionBD connBD;
    private Stage primaryLocalStage;
    private String nombreClienteSelected;
    private int idCliente;
    private Label lbNombre, titleCuentaCorr;
    private Button btPDF;
    private HBox hxButtonPDF;
    private VBox bxCuentaCorr;
    private int tamañoColumna = 200;
    private double anchoEscena; 
    private Statement stmt;
    private Alertas alerta = new Alertas();
    Insets insets = new Insets(13, 0, 0, 0); 
    TableView<CuentaCorriente.CuentaCorr> tabla = new TableView<>();
    private ObservableList<CuentaCorriente.CuentaCorr> listaVentas = FXCollections.observableArrayList();
      //color claro
    BackgroundFill backgroundFillClaro = new BackgroundFill(Color.web("#D9FFC1"), null, null);
    Background bkClaro = new Background(backgroundFillClaro);
    @Override
    public void start(Stage cuentaCorrStage) throws Exception {
        bxCuentaCorr = new VBox();
        hxButtonPDF = new HBox();
        lbNombre = new Label("Cliente: " + nombreClienteSelected);
        lbNombre.setFont(new Font(17));
       
        
        LocalDate fechaActual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String fechaFormateada = fechaActual.format(formato);
        // Convertir la fecha formateada de nuevo a LocalDate
        LocalDate date = LocalDate.parse(fechaFormateada, formato);
        
        
        titleCuentaCorr = new Label("Talabarteria Luiggi \n"
                + "Resumen al dia: " +  date.getDayOfMonth() +"/"+ date.getMonthValue() +"/"+ date.getYear()); 
        titleCuentaCorr.setFont(new Font(20));
        VBox.setMargin(titleCuentaCorr, insets);
        titleCuentaCorr.setStyle("-fx-font-weight: bold;");

        btPDF = new Button("Generar PDF");
        hxButtonPDF.getChildren().add(btPDF);
        hxButtonPDF.setAlignment(Pos.TOP_RIGHT);
        String btStyle = "-fx-min-width: 150px; " +
                "-fx-min-height: 28px; " +
                "-fx-max-width: 7px; " +
                "-fx-max-height: 15px; " +
                "-fx-font-size: 14px; " +
                "-fx-alignment: center;" +
                "-fx-border-radius: 15;";
        btPDF.setStyle(btStyle);
        btPDF.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    onPDFButtonClick(event);
                } catch (Exception ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        VBox.setMargin(hxButtonPDF, insets);
        HBox.setMargin(btPDF, new Insets(0,13,0,0));
        
        Scene cuentaCorrScene = new Scene(bxCuentaCorr, 1000, 600);
        cuentaCorrStage.setTitle("Resumen del cliente: " + nombreClienteSelected);
        VBox.setMargin(lbNombre, insets);
        VBox.setMargin(tabla, insets);
        
        // Crear columnas
        TableColumn<CuentaCorriente.CuentaCorr, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaFecha.setPrefWidth(tamañoColumna);
        
        TableColumn<CuentaCorriente.CuentaCorr, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaCantidad.setPrefWidth(tamañoColumna);
        
        TableColumn<CuentaCorriente.CuentaCorr, Double> columnaNombreProducto = new TableColumn<>("Descripcion");
        columnaNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        columnaNombreProducto.setPrefWidth(tamañoColumna);
        
        //en esta se almacena el precio del producto individual
        TableColumn<CuentaCorriente.CuentaCorr, String> columnaDebe = new TableColumn<>("Debe");
        columnaDebe.setCellValueFactory(new PropertyValueFactory<>("debe"));
        columnaDebe.setPrefWidth(tamañoColumna);
              
        //aca se multiplica el precio por la cantidad 
        TableColumn<CuentaCorriente.CuentaCorr, String> columnaSaldo = new TableColumn<>("Saldo");
        columnaSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        columnaSaldo.setPrefWidth(tamañoColumna);
        
        
        //captura el tamaño de la pantalla y setea el tamaño de las columnas
        cuentaCorrScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                anchoEscena = (double) newValue;
                tamañoColumna = (int) (anchoEscena / 5);
                columnaFecha.setPrefWidth(tamañoColumna);
                columnaNombreProducto.setPrefWidth(tamañoColumna); 
                columnaCantidad.setPrefWidth(tamañoColumna); 
                columnaDebe.setPrefWidth(tamañoColumna);
                columnaSaldo.setPrefWidth(tamañoColumna);                 
            }
        });
        
        cargarDatosDesdeBD(tabla);
        
        
        tabla.getColumns().addAll(columnaFecha, columnaCantidad, columnaNombreProducto, columnaDebe,columnaSaldo);
        bxCuentaCorr.getChildren().addAll(titleCuentaCorr, lbNombre, tabla, hxButtonPDF);
        bxCuentaCorr.setBackground(bkClaro);
        cuentaCorrStage.setScene(cuentaCorrScene);
        cuentaCorrStage.show();
    }
    
    private void onPDFButtonClick(ActionEvent evt) throws FileNotFoundException{
        generarPDF();
    }
    private void crearTablaPDF(Document document, PdfPTable pdfTable) throws DocumentException {
    // Agregar título
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17);
        
        Paragraph title = new Paragraph(titleCuentaCorr.getText(), titleFont);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(title);

        // Agregar label (lbNombre)
        Paragraph nombreLabel = new Paragraph(lbNombre.getText()); 
        nombreLabel.setAlignment(Paragraph.ALIGN_LEFT);
        nombreLabel.setSpacingAfter(10f);
        document.add(nombreLabel);
 
        // Agregar tabla
        document.add(pdfTable);
    }
    private PdfPTable generarDatosTabla() throws DocumentException {
        PdfPTable pdfTable = new PdfPTable(5); // Número de columnas
        float[] columnWidths = {2f, 2f, 3f, 2f, 2f};
       
        ObservableList<CuentaCorriente.CuentaCorr> items = tabla.getItems();
        
        PdfPCell colFecha = new PdfPCell(new Phrase("Fecha"));
        PdfPCell colCantidad = new PdfPCell(new Phrase("Cantidad"));
        PdfPCell colDescripcion = new PdfPCell(new Phrase("Descripcion"));
        PdfPCell colDebe = new PdfPCell(new Phrase("Debe"));
        PdfPCell colSaldo = new PdfPCell(new Phrase("Saldo"));
        
           
        pdfTable.addCell(colFecha);
        pdfTable.addCell(colCantidad);
        pdfTable.addCell(colDescripcion);
        pdfTable.addCell(colDebe);
        pdfTable.addCell(colSaldo);
        
        
          for (CuentaCorriente.CuentaCorr cuentaCorr : items) {
            PdfPCell cellFecha = new PdfPCell(new Phrase(String.valueOf(cuentaCorr.getFecha())));
            cellFecha.setFixedHeight(idCliente);
            PdfPCell cellCantidad = new PdfPCell(new Phrase(String.valueOf(cuentaCorr.getCantidad())));
            PdfPCell cellDescripcion = new PdfPCell(new Phrase(cuentaCorr.getNombreProducto()));
            PdfPCell cellDebe = new PdfPCell(new Phrase(String.valueOf(cuentaCorr.getDebe())));
            PdfPCell cellSaldo = new PdfPCell(new Phrase(String.valueOf(cuentaCorr.getSaldo())));

            pdfTable.addCell(cellFecha);
            
            pdfTable.addCell(cellCantidad);
            pdfTable.addCell(cellDescripcion);
            pdfTable.addCell(cellDebe);
            pdfTable.addCell(cellSaldo);
        }
        pdfTable.setWidthPercentage(100);
        pdfTable.setWidths(columnWidths);
        return pdfTable;
    }


    private void generarPDF() throws FileNotFoundException {
        Document document = new Document(PageSize.A4);
        //con esto obtengo la ruta a la carpeta de descargas
        String carpetaDescargas = System.getProperty("user.home") + File.separator + "Downloads";

        // Construir la ruta completa del archivo
        String rutaArchivo = carpetaDescargas + File.separator + "resumen_"+nombreClienteSelected+".pdf";
         

        try {
            PdfWriter.getInstance(document, new FileOutputStream(rutaArchivo));

            document.open();

            // Generar datos de la tabla
            PdfPTable pdfTable = generarDatosTabla();

            // Agregar título, label y tabla al documento PDF
            crearTablaPDF(document, pdfTable);
           
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        File archivoPDF = new File(rutaArchivo);
        if (archivoPDF.exists()) {
            alerta.mostrarAlerta("Exito", "El PDF fue creado, puedes encontrarlo en: " + rutaArchivo, "INFORMATION");
        } else {
            alerta.mostrarAlerta("Error", "Error al crear el PDF", "ERROR");
        }
    }

    
    public static class CuentaCorr{
        private String nombreProducto;
        private int cantidad;
        private double debe;
        private Date fecha;
        private double saldo;

        
        public CuentaCorr(String nombreProd, int cantidad, double debe, Date fecha, double saldo){
            this.nombreProducto = nombreProd;
            this.cantidad = cantidad;
            this.debe = debe;
            this.fecha = fecha;
            this.saldo = saldo;
        }
 
        public String getNombreProducto(){
            return nombreProducto;
        }
        public int getCantidad(){
            return cantidad;
        }
        public double getDebe(){
            return debe;
        }
        public Date getFecha(){
            return fecha;
        }
        public Double getSaldo(){
            return saldo;
        }
    }
    
    public CuentaCorriente(ConectionBD conn, String clienteSelected){
        connBD = conn;
        primaryLocalStage = new Stage();
        this.nombreClienteSelected = clienteSelected;
       
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
       
        }
    }
    
    private void cargarDatosDesdeBD(TableView<CuentaCorriente.CuentaCorr> tabla) throws SQLException{
        int cantidad = 0;
        int idProducto = 0;
        Date fecha = null;
        String nombreProducto = null;
        double precio, subtotal, saldoAnterior = 0;
        double saldo = 0;
        
  
        stmt = connBD.conect();
        String query;
        listaVentas.clear();
        
        //capturo el idCliente para ir a ventas y traer todas las ventas registradas con ese id
        query = "SELECT \"idCliente\", \"saldo\" FROM \"Clientes\" WHERE \"Clientes\".\"nombre\" = ?";
        try (PreparedStatement pstmt = stmt.getConnection().prepareStatement(query)) {
            pstmt.setString(1, nombreClienteSelected);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    idCliente = rs.getInt("idCliente");
                    saldo = rs.getDouble("saldo");
                }
            }
        }
        
        query = "SELECT \"idProducto\", \"cantidad\", \"fecha\" FROM \"Ventas\" WHERE \"Ventas\".\"idCliente\" = ?";
        try (PreparedStatement pstmtVentas = stmt.getConnection().prepareStatement(query)) {
            pstmtVentas.setInt(1, idCliente);

            try (ResultSet rsVentas = pstmtVentas.executeQuery()) {
                while (rsVentas.next()) {
                    idProducto = rsVentas.getInt("idProducto");
                    cantidad = rsVentas.getInt("cantidad");
                    fecha = rsVentas.getDate("fecha");
                    if (idProducto != 0) {
                        String qyDatosProd = "SELECT \"nombre\", \"precio\" FROM \"Productos\" WHERE \"Productos\".\"idProducto\" = ?";
                        try (PreparedStatement pstmtDatosProd = stmt.getConnection().prepareStatement(qyDatosProd)) {
                            pstmtDatosProd.setInt(1, idProducto);

                            try (ResultSet rsDatosProd = pstmtDatosProd.executeQuery()) {
                                while (rsDatosProd.next()) {
                                    nombreProducto = rsDatosProd.getString("nombre");
                                    precio = rsDatosProd.getDouble("precio");
                                    subtotal = saldoAnterior + (precio * cantidad);
                                    saldoAnterior = subtotal;
                                    listaVentas.add(new CuentaCorr(nombreProducto, cantidad, precio, fecha, subtotal));
                                }
                            }
                        }
                    }

                    
                    
                }
                tabla.setItems(listaVentas);
               
            } catch (SQLException e) {
                System.out.println("Error al obtener datos de ventas: " + e);
            }
        } catch (SQLException e) {
            System.out.println("Error al ejecutar consulta de ventas: " + e);
        }
  
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.talabarteria.Alertas;
import com.mycompany.talabarteria.ConectionBD;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author usuario
 */
public class Resumen extends Application{
    private ConectionBD connBD;
    private Stage primaryLocalStage;
    private VBox bxResumen, bxTable;
    private Label titleResumen;
    private Statement stmt;
    private Button btPDF;
    private HBox hxButtonPDF;
    private Alertas alerta = new Alertas();
    TableView<Resumen.ResumenVenta> tabla = new TableView<>();
    private ObservableList<Resumen.ResumenVenta> listaVentas = FXCollections.observableArrayList();
    //color claro
    BackgroundFill backgroundFillClaro = new BackgroundFill(Color.web("#D9FFC1"), null, null);
    Background bkClaro = new Background(backgroundFillClaro);
    Insets insets = new Insets(13, 0, 0, 0);
    private int tamañoColumna = 200;
    private double anchoEscena; 
    LocalDateTime fechaInicio, fechaFinal;

    @Override
    public void start(Stage resumenStage) throws Exception {
        VBox bxVentanaFecha = new VBox();
        Scene fechaScene = new Scene(bxVentanaFecha, 400, 200);
        
        
        Label labelFecha = new Label("Seleccione las fechas:");
        labelFecha.setFont(new Font(17));

        DatePicker dpFechaInicio = new DatePicker();
        dpFechaInicio.setValue(LocalDate.of(2024, 2, 1));
        DatePicker dpFechaFinal = new DatePicker();
               
        Button btConfirmar = new Button("Confirmar");
        VBox.setMargin(btConfirmar, insets);
        btConfirmar.setOnAction(e -> {
            fechaInicio = dpFechaInicio.getValue().atStartOfDay();
            fechaFinal = dpFechaFinal.getValue().atStartOfDay()
                .withHour(23)
                .withMinute(59)
                .withSecond(59);

            if (fechaInicio == null || fechaFinal == null || fechaInicio.isAfter(fechaFinal)) {
                alerta.mostrarAlerta("Error de fechas", "Las fechas ingresadas no son correctas", "ERROR");
            }else{
                resumenStage.close();
                abrirVentanaResumen();
            }
            
            
        });

        bxVentanaFecha.setBackground(bkClaro);
        bxVentanaFecha.setAlignment(Pos.CENTER);
        VBox.setMargin(labelFecha, insets);
        VBox.setMargin(dpFechaInicio, insets);
        VBox.setMargin(dpFechaFinal, insets);
        
        bxVentanaFecha.getChildren().addAll(labelFecha, dpFechaInicio, dpFechaFinal, btConfirmar);
        resumenStage.setTitle("Selección de Fechas");
        resumenStage.setScene(fechaScene);
        resumenStage.show();
    }
    
    private void abrirVentanaResumen() {
        Stage resumenStage = new Stage();
        bxResumen = new VBox();
        bxTable = new VBox();
        hxButtonPDF = new HBox(10);
        Scene resumenScene = new Scene(bxResumen, 1000, 600);

        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
           
        
        titleResumen = new Label("Resumen del periodo: " + fechaInicio.format(formatter) + " a "+ fechaFinal.format(formatter));
        titleResumen.setFont(new Font(17));
        VBox.setMargin(titleResumen, insets);
        
       
        bxResumen.setAlignment(Pos.TOP_CENTER);
        btPDF = new Button("Generar PDF");
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
        
        // Crear columnas
        TableColumn<Resumen.ResumenVenta, String> columnaProducto = new TableColumn<>("Descripcion");
        columnaProducto.setCellValueFactory(new PropertyValueFactory<>("producto"));
        columnaProducto.setPrefWidth(tamañoColumna);

        TableColumn<Resumen.ResumenVenta, Double> columnaPrecio = new TableColumn<>("Precio");
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaPrecio.setPrefWidth(tamañoColumna);

        TableColumn<Resumen.ResumenVenta, Integer> columnaCantidad = new TableColumn<>("Cantidad");
        columnaCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        columnaCantidad.setPrefWidth(tamañoColumna);

        TableColumn<Resumen.ResumenVenta, String> columnaFecha = new TableColumn<>("Fecha");
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        columnaFecha.setPrefWidth(tamañoColumna);
        
        TableColumn<Resumen.ResumenVenta, String> columnaCliente = new TableColumn<>("Cliente");
        columnaCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        columnaCliente.setPrefWidth(tamañoColumna);
        
        
        tabla.getColumns().addAll(columnaFecha, columnaCantidad, columnaProducto, columnaCliente, columnaPrecio );
        
        //captura el tamaño de la pantalla y setea el tamaño de las columnas
        resumenScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                anchoEscena = (double) newValue;
                tamañoColumna = (int) (anchoEscena / 4);
                columnaProducto.setPrefWidth(tamañoColumna); 
                columnaPrecio.setPrefWidth(tamañoColumna); 
                columnaCantidad.setPrefWidth(tamañoColumna); 
                columnaFecha.setPrefWidth(tamañoColumna);
            }
        });
        
        try {
            cargarResumenDesdeBD(tabla);
        } catch (SQLException ex) {
            Logger.getLogger(Resumen.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        double totalPrecio = listaVentas.stream().mapToDouble(Resumen.ResumenVenta::getPrecio).sum();
        listaVentas.add(new Resumen.ResumenVenta(totalPrecio));
        hxButtonPDF.getChildren().add(btPDF);
        bxTable.getChildren().add(tabla);
        VBox.setMargin(bxTable, insets);
        VBox.setMargin(hxButtonPDF, insets);    
        HBox.setMargin(btPDF, new Insets(0,13,0,0));
         
        bxResumen.getChildren().addAll(titleResumen, bxTable, hxButtonPDF);
        bxResumen.setBackground(bkClaro);
        
        resumenStage.setTitle("Resumen de ventas");
        resumenStage.setScene(resumenScene);
        resumenStage.show();
    
    }
    
    public static class ResumenVenta{
        private Date fecha;
        private String producto;
        private int cantidad;
        private double precio;
        private double subtotal;
        private double total;
        private String nombreCliente;
        
        public ResumenVenta(Date fecha, String producto, int cantidad, double precio, String nombreCliente){
            this.fecha = fecha;
            this.producto = producto;
            this.cantidad = cantidad;
            this.precio = precio;
            this.nombreCliente = nombreCliente;
        }
        
        public ResumenVenta(double totalPrecio) {
            this.producto = "Total:";
            this.precio = totalPrecio;
        }
        
        public Date getFecha(){
            return fecha;
        }
        public String getProducto(){
            return producto;
        }
        public int getCantidad(){
            return cantidad;
        }
        public double getPrecio(){
            return precio;
        }
        public String getNombreCliente(){
            return nombreCliente;
        }
        
    }
    
    
      private void onPDFButtonClick(ActionEvent evt) throws FileNotFoundException{
        generarPDF();
    }
    private void crearTablaPDF(Document document, PdfPTable pdfTable) throws DocumentException {
    // Agregar título
        com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 17);
        
        Paragraph title = new Paragraph(titleResumen.getText(), titleFont);
        title.setAlignment(Paragraph.ALIGN_LEFT);
        title.setSpacingAfter(10f);
        document.add(title);

 
        // Agregar tabla
        document.add(pdfTable);
    }
    private PdfPTable generarDatosTabla() throws DocumentException {
        
        float[] columnWidths = {2f, 2f, 3f, 2f, 2f};
        
        PdfPTable pdfTable = new PdfPTable(5);
        ObservableList<Resumen.ResumenVenta> items = tabla.getItems();
        
        PdfPCell colFecha = new PdfPCell(new Phrase("Fecha"));
        PdfPCell colCantidad = new PdfPCell(new Phrase("Cantidad"));
        PdfPCell colDescripcion = new PdfPCell(new Phrase("Descripcion"));
        PdfPCell colCliente = new PdfPCell(new Phrase("Cliente"));
        PdfPCell colPrecio = new PdfPCell(new Phrase("Precio"));
        
           
        pdfTable.addCell(colFecha);
        pdfTable.addCell(colCantidad);
        pdfTable.addCell(colDescripcion);
        pdfTable.addCell(colCliente);
        pdfTable.addCell(colPrecio);
       
        
        for (Resumen.ResumenVenta resumen : items) {
            PdfPCell cellFecha = new PdfPCell(new Phrase(String.valueOf(resumen.getFecha()))); 
            PdfPCell cellCantidad = new PdfPCell(new Phrase(String.valueOf(resumen.getCantidad())));
            PdfPCell cellDescripcion = new PdfPCell(new Phrase(resumen.getProducto()));
            PdfPCell cellTotalCliente = new PdfPCell(new Phrase(""));
            PdfPCell cellDebe = new PdfPCell(new Phrase(String.valueOf(resumen.getPrecio())));
            


            pdfTable.addCell(cellFecha);
            pdfTable.addCell(cellCantidad);
            pdfTable.addCell(cellDescripcion);
            pdfTable.addCell(cellTotalCliente); 
            pdfTable.addCell(cellDebe);
               
        }
        
        
            // Verificar si la última fila es "Total:" y agregar solo 3 celdas
        if (!items.isEmpty() && "Total:".equals(items.get(items.size() - 1).getProducto())) {
            Resumen.ResumenVenta totalRow = items.get(items.size() - 1);

            PdfPCell cellTotalFecha = new PdfPCell(new Phrase(String.valueOf(totalRow.getFecha())));
            PdfPCell cellTotalCantidad = new PdfPCell(new Phrase(totalRow.getCantidad()));
            PdfPCell cellTotalDescripcion = new PdfPCell(new Phrase("Total:"));
            PdfPCell cellTotalCliente = new PdfPCell(new Phrase(totalRow.getNombreCliente()));
            PdfPCell cellTotalPrecio = new PdfPCell(new Phrase(String.valueOf(totalRow.getPrecio())));
            

            // Establecer el colspan en 2 para que ocupe las celdas necesarias
            cellTotalDescripcion.setColspan(4);
            cellTotalCantidad.setColspan(2);

            pdfTable.deleteLastRow(); // Elimina la última fila que tenía 5 celdas

            // Agrega solo 3 celdas para la fila "Total:"
            pdfTable.addCell(cellTotalDescripcion);
            pdfTable.addCell(cellTotalPrecio);
            pdfTable.addCell(cellTotalCliente);
            pdfTable.addCell(cellTotalFecha);
            pdfTable.addCell(cellTotalCantidad);

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
        String rutaArchivo = carpetaDescargas + File.separator + "resumen_talabarteria.pdf";
         

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
    
    
    
    
    public void cargarResumenDesdeBD(TableView<ResumenVenta> tabla) throws SQLException{
        String nombreProducto = null;
        String nombreCliente = null;
        Date fechaVenta = null;
        int cantidadVendida = 0;
        double precioProducto = 0;
        int idCliente = 0;
        int idProducto = 0;
        double entrega = 0;
        String query;
        stmt = connBD.conect();
            
        java.sql.Timestamp fechaSqlInicio = java.sql.Timestamp.valueOf(fechaInicio);
        java.sql.Timestamp fechaSqlFinal = java.sql.Timestamp.valueOf(fechaFinal);
        query = "SELECT \"fecha\", \"idCliente\", \"idProducto\", \"cantidad\", \"entrega\" FROM \"Ventas\" " +
            "WHERE \"fecha\" BETWEEN ? AND ?";
        
        try (PreparedStatement pstmtVentas = stmt.getConnection().prepareStatement(query)) {
            // Configurar los parámetros de fechaInicio y fechaFin en la consulta
            pstmtVentas.setTimestamp(1, fechaSqlInicio);
            pstmtVentas.setTimestamp(2, fechaSqlFinal);

            try (ResultSet rsVentas = pstmtVentas.executeQuery()) {
                // Resto del código para procesar los resultados
                while (rsVentas.next()) {
                    fechaVenta = rsVentas.getDate("fecha");
                    idCliente = rsVentas.getInt("idCliente");
                    idProducto = rsVentas.getInt("idProducto");
                    cantidadVendida = rsVentas.getInt("cantidad");
                    entrega = rsVentas.getDouble("entrega");
                    if(idProducto != -1){
                        String qyProducto = "SELECT \"nombre\", \"precio\" FROM \"Productos\" WHERE \"Productos\".\"idProducto\" = ?";
                        try (PreparedStatement pstmtProducto = stmt.getConnection().prepareStatement(qyProducto)) {
                            pstmtProducto.setInt(1, idProducto);
                            try (ResultSet rsProducto = pstmtProducto.executeQuery()) {
                                while (rsProducto.next()) {
                                    nombreProducto = rsProducto.getString("nombre");
                                    if(nombreProducto.equals("ENTREGA")){
                                        precioProducto = entrega * (-1);
                                    }else{
                                        precioProducto = rsProducto.getDouble("precio") * cantidadVendida;
                                    }


                                    String qyCliente = "SELECT \"nombre\" FROM \"Clientes\" WHERE \"Clientes\".\"idCliente\" = ?";
                                    try (PreparedStatement pstmtCliente = stmt.getConnection().prepareStatement(qyCliente)) {
                                        pstmtCliente.setInt(1, idCliente);
                                        try (ResultSet rsCliente = pstmtCliente.executeQuery()) {
                                            while (rsCliente.next()) {
                                                nombreCliente = rsCliente.getString("nombre");
                                                if(nombreCliente.isEmpty()){
                                                    nombreCliente = "-";
                                                }else{
                                                     nombreCliente = rsCliente.getString("nombre");
                                                }

                                                // Agregar el resultado a la lista
                                                listaVentas.add(new ResumenVenta(fechaVenta, nombreProducto, cantidadVendida, precioProducto, nombreCliente));
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
                } catch (SQLException e) {
            System.out.println("Error al ejecutar la consulta: " + e);
                }
        
        //listaVentas.add(new ResumenVenta(fechaVenta, nombreProducto, cantidadVendida, precioProducto, nombreCliente));
        
        tabla.setItems(listaVentas);
        
        
    }
    
    public Resumen(ConectionBD conn){
        this.connBD = conn;
         primaryLocalStage = new Stage();
        
        try {
            this.start(primaryLocalStage);
        } catch (Exception ex) {
            Logger.getLogger(NuevaVenta.class.getName()).log(Level.SEVERE, null, ex);
       
        }
    }
     
}

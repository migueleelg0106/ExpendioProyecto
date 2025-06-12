/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ReporteProductoMasVendidoDAO;
import expendioproyecto.modelo.dao.ReporteProductoMenosVendidoDAO;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.ExportarAPDF;
import expendioproyecto.utilidad.ExportarAXLSX;
import expendioproyecto.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.TimeZone;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.io.FileOutputStream;
import java.awt.Color;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLReporteMenosVendidoController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<ReporteProductoVendido> tvProductoMenosVendido;
    @FXML
    private TableColumn<ReporteProductoVendido, String> colNombre;
    @FXML
    private TableColumn<ReporteProductoVendido, Long> colTotal;
    
    private ObservableList<ReporteProductoVendido> listaProductos;
    @FXML
    private MenuItem btnExportarXLSX;
    @FXML
    private MenuItem btnExportarPDF;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }    

    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));
        
        colNombre.setCellFactory(column -> new TableCell<ReporteProductoVendido, String>() {
        @Override
        protected void updateItem(String nombre, boolean empty) {
            super.updateItem(nombre, empty);
                if (empty || nombre == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(nombre);
                    TableRow<ReporteProductoVendido> row = getTableRow();
                    if (row != null && row.getIndex() == 0) {
                        setStyle("-fx-font-weight: bold; -fx-background-color: gold;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });

        colTotal.setCellFactory(column -> new TableCell<ReporteProductoVendido, Long>() {
            @Override
            protected void updateItem(Long total, boolean empty) {
                super.updateItem(total, empty);
                if (empty || total == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(total.toString());
                    TableRow<ReporteProductoVendido> row = getTableRow();
                    if (row != null && row.getIndex() == 0) {
                        setStyle("-fx-font-weight: bold; -fx-background-color: gold; -fx-alignment: CENTER;");
                    } else {
                        setStyle("-fx-alignment: CENTER;");
                    }
                }
            }
        });
    }
    
    private void cargarInformacion() {
        try {
            ArrayList<ReporteProductoVendido> productosDAO = ReporteProductoMenosVendidoDAO.obtenerProductosMenosVendidos();
            listaProductos = FXCollections.observableArrayList(productosDAO);
            tvProductoMenosVendido.setItems(listaProductos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar datos",
                    "No fue posible cargar el reporte de productos más vendidos.");
            ex.printStackTrace();
        }
    }    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.getEscenarioComponente(btnRegresar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();

            FXMLVentanaPrincipalController controlador = cargador.getController();
            controlador.configurarVistaSegunTipo(usuario);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicExportarXLSX(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                ExportarAXLSX.exportarAXLSX(
                    archivo,
                    "Reporte de Bebidas Menos Vendidas",
                    listaProductos,
                    Arrays.asList("Nombre del Producto", "Total Vendido"),
                    Arrays.asList(
                        producto -> producto.getNombre(),
                        producto -> String.valueOf(producto.getTotalVendido())
                    ),
                    true
                );
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Excel exportado correctamente.");
            } catch (IOException ex) {
                ex.printStackTrace();
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al exportar a Excel.");
            }
        } 
    }
    
    @FXML
    private void btnClicExportarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                ExportarAPDF.exportarAPDF(
                    archivo,
                    "Reporte de Bebidas Menos Vendidas",
                    listaProductos,
                    Arrays.asList("Nombre del Producto", "Total Vendido"),
                    Arrays.asList(
                        producto -> producto.getNombre(),
                        producto -> String.valueOf(producto.getTotalVendido())
                    ),
                    new Font(Font.HELVETICA, 12, Font.BOLD),
                    new Font(Font.HELVETICA, 10, Font.NORMAL),
                    true,
                    true
                );
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "PDF exportado correctamente.");
            } catch (IOException | DocumentException ex) {
                ex.printStackTrace();
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al exportar a PDF.");
            }
        }
    }
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

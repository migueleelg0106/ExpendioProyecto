/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ReporteVentasPorProductoDAO;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.ExportarAPDF;
import expendioproyecto.utilidad.ExportarAXLSX;
import expendioproyecto.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLReporteVentasPorBebidaController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<ReporteProductoVendido> tvProductos;
    @FXML
    private TableColumn<ReporteProductoVendido, String> colNombreProducto;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn<ReporteProductoVendido, Double> colSubtotal;
    @FXML
    private TableColumn<ReporteProductoVendido, Long> colCantidad;
    
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
        configurarBuscador();
    }    
    
    private void configurarTabla() {
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("totalVentasPrecio"));
        
        colCantidad.setCellFactory(column -> new TableCell<ReporteProductoVendido, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.valueOf(item));
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        colSubtotal.setCellFactory(column -> new TableCell<ReporteProductoVendido, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));  // Mantiene 2 decimales con símbolo $
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }
    
    private void cargarInformacion() {
        try {
            ArrayList<ReporteProductoVendido> productosDAO = ReporteVentasPorProductoDAO.obtenerVentasPorProducto();
            listaProductos = FXCollections.observableArrayList(productosDAO);
            tvProductos.setItems(listaProductos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar datos",
                    "No fue posible cargar el reporte de productos más vendidos.");
            ex.printStackTrace();
        }
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(btnRegresar);
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

    private void configurarBuscador() {
        tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (listaProductos != null) {
                ObservableList<ReporteProductoVendido> reportesFiltradas = FXCollections.observableArrayList();
                for (ReporteProductoVendido reporte : listaProductos) {
                    if (reporte.getNombre().toLowerCase().contains(newValue.toLowerCase())) {
                        reportesFiltradas.add(reporte);
                    }
                }
                tvProductos.setItems(reportesFiltradas);
            }
        });
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
                    "Reporte de Productos Vendidos",
                    listaProductos,
                    Arrays.asList("Nombre del Producto", "Cantidad Vendida", "Subtotal"),
                    Arrays.asList(
                        producto -> producto.getNombre(),
                        producto -> String.valueOf(producto.getTotalVendido()),
                        producto -> String.format("$%.2f", producto.getTotalVentasPrecio())
                    )
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
                    "Reporte de Productos Vendidos",
                    listaProductos,
                    Arrays.asList("Nombre del Producto", "Cantidad Vendida", "Subtotal"),
                    Arrays.asList(
                        producto -> producto.getNombre(),
                        producto -> String.valueOf(producto.getTotalVendido()),
                        producto -> String.format("$%.2f", producto.getTotalVentasPrecio())
                    ),
                    new Font(Font.HELVETICA, 12, Font.BOLD),
                    new Font(Font.HELVETICA, 10, Font.NORMAL),
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

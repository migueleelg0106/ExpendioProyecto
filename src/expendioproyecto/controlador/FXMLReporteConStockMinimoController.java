/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ReporteProductoStockMinimoDAO;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import expendioproyecto.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLReporteConStockMinimoController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<ReporteProductoVendido> tvProductoMasVendido;
    @FXML
    private TableColumn<ReporteProductoVendido, String> colNombre;
    @FXML
    private TableColumn<ReporteProductoVendido, Double> colStock;
    @FXML
    private TableColumn<ReporteProductoVendido, Double> colExistencia;
    
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
        tvProductoMasVendido.setPlaceholder(new Label("No hay bebidas con una existencia menor a su stock mínimo establecido."));
    }    
    
    private void configurarTabla() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colExistencia.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));

        colExistencia.setCellFactory(column -> new TableCell<ReporteProductoVendido, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%d", item.intValue()));
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });

        colStock.setCellFactory(column -> new TableCell<ReporteProductoVendido, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%d", item.intValue()));
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }

    
    private void cargarInformacion() {
        try {
            ArrayList<ReporteProductoVendido> productosDAO = ReporteProductoStockMinimoDAO.obtenerProductosStockMinimo();
            listaProductos = FXCollections.observableArrayList(productosDAO);
            tvProductoMasVendido.setItems(listaProductos);
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
        //exportarArchivoConExtension("Excel Workbook (*.xlsx)", "*.xlsx"); 
    }

    @FXML
    private void btnClicExportarPDF(ActionEvent event) {
        //exportarArchivoConExtension("PDF files (*.pdf)", "*.pdf");
    }
    
    
}

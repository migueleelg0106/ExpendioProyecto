/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ReporteProductoMasVendidoDAO;
import expendioproyecto.modelo.dao.ReporteProductoMenosVendidoDAO;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import expendioproyecto.utilidad.Utilidad;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

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
    @FXML
    private Button btnExportar;
    
    private ObservableList<ReporteProductoVendido> listaProductos;

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
    private void btnClicExportar(ActionEvent event) {
    }
    
}

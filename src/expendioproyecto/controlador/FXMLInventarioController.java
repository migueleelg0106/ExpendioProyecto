/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.pojo.Bebida;
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
public class FXMLInventarioController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Bebida> tvBebidas;
    @FXML
    private TableColumn<Bebida, String> colNombre;
    @FXML
    private TableColumn<Bebida, Integer> colExistencia;
    @FXML
    private TableColumn<Bebida, Float> colPrecio;
    @FXML
    private TableColumn<Bebida, Integer> colStockMin;
    @FXML
    private TableColumn<Bebida, String> colDescripcion;
    @FXML
    private TextField tfBuscar;
    
    private ObservableList<Bebida> bebidas;
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
        cargarInformacionTabla();
        configurarBuscador();
    }
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    private void configurarTabla(){
        colExistencia.setCellValueFactory(new PropertyValueFactory("existencia"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        colPrecio.setCellFactory(column -> new TableCell<Bebida, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));  // Mantiene 2 decimales con símbolo $
                }
            }
        });
        colStockMin.setCellValueFactory(new PropertyValueFactory("stockMinimo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
    }
    
    private void cargarInformacionTabla(){
        try {
            bebidas = FXCollections.observableArrayList();
            ArrayList<Bebida> bebidasDAO = BebidaDAO.obtenerBebidas();
            bebidas.addAll(bebidasDAO);
            tvBebidas.setItems(bebidas);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al "
                    + "cargar", "Lo sentimos por el momento no se puede "
                            + "mostrar la información de las bebidas.");
            cerrarVentana();
        }
    }
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfBuscar);
    }
    
    private void configurarBuscador() {
        tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (bebidas != null) {
                ObservableList<Bebida> bebidasFiltradas = FXCollections.observableArrayList();
                for (Bebida b : bebidas) {
                    if (b.getNombre().toLowerCase().contains(newValue.toLowerCase())) {
                        bebidasFiltradas.add(b);
                    }
                }
                tvBebidas.setItems(bebidasFiltradas);
            }
        });
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.getEscenarioComponente(tfBuscar);

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
                    "Inventario de Bebidas",
                    new ArrayList<>(tvBebidas.getItems()),
                    Arrays.asList("Nombre", "Existencia", "Precio", "Stock Mínimo", "Descripción"),
                    Arrays.asList(
                        bebida -> bebida.getNombre(),
                        bebida -> String.valueOf(bebida.getExistencia()),
                        bebida -> String.format("%.2f", bebida.getPrecio()),
                        bebida -> String.valueOf(bebida.getStockMinimo()),
                        bebida -> bebida.getDescripcion()
                    ),
                    false
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
                    "Inventario de Bebidas",
                    new ArrayList<>(tvBebidas.getItems()),
                    Arrays.asList("Nombre", "Existencia", "Precio", "Stock Mínimo", "Descripción"),
                    Arrays.asList(
                        bebida -> bebida.getNombre(),
                        bebida -> String.valueOf(bebida.getExistencia()),
                        bebida -> String.format("%.2f", bebida.getPrecio()),
                        bebida -> String.valueOf(bebida.getStockMinimo()),
                        bebida -> bebida.getDescripcion()
                    ),
                    new Font(Font.HELVETICA, 12, Font.BOLD),
                    new Font(Font.HELVETICA, 10, Font.NORMAL),
                    true,
                    false
                );
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "PDF exportado correctamente.");
            } catch (IOException | DocumentException ex) {
                ex.printStackTrace();
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al exportar a PDF.");
            }
        }
    }
}

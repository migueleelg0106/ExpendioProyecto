/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ReporteVentaFechaDAO;
import expendioproyecto.modelo.pojo.ReporteVentaFecha;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.ExportarAPDF;
import expendioproyecto.utilidad.ExportarAXLSX;
import expendioproyecto.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLReporteVentasPorFechaController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<ReporteVentaFecha> tvVentas;
    @FXML
    private TableColumn<ReporteVentaFecha, Integer> colSemana;
    @FXML
    private TableColumn<ReporteVentaFecha, Integer> colMes;
    @FXML
    private TableColumn<ReporteVentaFecha, Integer> colAño;
    @FXML
    private TableColumn<ReporteVentaFecha, Double> colTotal;
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
    }    

    private void configurarTabla() {
        colAño.setCellValueFactory(new PropertyValueFactory<>("año"));
        colAño.setCellFactory(col -> new TableCell<ReporteVentaFecha, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.valueOf(item));
                setStyle("-fx-alignment: CENTER;");
            }
        });
        colMes.setCellValueFactory(new PropertyValueFactory<>("mes"));
        colMes.setCellFactory(col -> new TableCell<ReporteVentaFecha, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.valueOf(item));
                setStyle("-fx-alignment: CENTER;");
            }
        });
        colSemana.setCellValueFactory(new PropertyValueFactory<>("semana"));
        colSemana.setCellFactory(col -> new TableCell<ReporteVentaFecha, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "" : String.valueOf(item));
                setStyle("-fx-alignment: CENTER;");
            }
        });
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalVentas"));
        colTotal.setCellFactory(column -> new TableCell<ReporteVentaFecha, Double>() {
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
    private void btnClicPorAño(ActionEvent event) {
        List<ReporteVentaFecha> lista = ReporteVentaFechaDAO.obtenerVentasPorAño();
        tvVentas.getItems().setAll(lista);
    }

    @FXML
    private void btnClicPorMes(ActionEvent event) {
        List<ReporteVentaFecha> lista = ReporteVentaFechaDAO.obtenerVentasPorMes();
        tvVentas.getItems().setAll(lista);
    }

    @FXML
    private void btnClicPorSemana(ActionEvent event) {
        List<ReporteVentaFecha> lista = ReporteVentaFechaDAO.obtenerVentasPorSemana();
        tvVentas.getItems().setAll(lista);
    }

    @FXML
    private void btnClicExportarXLSX(ActionEvent event) {
        if (tvVentas.getItems().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "Seleccione un tipo de venta primero.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                List<ReporteVentaFecha> listaVentas = tvVentas.getItems();

                ExportarAXLSX.exportarAXLSX(
                    archivo,
                    "Reporte de Ventas Según La Fecha",
                    listaVentas,
                    Arrays.asList("Año", "Mes", "Semana", "Total de Ventas"),
                    Arrays.asList(
                        venta -> String.valueOf(venta.getAño()),
                        venta -> venta.getMes() != null ? String.valueOf(venta.getMes()) : "",
                        venta -> venta.getSemana() != null ? String.valueOf(venta.getSemana()) : "",
                        venta -> String.format("$%.2f", venta.getTotalVentas())
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
        if (tvVentas.getItems().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "Seleccione un tipo de venta primero.");
            return;
        }
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                List<ReporteVentaFecha> listaVentas = tvVentas.getItems();

                ExportarAPDF.exportarAPDF(
                    archivo,
                    "Reporte de Ventas Según La Fecha",
                    listaVentas,
                    Arrays.asList("Año", "Mes", "Semana", "Total de Ventas"),
                    Arrays.asList(
                        venta -> String.valueOf(venta.getAño()),
                        venta -> venta.getMes() != null ? String.valueOf(venta.getMes()) : "",
                        venta -> venta.getSemana() != null ? String.valueOf(venta.getSemana()) : "",
                        venta -> String.format("$%.2f", venta.getTotalVentas())
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
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

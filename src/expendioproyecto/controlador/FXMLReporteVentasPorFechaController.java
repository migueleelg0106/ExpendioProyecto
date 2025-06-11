/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ReporteVentaFechaDAO;
import expendioproyecto.modelo.pojo.ReporteVentaFecha;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private Button btnExportar;
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
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLVentaController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<?> tvInventario;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colExistencia;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<?> tvProductos;
    @FXML
    private TableColumn<?, ?> colProductosVenta;
    @FXML
    private TableColumn<?, ?> colClientePrecio;
    @FXML
    private TableColumn<?, ?> colProductoPrecio;
    @FXML
    private TableColumn<?, ?> colPromocion;
    @FXML
    private TableColumn<?, ?> colCantidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductos.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
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
    private void btnClicAgregar(ActionEvent event) {
    }

    @FXML
    private void btnClicFinalizarVenta(ActionEvent event) {
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
    }
    
}

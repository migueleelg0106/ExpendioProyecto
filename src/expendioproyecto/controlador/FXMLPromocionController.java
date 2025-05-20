/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.pojo.Promoción;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLPromocionController implements Initializable {

    @FXML
    private TableColumn colBebida;
    @FXML
    private TableColumn colDescuento;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaVencimiento;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Promoción> tvPromocion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicEliminar(ActionEvent event) {
    }

    @FXML
    private void btnClicModificar(ActionEvent event) {
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(tfBuscar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}

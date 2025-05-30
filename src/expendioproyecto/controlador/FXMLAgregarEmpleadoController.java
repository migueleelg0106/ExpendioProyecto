/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class FXMLAgregarEmpleadoController implements Initializable {

    @FXML
    private Label lbExistencia;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private TextField tfStockMinimo;
    @FXML
    private TextField tfExistencia;
    @FXML
    private TextField tfPrecio;
    @FXML
    private TextField tfNombre;
    @FXML
    private Label lbStockMinimo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicGuardar(ActionEvent event) {
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfExistencia);
    }
    
}

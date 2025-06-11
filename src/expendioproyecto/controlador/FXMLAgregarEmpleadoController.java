/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.EmpleadoDAO;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class FXMLAgregarEmpleadoController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnMostrarContraseña;
    @FXML
    private TextField tfPasswordVisible;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarListeners();
    }    

    @FXML
    private void clicGuardar(ActionEvent event) {
        String username = tfUsuario.getText().trim();
        String password = obtenerContrasenaActual().trim();

        if (!validarCampos(username, password)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos", 
                "Por favor completa todos los campos obligatorios.");
            return;
    }

    guardarEmpleado(username, password);
    }

    private boolean validarCampos(String username, String password){
        boolean camposValidos = true;
        if(username.isEmpty()){
            camposValidos = false;
        }
        if(password.isEmpty()){
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void guardarEmpleado(String username, String password){
        Usuario empleado = new Usuario();
        empleado.setUsername(username);
        empleado.setPassword(password);

        try {
            boolean exito = EmpleadoDAO.insertarEmpleado(empleado);

            if (exito) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", 
                    "Empleado registrado correctamente.");
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                    "No se pudo registrar el empleado.");
            }
        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error crítico", 
                "Ocurrió un error al registrar el empleado.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfUsuario);
    }

    private void mostrarContrasenaVisible() {
        tfPasswordVisible.setText(pfPassword.getText());
        tfPasswordVisible.setVisible(true);
        tfPasswordVisible.setManaged(true);

        pfPassword.setVisible(false);
        pfPassword.setManaged(false);
    }

    private void ocultarContrasenaVisible() {
        pfPassword.setText(tfPasswordVisible.getText());
        pfPassword.setVisible(true);
        pfPassword.setManaged(true);

        tfPasswordVisible.setVisible(false);
        tfPasswordVisible.setManaged(false);
    }
    
    private void configurarListeners() {
        pfPassword.textProperty().addListener((obs, oldText, newText) -> {
            tfPasswordVisible.setText(newText);
        });

        tfPasswordVisible.textProperty().addListener((obs, oldText, newText) -> {
            pfPassword.setText(newText);
        });
    }
    
    private String obtenerContrasenaActual() {
        return pfPassword.isVisible() ? pfPassword.getText() : tfPasswordVisible.getText();
    }

    @FXML
    private void btnClicMostrarContraseña(ActionEvent event) {
        if (tfPasswordVisible.isVisible()) {
            ocultarContrasenaVisible();
        } else {
            mostrarContrasenaVisible();
        }
    }
    
}

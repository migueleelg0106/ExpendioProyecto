package expendioproyecto.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.dao.IniciarSesionDAO;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import javafx.scene.control.Button;

/**
 *
 * @author uriel
 */
public class FXMLIniciarSesionController implements Initializable{

    @FXML
    private TextField tfUsername;
    @FXML
    private Label lbErrorUsername;
    @FXML
    private Label lbErrorPassword;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private TextField tfPasswordVisible;
    @FXML
    private Button btnMostrarContraseña;
    
    @Override
    public void initialize (URL url, ResourceBundle rb){
        configurarListeners();
    }

    @FXML
    private void btnClicVerificarSesion(ActionEvent event) {
        String username = tfUsername.getText();
        String password = obtenerContrasenaActual();
        if(validarCampos(username, password))
            validarCredenciales(username, password);
    }
    
    private boolean validarCampos(String username, String password){
        //Limpiar campos
        lbErrorUsername.setText("");
        lbErrorPassword.setText("");
        boolean camposValidos = true;
        if(username.isEmpty()){
            lbErrorUsername.setText("Usuario obligatorio");
            camposValidos = false;
        }
        if(password.isEmpty()){
            lbErrorPassword.setText("Contraseña obligatoria");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    private void validarCredenciales(String username, String password){
        try {
            Usuario usuario = new IniciarSesionDAO().iniciarSesion(username, password);
            if (usuario != null) {
                irPantallaPrincipal(usuario); // pasa el usuario a la siguiente vista
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, 
                    "Credenciales incorrectas", 
                    "Usuario y/o contraseña incorrectos, por favor verifica tu información.");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
                "Problemas de conexión", ex.getMessage());
        }
    }

    
    private void irPantallaPrincipal(Usuario usuario){
        try {
            Stage escenarioBase = Utilidad.getEscenarioComponente(tfUsername);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();

            // Pasar datos al controlador de la ventana principal
            FXMLVentanaPrincipalController controller = cargador.getController();
            controller.configurarVistaSegunTipo(usuario);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Error al cargar la vista principal",
                    "No se pudo cargar la interfaz de la pantalla principal.");
        }
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
//import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;

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
    private PasswordField tfPassword;
    
    @Override
    public void initialize (URL url, ResourceBundle rb){
        
    }

    @FXML
    private void btnClicVerificarSesion(ActionEvent event) {
        String username = tfUsername.getText();
        String password = tfPassword.getText();
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
        try{
            Usuario usuarioSesion = IniciarSesionDAO.verificarCredenciales
            (username, password);
            if (usuarioSesion != null){
                //Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, 
                        //"Credenciales correctas", "Bienvenido(a) " 
                        //+ usuarioSesion.toString() + " al sistema.");
                irPantallaPrincipal(usuarioSesion);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, 
                        "Credenciales incorrectas", "Usuario y/o contraseña "
                        + "incorrectos, por favor verifica tu información.");
            }
        } catch (SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, 
                    "Problemas de conexión", ex.getMessage());
        }
    }
    
    private void irPantallaPrincipal(Usuario usuario){
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(tfUsername);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

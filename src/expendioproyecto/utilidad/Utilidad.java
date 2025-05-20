/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.utilidad;

import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author uriel
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String mensaje){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    public static Stage gestEscenarioComponente(Control componente){
        return (Stage) componente.getScene().getWindow();
    }
    
    public static void cerrarVentanaComponente(Control componente){
        ((Stage) componente.getScene().getWindow()).close();
    }
}

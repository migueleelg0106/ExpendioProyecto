/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.utilidad;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
    
    public static Stage getEscenarioComponente(Control componente){
        return (Stage) componente.getScene().getWindow();
    }
    
    public static void cerrarVentanaComponente(Control componente){
        ((Stage) componente.getScene().getWindow()).close();
    }
    
    public static boolean mostrarConfirmacion(String titulo, String encabezado, String contenido) {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(contenido);

        Optional<ButtonType> respuesta = alerta.showAndWait();
        return respuesta.isPresent() && respuesta.get() == ButtonType.OK;
    }
    
    @FunctionalInterface
    public interface ValorCelda<T> {
        String obtenerValor(T objeto);
    }


}

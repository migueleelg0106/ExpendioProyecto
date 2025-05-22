/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLFormularioBebidaController implements Initializable {

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
    
    private Bebida bebidaEnEdicion = null;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicGuardar(ActionEvent event) {
        String nombre = tfNombre.getText().trim();
        String descripcion = tfDescripcion.getText().trim();
        String existenciaTexto = tfExistencia.getText().trim();
        String precioTexto = tfPrecio.getText().trim();
        String stockMinimoTexto = tfStockMinimo.getText().trim();

        // Validación de campos vacíos
        if (nombre.isEmpty() || descripcion.isEmpty() || existenciaTexto.isEmpty()
                || precioTexto.isEmpty() || stockMinimoTexto.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor, completa todos los campos.");
            return;
        }

        // Validación de formato
        if (!nombre.matches("^[a-zA-ZÁÉÍÓÚáéíóúñÑ\\s]{3,45}$")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Nombre inválido",
                    "El nombre debe contener solo letras y tener entre 3 y 45 caracteres.");
            return;
        }

        if (descripcion.length() < 5 || descripcion.length() > 100) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Descripción inválida",
                    "La descripción debe tener entre 5 y 100 caracteres.");
            return;
        }

        int existencia, stockMinimo;
        float precio;

        try {
            existencia = Integer.parseInt(existenciaTexto);
            stockMinimo = Integer.parseInt(stockMinimoTexto);
            precio = Float.parseFloat(precioTexto);

            if (existencia < 0 || stockMinimo < 0 || precio < 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Valores inválidos",
                        "Existencia, precio y stock mínimo deben ser valores positivos.");
                return;
            }
        } catch (NumberFormatException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "Verifica que existencia, stock mínimo y precio sean números válidos.");
            return;
        }

        // Crear o modificar bebida
        Bebida bebida = (bebidaEnEdicion == null) ? new Bebida() : bebidaEnEdicion;

        bebida.setNombre(nombre);
        bebida.setDescripcion(descripcion);
        bebida.setExistencia(existencia);
        bebida.setStockMinimo(stockMinimo);
        bebida.setPrecio(precio);

        try {
            boolean exito;
            if (bebidaEnEdicion == null) {
                exito = BebidaDAO.insertarBebida(bebida);
            } else {
                exito = BebidaDAO.modificarBebida(bebida);
            }

            if (exito) {
                String mensaje = (bebidaEnEdicion == null) ? "Bebida registrada correctamente." : "Bebida modificada correctamente.";
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", mensaje);
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Operación fallida",
                        "No se pudo completar la operación.");
            }

        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error crítico",
                    "Ocurrió un error al intentar guardar la bebida.");
            e.printStackTrace();
        }   
    }




    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfDescripcion);
    }
    
    public void inicializarFormulario(Bebida bebida) {
        if (bebida != null) {
            this.bebidaEnEdicion = bebida;
            tfNombre.setText(bebida.getNombre());
            tfDescripcion.setText(bebida.getDescripcion());
            tfExistencia.setText(String.valueOf(bebida.getExistencia()));
            tfPrecio.setText(String.valueOf(bebida.getPrecio()));
            tfStockMinimo.setText(String.valueOf(bebida.getStockMinimo()));
        }
    }

}

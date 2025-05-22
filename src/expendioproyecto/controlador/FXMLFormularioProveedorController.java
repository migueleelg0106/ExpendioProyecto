/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.ProveedorDAO;
import expendioproyecto.modelo.pojo.Proveedor;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.sql.SQLException;
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
public class FXMLFormularioProveedorController implements Initializable {

    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfRazonSocial;
    @FXML
    private TextField tfCorreo;
    
    private Proveedor proveedorEnEdicion = null;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicGuardar(ActionEvent event) {
        String razonSocial = tfRazonSocial.getText().trim();
        String direccion = tfDireccion.getText().trim();
        String correo = tfCorreo.getText().trim();
        String telefono = tfTelefono.getText().trim();

        if (razonSocial.isEmpty() || direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor completa todos los campos.");
            return;
        }

        Proveedor proveedor = (proveedorEnEdicion == null) ? new Proveedor() : proveedorEnEdicion;
        proveedor.setRazonSocial(razonSocial);
        proveedor.setDireccion(direccion);
        proveedor.setCorreo(correo);
        proveedor.setTelefono(telefono);

        try {
            boolean exito;
            if (proveedorEnEdicion == null) {
                exito = ProveedorDAO.insertarProveedor(proveedor);
            } else {
                exito = ProveedorDAO.modificarProveedor(proveedor);
            }

            if (exito) {
                String mensaje = proveedorEnEdicion == null ? "Proveedor registrado correctamente." : "Proveedor modificado correctamente.";
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", mensaje);
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar el proveedor.");
            }

        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error crítico", "Ocurrió un error al guardar el proveedor.");
            e.printStackTrace();
        }
    }


    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfDireccion);
    }
    

    public void inicializarFormulario(Proveedor proveedor) {
        if (proveedor != null) {
            this.proveedorEnEdicion = proveedor;
            tfRazonSocial.setText(proveedor.getRazonSocial());
            tfDireccion.setText(proveedor.getDireccion());
            tfCorreo.setText(proveedor.getCorreo());
            tfTelefono.setText(proveedor.getTelefono());
        }
    }

    
}

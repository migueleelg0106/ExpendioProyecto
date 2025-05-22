/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.pojo.Cliente;
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
public class FXMLFormularioClienteController implements Initializable {

    @FXML
    private TextField tfDirección;
    @FXML
    private TextField tfRFC;
    @FXML
    private TextField tfTipo;
    @FXML
    private TextField tfTeléfono;
    @FXML
    private TextField tfRazonSocial;
    @FXML
    private TextField tfCorreo;
    
    private Cliente clienteEnEdicion = null;


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
        String direccion = tfDirección.getText().trim();
        String correo = tfCorreo.getText().trim();
        String telefono = tfTeléfono.getText().trim();
        String tipo = tfTipo.getText().trim();
        String rfc = tfRFC.getText().trim();

        if (direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty() || tipo.isEmpty() ) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos",
                    "Rellenar al menos los campos obligatorios: tipo, dirección, correo y teléfono.");
            return;
        }

        Cliente cliente = (clienteEnEdicion == null) ? new Cliente() : clienteEnEdicion;
        cliente.setRazonSocial(razonSocial.isEmpty() ? null : razonSocial);
        cliente.setDireccion(direccion);
        cliente.setCorreo(correo);
        cliente.setTelefono(telefono);
        cliente.setTipo(tipo);
        cliente.setRfc(rfc.isEmpty() ? null : rfc);

        try {
            boolean exito;
            if (clienteEnEdicion == null) {
                exito = ClienteDAO.insertarCliente(cliente);
            } else {
                exito = ClienteDAO.modificarCliente(cliente);
            }

            if (exito) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito",
                        clienteEnEdicion == null ? "Cliente registrado correctamente." : "Cliente modificado correctamente.");
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                        "No se pudo guardar el cliente.");
            }

        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error crítico",
                    "Ocurrió un error al guardar el cliente.");
            e.printStackTrace();
        }
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfDirección);
    }
    
    public void inicializarFormulario(Cliente cliente) {
        if (cliente != null) {
            this.clienteEnEdicion = cliente;
            tfRazonSocial.setText(cliente.getRazonSocial());
            tfDirección.setText(cliente.getDireccion());
            tfCorreo.setText(cliente.getCorreo());
            tfTeléfono.setText(cliente.getTelefono());
            tfTipo.setText(cliente.getTipo());
            tfRFC.setText(cliente.getRfc());
        }
    }
    
}

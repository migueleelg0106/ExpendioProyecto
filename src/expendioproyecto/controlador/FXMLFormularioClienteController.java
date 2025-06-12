package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.pojo.Cliente;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class FXMLFormularioClienteController implements Initializable {

    @FXML
    private TextField tfDirección;
    @FXML
    private TextField tfRFC;
    @FXML
    private ComboBox<String> cbTipo;
    @FXML
    private TextField tfTeléfono;
    @FXML
    private TextField tfRazonSocial;
    @FXML
    private TextField tfCorreo;
    @FXML
    private Label lbRFC;

    private Cliente clienteEnEdicion = null;
    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbTipo.setItems(FXCollections.observableArrayList("ocasional", "frecuente"));

        cbTipo.valueProperty().addListener((obs, oldValue, newValue) -> {
            if ("ocasional".equalsIgnoreCase(newValue)) {
                tfRFC.clear();
                tfRFC.setDisable(true);
                lbRFC.setText("Sin RFC");
            } else {
                tfRFC.setDisable(false);
                lbRFC.setText("Ingrese el RFC:");
            }
        });
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String razonSocial = tfRazonSocial.getText().trim();
        String direccion = tfDirección.getText().trim();
        String correo = tfCorreo.getText().trim();
        String telefono = tfTeléfono.getText().trim();
        String tipo = cbTipo.getValue();
        String rfc = tfRFC.getText().trim();

        Cliente cliente = (clienteEnEdicion == null) ? new Cliente() : clienteEnEdicion;
        cliente.setRazonSocial(razonSocial);
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

    private void cerrarVentana() {
        Utilidad.cerrarVentanaComponente(tfDirección);
    }

    public void inicializarFormulario(Cliente cliente) {
        if (cliente != null) {
            this.clienteEnEdicion = cliente;
            tfRazonSocial.setText(cliente.getRazonSocial());
            tfDirección.setText(cliente.getDireccion());
            tfCorreo.setText(cliente.getCorreo());
            tfTeléfono.setText(cliente.getTelefono());
            cbTipo.setValue(cliente.getTipo());
            tfRFC.setText(cliente.getRfc());
            tfRFC.setDisable("ocasional".equalsIgnoreCase(cliente.getTipo()));
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private boolean validarCampos() {
        String razonSocial = tfRazonSocial.getText().trim();
        String direccion = tfDirección.getText().trim();
        String correo = tfCorreo.getText().trim();
        String telefono = tfTeléfono.getText().trim();
        String tipo = cbTipo.getValue();

        if (razonSocial.isEmpty() || direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty() || tipo == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos",
                "Rellenar todos los campos obligatorios: razón social, tipo, dirección, correo y teléfono.");
            return false;
        }

        if (!telefono.matches("\\d{10}")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Teléfono inválido",
                "El teléfono debe contener exactamente 10 dígitos numéricos.");
            return false;
        }

        if (!correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Correo inválido",
                "Ingrese un correo electrónico válido.");
            return false;
        }

        return true;
    }
}
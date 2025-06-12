package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.ProveedorDAO;
import expendioproyecto.modelo.pojo.Proveedor;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

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
    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicialización si es necesaria
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String razonSocial = tfRazonSocial.getText().trim();
        String direccion = tfDireccion.getText().trim();
        String correo = tfCorreo.getText().trim();
        String telefono = tfTelefono.getText().trim();

        Proveedor proveedor = (proveedorEnEdicion == null) ? new Proveedor() : proveedorEnEdicion;
        proveedor.setRazonSocial(razonSocial);
        proveedor.setDireccion(direccion);
        proveedor.setCorreo(correo);
        proveedor.setTelefono(telefono);

        try {
            boolean exito = (proveedorEnEdicion == null)
                    ? ProveedorDAO.insertarProveedor(proveedor)
                    : ProveedorDAO.modificarProveedor(proveedor);

            if (exito) {
                String mensaje = proveedorEnEdicion == null
                        ? "Proveedor registrado correctamente."
                        : "Proveedor modificado correctamente.";
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

    private boolean validarCampos() {
        String razonSocial = tfRazonSocial.getText().trim();
        String direccion = tfDireccion.getText().trim();
        String correo = tfCorreo.getText().trim();
        String telefono = tfTelefono.getText().trim();

        if (razonSocial.isEmpty() || direccion.isEmpty() || correo.isEmpty() || telefono.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor completa todos los campos.");
            return false;
        }

        if (!telefono.matches("\\d{10}")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Teléfono inválido",
                "El teléfono debe contener exactamente 10 dígitos numéricos.");
            return false;
        }

        if (!correo.matches("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,}$")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Correo inválido",
                    "Ingresa un correo electrónico válido.");
            return false;
        }

        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
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

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
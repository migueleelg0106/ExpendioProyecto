package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
    @FXML
    private Label lbExistencia;
    @FXML
    private Label lbStockMinimo;

    private Bebida bebidaEnEdicion = null;
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

        String nombre = tfNombre.getText().trim();
        String descripcion = tfDescripcion.getText().trim();
        int existencia = Integer.parseInt(tfExistencia.getText().trim());
        int stockMinimo = Integer.parseInt(tfStockMinimo.getText().trim());
        float precio = Float.parseFloat(tfPrecio.getText().trim());

        Bebida bebida = (bebidaEnEdicion == null) ? new Bebida() : bebidaEnEdicion;
        bebida.setNombre(nombre);
        bebida.setDescripcion(descripcion);
        bebida.setExistencia(existencia);
        bebida.setStockMinimo(stockMinimo);
        bebida.setPrecio(precio);

        try {
            boolean exito = (bebidaEnEdicion == null)
                    ? BebidaDAO.insertarBebida(bebida)
                    : BebidaDAO.modificarBebida(bebida);

            if (exito) {
                String mensaje = (bebidaEnEdicion == null)
                        ? "Bebida registrada correctamente."
                        : "Bebida modificada correctamente.";
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

    private boolean validarCampos() {
        String nombre = tfNombre.getText().trim();
        String descripcion = tfDescripcion.getText().trim();
        String existenciaTexto = tfExistencia.getText().trim();
        String precioTexto = tfPrecio.getText().trim();
        String stockMinimoTexto = tfStockMinimo.getText().trim();

        if (nombre.isEmpty() || descripcion.isEmpty() || existenciaTexto.isEmpty()
                || precioTexto.isEmpty() || stockMinimoTexto.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos",
                    "Por favor, completa todos los campos.");
            return false;
        }

        if (!nombre.matches("^[a-zA-ZÁÉÍÓÚáéíóúñÑ\\s]{3,45}$")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Nombre inválido",
                    "El nombre debe contener solo letras y tener entre 3 y 45 caracteres.");
            return false;
        }

        if (descripcion.length() < 5 || descripcion.length() > 100) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Descripción inválida",
                    "La descripción debe tener entre 5 y 100 caracteres.");
            return false;
        }

        try {
            int existencia = Integer.parseInt(existenciaTexto);
            int stockMinimo = Integer.parseInt(stockMinimoTexto);
            float precio = Float.parseFloat(precioTexto);

            if (existencia < 0 || stockMinimo < 0 || precio < 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Valores inválidos",
                        "Existencia, precio y stock mínimo deben ser valores positivos.");
                return false;
            }

        } catch (NumberFormatException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "Verifica que existencia, stock mínimo y precio sean números válidos.");
            return false;
        }

        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
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
            lbExistencia.setText("Existencia:");
            lbStockMinimo.setText("Stock mínimo:");

            // Deshabilitar el campo al editar
            tfExistencia.setDisable(true);
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
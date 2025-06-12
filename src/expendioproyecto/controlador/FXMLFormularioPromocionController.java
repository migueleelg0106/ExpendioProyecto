package expendioproyecto.controlador;

import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.dao.PromocionDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.Promocion;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class FXMLFormularioPromocionController implements Initializable {

    @FXML
    private TextField tfDescuento;
    @FXML
    private DatePicker dpFechaInicio;
    @FXML
    private DatePicker dpFechaVencimiento;
    @FXML
    private TextField tfDescripcion;
    @FXML
    private ComboBox<Bebida> cbBebidas;

    private ObservableList<Bebida> bebidas;
    private Promocion promocionEnEdicion = null;
    private Usuario usuario;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarBebidas();
    }

    @FXML
    private void clicGuardar(ActionEvent event) {
        if (!validarCampos()) {
            return;
        }

        String descripcion = tfDescripcion.getText().trim();
        int descuento = Integer.parseInt(tfDescuento.getText().trim());
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaVencimiento = dpFechaVencimiento.getValue();
        Bebida bebidaSeleccionada = cbBebidas.getValue();

        Promocion promocion = (promocionEnEdicion == null) ? new Promocion() : promocionEnEdicion;
        promocion.setDescripcion(descripcion);
        promocion.setDescuento(descuento);
        promocion.setFechaInicio(fechaInicio.toString());
        promocion.setFechaVencimiento(fechaVencimiento.toString());
        promocion.setIdProducto(bebidaSeleccionada.getIdProducto());

        try {
            boolean exito = (promocionEnEdicion == null)
                    ? PromocionDAO.insertarPromocion(promocion)
                    : PromocionDAO.modificarPromocion(promocion);

            if (exito) {
                String mensaje = (promocionEnEdicion == null)
                        ? "Promoción registrada correctamente."
                        : "Promoción modificada correctamente.";
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", mensaje);
                cerrarVentana();
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo guardar la promoción.");
            }

        } catch (Exception e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error crítico", "Ocurrió un error al guardar la promoción.");
            e.printStackTrace();
        }
    }

    private boolean validarCampos() {
        String descripcion = tfDescripcion.getText().trim();
        String descuentoTexto = tfDescuento.getText().trim();
        LocalDate fechaInicio = dpFechaInicio.getValue();
        LocalDate fechaVencimiento = dpFechaVencimiento.getValue();
        Bebida bebidaSeleccionada = cbBebidas.getValue();

        if (descripcion.isEmpty() || descuentoTexto.isEmpty() || fechaInicio == null || fechaVencimiento == null || bebidaSeleccionada == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campos incompletos", "Completa todos los campos obligatorios.");
            return false;
        }

        int descuento;
        try {
            descuento = Integer.parseInt(descuentoTexto);
        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido", "El descuento debe ser un número válido.");
            return false;
        }

        if (descuento < 1 || descuento > 100) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Descuento inválido", "El descuento debe estar entre 1 y 100.");
            return false;
        }

        if (fechaVencimiento.isBefore(fechaInicio)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Fechas inválidas", "La fecha de vencimiento no puede ser anterior a la fecha de inicio.");
            return false;
        }

        return true;
    }

    @FXML
    private void clicCancelar(ActionEvent event) {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Utilidad.cerrarVentanaComponente(tfDescuento);
    }

    private void cargarBebidas() {
        try {
            bebidas = FXCollections.observableArrayList(BebidaDAO.obtenerBebidas());
            cbBebidas.setItems(bebidas);
        } catch (SQLException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudieron cargar las bebidas.");
            e.printStackTrace();
        }
    }

    public void inicializarFormulario(Promocion promocion) {
        if (promocion != null) {
            this.promocionEnEdicion = promocion;
            tfDescripcion.setText(promocion.getDescripcion());
            tfDescuento.setText(String.valueOf(promocion.getDescuento()));
            dpFechaInicio.setValue(LocalDate.parse(promocion.getFechaInicio()));
            dpFechaVencimiento.setValue(LocalDate.parse(promocion.getFechaVencimiento()));

            // Seleccionar bebida en el ComboBox por ID
            for (Bebida b : bebidas) {
                if (b.getIdProducto() == promocion.getIdProducto()) {
                    cbBebidas.setValue(b);
                    break;
                }
            }
        }
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
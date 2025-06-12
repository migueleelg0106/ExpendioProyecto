/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.PromocionDAO;
import expendioproyecto.modelo.pojo.Promocion;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLPromocionController implements Initializable {

    @FXML
    private TableColumn colBebida;
    @FXML
    private TableColumn colDescuento;
    @FXML
    private TableColumn colFechaInicio;
    @FXML
    private TableColumn colFechaVencimiento;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Promocion> tvPromocion;
    
    private ObservableList<Promocion> promociones;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
        configurarBuscador();
    }    
    
    private void configurarTabla(){
        colBebida.setCellValueFactory(new PropertyValueFactory("producto"));
        colDescuento.setCellValueFactory(new PropertyValueFactory("descuento"));
        colDescuento.setCellFactory(column -> new TableCell<Promocion, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + "%");
                }
            }
        });
        colFechaInicio.setCellValueFactory(new PropertyValueFactory("fechaInicio"));
        colFechaVencimiento.setCellValueFactory(new PropertyValueFactory("fechaVencimiento"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
    }
    
    private void cargarInformacionTabla(){
        try {
            promociones = FXCollections.observableArrayList();
            ArrayList<Promocion> promocionDAO = PromocionDAO.obtenerPromociones();
            promociones.addAll(promocionDAO);
            tvPromocion.setItems(promociones);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al "
                    + "cargar", "Lo sentimos por el momento no se puede "
                            + "mostrar la información de las promociones.");
            cerrarVentana();
            ex.printStackTrace();
        }
    }   
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfBuscar);
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        Promocion seleccionada = tvPromocion.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona una promoción para eliminar.");
            return;
        }

        boolean confirmado = Utilidad.mostrarConfirmacion(
                "Confirmar eliminación",
                "¿Deseas eliminar la promoción seleccionada?",
                "Esta acción no se puede deshacer.");

        if (confirmado) {
            try {
                boolean exito = PromocionDAO.eliminarPromocion(seleccionada.getIdPromocion());
                if (exito) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Promoción eliminada correctamente.");
                    cargarInformacionTabla();
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo eliminar la promoción.");
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                        "Error crítico",
                        "Ocurrió un error al intentar eliminar a la promoción.");
            }
        }
    }


    @FXML
    private void btnClicModificar(ActionEvent event) {
        Promocion seleccionada = tvPromocion.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona una promoción para modificar.");
            return;
        }

        irFormularioPromocion(seleccionada);
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioPromocion(null);
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.getEscenarioComponente(tfBuscar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();

            FXMLVentanaPrincipalController controlador = cargador.getController();
            controlador.configurarVistaSegunTipo(usuario);
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irFormularioPromocion(Promocion promocionEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLFormularioPromocion.fxml"));
            Parent vista = loader.load();

            FXMLFormularioPromocionController controlador = loader.getController();
            controlador.inicializarFormulario(promocionEditar);

            Stage ventana = new Stage();
            ventana.setScene(new Scene(vista));
            ventana.setTitle(promocionEditar == null ? "Agregar Promoción" : "Modificar Promoción");
            ventana.centerOnScreen();
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            cargarInformacionTabla();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void configurarBuscador() {
        tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (promociones != null) {
                ObservableList<Promocion> filtradas = FXCollections.observableArrayList();
                String filtro = newValue.toLowerCase();

                for (Promocion p : promociones) {
                    String bebida = p.getProducto() != null ? p.getProducto().toLowerCase() : "";
                    if (bebida.contains(filtro)) {
                        filtradas.add(p);
                    }
                }

                tvPromocion.setItems(filtradas);
            }
        });
    }

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.pojo.Bebida;
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
public class FXMLBebidaController implements Initializable {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colExistencia;
    @FXML
    private TableColumn colPrecio;
    @FXML
    private TableColumn colStockMin;
    @FXML
    private TableColumn colDescripcion;
    @FXML
    private TableView<Bebida> tvBebidas;
    
    private ObservableList<Bebida> bebidas;

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
        colExistencia.setCellValueFactory(new PropertyValueFactory("existencia"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
        colPrecio.setCellFactory(column -> new TableCell<Bebida, Float>() {
            @Override
            protected void updateItem(Float item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", item));  // Mantiene 2 decimales con símbolo $
                }
            }
        });
        colStockMin.setCellValueFactory(new PropertyValueFactory("stockMinimo"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory("descripcion"));
    }
    
    private void cargarInformacionTabla(){
        try {
            bebidas = FXCollections.observableArrayList();
            ArrayList<Bebida> bebidasDAO = BebidaDAO.obtenerBebidas();
            bebidas.addAll(bebidasDAO);
            tvBebidas.setItems(bebidas);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al "
                    + "cargar", "Lo sentimos por el momento no se puede "
                            + "mostrar la información de las bebidas.");
            cerrarVentana();
        }
    }   

    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfBuscar);
    }
    
    @FXML
    private void btnClicEliminar(ActionEvent event) {        
        Bebida bebidaSeleccionada = tvBebidas.getSelectionModel().getSelectedItem();

        if (bebidaSeleccionada == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección",
                    "Selecciona una bebida para eliminar.");
            return;
        }

        boolean confirmado = Utilidad.mostrarConfirmacion(
                "Confirmar eliminación",
                "¿Deseas eliminar la bebida seleccionada?",
                "Esta acción no se puede deshacer.");

        if (confirmado) {
            try {
                boolean exito = BebidaDAO.eliminarBebida(bebidaSeleccionada.getIdProducto());
                if (exito) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito",
                            "Bebida eliminada correctamente.");
                    cargarInformacionTabla();
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                            "No se pudo eliminar la bebida.");
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,
                            "No se puede eliminar",
                            "No se puede eliminar una bebida asociada a alguna transacción o promoción.");
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                            "Error crítico",
                            "Ocurrió un error al intentar eliminar la bebida.");
                }
            }
        }
    }



    @FXML
    private void btnClicModificar(ActionEvent event) {
        Bebida seleccionada = tvBebidas.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selección requerida", "Selecciona una bebida para modificar.");
            return;
        }
        
        irFormularioBebida(seleccionada);

    }


    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioBebida(null);
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

    private void irFormularioBebida(Bebida bebidaEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLFormularioBebida.fxml"));
            Parent vista = loader.load();

            FXMLFormularioBebidaController controlador = loader.getController();
            controlador.inicializarFormulario(bebidaEditar);

            Stage ventana = new Stage();
            ventana.setScene(new Scene(vista));
            ventana.setTitle(bebidaEditar == null ? "Agregar Bebida" : "Modificar Bebida");
            ventana.centerOnScreen();
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            cargarInformacionTabla(); // recarga la tabla al cerrar

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void configurarBuscador() {
        tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (bebidas != null) {
                ObservableList<Bebida> bebidasFiltradas = FXCollections.observableArrayList();
                for (Bebida b : bebidas) {
                    if (b.getNombre().toLowerCase().contains(newValue.toLowerCase())) {
                        bebidasFiltradas.add(b);
                    }
                }
                tvBebidas.setItems(bebidasFiltradas);
            }
        });
    }

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

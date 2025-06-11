/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ProveedorDAO;
import expendioproyecto.modelo.pojo.Proveedor;
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
public class FXMLProveedorController implements Initializable {

    @FXML
    private TableColumn colRazonSocial;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Proveedor> tvProveedor;
    
    private ObservableList<Proveedor> proveedores;

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
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
    }
    
    private void cargarInformacionTabla(){
        try {
            proveedores = FXCollections.observableArrayList();
            ArrayList<Proveedor> proveedorDAO = ProveedorDAO.obtenerProveedores();
            proveedores.addAll(proveedorDAO);
            tvProveedor.setItems(proveedores);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al "
                    + "cargar", "Lo sentimos por el momento no se puede "
                            + "mostrar la información de los proveedores.");
            cerrarVentana();
        }
    }   
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfBuscar);
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        Proveedor seleccionado = tvProveedor.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona un proveedor para eliminar.");
            return;
        }

        boolean confirmado = Utilidad.mostrarConfirmacion(
                "Confirmar eliminación",
                "¿Deseas eliminar el proveedor seleccionado?",
                "Esta acción no se puede deshacer.");

        if (confirmado) {
            try {
                boolean exito = ProveedorDAO.eliminarProveedor(seleccionado.getIdProveedor());
                if (exito) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Proveedor eliminado correctamente.");
                    cargarInformacionTabla();
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo eliminar el proveedor.");
                }
            } catch (SQLException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error crítico", "Ocurrió un error al eliminar el proveedor.");
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void btnClicModificar(ActionEvent event) {
        Proveedor seleccionado = tvProveedor.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona un proveedor para modificar.");
            return;
        }
        irFormularioProveedor(seleccionado);
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioProveedor(null);
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(tfBuscar);
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
    
    private void irFormularioProveedor(Proveedor proveedorEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLFormularioProveedor.fxml"));
            Parent vista = loader.load();

            FXMLFormularioProveedorController controlador = loader.getController();
            controlador.inicializarFormulario(proveedorEditar);

            Stage ventana = new Stage();
            ventana.setScene(new Scene(vista));
            ventana.setTitle(proveedorEditar == null ? "Agregar Proveedor" : "Modificar Proveedor");
            ventana.centerOnScreen();
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();

            cargarInformacionTabla();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void configurarBuscador() {
        tfBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            if (proveedores != null) {
                ObservableList<Proveedor> filtrados = FXCollections.observableArrayList();
                String filtro = newValue.toLowerCase();

                for (Proveedor p : proveedores) {
                    if (p.getRazonSocial().toLowerCase().contains(filtro)){
                        filtrados.add(p);
                    }
                }

                tvProveedor.setItems(filtrados);
            }
        });
    }
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.pojo.Cliente;
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
import javafx.scene.control.Button;
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
public class FXMLClienteController implements Initializable {


    @FXML
    private TableColumn colRazonSocial;
    @FXML
    private TableColumn colDireccion;
    @FXML
    private TableColumn colCorreo;
    @FXML
    private TableColumn colTelefono;
    @FXML
    private TableColumn colTipo;
    @FXML
    private TableColumn colRFC;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Cliente> tvClientes;
    
    private ObservableList<Cliente> clientes;
    
    private Cliente clienteEnEdicion = null;
    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacionTabla();
        configurarBuscador();
    }    
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    private void configurarTabla(){
        colRazonSocial.setCellValueFactory(new PropertyValueFactory("razonSocial"));
        colDireccion.setCellValueFactory(new PropertyValueFactory("direccion"));
        colCorreo.setCellValueFactory(new PropertyValueFactory("correo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory("telefono"));
        colTipo.setCellValueFactory(new PropertyValueFactory("tipo"));
        colRFC.setCellValueFactory(new PropertyValueFactory("rfc"));
    }
    
    private void cargarInformacionTabla(){
        try {
            clientes = FXCollections.observableArrayList();
            ArrayList<Cliente> bebidasDAO = ClienteDAO.obtenerClientes();
            clientes.addAll(bebidasDAO);
            tvClientes.setItems(clientes);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al "
                    + "cargar", "Lo sentimos por el momento no se puede "
                            + "mostrar la información de los clientes.");
            cerrarVentana();
        }
    }   
    
    private void cerrarVentana(){
        Utilidad.cerrarVentanaComponente(tfBuscar);
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección",
                    "Selecciona un cliente para eliminar.");
            return;
        }

        boolean confirmado = Utilidad.mostrarConfirmacion(
                "Confirmar eliminación",
                "¿Deseas eliminar al cliente seleccionado?",
                "Esta acción no se puede deshacer.");

        if (confirmado) {
            try {
                boolean exito = ClienteDAO.eliminarCliente(clienteSeleccionado.getIdCliente());
                if (exito) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito",
                            "Cliente eliminado correctamente.");
                    cargarInformacionTabla();
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                            "No se pudo eliminar el cliente.");
                }
            } catch (SQLException e) {
                if (e.getErrorCode() == 1451) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING,
                            "No se puede eliminar",
                            "No se puede eliminar un cliente asociado a alguna transacción, pedido o historial de ventas.");
                } else {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                            "Error crítico",
                            "Ocurrió un error al intentar eliminar al cliente.");
                }
            }
        }
    }



    @FXML
    private void btnClicModificar(ActionEvent event) {
        Cliente seleccionado = tvClientes.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selecciona un cliente", "Selecciona un cliente para modificar.");
            return;
        }
        
        irFormularioCliente(seleccionado);
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioCliente(null);
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.getEscenarioComponente(btnRegresar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();

            FXMLVentanaPrincipalController controlador = cargador.getController();
            controlador.configurarVistaSegunTipo(usuario);  // Pasa el usuario de vuelta

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
    private void irFormularioCliente(Cliente clienteEditar) {
        try {
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLFormularioCliente.fxml"));
            Parent vista = loader.load();

            FXMLFormularioClienteController controlador = loader.getController();
            controlador.inicializarFormulario(clienteEditar);
            Stage ventana = new Stage();
            ventana.setScene(new Scene(vista));
            ventana.setTitle(clienteEditar == null ? "Agregar Cliente" : "Modificar Cliente");
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
            if (clientes != null) {
                ObservableList<Cliente> filtrados = FXCollections.observableArrayList();
                for (Cliente c : clientes) {
                    if (c.getCorreo().toLowerCase().contains(newValue.toLowerCase())
                        || c.getTelefono().toLowerCase().contains(newValue.toLowerCase())) {
                        filtrados.add(c);
                    }
                }
                tvClientes.setItems(filtrados);
            }
        });
    }

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ProveedorDAO;
import expendioproyecto.modelo.pojo.Proveedor;
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
    }

    @FXML
    private void btnClicModificar(ActionEvent event) {
    }

    @FXML
    private void btnClicAgregar(ActionEvent event) {
        irFormularioProveedor();
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(tfBuscar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void irFormularioProveedor() {
        try {
            Stage escenarioFormulario = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent vista = loader.load(ExpendioProyecto.class.getResource("vista/FXMLFormularioProveedor.fxml"));
            //TODO paso de parametros
            Scene escena = new Scene(vista);
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Formulario Proveedor");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
}

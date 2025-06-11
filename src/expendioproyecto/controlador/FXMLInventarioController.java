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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLInventarioController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Bebida> tvBebidas;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colExistencia;
    @FXML
    private TableColumn<?, ?> colPrecio;
    @FXML
    private TableColumn<?, ?> colStockMin;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TextField tfBuscar;
    
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
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    private void configurarTabla(){
        colExistencia.setCellValueFactory(new PropertyValueFactory("existencia"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colPrecio.setCellValueFactory(new PropertyValueFactory("precio"));
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

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(tfBuscar); // puedes usar otro nodo si no deseas declarar btnRegresar

            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();

            FXMLVentanaPrincipalController controlador = cargador.getController();
            controlador.configurarVistaSegunTipo(usuario); // PASAR EL USUARIO DE REGRESO

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.dao.ReporteMasVendidoAClienteDAO;
import expendioproyecto.modelo.pojo.Cliente;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
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
public class FXMLReporteMasVendidoAClienteController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Cliente> tvClientes;
    @FXML
    private TableColumn<Cliente, String> colRazonSocial;
    @FXML
    private TableColumn<Cliente, String> colTelefono;
    @FXML
    private TableColumn<Cliente, String> colCorreo;
    @FXML
    private TableColumn<Cliente, String> colDireccion;
    @FXML
    private TableView<ReporteProductoVendido> tvProductosMasVendidos;
    @FXML
    private TableColumn<ReporteProductoVendido, String> colNombreProducto;
    @FXML
    private TableColumn<ReporteProductoVendido, Long> colCantidadCompra;
    @FXML
    private TextField tfBuscarRazon;
    @FXML
    private Button btnExportar;
    
    private ObservableList<Cliente> listaClientes;
    private ObservableList<ReporteProductoVendido> listaProductos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductosMasVendidos.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
        configurarTablaClientes();
        cargarClientes();
        configurarTablaProductos();
        configurarBuscador();
        tvProductosMasVendidos.setPlaceholder(new Label("No hay ventas."));
        
        // Agregar listener para detectar la selección de un cliente
        tvClientes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, nuevoCliente) -> {
            if (nuevoCliente != null) {
                cargarProductosMasVendidos(nuevoCliente);
            } else {
                // Limpiar si no hay selección
                tvProductosMasVendidos.getItems().clear();
                tvProductosMasVendidos.setPlaceholder(new Label("No hay ventas."));
            }
        });
    }    

    private void configurarTablaClientes() {
        colRazonSocial.setCellValueFactory(new PropertyValueFactory<>("razonSocial"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
    }
    
    private void cargarClientes() {
        try {
            ArrayList<Cliente> clientesDAO = ClienteDAO.obtenerClientesReporte();
            listaClientes = FXCollections.observableArrayList(clientesDAO);
            tvClientes.setItems(listaClientes);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR, "Error", "No fue posible cargar los clientes.");
            ex.printStackTrace();
        }
    }
    
    private void configurarTablaProductos() {
        colNombreProducto.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidadCompra.setCellValueFactory(new PropertyValueFactory<>("totalVendido"));
        
        colCantidadCompra.setCellFactory(column -> new TableCell<ReporteProductoVendido, Long>() {
            @Override
            protected void updateItem(Long item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.toString());
                    setStyle("-fx-alignment: CENTER;");
                }
            }
        });
    }
    
    private void cargarProductosMasVendidos(Cliente cliente) {
        try {
            ArrayList<ReporteProductoVendido> productosDAO = ReporteMasVendidoAClienteDAO.obtenerProductoMasVendido(cliente.getIdCliente());
            listaProductos = FXCollections.observableArrayList(productosDAO);
            tvProductosMasVendidos.setItems(listaProductos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR, "Error", "No fue posible cargar el producto más vendido para el cliente seleccionado.");
            ex.printStackTrace();
        }
    }
    
    private void configurarBuscador() {
        tfBuscarRazon.textProperty().addListener((observable, oldValue, newValue) -> {
            if (listaClientes != null) {
                ObservableList<Cliente> clientesFiltrados = FXCollections.observableArrayList();
                for (Cliente c : listaClientes) {
                    if (c.getRazonSocial().toLowerCase().contains(newValue.toLowerCase())) {
                        clientesFiltrados.add(c);
                    }
                }
                tvClientes.setItems(clientesFiltrados);
            }
        });
    }
    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(btnRegresar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVentanaPrincipal.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Menú Principal");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicExportar(ActionEvent event) {
    }
    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.dao.ReporteMasVendidoAClienteDAO;
import expendioproyecto.modelo.pojo.Cliente;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.ExportarAPDF;
import expendioproyecto.utilidad.ExportarAXLSX;
import expendioproyecto.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
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
    
    private ObservableList<Cliente> listaClientes;
    private ObservableList<ReporteProductoVendido> listaProductos;
    @FXML
    private MenuItem btnExportarXLSX;
    @FXML
    private MenuItem btnExportarPDF;

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
            Stage escenarioBase = Utilidad.getEscenarioComponente(btnRegresar);
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

    @FXML
    private void btnClicExportarXLSX(ActionEvent event) {
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "Seleccione un cliente primero.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        File archivo = fileChooser.showSaveDialog(btnExportarXLSX.getParentPopup().getScene().getWindow());

        if (archivo != null) {
            try {
                ExportarAXLSX.exportarAXLSXConCliente(
                    archivo,
                    "Producto Más Vendido A Un Cliente",
                    clienteSeleccionado,
                    Arrays.asList("Razón Social", "Correo", "Teléfono", "Dirección"),
                    Arrays.asList(
                        c -> c.getRazonSocial(),
                        c -> c.getCorreo(),
                        c -> c.getTelefono(),
                        c -> c.getDireccion()
                    ),
                    listaProductos,
                    Arrays.asList("Producto", "Cantidad Vendida"),
                    Arrays.asList(
                        p -> p.getNombre(),
                        p -> String.valueOf(p.getTotalVendido())
                    )
                );

                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "El archivo se ha exportado correctamente.");
            } catch (IOException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo exportar el archivo.");
                e.printStackTrace();
            }
        }
    }
    
    @FXML
    private void btnClicExportarPDF(ActionEvent event) {
        Cliente clienteSeleccionado = tvClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Advertencia", "Seleccione un cliente primero.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Reporte PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        File archivo = fileChooser.showSaveDialog(btnExportarPDF.getParentPopup().getScene().getWindow());

        if (archivo != null) {
            try {
                Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
                Font fontCelda = FontFactory.getFont(FontFactory.HELVETICA, 12);

                ExportarAPDF.exportarAPDFConCliente(
                    archivo,
                    "Producto Más Vendido A Un Cliente",
                    clienteSeleccionado,
                    Arrays.asList("Razón Social", "Correo", "Teléfono", "Dirección"),
                    Arrays.asList(
                        c -> c.getRazonSocial(),
                        c -> c.getCorreo(),
                        c -> c.getTelefono(),
                        c -> c.getDireccion()
                    ),
                    listaProductos,
                    Arrays.asList("Producto", "Cantidad Vendida"),
                    Arrays.asList(
                        p -> p.getNombre(),
                        p -> String.valueOf(p.getTotalVendido())
                    ),
                    fontTitulo,
                    fontCelda,
                    true
                );

                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "El archivo se ha exportado correctamente.");
            } catch (IOException | DocumentException e) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo exportar el archivo.");
                e.printStackTrace();
            }
        }
    }
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

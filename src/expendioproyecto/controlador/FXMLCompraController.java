
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.BebidaCompra;
import expendioproyecto.modelo.pojo.Proveedor;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLCompraController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Bebida> tvInventario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<BebidaCompra> tvProductos;
    @FXML
    private TableColumn<BebidaCompra, String> colProductosCompra;
    @FXML
    private TableColumn<BebidaCompra, Float> colPrecioCompra;
    @FXML
    private TableColumn<BebidaCompra, Integer> colCantidadCompra;
    @FXML
    private TextField tfFolio;
    @FXML
    private ComboBox<Proveedor> cbProveedor;
    @FXML
    private Label tfFecha;
    @FXML
    private Label tfTotalCompra;
    @FXML
    private TableColumn<?, ?> colNombreBebida;
    @FXML
    private TableColumn<?, ?> colExistenciaBebida;
    @FXML
    private TableColumn<?, ?> colStockMinimoBebida;

    private ObservableList<BebidaCompra> listaCompra = FXCollections.observableArrayList();
    private FloatProperty totalCompra = new SimpleFloatProperty(0);



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductos.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
        
        colProductosCompra.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colPrecioCompra.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getPrecioCompra()).asObject());
        colPrecioCompra.setCellFactory(column -> new TableCell<BebidaCompra, Float>() {
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
        colCantidadCompra.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCantidad()).asObject());

        tvProductos.setItems(listaCompra);
        tfTotalCompra.setText("$ 0.00");
        cargarProveedores();
        cargarBebidasInventario();
        configurarFiltroBusqueda();
        tfFecha.setText(java.time.LocalDate.now().toString());

    }    

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        
    @FXML
    private void btnClicAgregar(ActionEvent event) {
        Bebida bebidaSeleccionada = tvInventario.getSelectionModel().getSelectedItem();
        if (bebidaSeleccionada == null) return;

        TextInputDialog cantidadDialog = new TextInputDialog("1");
        cantidadDialog.setTitle("Cantidad");
        cantidadDialog.setHeaderText("¿Cuántas unidades vas a comprar?");
        Optional<String> resultadoCantidad = cantidadDialog.showAndWait();

        if (!resultadoCantidad.isPresent()) return;

        TextInputDialog precioDialog = new TextInputDialog(String.valueOf(bebidaSeleccionada.getPrecio()));
        precioDialog.setTitle("Precio de compra");
        precioDialog.setHeaderText("¿Cuál es el precio por unidad?");
        Optional<String> resultadoPrecio = precioDialog.showAndWait();

        if (!resultadoPrecio.isPresent()) return;

        try {
            int cantidad = Integer.parseInt(resultadoCantidad.get());
            float precio = Float.parseFloat(resultadoPrecio.get());

            BebidaCompra compra = new BebidaCompra(bebidaSeleccionada, cantidad, precio);
            listaCompra.add(compra);

            totalCompra.set(totalCompra.get() + compra.getSubtotal());
            tfTotalCompra.setText(String.format("$ %.2f", totalCompra.get()));

        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Valor inválido", "Ingresa solo números válidos.");
        }
    }



    @FXML
    private void btnClicEliminar(ActionEvent event) {
        BebidaCompra seleccion = tvProductos.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            totalCompra.set(totalCompra.get() - seleccion.getSubtotal());
            listaCompra.remove(seleccion);
            tfTotalCompra.setText(String.format("$ %.2f", totalCompra.get()));
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona un producto para eliminar.");
        }
    }


    @FXML
    private void btnClicFinalizarCompra(ActionEvent event) {
        if (!validarDatosCompra()) return;

        Proveedor proveedor = cbProveedor.getSelectionModel().getSelectedItem();
        String folio = tfFolio.getText();

        try (Connection conexion = ConexionBD.abrirConexion()) {
            conexion.setAutoCommit(false);

            crearTablaTemporalCompra(conexion);
            insertarProductosCompraTemporales(conexion);

            ejecutarProcedimientoCompra(conexion, proveedor.getIdProveedor(), folio, tfFecha.getText());

            conexion.commit();

            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Compra registrada", "La compra fue registrada correctamente.");
            limpiarFormularioCompra();
            cargarBebidasInventario();

        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar compra", e.getMessage());
        }
    }

    private void configurarFiltroBusqueda() {
        FilteredList<Bebida> filtro = new FilteredList<>(listaBebidas, p -> true);
        tfBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            filtro.setPredicate(bebida -> bebida.getNombre().toLowerCase().contains(newVal.toLowerCase()));
        });
        SortedList<Bebida> ordenado = new SortedList<>(filtro);
        ordenado.comparatorProperty().bind(tvInventario.comparatorProperty());
        tvInventario.setItems(ordenado);
    }
    
    private boolean validarDatosCompra() {
        if (cbProveedor.getSelectionModel().getSelectedItem() == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Proveedor no seleccionado", "Selecciona un proveedor.");
            return false;
        }

        if (tfFolio.getText().isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Folio vacío", "Ingresa un folio.");
            return false;
        }

        if (listaCompra.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin productos", "Agrega productos a la compra.");
            return false;
        }

        return true;
    }

    private void ejecutarProcedimientoCompra(Connection conexion, int idProveedor, String folio, String fecha) throws SQLException {
        CallableStatement cs = conexion.prepareCall("{CALL registrar_compra_completa(?, ?, ?)}");
        cs.setInt(1, idProveedor);
        cs.setString(2, folio);
        cs.setDate(3, Date.valueOf(fecha));
        cs.execute();
    }

    private void crearTablaTemporalCompra(Connection conexion) throws SQLException {
        String sql = "CREATE TEMPORARY TABLE IF NOT EXISTS detalle_compra_temp (producto_idProducto INT, cantidad INT, precio DECIMAL(10,2))";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private void insertarProductosCompraTemporales(Connection conexion) throws SQLException {
        String sql = "INSERT INTO detalle_compra_temp(producto_idProducto, cantidad, precio) VALUES (?, ?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (BebidaCompra compra : listaCompra) {
                ps.setInt(1, compra.getBebida().getIdProducto());
                ps.setInt(2, compra.getCantidad());
                ps.setFloat(3, compra.getPrecioCompra());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void limpiarFormularioCompra() {
        listaCompra.clear();
        totalCompra.set(0);
        tfTotalCompra.setText("$ 0.00");
        tfFolio.clear();
        cbProveedor.getSelectionModel().clearSelection();
    }

    private void cargarProveedores() {
        try {
            ObservableList<Proveedor> proveedores = FXCollections.observableArrayList(
                expendioproyecto.modelo.dao.ProveedorDAO.obtenerProveedores()
            );
            cbProveedor.setItems(proveedores);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar proveedores", e.getMessage());
        }
    }

    private ObservableList<Bebida> listaBebidas = FXCollections.observableArrayList();

    private void cargarBebidasInventario() {
        try {
            listaBebidas.setAll(expendioproyecto.modelo.dao.BebidaDAO.obtenerBebidas());

            colNombreBebida.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colExistenciaBebida.setCellValueFactory(new PropertyValueFactory<>("existencia"));
            colStockMinimoBebida.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));

            tvInventario.setItems(listaBebidas);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar bebidas", e.getMessage());
        }
    }

    
}

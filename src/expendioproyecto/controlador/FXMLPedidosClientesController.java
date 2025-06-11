/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.BebidaPedido;
import expendioproyecto.modelo.pojo.Cliente;
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
public class FXMLPedidosClientesController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Bebida> tvInventario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<BebidaPedido> tvProductos;
    @FXML
private ComboBox<Cliente> cbCliente;
    @FXML
    private Label tfFecha;
    @FXML
    private TableColumn<?, ?> colNombreBebida;
    @FXML
    private TableColumn<?, ?> colExistenciaBebida;
    @FXML
    private TableColumn<?, ?> colPrecioBebida;
    @FXML
    private TableColumn<BebidaPedido, String> colProductosPedido;
    @FXML
    private TableColumn<BebidaPedido, Integer> colCantidadPedido;
    @FXML
    private Label tfSubtotal;

    private ObservableList<BebidaPedido> listaPedido = FXCollections.observableArrayList();
    private ObservableList<Bebida> listaBebidas = FXCollections.observableArrayList();

    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductos.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
        colProductosPedido.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colCantidadPedido.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCantidad()).asObject());
        
        tvProductos.setItems(listaPedido);
        tfSubtotal.setText("$ 0.00");
        tfFecha.setText(java.time.LocalDate.now().toString());

        cargarBebidasInventario();  
        configurarFiltroBusqueda();
        cargarClientes();

    }    

    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    
    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(btnRegresar);
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
        if (bebidaSeleccionada == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona una bebida del inventario.");
            return;
        }

        TextInputDialog dialogo = new TextInputDialog("1");
        dialogo.setTitle("Cantidad");
        dialogo.setHeaderText("¿Cuántas unidades desea agregar al pedido?");
        Optional<String> resultado = dialogo.showAndWait();

        if (!resultado.isPresent()) return;

        try {
            int cantidad = Integer.parseInt(resultado.get());

            if (cantidad <= 0) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cantidad inválida", "Debe ser un número mayor que cero.");
                return;
            }

            BebidaPedido pedido = new BebidaPedido(bebidaSeleccionada, cantidad);
            listaPedido.add(pedido);

            // Opcional: actualizar subtotal estimado (basado en precio actual)
            float subtotalActual = Float.parseFloat(tfSubtotal.getText().replace("$", "").trim());
            subtotalActual += bebidaSeleccionada.getPrecio() * cantidad;
            tfSubtotal.setText(String.format("$ %.2f", subtotalActual));

        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error", "Ingresa un número válido.");
        }
    }


    @FXML
    private void btnClicFinalizarPedido(ActionEvent event) {
        if (!validarDatosPedido()) return;

        Cliente cliente = cbCliente.getSelectionModel().getSelectedItem();

        try (Connection conexion = ConexionBD.abrirConexion()) {
            conexion.setAutoCommit(false);

            crearTablaTemporalPedido(conexion);
            insertarDetallePedidoTemp(conexion);

            CallableStatement cs = conexion.prepareCall("{CALL registrar_pedido_completo(?, ?)}");
            cs.setInt(1, cliente.getIdCliente());
            cs.setDate(2, Date.valueOf(tfFecha.getText()));
            cs.execute();

            conexion.commit();

            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Pedido registrado", "El pedido fue guardado correctamente.");
            limpiarFormularioPedido();

        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al registrar pedido", e.getMessage());
        }
    }

    @FXML
    private void btnClicEliminar(ActionEvent event) {
        BebidaPedido seleccion = tvProductos.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            listaPedido.remove(seleccion);

            // Actualizar subtotal estimado (si lo estás usando)
            float subtotalActual = Float.parseFloat(tfSubtotal.getText().replace("$", "").trim());
            float subtotalEliminado = seleccion.getCantidad() * seleccion.getBebida().getPrecio();
            float nuevoSubtotal = subtotalActual - subtotalEliminado;

            tfSubtotal.setText(String.format("$ %.2f", Math.max(nuevoSubtotal, 0))); // no permitir negativos
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Selecciona un producto del pedido para eliminar.");
        }
    }

    private boolean validarDatosPedido() {
        if (cbCliente.getSelectionModel().getSelectedItem() == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cliente no seleccionado", "Selecciona un cliente.");
            return false;
        }

        if (listaPedido.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin productos", "Agrega productos al pedido.");
            return false;
        }

        return true;
    }

    private void crearTablaTemporalPedido(Connection conexion) throws SQLException {
        String sql = "CREATE TEMPORARY TABLE IF NOT EXISTS detalle_pedido_temp (producto_idProducto INT, cantidad INT)";
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }

    private void insertarDetallePedidoTemp(Connection conexion) throws SQLException {
        String sql = "INSERT INTO detalle_pedido_temp(producto_idProducto, cantidad) VALUES (?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (BebidaPedido pedido : listaPedido) {
                ps.setInt(1, pedido.getBebida().getIdProducto());
                ps.setInt(2, pedido.getCantidad());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void limpiarFormularioPedido() {
        listaPedido.clear();
        tfSubtotal.setText("$ 0.00");
        cbCliente.getSelectionModel().clearSelection();
    }
    
    private void cargarBebidasInventario() {
        try {
            listaBebidas.setAll(BebidaDAO.obtenerBebidas());

            colNombreBebida.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colExistenciaBebida.setCellValueFactory(new PropertyValueFactory<>("existencia"));
            colPrecioBebida.setCellValueFactory(new PropertyValueFactory<>("precio"));

            tvInventario.setItems(listaBebidas);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar bebidas", e.getMessage());
        }
    }

    private void cargarClientes() {
        try {
            ObservableList<Cliente> clientes = FXCollections.observableArrayList(ClienteDAO.obtenerClientes());
            cbCliente.setItems(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar clientes", e.getMessage());
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
    
}

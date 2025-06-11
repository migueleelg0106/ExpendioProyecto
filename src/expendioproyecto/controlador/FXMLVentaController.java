/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.dao.ClienteDAO;
import expendioproyecto.modelo.dao.PromocionDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.BebidaVenta;
import expendioproyecto.modelo.pojo.Cliente;
import expendioproyecto.modelo.pojo.Promocion;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
public class FXMLVentaController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Bebida> tvInventario;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<BebidaVenta> tvProductos;

    @FXML
    private ComboBox<Cliente> cbCliente;

    @FXML
    private Label tfFecha;
    @FXML
    private Label tfTotal;
    @FXML
    private TableColumn<Bebida, String> colNombreBebida;
    @FXML
    private TableColumn<Bebida, Integer> colExistenciaBebida;
    @FXML
    private TableColumn<Bebida, Float> colPrecioBebida;
    @FXML
    private TableColumn<BebidaVenta, String> colProductosVenta;
    @FXML
    private TableColumn<BebidaVenta, Float> colPrecioVenta;
    @FXML
    private TableColumn<BebidaVenta, Integer> colCantidadVenta;
    @FXML
    private TableColumn<BebidaVenta, Integer> colPromocionVenta;

    private ObservableList<Bebida> listaBebidas = FXCollections.observableArrayList();
    private ObservableList<BebidaVenta> listaVenta = FXCollections.observableArrayList();
    private FloatProperty totalVenta = new SimpleFloatProperty(0);


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductos.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
        tfFecha.setText(LocalDate.now().toString());

        colNombreBebida.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colExistenciaBebida.setCellValueFactory(new PropertyValueFactory<>("existencia"));
        colPrecioBebida.setCellValueFactory(new PropertyValueFactory<>("precio"));

        cargarBebidas();
        configurarFiltroBusqueda();
        
        colProductosVenta.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colPrecioVenta.setCellValueFactory(cell -> new SimpleFloatProperty(cell.getValue().getPrecioVenta()).asObject());
        colPrecioVenta.setCellFactory(column -> new TableCell<BebidaVenta, Float>() {
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
        colCantidadVenta.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCantidad()).asObject());
        colPromocionVenta.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getPromocion()).asObject());
        colPromocionVenta.setCellFactory(column -> new TableCell<BebidaVenta, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item < 1) {
                    setText("0");
                } else {
                    setText(item + "%");
                }
            }
        });

        tvProductos.setItems(listaVenta);
        
        tfTotal.setText("$ 0.00");
        
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
        if (bebidaSeleccionada == null) return;

        TextInputDialog dialogo = new TextInputDialog("1");
        dialogo.setTitle("Cantidad");
        dialogo.setHeaderText("¿Cuántas unidades deseas vender?");
        Optional<String> resultado = dialogo.showAndWait();

        resultado.ifPresent(cantStr -> {
            try {
                int cantidad = Integer.parseInt(cantStr);

                if (cantidad > bebidaSeleccionada.getExistencia()) {
                    Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.WARNING,
                        "Stock insuficiente",
                        "Solo hay " + bebidaSeleccionada.getExistencia() + " unidades disponibles."
                    );
                    return;
                }

                int promocion = obtenerPromocion(bebidaSeleccionada.getIdProducto());
                float precioBase = bebidaSeleccionada.getPrecio();
                float precioConDescuento = precioBase * (1 - promocion / 100f);

                BebidaVenta venta = new BebidaVenta(bebidaSeleccionada, cantidad, precioConDescuento, promocion);
                listaVenta.add(venta);

                totalVenta.set(totalVenta.get() + venta.getSubtotal());
                tfTotal.setText(String.format("$ %.2f", totalVenta.get()));

            } catch (NumberFormatException e) {
                Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.WARNING,
                    "Cantidad inválida",
                    "Ingresa un número válido para la cantidad."
                );
            }
        });
    }



    @FXML
    private void btnClicFinalizarVenta(ActionEvent event) {
        if (!validarDatosVenta()) return;

        Cliente cliente = cbCliente.getSelectionModel().getSelectedItem();

        try (Connection conexion = ConexionBD.abrirConexion()) {
            conexion.setAutoCommit(false);

            crearTablaTemporalVenta(conexion); // 👈 crear temporal aquí
            insertarProductosTemporales(conexion);

            int folio = generarFolioUnico(conexion);
            registrarVenta(conexion, cliente, folio);

            conexion.commit();

            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Venta registrada", "Folio: " + folio);
            limpiarFormularioVenta();
            cargarBebidas();

        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al finalizar venta", e.getMessage());
        }
    }



    @FXML
    private void btnClicEliminar(ActionEvent event) {
        BebidaVenta seleccion = tvProductos.getSelectionModel().getSelectedItem();

        if (seleccion != null) {
            // Restar el subtotal del producto eliminado al total general
            totalVenta.set(totalVenta.get() - seleccion.getSubtotal());

            // Eliminarlo de la lista
            listaVenta.remove(seleccion);

            // Actualizar el campo tfTotal con el nuevo total
            tfTotal.setText(String.format("$ %.2f", totalVenta.get()));
        }
    }

    
    
    private void cargarBebidas() {
        try {
            listaBebidas.setAll(BebidaDAO.obtenerBebidas());
            tvInventario.setItems(listaBebidas);
        } catch (SQLException e) {
            e.printStackTrace();
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

        private int obtenerPromocion(int idProducto) {
        try {
            LocalDate fechaActual = LocalDate.parse(tfFecha.getText()); // usar la etiqueta de fecha de la ventana
            List<Promocion> promociones = PromocionDAO.obtenerPromociones();

            for (Promocion promo : promociones) {
                if (promo.getIdProducto() == idProducto) {
                    LocalDate inicio = LocalDate.parse(promo.getFechaInicio());
                    LocalDate fin = LocalDate.parse(promo.getFechaVencimiento());

                    if ((fechaActual.isEqual(inicio) || fechaActual.isAfter(inicio)) &&
                        (fechaActual.isEqual(fin) || fechaActual.isBefore(fin))) {
                        return promo.getDescuento();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            // puedes mostrar un Alert si deseas
        }

        return 0; // no tiene promoción activa
    }

        
    private void cargarClientes() {
        try {
            ArrayList<Cliente> lista = ClienteDAO.obtenerClientes();
            ObservableList<Cliente> clientes = FXCollections.observableArrayList(lista);
            cbCliente.setItems(clientes);
        } catch (SQLException e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "La venta fue registrada correctamente.");
        }
    }

    private int generarFolioVentaNumerico() {
        return (int) (Math.random() * 90000) + 10000; // Rango: 10000 - 99999
    }


    private int generarFolioUnico(Connection conexion) throws SQLException {
        int folio;
        boolean existe;

        do {
            folio = generarFolioVentaNumerico();

            PreparedStatement stmt = conexion.prepareStatement(
                "SELECT COUNT(*) FROM venta WHERE folioVenta = ?"
            );
            stmt.setInt(1, folio);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            existe = rs.getInt(1) > 0;

            rs.close();
            stmt.close();

        } while (existe);

        return folio;
    }


    private boolean validarDatosVenta() {
        if (cbCliente.getSelectionModel().getSelectedItem() == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cliente no seleccionado", "Por favor selecciona un cliente.");
            return false;
        }

        if (listaVenta.isEmpty()) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin productos", "Agrega al menos un producto a la venta.");
            return false;
        }

        return true;
    }

    private void crearTablaTemporalVenta(Connection conexion) throws SQLException {
        String sql = "CREATE TEMPORARY TABLE IF NOT EXISTS detalle_venta_temp ("
                   + "producto_idProducto INT NOT NULL, "
                   + "cantidad INT NOT NULL"
                   + ");";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            ps.executeUpdate();
        }
    }



    private void insertarProductosTemporales(Connection conexion) throws SQLException {
        String sql = "INSERT INTO detalle_venta_temp(producto_idProducto, cantidad) VALUES (?, ?)";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            for (BebidaVenta venta : listaVenta) {
                ps.setInt(1, venta.getBebida().getIdProducto());
                ps.setInt(2, venta.getCantidad());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }

    private void registrarVenta(Connection conexion, Cliente cliente, int folio) throws SQLException {
        try (CallableStatement cs = conexion.prepareCall("{CALL registrar_venta_completa(?, ?, ?)}")) {
            cs.setInt(1, cliente.getIdCliente());
            cs.setInt(2, folio);
            cs.setDate(3, Date.valueOf(tfFecha.getText()));
            cs.execute();
        }
    }

    private void limpiarFormularioVenta() {
        listaVenta.clear();
        totalVenta.set(0);
        tfTotal.setText("$ 0.00");
    }

}

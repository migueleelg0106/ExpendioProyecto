package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.BebidaAgregarPedido;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
public class FXMLPedidosProveedorAgregarController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableView<Bebida> tvBebidas;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<BebidaAgregarPedido> tvProductosAAgregar;
    @FXML
    private TableColumn<Bebida, String> colNombreBebida;
    @FXML
    private TableColumn<Bebida, Integer> colExistenciaBebida;
    @FXML
    private TableColumn<Bebida, Integer> colStockMinBebida;
    @FXML
    private TableColumn<Bebida, String> colDescripcionBebida;
    @FXML
    private TableColumn<BebidaAgregarPedido, String> colProductosPedidoAgregar;
    @FXML
    private TableColumn<BebidaAgregarPedido, Integer> colAgregadosCantidadPedidoAgregar;

    private ObservableList<Bebida> bebidasDisponibles = FXCollections.observableArrayList();
    private ObservableList<BebidaAgregarPedido> bebidasAAgregar = FXCollections.observableArrayList();
    private List<Integer> idsExcluidos = new ArrayList<>();
    private FXMLPedidosProveedorController controladorPrincipal;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductosAAgregar.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
        cargarBebidasDisponibles();
        
    }    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnClicAgregarBebida(ActionEvent event) {
        Bebida seleccion = tvBebidas.getSelectionModel().getSelectedItem();
        if (seleccion == null) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin selección", "Debes seleccionar una bebida para agregar.");
            return;
        }

        // Validar duplicado
        for (BebidaAgregarPedido b : bebidasAAgregar) {
            if (b.getBebida().getIdProducto() == seleccion.getIdProducto()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Ya agregada", "Esta bebida ya fue agregada.");
                return;
            }
        }

        // Pedir cantidad
        TextInputDialog dialogo = new TextInputDialog("1");
        dialogo.setTitle("Cantidad a pedir");
        dialogo.setHeaderText("Ingresa la cantidad para: " + seleccion.getNombre());

        Optional<String> resultado = dialogo.showAndWait();
        if (!resultado.isPresent()) return;

        try {
            int cantidad = Integer.parseInt(resultado.get().trim());
            if (cantidad <= 0) throw new NumberFormatException();

            BebidaAgregarPedido nueva = new BebidaAgregarPedido(seleccion, cantidad);
            bebidasAAgregar.add(nueva);

            // Configurar columnas (solo una vez)
            if (tvProductosAAgregar.getItems().isEmpty()) {
                colProductosPedidoAgregar.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
                colAgregadosCantidadPedidoAgregar.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCantidad()).asObject());
                tvProductosAAgregar.setItems(bebidasAAgregar);
            } else {
                tvProductosAAgregar.refresh();
            }

        } catch (NumberFormatException e) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Cantidad inválida", "Debes ingresar un número válido mayor a 0.");
        }
    }



    @FXML
    private void btnClicIncluirPedido(ActionEvent event) {
        if (controladorPrincipal != null && !bebidasAAgregar.isEmpty()) {
            controladorPrincipal.agregarBebidasDesdeSecundaria(new ArrayList<>(bebidasAAgregar));
            ((Stage) btnRegresar.getScene().getWindow()).close(); // cerrar ventana actual
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Nada que agregar", "No has seleccionado bebidas.");
        }
    }

    
    public void setIdsExcluidos(List<Integer> ids) {
        this.idsExcluidos = ids;
        cargarBebidasDisponibles();
    }
    
    private void cargarBebidasDisponibles() {
        try {
            List<Bebida> todas = expendioproyecto.modelo.dao.BebidaDAO.obtenerBebidas();
            bebidasDisponibles.clear();

            for (Bebida b : todas) {
                if (!idsExcluidos.contains(b.getIdProducto())) {
                    bebidasDisponibles.add(b);
                }
            }

            colNombreBebida.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            colExistenciaBebida.setCellValueFactory(new PropertyValueFactory<>("existencia"));
            colStockMinBebida.setCellValueFactory(new PropertyValueFactory<>("stockMinimo"));
            colDescripcionBebida.setCellValueFactory(new PropertyValueFactory<>("descripcion"));

            configurarFiltroBusqueda();

        } catch (Exception e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar bebidas", e.getMessage());
        }
    }



    public void setControladorPrincipal(FXMLPedidosProveedorController controladorPrincipal) {
        this.controladorPrincipal = controladorPrincipal;
    }

    private void configurarFiltroBusqueda() {
        FilteredList<Bebida> filtrado = new FilteredList<>(bebidasDisponibles, p -> true);

        tfBuscar.textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal.toLowerCase().trim();
            filtrado.setPredicate(bebida ->
                bebida.getNombre().toLowerCase().contains(filtro)
            );
        });

        SortedList<Bebida> ordenado = new SortedList<>(filtrado);
        ordenado.comparatorProperty().bind(tvBebidas.comparatorProperty());
        tvBebidas.setItems(ordenado); 
    }
    
    private Usuario usuario;

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.BebidaAgregarPedido;
import expendioproyecto.modelo.pojo.BebidaPedidoProveedor;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.List;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.stream.Collectors;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.IntegerStringConverter;


/**
 * FXML Controller class
 *
 * @author uriel
 */
public class FXMLPedidosProveedorController implements Initializable {

    @FXML
    private Button btnRegresar;
    @FXML
    private TableColumn<BebidaPedidoProveedor, String> colNombre;
    @FXML
    private TableColumn<BebidaPedidoProveedor, String> colDescripcion;
    @FXML
    private TableColumn<BebidaPedidoProveedor, Integer> colCantidad;
    @FXML
    private TableView<BebidaPedidoProveedor> tvBebidasAPedir;

    private ObservableList<BebidaPedidoProveedor> listaPedidoAuto = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarBebidasConStockMinimo();

        tvBebidasAPedir.setEditable(true);

        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));

        colCantidad.setOnEditCommit(event -> {
            BebidaPedidoProveedor bebida = event.getRowValue();
            int nuevoValor = event.getNewValue();

            if (nuevoValor > 0) {
                bebida.setCantidadSugerida(nuevoValor);
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Cantidad inválida", "Debe ser un número mayor a cero.");
                tvBebidasAPedir.refresh();
            }
        });
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
    private void btnCliAgregarBebida(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLPedidosProveedorAgregar.fxml"));
            Parent vista = loader.load();

            FXMLPedidosProveedorAgregarController controlador = loader.getController();

            List<Integer> ids = listaPedidoAuto.stream()
                    .map(BebidaPedidoProveedor::getIdProducto)
                    .collect(Collectors.toList()); 

            controlador.setIdsExcluidos(ids);             // excluir existentes
            controlador.setControladorPrincipal(this);    // referenciar controlador

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Agregar Bebidas al Pedido");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void btnClicExportarPDF(ActionEvent event) {
    }
    
    
    private void cargarBebidasConStockMinimo() {
        try {
            List<Bebida> bebidas = BebidaDAO.obtenerBebidas();

            listaPedidoAuto.clear();
            for (Bebida b : bebidas) {
                if (b.getExistencia() <= b.getStockMinimo()) {
                    listaPedidoAuto.add(new BebidaPedidoProveedor(b));
                }
            }

            colNombre.setCellValueFactory(cell -> cell.getValue().nombreProperty());
            colDescripcion.setCellValueFactory(cell -> cell.getValue().descripcionProperty());
            colCantidad.setCellValueFactory(cell -> cell.getValue().cantidadSugeridaProperty().asObject());

            tvBebidasAPedir.setItems(listaPedidoAuto);

        } catch (Exception e) {
            e.printStackTrace();
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar bebidas", e.getMessage());
        }
    }

    public void agregarBebidasDesdeSecundaria(List<BebidaAgregarPedido> bebidasAgregadas) {
        for (BebidaAgregarPedido item : bebidasAgregadas) {
            BebidaPedidoProveedor nuevo = new BebidaPedidoProveedor(item.getBebida(), item.getCantidad());
            listaPedidoAuto.add(nuevo); 
        }
        tvBebidasAPedir.refresh(); // actualiza la tabla
    }
}

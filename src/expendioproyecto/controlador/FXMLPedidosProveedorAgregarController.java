/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private TableView<?> tvBebidas;
    @FXML
    private TableColumn<?, ?> colNombre;
    @FXML
    private TableColumn<?, ?> colExistencia;
    @FXML
    private TableColumn<?, ?> colProductosCantidad;
    @FXML
    private TableColumn<?, ?> colStockMin;
    @FXML
    private TableColumn<?, ?> colDescripcion;
    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<?> tvProductos;
    @FXML
    private TableColumn<?, ?> colProductosVenta;
    @FXML
    private TableColumn<?, ?> colAgregadosCantidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tvProductos.getStylesheets().add(getClass().getResource("/expendioproyecto/recurso/productos.css").toExternalForm());
    }    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(btnRegresar);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLPedidosProveedor.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Pedidos al proveedor");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAgregarBebida(ActionEvent event) {
    }

    @FXML
    private void btnClicIncluirPedido(ActionEvent event) {
    }
    
}

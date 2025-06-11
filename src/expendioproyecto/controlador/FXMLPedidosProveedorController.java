/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package expendioproyecto.controlador;

import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.dao.BebidaDAO;
import expendioproyecto.modelo.pojo.Bebida;
import expendioproyecto.modelo.pojo.BebidaAgregarPedido;
import expendioproyecto.modelo.pojo.BebidaPedidoProveedor;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.ExportarAPDF;
import expendioproyecto.utilidad.ExportarAXLSX;
import expendioproyecto.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.FileChooser;
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
    @FXML
    private MenuItem btnExportarXLSX;
    @FXML
    private MenuItem btnExportarPDF;
    @FXML
    private Button btnAgregarBebida;

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
    private void btnCliAgregarBebida(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLPedidosProveedorAgregar.fxml"));
            Parent vista = loader.load();

            FXMLPedidosProveedorAgregarController controlador = loader.getController();

            // Pasar usuario
            controlador.setUsuario(usuario);

            // Pasar lista de IDs excluidos
            List<Integer> ids = listaPedidoAuto.stream()
                    .map(BebidaPedidoProveedor::getIdProducto)
                    .collect(Collectors.toList());
            controlador.setIdsExcluidos(ids);

            // Pasar referencia al controlador principal
            controlador.setControladorPrincipal(this);

            Stage escenario = new Stage();
            escenario.setScene(new Scene(vista));
            escenario.setTitle("Agregar Bebidas al Pedido");
            escenario.initModality(Modality.APPLICATION_MODAL);
            escenario.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

    @FXML
    private void btnClicExportarXLSX(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                ExportarAXLSX.exportarAXLSX(
                    archivo,
                    "Bebidas a Pedir",
                    listaPedidoAuto,
                    Arrays.asList("Nombre", "Descripción", "Cantidad Sugerida"),
                    Arrays.asList(
                        bebida -> bebida.getNombre(),
                        bebida -> bebida.getDescripcion(),
                        bebida -> String.valueOf(bebida.getCantidadSugerida())
                    )
                );
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "Excel exportado correctamente.");
            } catch (IOException ex) {
                ex.printStackTrace();
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al exportar a Excel.");
            }
        } 
    }
    
    @FXML
    private void btnClicExportarPDF(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Exportar a PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        File archivo = fileChooser.showSaveDialog(null);

        if (archivo != null) {
            try {
                ExportarAPDF.exportarAPDF(
                    archivo,
                    "Bebidas a Pedir",
                    listaPedidoAuto,
                    Arrays.asList("Nombre", "Descripción", "Cantidad Sugerida"),
                    Arrays.asList(
                        bebida -> bebida.getNombre(),
                        bebida -> bebida.getDescripcion(),
                        bebida -> String.valueOf(bebida.getCantidadSugerida())
                    ),
                    new Font(Font.HELVETICA, 12, Font.BOLD),
                    new Font(Font.HELVETICA, 10, Font.NORMAL),
                    true
                );
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "PDF exportado correctamente.");
            } catch (IOException | DocumentException ex) {
                ex.printStackTrace();
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "Error al exportar a PDF.");
            }
        }
    }
}

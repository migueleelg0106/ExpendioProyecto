package expendioproyecto.controlador;

import expendioproyecto.ExpendioProyecto;
import expendioproyecto.modelo.pojo.Usuario;
import expendioproyecto.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class FXMLVentanaPrincipalController implements Initializable {
    private Usuario usuarioActual;


    @FXML
    private Label lbSeleccionarOpcion;
    @FXML
    private Button btnInvencatio;
    @FXML
    private Button btnCompras;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnPedidosClientes;
    @FXML
    private Button btnPedidosProveedor;
    @FXML
    private Button btnBebidas;
    @FXML
    private Button btnProveedores;
    @FXML
    private Button btnPromociones;
    @FXML
    private Button btnClientes;
    @FXML
    private Button BtnAgregarEmpleados;
    @FXML
    private Button btnBebidaMasVendida;
    @FXML
    private Button btnBebidaMenosVendida;
    @FXML
    private Button btnBebidaMasVendidaCliente;
    @FXML
    private Button btnBebidaNoVendidaCliente;
    @FXML
    private Button btnBebidaStockMinimo;
    @FXML
    private Button btnVentasPorBebida;
    @FXML
    private Button btnVentasPorFecha;
    @FXML
    private Menu mnDeInformacion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicManejarInventario(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLInventario.fxml"));
            Parent vista = cargador.load();

            FXMLInventarioController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inventario");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void clicPedidosDeClientes(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLPedidosClientes.fxml"));
            Parent vista = cargador.load();

            FXMLPedidosClientesController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Pedidos de Clientes");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void clicPedidosDeProveedor(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLPedidosProveedor.fxml"));
            Parent vista = cargador.load();

            FXMLPedidosProveedorController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Pedidos de Proveedores");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVentas(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLVenta.fxml"));
            Parent vista = cargador.load();

            FXMLVentaController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Ventas");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void clicBebidas(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLBebida.fxml"));
            Parent vista = cargador.load();

            FXMLBebidaController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebidas");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicProveedores(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLProveedor.fxml"));
            Parent vista = cargador.load();

            FXMLProveedorController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Proveedores");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicPromociones(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLPromocion.fxml"));
            Parent vista = cargador.load();

            FXMLPromocionController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Promociones");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicCompras(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLCompra.fxml"));
            Parent vista = cargador.load();

            FXMLCompraController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Compras");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    @FXML
    private void clicClientes(ActionEvent event) {
        try {
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLCliente.fxml"));
            Parent vista = cargador.load();

            FXMLClienteController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Clientes");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    @FXML
    private void clicCerrarSesión(ActionEvent event) {
        boolean confirmado = Utilidad.mostrarConfirmacion(
            "Confirmar cierre de sesión",
            "¿Está seguro(a) de querer cerrar la sesión actual?",
            "Se perderá el acceso a esta sesión.");

        if (confirmado) {
            try {
                Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
                Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLIniciarSesion.fxml"));

                Scene escenaPrincipal = new Scene(vista);
                escenarioBase.setScene(escenaPrincipal);
                escenarioBase.setTitle("Inicio Sesión");
                escenarioBase.centerOnScreen();
                escenarioBase.show();
            } catch (IOException ex) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo cerrar sesión.");
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void clicMasVendido(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteMasVendido.fxml"));
            Parent vista = cargador.load();

            FXMLReporteMasVendidoController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebida Más Vendida");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicMenosVendido(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteMenosVendido.fxml"));
            Parent vista = cargador.load();

            FXMLReporteMenosVendidoController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebida Menos Vendida");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicMasVendidoACliente(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteMasVendidoACliente.fxml"));
            Parent vista = cargador.load();

            FXMLReporteMasVendidoAClienteController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebida Más Vendida A Un Cliente");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicNoVendidoACliente(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteSinVenderACliente.fxml"));
            Parent vista = cargador.load();

            FXMLReporteSinVenderAClienteController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebida Sin Vender A Un Cliente");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }


    @FXML
    private void clicAgregarEmpleados(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLAgregarEmpleado.fxml"));
            Parent vista = cargador.load();

            FXMLAgregarEmpleadoController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);

            Stage ventana = new Stage();
            ventana.setScene(new Scene(vista));
            ventana.setTitle ("Agregar Empleado");
            ventana.centerOnScreen();
            ventana.initModality(Modality.APPLICATION_MODAL);
            ventana.showAndWait();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVentasPorFecha(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteVentasPorFecha.fxml"));
            Parent vista = cargador.load();

            FXMLReporteVentasPorFechaController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Ventas Por Fecha");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVentasPorProducto(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteVentasPorBebida.fxml"));
            Parent vista = cargador.load();

            FXMLReporteVentasPorBebidaController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Ventas Por Producto");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicConStockMinimo(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            FXMLLoader cargador = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLReporteConStockMinimo.fxml"));
            Parent vista = cargador.load();

            FXMLReporteConStockMinimoController controlador = cargador.getController();
            controlador.setUsuario(usuarioActual);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebidas Con Stock Mínimo");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    public void configurarVistaSegunTipo(Usuario usuario) {
        this.usuarioActual = usuario;

        if ("empleado".equalsIgnoreCase(usuario.getTipo())) {
            ocultarNodo(btnBebidas);
            ocultarNodo(btnProveedores);
            ocultarNodo(btnPromociones);
            ocultarNodo(BtnAgregarEmpleados);

            ocultarMenu(mnDeInformacion);
        } else if ("administrador".equalsIgnoreCase(usuario.getTipo())) {
        mostrarNodo(btnBebidas);
        mostrarNodo(btnProveedores);
        mostrarNodo(btnPromociones);
        mostrarNodo(BtnAgregarEmpleados);
        mostrarMenu(mnDeInformacion);
        }
    }

    private void ocultarNodo(Node nodo) {
        nodo.setVisible(false);
        nodo.setManaged(false);
    }

    private void ocultarMenu(Menu menu) {
        menu.setVisible(false);
    }
    
    private void mostrarNodo(Node nodo) {
        nodo.setVisible(true);
        nodo.setManaged(true);
    }

    private void mostrarMenu(Menu menu) {
        menu.setVisible(true);
    }
}

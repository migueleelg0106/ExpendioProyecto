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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author MSI
 */
public class FXMLVentanaPrincipalController implements Initializable {

    @FXML
    private Label lbSeleccionarOpcion;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void clicManejarInventario(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLInventario.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inventario");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicPedidosDeClientes(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLPedidosClientes.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Pedidos de Clientes");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicPedidosDeProveedor(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLPedidosProveedor.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Pedidos de Proveedores");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicVentas(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLVenta.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Ventas");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicBebidas(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLBebida.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLProveedor.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLPromocion.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Promociones");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void clicReportes(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporte.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Reportes");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicCompras(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLCompra.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Compras");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicClientes(ActionEvent event) {
        try{
            Stage escenarioBase = Utilidad.gestEscenarioComponente(lbSeleccionarOpcion);
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLCliente.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Clientes");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteMasVendido.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteMenosVendido.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteMasVendidoACliente.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteSinVenderACliente.fxml"));
            
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
            FXMLLoader loader = new FXMLLoader(ExpendioProyecto.class.getResource("vista/FXMLAgregarEmpleado.fxml"));
            Parent vista = loader.load();

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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteVentasPorFecha.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteVentasPorBebida.fxml"));
            
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
            Parent vista = FXMLLoader.load(ExpendioProyecto.class.getResource("vista/FXMLReporteConStockMinimo.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Bebidas Con Stock Mínimo");
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    
}

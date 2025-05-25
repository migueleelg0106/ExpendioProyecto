/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package expendioproyecto;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author migue
 */
public class ExpendioProyecto extends Application{

    /**
     * @param args the command line arguments
     */
    @Override
    public void start(Stage primaryStage){
        try {
            Parent vista = FXMLLoader.load(getClass().getResource("vista/FXMLVentanaPrincipal.fxml"));
            Scene escenaInicioSesion = new Scene(vista);
            primaryStage.setScene(escenaInicioSesion);
            primaryStage.setTitle("Inicio de sesion");
            primaryStage.centerOnScreen();
            primaryStage.show();
        } catch (IOException ex) {
            Logger.getLogger(ExpendioProyecto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

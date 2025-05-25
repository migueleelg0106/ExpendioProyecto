/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexionBD {

    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";

    public static Connection abrirConexion() {
        Connection conexionBD = null;
        String[] credentials = getCredentials();

        try {
            Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(credentials[0], credentials[1], credentials[2]);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }

        return conexionBD;
    }

    private static String[] getCredentials() {
        String[] credentials = new String[3];

        try (InputStream input = ConexionBD.class.getClassLoader()
                .getResourceAsStream("expendioproyecto/config.properties")) {

            Properties properties = new Properties();
            properties.load(input);

            credentials[0] = properties.getProperty("DB_URL");
            credentials[1] = properties.getProperty("DB_USER");
            credentials[2] = properties.getProperty("DB_PASSWORD");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    public static String getAESKEY() {
        String AES_KEY = "";

        try (InputStream input = ConexionBD.class.getClassLoader()
                .getResourceAsStream("expendioproyecto/config.properties")) {

            Properties properties = new Properties();
            properties.load(input);
            AES_KEY = properties.getProperty("AES_KEY");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return AES_KEY;
    }
}
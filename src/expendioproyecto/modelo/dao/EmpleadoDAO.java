/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author uriel
 */
public class EmpleadoDAO {
    
    public static boolean insertarEmpleado(Usuario usuario) throws SQLException {
        String query = "INSERT INTO usuario (username, password) VALUES (?, AES_ENCRYPT(?, 'ClaveExpendio2025@'))";

        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, usuario.getUsername());
            sentencia.setString(2, usuario.getPassword());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error al insertar empleado: " + e.getMessage(), e);
        }
    }

}

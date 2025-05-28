/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import expendioproyecto.modelo.ConexionBD;

/**
 *
 * @author uriel
 */
public class IniciarSesionDAO {
    
    public boolean iniciarSesion(String username, String password) throws SQLException {
        Connection connection = ConexionBD.abrirConexion();
        if (connection == null) throw new SQLException();

        String sql = "SELECT username, password FROM usuario WHERE username = ? AND password = AES_ENCRYPT(?, ?)";
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, ConexionBD.getAESKEY());

        ResultSet rs = stmt.executeQuery();
        return rs.next(); 
    }
}

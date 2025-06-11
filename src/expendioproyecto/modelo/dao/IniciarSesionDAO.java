package expendioproyecto.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Usuario;

public class IniciarSesionDAO {

    public Usuario iniciarSesion(String username, String password) throws SQLException {
        Connection connection = ConexionBD.abrirConexion();
        if (connection == null) throw new SQLException("No se pudo conectar a la base de datos.");

        String sql = "SELECT username, tipo FROM usuario WHERE BINARY username = ? AND password = AES_ENCRYPT(?, ?)";

        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setString(1, username);
        stmt.setString(2, password);
        stmt.setString(3, ConexionBD.getAESKEY());

        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            Usuario user = new Usuario();
            user.setUsername(rs.getString("username"));
            user.setPassword(null); // por seguridad, no lo devolvemos
            user.setTipo(rs.getString("tipo"));
            return user;
        } else {
            return null;
        }
    }
}

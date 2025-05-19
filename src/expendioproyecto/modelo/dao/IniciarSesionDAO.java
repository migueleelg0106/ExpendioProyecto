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
import expendioproyecto.modelo.pojo.Usuario;
//import expendioproyecto.modelo.pojo.Usuario;

/**
 *
 * @author uriel
 */
public class IniciarSesionDAO {
    
    public static Usuario verificarCredenciales(String username, String password) 
            throws SQLException{
        Usuario usuarioSesion = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT * FROM usuario WHERE username = ? AND "
                + "AES_DECRYPT(password, 'ClaveExpendio2025@') = ?";

            
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                usuarioSesion = convertirRegistroUsuario(resultado);
            }
        }else{
            throw new SQLException("Error: Sin conexión a la Base de datos.");
        }
        
        return usuarioSesion;
    }
        
    private static Usuario convertirRegistroUsuario(ResultSet resultado) throws SQLException{
        Usuario usuario = new Usuario();
        usuario.setUsername(resultado.getString("username"));
        usuario.setPassword(resultado.getString("password"));
        
        return usuario;
    }
}

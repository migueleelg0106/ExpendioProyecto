/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author uriel
 */
public class ClienteDAO {
    
    public static ArrayList<Cliente> obtenerClientes() throws SQLException{
        ArrayList<Cliente> bebidas = new ArrayList<Cliente>();
        Connection conexionBD = ConexionBD.abrirConexion();
        
        if(conexionBD != null){
            String consulta = "SELECT idCliente, razonSocial, direccion, correo, rfc, telefono, tipo FROM cliente";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                bebidas.add(convertirRegistroCliente(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión en la BD");
        }
        return bebidas;
    }
    
    private static Cliente convertirRegistroCliente(ResultSet resultado) throws SQLException{
        Cliente cliente = new Cliente();
        cliente.setIdCliente(resultado.getInt("idCliente"));
        cliente.setRazonSocial(resultado.getString("razonSocial"));
        cliente.setDireccion(resultado.getString("direccion"));
        cliente.setCorreo(resultado.getString("correo"));
        cliente.setRfc(resultado.getString("rfc"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setTipo(resultado.getString("tipo"));
        
        return cliente;
    }
    
}

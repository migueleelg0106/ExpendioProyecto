/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Cliente;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import java.sql.CallableStatement;
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
    
    public static boolean insertarCliente(Cliente cliente) throws SQLException {
        String query = "INSERT INTO cliente(direccion, correo, telefono, tipo, rfc, razonSocial) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, cliente.getDireccion());
            sentencia.setString(2, cliente.getCorreo());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.setString(4, cliente.getTipo());
            sentencia.setString(5, cliente.getRfc());
            sentencia.setString(6, cliente.getRazonSocial());

            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error al insertar cliente: " + e.getMessage(), e);
        }
    }
    
    public static boolean modificarCliente(Cliente cliente) throws SQLException {
        String query = "UPDATE cliente SET direccion=?, correo=?, telefono=?, tipo=?, rfc=?, razonSocial=? WHERE idCliente=?";
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, cliente.getDireccion());
            sentencia.setString(2, cliente.getCorreo());
            sentencia.setString(3, cliente.getTelefono());
            sentencia.setString(4, cliente.getTipo());
            sentencia.setString(5, cliente.getRfc());
            sentencia.setString(6, cliente.getRazonSocial());
            sentencia.setInt(7, cliente.getIdCliente());

            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("Error al modificar cliente: " + e.getMessage(), e);
        }
    }

    public static boolean eliminarCliente(int idCliente) throws SQLException {
        String query = "DELETE FROM cliente WHERE idCliente = ?";
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idCliente);
            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error al eliminar cliente: " + e.getMessage(), e.getSQLState(), e.getErrorCode(), e);
        }
    }

    public static ArrayList<Cliente> obtenerClientesReporte() throws SQLException {
        ArrayList<Cliente> clientes = new ArrayList<>();
        String query = "SELECT idCliente, razonSocial, direccion, correo, telefono FROM cliente";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                clientes.add(new Cliente(
                    rs.getInt("idCliente"),
                    rs.getString("razonSocial"),
                    rs.getString("direccion"),
                    rs.getString("correo"),
                    rs.getString("telefono")
                ));
            }
        }
        return clientes;
    }
}

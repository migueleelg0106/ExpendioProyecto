/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Bebida;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author uriel
 */
public class BebidaDAO {
    public static ArrayList<Bebida> obtenerBebidas() throws SQLException{
        ArrayList<Bebida> bebidas = new ArrayList<Bebida>();
        Connection conexionBD = ConexionBD.abrirConexion();
        
        if(conexionBD != null){
            String consulta = "SELECT idProducto, nombre, existencia, precio, stockMinimo, descripcion FROM producto";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                bebidas.add(convertirRegistroBebida(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión en la BD");
        }
        return bebidas;
    }
    
    private static Bebida convertirRegistroBebida(ResultSet resultado) throws SQLException{
        Bebida bebida = new Bebida();
        bebida.setIdProducto(resultado.getInt("idProducto"));
        bebida.setNombre(resultado.getString("nombre"));
        bebida.setExistencia(resultado.getInt("existencia"));
        bebida.setPrecio(resultado.getFloat("precio"));
        bebida.setStockMinimo(resultado.getInt("stockMinimo"));
        bebida.setDescripcion(resultado.getString("descripcion"));
        
        return bebida;
    }
    
    public static boolean insertarBebida(Bebida bebida) throws SQLException {
        boolean resultado = false;
        String query = "INSERT INTO producto(nombre, existencia, precio, stockMinimo, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.abrirConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, bebida.getNombre());
            sentencia.setInt(2, bebida.getExistencia());
            sentencia.setFloat(3, bebida.getPrecio());
            sentencia.setInt(4, bebida.getStockMinimo());
            sentencia.setString(5, bebida.getDescripcion());

            resultado = sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error al insertar bebida: " + e.getMessage(), e);
        }

        return resultado;
    }

    public static boolean modificarBebida(Bebida bebida) throws SQLException {
        String query = "UPDATE producto SET nombre=?, existencia=?, precio=?, stockMinimo=?, descripcion=? WHERE idProducto=?";
        try (Connection conexion = ConexionBD.abrirConexion();
            PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setString(1, bebida.getNombre());
            sentencia.setInt(2, bebida.getExistencia());
            sentencia.setFloat(3, bebida.getPrecio());
            sentencia.setInt(4, bebida.getStockMinimo());
            sentencia.setString(5, bebida.getDescripcion());
            sentencia.setInt(6, bebida.getIdProducto());

            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("Error al modificar bebida: " + e.getMessage(), e);
        }
    }

    public static boolean eliminarBebida(int idProducto) throws SQLException {
        String query = "DELETE FROM producto WHERE idProducto = ?";
        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query)) {

            sentencia.setInt(1, idProducto);
            return sentencia.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new SQLException("Error al eliminar bebida: " + e.getMessage(), e.getSQLState(), e.getErrorCode(), e);
        }
    }
}

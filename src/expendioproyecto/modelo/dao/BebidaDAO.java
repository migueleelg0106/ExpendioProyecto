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
    public static ArrayList<Bebida> obtenerAlumnos() throws SQLException{
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
}

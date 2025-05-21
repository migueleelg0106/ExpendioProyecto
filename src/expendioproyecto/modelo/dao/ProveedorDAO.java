/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Proveedor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author uriel
 */
public class ProveedorDAO {
    public static ArrayList<Proveedor> obtenerProveedores() throws SQLException{
        ArrayList<Proveedor> proveedores = new ArrayList<Proveedor>();
        Connection conexionBD = ConexionBD.abrirConexion();
        
        if(conexionBD != null){
            String consulta = "SELECT idProveedor, razonSocial, direccion, correo, telefono FROM proveedor";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                proveedores.add(convertirRegistroProveedores(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión en la BD");
        }
        return proveedores;
    }
    
    private static Proveedor convertirRegistroProveedores(ResultSet resultado) throws SQLException{
        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor(resultado.getInt("idProveedor"));
        proveedor.setRazonSocial(resultado.getString("razonSocial"));
        proveedor.setDireccion(resultado.getString("direccion"));
        proveedor.setCorreo(resultado.getString("correo"));
        proveedor.setTelefono(resultado.getString("telefono"));
        
        return proveedor;
    }
    
}

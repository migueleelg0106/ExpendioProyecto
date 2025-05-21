/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.Promoción;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author uriel
 */
public class PromocionDAO {
    public static ArrayList<Promoción> obtenerPromociones() throws SQLException{
        ArrayList<Promoción> promociones = new ArrayList<Promoción>();
        Connection conexionBD = ConexionBD.abrirConexion();
        
        if(conexionBD != null){
            String consulta = "SELECT promocion.idPromocion, "
                    + "promocion.fechaInicio, promocion.fechaVencimiento, "
                    + "promocion.descuento, promocion.descripcion, "
                    + "promocion.Producto_idProducto, "
                    + "producto.nombre AS producto FROM promocion INNER JOIN "
                    + "producto ON promocion.Producto_idProducto = producto.idProducto";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                promociones.add(convertirRegistroPromocion(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión en la BD");
        }
        return promociones;
    }
    
    private static Promoción convertirRegistroPromocion(ResultSet resultado) throws SQLException{
        Promoción promocion = new Promoción();
        promocion.setIdPromocion(resultado.getInt("idPromocion"));
        promocion.setFechaInicio(resultado.getString("fechaInicio"));
        promocion.setFechaVencimiento(resultado.getString("fechaVencimiento"));
        promocion.setDescuento(resultado.getInt("descuento"));
        promocion.setDescripcion(resultado.getString("descripcion"));
        promocion.setIdProducto(resultado.getInt("Producto_idProducto"));
        promocion.setProducto(resultado.getString("producto"));
        
        return promocion;
    }
    
}

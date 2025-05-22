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
    
    public static boolean insertarPromocion(Promoción promocion) throws SQLException {
        String sql = "INSERT INTO promocion (fechaInicio, fechaVencimiento, descuento, descripcion, Producto_idProducto) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, promocion.getFechaInicio());
            stmt.setString(2, promocion.getFechaVencimiento());
            stmt.setInt(3, promocion.getDescuento());
            stmt.setString(4, promocion.getDescripcion());
            stmt.setInt(5, promocion.getIdProducto());

            return stmt.executeUpdate() > 0;
        }
    }

    public static boolean modificarPromocion(Promoción promocion) throws SQLException {
        String sql = "UPDATE promocion SET fechaInicio=?, fechaVencimiento=?, descuento=?, descripcion=?, Producto_idProducto=? WHERE idPromocion=?";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, promocion.getFechaInicio());
            stmt.setString(2, promocion.getFechaVencimiento());
            stmt.setInt(3, promocion.getDescuento());
            stmt.setString(4, promocion.getDescripcion());
            stmt.setInt(5, promocion.getIdProducto());
            stmt.setInt(6, promocion.getIdPromocion());

            return stmt.executeUpdate() > 0;
        }
    }
    
    public static boolean eliminarPromocion(int idPromocion) throws SQLException {
        String sql = "DELETE FROM promocion WHERE idPromocion=?";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPromocion);
            return stmt.executeUpdate() > 0;
        }
    }


    
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author uriel
 */
public class ReporteProductoStockMinimoDAO {
    public static ArrayList<ReporteProductoVendido> obtenerProductosStockMinimo() throws SQLException {
        ArrayList<ReporteProductoVendido> productos = new ArrayList<>();
        String query = "SELECT nombre, existencia, stockMinimo FROM vista_productos_stock_minimo";

        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                String nombre = resultado.getString("nombre");
                double existencia = resultado.getDouble("existencia");
                double stock = resultado.getDouble("stockMinimo");

                productos.add(new ReporteProductoVendido(nombre, (double) existencia, stock));
            }
        }

        return productos;
    }
}

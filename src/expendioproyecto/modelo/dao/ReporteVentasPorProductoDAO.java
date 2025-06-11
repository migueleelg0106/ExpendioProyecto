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
public class ReporteVentasPorProductoDAO {
    public static ArrayList<ReporteProductoVendido> obtenerVentasPorProducto() throws SQLException {
        ArrayList<ReporteProductoVendido> productos = new ArrayList<>();
        String query = "SELECT producto, unidades_vendidas, total_ventas FROM vista_ventas_por_producto";

        try (Connection conexion = ConexionBD.abrirConexion();
             PreparedStatement sentencia = conexion.prepareStatement(query);
             ResultSet resultado = sentencia.executeQuery()) {

            while (resultado.next()) {
                String nombre = resultado.getString("producto");
                long total = resultado.getLong("unidades_vendidas");
                double totalPrecio = resultado.getLong("total_ventas");

                productos.add(new ReporteProductoVendido(nombre, total, totalPrecio));
            }
        }

        return productos;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.ConexionBD;
import expendioproyecto.modelo.pojo.ReporteProductoVendido;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author uriel
 */
public class ReporteSinVenderAClienteDAO {
    public static ArrayList<ReporteProductoVendido> obtenerProductoMenosVendido(int idCliente) throws SQLException {
        ArrayList<ReporteProductoVendido> productos = new ArrayList<>();
        String query = "{CALL productos_no_vendidos_a_cliente(?)}";

        try (Connection conn = ConexionBD.abrirConexion();
             CallableStatement cs = conn.prepareCall(query)) {

            cs.setInt(1, idCliente);
            try (ResultSet rs = cs.executeQuery()) {
                while (rs.next()) {
                    ReporteProductoVendido producto = new ReporteProductoVendido(
                        rs.getString("nombre")
                    );
                    productos.add(producto);
                }
            }
        }

        return productos;
    }
}

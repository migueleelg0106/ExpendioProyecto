/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.dao;

import expendioproyecto.modelo.pojo.ReporteVentaFecha;
import expendioproyecto.modelo.ConexionBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author uriel
 */
public class ReporteVentaFechaDAO {
    public static List<ReporteVentaFecha> obtenerVentasPorAño() {
        List<ReporteVentaFecha> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT anio, total_ventas FROM vista_ventas_por_anio")) {

            while (rs.next()) {
                ReporteVentaFecha reporte = new ReporteVentaFecha();
                reporte.setAño(rs.getInt("anio"));
                reporte.setTotalVentas(rs.getDouble("total_ventas"));
                reporte.setMes(null);
                reporte.setSemana(null);
                lista.add(reporte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static List<ReporteVentaFecha> obtenerVentasPorMes() {
        List<ReporteVentaFecha> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT anio, mes, total_ventas FROM vista_ventas_por_mes")) {

            while (rs.next()) {
                ReporteVentaFecha reporte = new ReporteVentaFecha();
                reporte.setAño(rs.getInt("anio"));
                reporte.setMes(rs.getInt("mes"));
                reporte.setSemana(null);
                reporte.setTotalVentas(rs.getDouble("total_ventas"));
                lista.add(reporte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
    
    public static List<ReporteVentaFecha> obtenerVentasPorSemana() {
        List<ReporteVentaFecha> lista = new ArrayList<>();
        try (Connection conn = ConexionBD.abrirConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT anio, semana, total_ventas FROM vista_ventas_por_semana")) {

            while (rs.next()) {
                ReporteVentaFecha reporte = new ReporteVentaFecha();
                reporte.setAño(rs.getInt("anio"));
                reporte.setSemana(rs.getInt("semana"));
                reporte.setMes(null);
                reporte.setTotalVentas(rs.getDouble("total_ventas"));
                lista.add(reporte);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

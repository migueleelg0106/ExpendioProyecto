/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.pojo;

/**
 *
 * @author uriel
 */
public class ReporteVentaFecha {
    private Integer año;
    private Integer mes;
    private Integer semana;
    private double totalVentas;

    public ReporteVentaFecha() {
    }

    public ReporteVentaFecha(Integer año, Integer mes, Integer semana, double totalVentas) {
        this.año = año;
        this.mes = mes;
        this.semana = semana;
        this.totalVentas = totalVentas;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Integer getSemana() {
        return semana;
    }

    public void setSemana(Integer semana) {
        this.semana = semana;
    }

    public double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }
}

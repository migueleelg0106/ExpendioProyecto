/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.pojo;

/**
 *
 * @author uriel
 */
public class ReporteProductoVendido {
    private String nombre;
    private long totalVendido;
    private double existencia;
    private double stock;

    public ReporteProductoVendido(String nombre, long totalVendido) {
        this.nombre = nombre;
        this.totalVendido = totalVendido;
    }

    public ReporteProductoVendido(String nombre, double existencia, double stock) {
        this.nombre = nombre;
        this.existencia = existencia;
        this.stock = stock;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public long getTotalVendido() {
        return totalVendido;
    }

    public void setTotalVendido(long totalVendido) {
        this.totalVendido = totalVendido;
    }

    public double getExistencia() {
        return existencia;
    }

    public void setExistencia(double existencia) {
        this.existencia = existencia;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.pojo;

import java.time.LocalDateTime;

/**
 *
 * @author uriel
 */
public class Venta {
    
    private int idVenta;
    private LocalDateTime fecha;
    private float total;
    private int folioVenta;
    private int idCliente;

    public Venta() {
    }

    public Venta(int idVenta, LocalDateTime fecha, float total, int folioVenta, int idCliente) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.total = total;
        this.folioVenta = folioVenta;
        this.idCliente = idCliente;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public int getFolioVenta() {
        return folioVenta;
    }

    public void setFolioVenta(int folioVenta) {
        this.folioVenta = folioVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
        
}

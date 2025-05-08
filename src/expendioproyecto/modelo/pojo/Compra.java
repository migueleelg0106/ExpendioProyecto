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
public class Compra {
    
    private int idCompra;
    private LocalDateTime fecha;
    private float total;
    private String folioCompra;
    private int IdProveedor;

    public Compra() {
    }

    public Compra(int idCompra, LocalDateTime fecha, float total, String folioCompra, int IdProveedor) {
        this.idCompra = idCompra;
        this.fecha = fecha;
        this.total = total;
        this.folioCompra = folioCompra;
        this.IdProveedor = IdProveedor;
    }

    public int getIdCompra() {
        return idCompra;
    }

    public void setIdCompra(int idCompra) {
        this.idCompra = idCompra;
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

    public String getFolioCompra() {
        return folioCompra;
    }

    public void setFolioCompra(String folioCompra) {
        this.folioCompra = folioCompra;
    }

    public int getIdProveedor() {
        return IdProveedor;
    }

    public void setIdProveedor(int IdProveedor) {
        this.IdProveedor = IdProveedor;
    }
          
}

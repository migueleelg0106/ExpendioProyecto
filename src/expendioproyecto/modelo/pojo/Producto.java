/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package expendioproyecto.modelo.pojo;

/**
 *
 * @author uriel
 */
public class Producto {
    
    private int idProducto;
    private String nombre;
    private int existencia;
    private float precio;
    private int StockMinimo;
    private String descripcion;

    public Producto() {
    }

    public Producto(int idProducto, String nombre, int existencia, float precio, int StockMinimo, String descripcion) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.existencia = existencia;
        this.precio = precio;
        this.StockMinimo = StockMinimo;
        this.descripcion = descripcion;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExistencia() {
        return existencia;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getStockMinimo() {
        return StockMinimo;
    }

    public void setStockMinimo(int StockMinimo) {
        this.StockMinimo = StockMinimo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
        
}

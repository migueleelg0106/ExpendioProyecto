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
public class PedidoCliente {
    
    private int idPedidoCliente;
    private LocalDateTime fecha;
    private int idCliente;

    public PedidoCliente() {
    }

    public PedidoCliente(int idPedidoCliente, LocalDateTime fecha, int idCliente) {
        this.idPedidoCliente = idPedidoCliente;
        this.fecha = fecha;
        this.idCliente = idCliente;
    }

    public int getIdPedidoCliente() {
        return idPedidoCliente;
    }

    public void setIdPedidoCliente(int idPedidoCliente) {
        this.idPedidoCliente = idPedidoCliente;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }
        
}

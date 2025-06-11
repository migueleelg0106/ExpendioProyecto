package expendioproyecto.modelo.pojo;

import javafx.beans.property.*;

public class BebidaPedido {

    private final Bebida bebida;
    private final IntegerProperty cantidad;

    public BebidaPedido(Bebida bebida, int cantidad) {
        this.bebida = bebida;
        this.cantidad = new SimpleIntegerProperty(cantidad);
    }

    public Bebida getBebida() {
        return bebida;
    }

    public String getNombre() {
        return bebida.getNombre();
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }
}

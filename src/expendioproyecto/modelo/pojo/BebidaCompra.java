package expendioproyecto.modelo.pojo;

import javafx.beans.property.*;

public class BebidaCompra {

    private final Bebida bebida; // bebida base con existencia, etc.
    private final IntegerProperty cantidad;
    private final FloatProperty precioCompra;

    public BebidaCompra(Bebida bebida, int cantidad, float precioCompra) {
        this.bebida = bebida;
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precioCompra = new SimpleFloatProperty(precioCompra);
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

    public float getPrecioCompra() {
        return precioCompra.get();
    }

    public float getSubtotal() {
        return getCantidad() * getPrecioCompra();
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public FloatProperty precioCompraProperty() {
        return precioCompra;
    }
}

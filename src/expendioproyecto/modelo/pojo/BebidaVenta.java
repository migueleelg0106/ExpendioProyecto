package expendioproyecto.modelo.pojo;

import javafx.beans.property.*;

public class BebidaVenta {

    private final Bebida bebida;  // referencia al objeto original
    private final IntegerProperty cantidad;
    private final FloatProperty precioVenta;  // ya con descuento aplicado
    private final IntegerProperty promocion;  // porcentaje

    public BebidaVenta(Bebida bebida, int cantidad, float precioVenta, int promocion) {
        this.bebida = bebida;
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precioVenta = new SimpleFloatProperty(precioVenta);
        this.promocion = new SimpleIntegerProperty(promocion);
    }

    public Bebida getBebida() {
        return bebida;
    }

    public String getNombre() {
        return bebida.getNombre();
    }

    public float getPrecioVenta() {
        return precioVenta.get();
    }

    public int getCantidad() {
        return cantidad.get();
    }

    public int getPromocion() {
        return promocion.get();
    }

    public float getSubtotal() {
        return getCantidad() * getPrecioVenta();
    }

    public IntegerProperty cantidadProperty() {
        return cantidad;
    }

    public FloatProperty precioVentaProperty() {
        return precioVenta;
    }

    public IntegerProperty promocionProperty() {
        return promocion;
    }
}

package expendioproyecto.modelo.pojo;

import javafx.beans.property.*;

public class BebidaPedidoProveedor {

    private final StringProperty nombre;
    private final StringProperty descripcion;
    private final IntegerProperty cantidadSugerida;
    private final int idProducto;

    public BebidaPedidoProveedor(Bebida bebida) {
        this.nombre = new SimpleStringProperty(bebida.getNombre());
        this.descripcion = new SimpleStringProperty(bebida.getDescripcion());
        this.idProducto = bebida.getIdProducto();

        int cantidad = (bebida.getStockMinimo() - bebida.getExistencia()) + 20;
        this.cantidadSugerida = new SimpleIntegerProperty(Math.max(cantidad, 0));
    }

    public BebidaPedidoProveedor(Bebida bebida, int cantidad) {
        this.nombre = new SimpleStringProperty(bebida.getNombre());
        this.descripcion = new SimpleStringProperty(bebida.getDescripcion());
        this.idProducto = bebida.getIdProducto();
        this.cantidadSugerida = new SimpleIntegerProperty(Math.max(cantidad, 0));
    }

    
    
    public String getNombre() {
        return nombre.get();
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public int getCantidadSugerida() {
        return cantidadSugerida.get();
    }

    public int getIdProducto() {
        return idProducto;
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public IntegerProperty cantidadSugeridaProperty() {
        return cantidadSugerida;
    }
    
    public void setCantidadSugerida(int cantidad) {
        this.cantidadSugerida.set(Math.max(cantidad, 0));
    }

}

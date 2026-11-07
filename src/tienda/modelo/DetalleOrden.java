package tienda.modelo;

public class DetalleOrden {

    private final Producto producto;
    private int cantidad;
    private final double precioUnitario;

    public DetalleOrden(Producto producto, int cantidad, double precioUnitario) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void sumar(int n) {
        cantidad += n;
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }
}

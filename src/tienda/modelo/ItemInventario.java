package tienda.modelo;

public class ItemInventario {

    private final Producto producto;
    private int cantidad;

    public ItemInventario(Producto producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public Producto getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void sumar(int n) {
        cantidad += n;
    }

    public void restar(int n) {
        cantidad -= n;
    }
}

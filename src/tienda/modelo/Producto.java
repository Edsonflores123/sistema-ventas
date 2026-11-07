package tienda.modelo;

public class Producto {

    private final String codigo;
    private final String nombre;
    private final String descripcion;
    private final String marca;
    private final double precio;

    public Producto(String codigo, String nombre, String descripcion, String marca, double precio) {
        this.codigo = Texto.limpiarCodigo(codigo);
        this.nombre = Texto.limpiar(nombre);
        this.descripcion = Texto.limpiar(descripcion);
        this.marca = Texto.limpiar(marca);
        this.precio = precio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public double getPrecio() {
        return precio;
    }

    public String toLinea() {
        return codigo + "|" + nombre + "|" + descripcion + "|" + marca + "|" + precio;
    }

    public static Producto desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Producto(p[0], p[1], p[2], p[3], Double.parseDouble(p[4]));
    }

    @Override
    public String toString() {
        return codigo + " - " + nombre;
    }
}

package tienda.modelo;

public class Bodega {

    private final String id;
    private final Empleado encargado;
    private final int capacidad;
    private final String direccion;

    public Bodega(String id, Empleado encargado, int capacidad, String direccion) {
        this.id = Texto.limpiarCodigo(id);
        this.encargado = encargado;
        this.capacidad = capacidad;
        this.direccion = Texto.limpiar(direccion);
    }

    public String getId() {
        return id;
    }

    public Empleado getEncargado() {
        return encargado;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public String toLinea() {
        return id + "|" + encargado.getId() + "|" + capacidad + "|" + direccion;
    }

    // el empleado por su ID y se pasa ya listo
    public static Bodega desdeLinea(String linea, Empleado encargado) {
        String[] p = linea.split("\\|", -1);
        return new Bodega(p[0], encargado, Integer.parseInt(p[2]), p[3]);
    }

    @Override
    public String toString() {
        return id + " (" + direccion + ")";
    }
}

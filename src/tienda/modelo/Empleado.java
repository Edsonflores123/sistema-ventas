package tienda.modelo;

public class Empleado {

    private final String id;
    private final String nombre;
    private final String dpi;
    private final String direccion;
    private final String puesto;
    private final double salario;

    public Empleado(String id, String nombre, String dpi, String direccion, String puesto, double salario) {
        this.id = Texto.limpiarCodigo(id);
        this.nombre = Texto.limpiar(nombre);
        this.dpi = Texto.limpiar(dpi);
        this.direccion = Texto.limpiar(direccion);
        this.puesto = Texto.limpiar(puesto);
        this.salario = salario;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDpi() {
        return dpi;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPuesto() {
        return puesto;
    }

    public double getSalario() {
        return salario;
    }

    public String toLinea() {
        return id + "|" + nombre + "|" + dpi + "|" + direccion + "|" + puesto + "|" + salario;
    }

    public static Empleado desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Empleado(p[0], p[1], p[2], p[3], p[4], Double.parseDouble(p[5]));
    }

    @Override
    public String toString() {
        return id + " - " + nombre;
    }
}

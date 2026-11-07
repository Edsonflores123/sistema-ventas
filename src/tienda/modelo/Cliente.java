package tienda.modelo;

import java.time.LocalDate;

public class Cliente {

    private final String dpi;
    private final String nombre;
    private final String direccion;
    private final LocalDate fechaNacimiento;
    private final TipoCliente tipo;

    public Cliente(String dpi, String nombre, String direccion, LocalDate fechaNacimiento, TipoCliente tipo) {
        this.dpi = Texto.limpiar(dpi);
        this.nombre = Texto.limpiar(nombre);
        this.direccion = Texto.limpiar(direccion);
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }

    public String getDpi() {
        return dpi;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public String toLinea() {
        return dpi + "|" + nombre + "|" + direccion + "|" + fechaNacimiento + "|" + tipo;
    }

    public static Cliente desdeLinea(String linea) {
        String[] p = linea.split("\\|", -1);
        return new Cliente(p[0], p[1], p[2], LocalDate.parse(p[3]), TipoCliente.valueOf(p[4]));
    }

    @Override
    public String toString() {
        return nombre + " (" + dpi + ")";
    }
}

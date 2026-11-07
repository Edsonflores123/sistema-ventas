package tienda.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class OrdenCompra {

    private final int numero;
    private final Cliente cliente;
    private final List<DetalleOrden> detalles;
    private final LocalDate fecha;
    private EstadoOrden estado;

    public OrdenCompra(int numero, Cliente cliente, List<DetalleOrden> detalles, LocalDate fecha, EstadoOrden estado) {
        this.numero = numero;
        this.cliente = cliente;
        this.detalles = new ArrayList<>(detalles);
        this.fecha = fecha;
        this.estado = estado;
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<DetalleOrden> getDetalles() {
        return detalles;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }

    public double getTotal() {
        double total = 0;
        for (DetalleOrden d : detalles) {
            total += d.getSubtotal();
        }
        return total;
    }

    public String toLinea() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < detalles.size(); i++) {
            DetalleOrden d = detalles.get(i);
            if (i > 0) {
                sb.append(";");
            }
            sb.append(d.getProducto().getCodigo()).append(":").append(d.getCantidad()).append(":").append(d.getPrecioUnitario());
        }
        return numero + "|" + cliente.getDpi() + "|" + fecha + "|" + getTotal() + "|" + estado + "|" + sb;
    }

    public static OrdenCompra desdeLinea(String linea, Cliente cliente, Function<String, Producto> buscarProducto) {
        String[] p = linea.split("\\|", -1);
        List<DetalleOrden> detalles = new ArrayList<>();
        for (String texto : p[5].split(";")) {
            if (texto.isBlank()) {
                continue;
            }
            String[] partes = texto.split(":");
            Producto producto = buscarProducto.apply(partes[0]);
            if (producto != null) {
                detalles.add(new DetalleOrden(producto, Integer.parseInt(partes[1]), Double.parseDouble(partes[2])));
            }
        }
        return new OrdenCompra(Integer.parseInt(p[0]), cliente, detalles, LocalDate.parse(p[2]), EstadoOrden.valueOf(p[4]));
    }
}

package tienda.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Inventario {

    private final String id;
    private final Bodega bodega;
    private final List<ItemInventario> items = new ArrayList<>();

    public Inventario(String id, Bodega bodega) {
        this.id = Texto.limpiarCodigo(id);
        this.bodega = bodega;
    }

    public String getId() {
        return id;
    }

    public Bodega getBodega() {
        return bodega;
    }

    public List<ItemInventario> getItems() {
        return items;
    }

    public int totalUnidades() {
        int total = 0;
        for (ItemInventario item : items) {
            total += item.getCantidad();
        }
        return total;
    }

    public int stockDe(String codigoProducto) {
        ItemInventario item = buscarItem(codigoProducto);
        return item == null ? 0 : item.getCantidad();
    }

    public void agregar(Producto producto, int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        int ocupado = totalUnidades();
        if (ocupado + cantidad > bodega.getCapacidad()) {
            throw new IllegalArgumentException("La bodega " + bodega.getId() + " no tiene espacio suficiente (capacidad "
                    + bodega.getCapacidad() + ", ocupado " + ocupado + ").");
        }
        ItemInventario item = buscarItem(producto.getCodigo());
        if (item == null) {
            items.add(new ItemInventario(producto, cantidad));
        } else {
            item.sumar(cantidad);
        }
    }

    public int quitar(String codigoProducto, int cantidad) {
        ItemInventario item = buscarItem(codigoProducto);
        if (item == null) {
            return 0;
        }
        int quitadas = Math.min(item.getCantidad(), cantidad);
        item.restar(quitadas);
        return quitadas;
    }

    private ItemInventario buscarItem(String codigoProducto) {
        for (ItemInventario item : items) {
            if (item.getProducto().getCodigo().equals(codigoProducto)) {
                return item;
            }
        }
        return null;
    }

    public String toLinea() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(items.get(i).getProducto().getCodigo()).append(":").append(items.get(i).getCantidad());
        }
        return id + "|" + bodega.getId() + "|" + sb;
    }

    public static Inventario desdeLinea(String linea, Bodega bodega, Function<String, Producto> buscarProducto) {
        String[] p = linea.split("\\|", -1);
        Inventario inv = new Inventario(p[0], bodega);
        for (String texto : p[2].split(";")) {
            if (texto.isBlank()) {
                continue;
            }
            String[] partes = texto.split(":");
            Producto producto = buscarProducto.apply(partes[0]);
            if (producto != null) {
                inv.items.add(new ItemInventario(producto, Integer.parseInt(partes[1])));
            }
        }
        return inv;
    }
}

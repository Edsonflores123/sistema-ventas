package tienda.persistencia;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import tienda.modelo.Bodega;
import tienda.modelo.Cliente;
import tienda.modelo.Empleado;
import tienda.modelo.Inventario;
import tienda.modelo.OrdenCompra;
import tienda.modelo.Producto;

public class Almacen {

    private final Path carpeta;

    private final List<Producto> productos = new ArrayList<>();
    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Empleado> empleados = new ArrayList<>();
    private final List<Bodega> bodegas = new ArrayList<>();
    private final List<Inventario> inventarios = new ArrayList<>();
    private final List<OrdenCompra> ordenes = new ArrayList<>();

    public Almacen(String rutaCarpeta) {
        this.carpeta = Paths.get(rutaCarpeta);
        try {
            Files.createDirectories(carpeta);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo crear la carpeta de datos", e);
        }
        cargarTodo();
    }

    // lectura 

    private void cargarTodo() {
        for (String linea : leer("productos.txt")) {
            productos.add(Producto.desdeLinea(linea));
        }
        for (String linea : leer("clientes.txt")) {
            clientes.add(Cliente.desdeLinea(linea));
        }
        for (String linea : leer("empleados.txt")) {
            empleados.add(Empleado.desdeLinea(linea));
        }

        // las bodegas dependen de los empleados
        for (String linea : leer("bodegas.txt")) {
            Empleado encargado = buscarEmpleado(linea.split("\\|", -1)[1]);
            if (encargado != null) {
                bodegas.add(Bodega.desdeLinea(linea, encargado));
            }
        }

        // los inventarios dependen de bodegas y productos
        for (String linea : leer("inventarios.txt")) {
            Bodega bodega = buscarBodega(linea.split("\\|", -1)[1]);
            if (bodega != null) {
                inventarios.add(Inventario.desdeLinea(linea, bodega, this::buscarProducto));
            }
        }

        // las órdenes dependen de clientes y productos
        for (String linea : leer("ordenes.txt")) {
            Cliente cliente = buscarCliente(linea.split("\\|", -1)[1]);
            if (cliente != null) {
                ordenes.add(OrdenCompra.desdeLinea(linea, cliente, this::buscarProducto));
            }
        }
    }

    private List<String> leer(String archivo) {
        Path ruta = carpeta.resolve(archivo);
        List<String> lineas = new ArrayList<>();
        if (!Files.exists(ruta)) {
            return lineas;
        }
        try {
            for (String linea : Files.readAllLines(ruta, StandardCharsets.UTF_8)) {
                if (!linea.isBlank()) {
                    lineas.add(linea);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer " + archivo, e);
        }
        return lineas;
    }

    //  escritura

    private void escribir(String archivo, List<String> lineas) {
        try {
            Files.write(carpeta.resolve(archivo), lineas, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo guardar " + archivo, e);
        }
    }

    public void guardarProductos() {
        List<String> lineas = new ArrayList<>();
        for (Producto p : productos) {
            lineas.add(p.toLinea());
        }
        escribir("productos.txt", lineas);
    }

    public void guardarClientes() {
        List<String> lineas = new ArrayList<>();
        for (Cliente c : clientes) {
            lineas.add(c.toLinea());
        }
        escribir("clientes.txt", lineas);
    }

    public void guardarEmpleados() {
        List<String> lineas = new ArrayList<>();
        for (Empleado e : empleados) {
            lineas.add(e.toLinea());
        }
        escribir("empleados.txt", lineas);
    }

    public void guardarBodegas() {
        List<String> lineas = new ArrayList<>();
        for (Bodega b : bodegas) {
            lineas.add(b.toLinea());
        }
        escribir("bodegas.txt", lineas);
    }

    public void guardarInventarios() {
        List<String> lineas = new ArrayList<>();
        for (Inventario i : inventarios) {
            lineas.add(i.toLinea());
        }
        escribir("inventarios.txt", lineas);
    }

    public void guardarOrdenes() {
        List<String> lineas = new ArrayList<>();
        for (OrdenCompra o : ordenes) {
            lineas.add(o.toLinea());
        }
        escribir("ordenes.txt", lineas);
    }

    //  búsquedas

    public Producto buscarProducto(String codigo) {
        for (Producto p : productos) {
            if (p.getCodigo().equalsIgnoreCase(codigo)) {
                return p;
            }
        }
        return null;
    }

    public Cliente buscarCliente(String dpi) {
        for (Cliente c : clientes) {
            if (c.getDpi().equals(dpi)) {
                return c;
            }
        }
        return null;
    }

    public Empleado buscarEmpleado(String id) {
        for (Empleado e : empleados) {
            if (e.getId().equalsIgnoreCase(id)) {
                return e;
            }
        }
        return null;
    }

    public Bodega buscarBodega(String id) {
        for (Bodega b : bodegas) {
            if (b.getId().equalsIgnoreCase(id)) {
                return b;
            }
        }
        return null;
    }

    public Inventario buscarInventarioDeBodega(String idBodega) {
        for (Inventario i : inventarios) {
            if (i.getBodega().getId().equalsIgnoreCase(idBodega)) {
                return i;
            }
        }
        return null;
    }

    // listas

    public List<Producto> getProductos() {
        return productos;
    }

    public List<Cliente> getClientes() {
        return clientes;
    }

    public List<Empleado> getEmpleados() {
        return empleados;
    }

    public List<Bodega> getBodegas() {
        return bodegas;
    }

    public List<Inventario> getInventarios() {
        return inventarios;
    }

    public List<OrdenCompra> getOrdenes() {
        return ordenes;
    }
}

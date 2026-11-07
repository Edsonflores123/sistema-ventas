package tienda.servicio;

import java.time.LocalDate;
import java.util.List;

import tienda.modelo.Bodega;
import tienda.modelo.Cliente;
import tienda.modelo.DetalleOrden;
import tienda.modelo.Empleado;
import tienda.modelo.EstadoOrden;
import tienda.modelo.Inventario;
import tienda.modelo.OrdenCompra;
import tienda.modelo.Producto;
import tienda.modelo.TipoCliente;
import tienda.persistencia.Almacen;

//validaciones, registro de datos y el manejo del stock cuando una orden se completa

public class Tienda {

    private final Almacen almacen;

    public Tienda(Almacen almacen) {
        this.almacen = almacen;
    }

    //consultas

    public List<Producto> getProductos() {
        return almacen.getProductos();
    }

    public List<Cliente> getClientes() {
        return almacen.getClientes();
    }

    public List<Empleado> getEmpleados() {
        return almacen.getEmpleados();
    }

    public List<Bodega> getBodegas() {
        return almacen.getBodegas();
    }

    public List<Inventario> getInventarios() {
        return almacen.getInventarios();
    }

    public List<OrdenCompra> getOrdenes() {
        return almacen.getOrdenes();
    }

    // registros

    public void registrarProducto(String codigo, String nombre, String descripcion, String marca, double precio) {
        Producto nuevo = new Producto(codigo, nombre, descripcion, marca, precio);
        if (nuevo.getCodigo().isEmpty() || nuevo.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El código y el nombre del producto son obligatorios.");
        }
        if (precio <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero.");
        }
        if (almacen.buscarProducto(nuevo.getCodigo()) != null) {
            throw new IllegalArgumentException("Ya existe un producto con el código " + nuevo.getCodigo() + ".");
        }
        almacen.getProductos().add(nuevo);
        almacen.guardarProductos();
    }

    public void registrarCliente(String dpi, String nombre, String direccion, LocalDate nacimiento, TipoCliente tipo) {
        Cliente nuevo = new Cliente(dpi, nombre, direccion, nacimiento, tipo);
        if (!nuevo.getDpi().matches("\\d{13}")) {
            throw new IllegalArgumentException("El DPI debe tener 13 dígitos.");
        }
        if (nuevo.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }
        if (nacimiento.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura.");
        }
        if (almacen.buscarCliente(nuevo.getDpi()) != null) {
            throw new IllegalArgumentException("Ya hay un cliente registrado con ese DPI.");
        }
        almacen.getClientes().add(nuevo);
        almacen.guardarClientes();
    }

    public void registrarEmpleado(String id, String nombre, String dpi, String direccion, String puesto, double salario) {
        Empleado nuevo = new Empleado(id, nombre, dpi, direccion, puesto, salario);
        if (nuevo.getId().isEmpty() || nuevo.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El ID y el nombre del empleado son obligatorios.");
        }
        if (!nuevo.getDpi().matches("\\d{13}")) {
            throw new IllegalArgumentException("El DPI debe tener 13 dígitos.");
        }
        if (salario <= 0) {
            throw new IllegalArgumentException("El salario debe ser mayor a cero.");
        }
        if (almacen.buscarEmpleado(nuevo.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un empleado con el ID " + nuevo.getId() + ".");
        }
        almacen.getEmpleados().add(nuevo);
        almacen.guardarEmpleados();
    }

    public void registrarBodega(String id, Empleado encargado, int capacidad, String direccion) {
        Bodega nueva = new Bodega(id, encargado, capacidad, direccion);
        if (nueva.getId().isEmpty()) {
            throw new IllegalArgumentException("El ID de la bodega es obligatorio.");
        }
        if (encargado == null) {
            throw new IllegalArgumentException("Primero registra un empleado para asignarlo a la bodega.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor a cero.");
        }
        if (almacen.buscarBodega(nueva.getId()) != null) {
            throw new IllegalArgumentException("Ya existe una bodega con el ID " + nueva.getId() + ".");
        }
        almacen.getBodegas().add(nueva);
        almacen.guardarBodegas();
    }

    // cada bodega tiene un solo inventario si no existe se crea al registrar el primer producto
    public void registrarInventario(Bodega bodega, Producto producto, int cantidad) {
        if (bodega == null || producto == null) {
            throw new IllegalArgumentException("Selecciona una bodega y un producto.");
        }
        Inventario inventario = almacen.buscarInventarioDeBodega(bodega.getId());
        if (inventario == null) {
            inventario = new Inventario("INV-" + bodega.getId(), bodega);
            inventario.agregar(producto, cantidad); 
            almacen.getInventarios().add(inventario);
        } else {
            inventario.agregar(producto, cantidad);
        }
        almacen.guardarInventarios();
    }

    public void registrarOrden(Cliente cliente, List<DetalleOrden> detalles) {
        if (cliente == null) {
            throw new IllegalArgumentException("Selecciona un cliente.");
        }
        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La orden debe tener al menos un producto.");
        }
        int numero = 1;
        for (OrdenCompra o : almacen.getOrdenes()) {
            numero = Math.max(numero, o.getNumero() + 1);
        }
        almacen.getOrdenes().add(new OrdenCompra(numero, cliente, detalles, LocalDate.now(), EstadoOrden.PENDIENTE));
        almacen.guardarOrdenes();
    }

    // cambios de estado de una orden

    public void completarOrden(OrdenCompra orden) {
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden completar órdenes pendientes.");
        }

        for (DetalleOrden d : orden.getDetalles()) {
            int disponible = stockTotal(d.getProducto().getCodigo());
            if (disponible < d.getCantidad()) {
                throw new IllegalStateException("Stock insuficiente de " + d.getProducto().getNombre()
                        + ": hay " + disponible + " y se necesitan " + d.getCantidad() + ".");
            }
        }

        for (DetalleOrden d : orden.getDetalles()) {
            descontarStock(d.getProducto().getCodigo(), d.getCantidad());
        }

        orden.setEstado(EstadoOrden.COMPLETADA);
        almacen.guardarInventarios();
        almacen.guardarOrdenes();
    }

    public void cancelarOrden(OrdenCompra orden) {
        if (orden.getEstado() != EstadoOrden.PENDIENTE) {
            throw new IllegalStateException("Solo se pueden cancelar órdenes pendientes.");
        }
        orden.setEstado(EstadoOrden.CANCELADA);
        almacen.guardarOrdenes();
    }

    // stock

    public int stockTotal(String codigoProducto) {
        int total = 0;
        for (Inventario inv : almacen.getInventarios()) {
            total += inv.stockDe(codigoProducto);
        }
        return total;
    }

    // va restando de las bodegas en orden hasta cubrir la cantidad vendida
    private void descontarStock(String codigoProducto, int cantidad) {
        int pendiente = cantidad;
        for (Inventario inv : almacen.getInventarios()) {
            if (pendiente == 0) {
                break;
            }
            pendiente -= inv.quitar(codigoProducto, pendiente);
        }
    }
}

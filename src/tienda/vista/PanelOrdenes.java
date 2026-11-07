package tienda.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import tienda.modelo.Cliente;
import tienda.modelo.DetalleOrden;
import tienda.modelo.OrdenCompra;
import tienda.modelo.Producto;
import tienda.modelo.Texto;
import tienda.servicio.Tienda;

public class PanelOrdenes extends PanelBase {

    private final Tienda tienda;

    private final JComboBox<Cliente> cmbCliente = Ui.combo(new JComboBox<>());
    private final JComboBox<Producto> cmbProducto = Ui.combo(new JComboBox<>());
    private final JSpinner spnCantidad = Ui.spinner(new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1)));

    private final DefaultTableModel modeloCarrito = Ui.modelo("Código", "Producto", "Cantidad", "Precio", "Subtotal");
    private final DefaultTableModel modeloOrdenes = Ui.modelo("No.", "Cliente", "Fecha", "Total", "Estado");
    private final JTable tablaCarrito = Ui.tabla(modeloCarrito, "Agrega productos a la orden");
    private final JTable tablaOrdenes = Ui.tabla(modeloOrdenes, "Todavía no hay órdenes registradas");
    private final JLabel lblTotal = new JLabel("Total: Q0.00");

    private final List<DetalleOrden> carrito = new ArrayList<>();

    public PanelOrdenes(Tienda tienda) {
        this.tienda = tienda;
        setLayout(new BorderLayout());

        JPanel form = Ui.formulario("Nueva orden de compra", 3);
        Ui.agregarFila(form, 0, "Cliente:", cmbCliente);
        Ui.agregarFila(form, 1, "Producto:", cmbProducto);
        Ui.agregarFila(form, 2, "Cantidad:", spnCantidad);

        JButton btnAgregar = Ui.boton("Agregar producto", true);
        btnAgregar.addActionListener(e -> agregarAlCarrito());
        JButton btnQuitar = Ui.boton("Quitar seleccionado", false);
        btnQuitar.addActionListener(e -> quitarDelCarrito());
        JPanel botonesForm = Ui.barra();
        botonesForm.add(btnAgregar);
        botonesForm.add(btnQuitar);
        Ui.agregarFila(form, 3, "", botonesForm);

        JButton btnRegistrar = Ui.boton("Registrar orden", true);
        btnRegistrar.addActionListener(e -> registrarOrden());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        pie.setOpaque(false);
        Ui.destacar(lblTotal);
        pie.add(lblTotal);
        pie.add(btnRegistrar);

        JPanel arriba = new JPanel(new BorderLayout(0, 12));
        arriba.setOpaque(false);
        arriba.add(form, BorderLayout.NORTH);
        arriba.add(Ui.enTarjeta(tablaCarrito), BorderLayout.CENTER);
        arriba.add(pie, BorderLayout.SOUTH);

        JButton btnCompletar = Ui.boton("Completar orden", true);
        btnCompletar.addActionListener(e -> completar());
        JButton btnCancelar = Ui.boton("Cancelar orden", false);
        btnCancelar.addActionListener(e -> cancelar());
        JPanel botonesOrdenes = Ui.barra();
        botonesOrdenes.add(btnCompletar);
        botonesOrdenes.add(btnCancelar);

        JPanel abajo = new JPanel(new BorderLayout(0, 12));
        abajo.setOpaque(false);
        abajo.add(Ui.subtitulo("Órdenes registradas"), BorderLayout.NORTH);
        abajo.add(Ui.enTarjeta(tablaOrdenes), BorderLayout.CENTER);
        abajo.add(botonesOrdenes, BorderLayout.SOUTH);

        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, arriba, abajo);
        division.setResizeWeight(0.55);
        Ui.divisor(division);
        add(division, BorderLayout.CENTER);

        refrescar();
    }

    private void agregarAlCarrito() {
        Producto producto = (Producto) cmbProducto.getSelectedItem();
        if (producto == null) {
            Ui.error(this, "No hay productos registrados.");
            return;
        }
        int cantidad = (Integer) spnCantidad.getValue();

        for (DetalleOrden d : carrito) {
            if (d.getProducto().getCodigo().equals(producto.getCodigo())) {
                d.sumar(cantidad);
                mostrarCarrito();
                return;
            }
        }
        carrito.add(new DetalleOrden(producto, cantidad, producto.getPrecio()));
        mostrarCarrito();
    }

    private void quitarDelCarrito() {
        int fila = tablaCarrito.getSelectedRow();
        if (fila < 0) {
            Ui.error(this, "Selecciona un producto de la lista.");
            return;
        }
        carrito.remove(fila);
        mostrarCarrito();
    }

    private void registrarOrden() {
        try {
            tienda.registrarOrden((Cliente) cmbCliente.getSelectedItem(), carrito);
            carrito.clear();
            mostrarCarrito();
            mostrarOrdenes();
            Ui.info(this, "Orden registrada. Queda pendiente hasta que la completes.");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void completar() {
        OrdenCompra orden = ordenSeleccionada();
        if (orden == null) {
            return;
        }
        try {
            tienda.completarOrden(orden);
            mostrarOrdenes();
            Ui.info(this, "Orden completada y stock actualizado.");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void cancelar() {
        OrdenCompra orden = ordenSeleccionada();
        if (orden == null) {
            return;
        }
        try {
            tienda.cancelarOrden(orden);
            mostrarOrdenes();
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private OrdenCompra ordenSeleccionada() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila < 0) {
            Ui.error(this, "Selecciona una orden de la tabla.");
            return null;
        }
        return tienda.getOrdenes().get(fila);
    }

    private void mostrarCarrito() {
        modeloCarrito.setRowCount(0);
        double total = 0;
        for (DetalleOrden d : carrito) {
            modeloCarrito.addRow(new Object[] { d.getProducto().getCodigo(), d.getProducto().getNombre(),
                    d.getCantidad(), Texto.dinero(d.getPrecioUnitario()), Texto.dinero(d.getSubtotal()) });
            total += d.getSubtotal();
        }
        lblTotal.setText("Total: " + Texto.dinero(total));
    }

    private void mostrarOrdenes() {
        modeloOrdenes.setRowCount(0);
        for (OrdenCompra o : tienda.getOrdenes()) {
            modeloOrdenes.addRow(new Object[] { o.getNumero(), o.getCliente().getNombre(),
                    o.getFecha().format(Texto.FECHA), Texto.dinero(o.getTotal()), o.getEstado() });
        }
    }

    @Override
    public void refrescar() {
        cmbCliente.removeAllItems();
        for (Cliente c : tienda.getClientes()) {
            cmbCliente.addItem(c);
        }
        cmbProducto.removeAllItems();
        for (Producto p : tienda.getProductos()) {
            cmbProducto.addItem(p);
        }
        mostrarCarrito();
        mostrarOrdenes();
    }
}

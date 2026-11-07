package tienda.vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import tienda.modelo.Bodega;
import tienda.modelo.Inventario;
import tienda.modelo.ItemInventario;
import tienda.modelo.Producto;
import tienda.servicio.Tienda;

public class PanelInventario extends PanelBase {

    private final Tienda tienda;

    private final JComboBox<Bodega> cmbBodega = Ui.combo(new JComboBox<>());
    private final JComboBox<Producto> cmbProducto = Ui.combo(new JComboBox<>());
    private final JSpinner spnCantidad = Ui.spinner(new JSpinner(new SpinnerNumberModel(1, 1, 1000000, 1)));

    private final DefaultTableModel modelo = Ui.modelo("Bodega", "Código", "Producto", "Stock");

    public PanelInventario(Tienda tienda) {
        this.tienda = tienda;
        setLayout(new BorderLayout(0, 16));

        JPanel form = Ui.formulario("Agregar stock a una bodega");
        Ui.agregarFila(form, 0, "Bodega:", cmbBodega);
        Ui.agregarFila(form, 1, "Producto:", cmbProducto);
        Ui.agregarFila(form, 2, "Cantidad:", spnCantidad);

        JButton btnGuardar = Ui.boton("Registrar en inventario", true);
        btnGuardar.addActionListener(e -> guardar());
        JPanel botones = Ui.barra();
        botones.add(btnGuardar);
        Ui.agregarFila(form, 3, "", botones);

        add(form, BorderLayout.NORTH);
        add(Ui.enTarjeta(Ui.tabla(modelo, "Todavía no hay existencias registradas")), BorderLayout.CENTER);
        refrescar();
    }

    private void guardar() {
        try {
            int cantidad = (Integer) spnCantidad.getValue();
            tienda.registrarInventario((Bodega) cmbBodega.getSelectedItem(), (Producto) cmbProducto.getSelectedItem(),
                    cantidad);
            refrescar();
            Ui.info(this, "Inventario actualizado correctamente.");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    @Override
    public void refrescar() {
        cmbBodega.removeAllItems();
        for (Bodega b : tienda.getBodegas()) {
            cmbBodega.addItem(b);
        }
        cmbProducto.removeAllItems();
        for (Producto p : tienda.getProductos()) {
            cmbProducto.addItem(p);
        }

        modelo.setRowCount(0);
        for (Inventario inv : tienda.getInventarios()) {
            for (ItemInventario item : inv.getItems()) {
                modelo.addRow(new Object[] { inv.getBodega().getId(), item.getProducto().getCodigo(),
                        item.getProducto().getNombre(), item.getCantidad() });
            }
        }
    }
}

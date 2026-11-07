package tienda.vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tienda.modelo.Bodega;
import tienda.modelo.Empleado;
import tienda.servicio.Tienda;

public class PanelBodegas extends PanelBase {

    private final Tienda tienda;

    private final JTextField txtId = Ui.campo();
    private final JComboBox<Empleado> cmbEmpleado = Ui.combo(new JComboBox<>());
    private final JTextField txtCapacidad = Ui.campo();
    private final JTextField txtDireccion = Ui.campo();

    private final DefaultTableModel modelo = Ui.modelo("ID", "Encargado", "Capacidad", "Dirección");

    public PanelBodegas(Tienda tienda) {
        this.tienda = tienda;
        setLayout(new BorderLayout(0, 16));

        JPanel form = Ui.formulario("Nueva bodega");
        Ui.agregarFila(form, 0, "ID bodega:", txtId);
        Ui.agregarFila(form, 1, "Empleado encargado:", cmbEmpleado);
        Ui.agregarFila(form, 2, "Capacidad (unidades):", txtCapacidad);
        Ui.agregarFila(form, 3, "Dirección:", txtDireccion);

        JButton btnGuardar = Ui.boton("Registrar bodega", true);
        btnGuardar.addActionListener(e -> guardar());
        JButton btnLimpiar = Ui.boton("Limpiar", false);
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel botones = Ui.barra();
        botones.add(btnGuardar);
        botones.add(btnLimpiar);
        Ui.agregarFila(form, 4, "", botones);

        add(form, BorderLayout.NORTH);
        add(Ui.enTarjeta(Ui.tabla(modelo, "Todavía no hay bodegas registradas")), BorderLayout.CENTER);
        refrescar();
    }

    private void guardar() {
        try {
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            tienda.registrarBodega(txtId.getText(), (Empleado) cmbEmpleado.getSelectedItem(), capacidad,
                    txtDireccion.getText());
            limpiar();
            refrescar();
            Ui.info(this, "Bodega registrada correctamente.");
        } catch (NumberFormatException ex) {
            Ui.error(this, "La capacidad debe ser un número entero.");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtCapacidad.setText("");
        txtDireccion.setText("");
    }

    @Override
    public void refrescar() {
        cmbEmpleado.removeAllItems();
        for (Empleado e : tienda.getEmpleados()) {
            cmbEmpleado.addItem(e);
        }

        modelo.setRowCount(0);
        for (Bodega b : tienda.getBodegas()) {
            modelo.addRow(new Object[] { b.getId(), b.getEncargado().getNombre(), b.getCapacidad(), b.getDireccion() });
        }
    }
}

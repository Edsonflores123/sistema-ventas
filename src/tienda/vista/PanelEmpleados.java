package tienda.vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tienda.modelo.Empleado;
import tienda.modelo.Texto;
import tienda.servicio.Tienda;

public class PanelEmpleados extends PanelBase {

    private final Tienda tienda;

    private final JTextField txtId = Ui.campo();
    private final JTextField txtNombre = Ui.campo();
    private final JTextField txtDpi = Ui.campo();
    private final JTextField txtDireccion = Ui.campo();
    private final JTextField txtPuesto = Ui.campo();
    private final JTextField txtSalario = Ui.campo();

    private final DefaultTableModel modelo = Ui.modelo("ID", "Nombre", "DPI", "Dirección", "Puesto", "Salario");

    public PanelEmpleados(Tienda tienda) {
        this.tienda = tienda;
        setLayout(new BorderLayout(0, 16));

        JPanel form = Ui.formulario("Nuevo empleado");
        Ui.agregarFila(form, 0, "ID empleado:", txtId);
        Ui.agregarFila(form, 1, "Nombre:", txtNombre);
        Ui.agregarFila(form, 2, "DPI (13 dígitos):", txtDpi);
        Ui.agregarFila(form, 3, "Dirección:", txtDireccion);
        Ui.agregarFila(form, 4, "Puesto:", txtPuesto);
        Ui.agregarFila(form, 5, "Salario (Q):", txtSalario);

        JButton btnGuardar = Ui.boton("Registrar empleado", true);
        btnGuardar.addActionListener(e -> guardar());
        JButton btnLimpiar = Ui.boton("Limpiar", false);
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel botones = Ui.barra();
        botones.add(btnGuardar);
        botones.add(btnLimpiar);
        Ui.agregarFila(form, 6, "", botones);

        add(form, BorderLayout.NORTH);
        add(Ui.enTarjeta(Ui.tabla(modelo, "Todavía no hay empleados registrados")), BorderLayout.CENTER);
        refrescar();
    }

    private void guardar() {
        try {
            double salario = Double.parseDouble(txtSalario.getText().trim());
            tienda.registrarEmpleado(txtId.getText(), txtNombre.getText(), txtDpi.getText(),
                    txtDireccion.getText(), txtPuesto.getText(), salario);
            limpiar();
            refrescar();
            Ui.info(this, "Empleado registrado correctamente.");
        } catch (NumberFormatException ex) {
            Ui.error(this, "El salario debe ser un número, por ejemplo 3500.00");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void limpiar() {
        txtId.setText("");
        txtNombre.setText("");
        txtDpi.setText("");
        txtDireccion.setText("");
        txtPuesto.setText("");
        txtSalario.setText("");
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Empleado e : tienda.getEmpleados()) {
            modelo.addRow(new Object[] { e.getId(), e.getNombre(), e.getDpi(), e.getDireccion(), e.getPuesto(),
                    Texto.dinero(e.getSalario()) });
        }
    }
}

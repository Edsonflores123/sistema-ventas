package tienda.vista;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tienda.modelo.Cliente;
import tienda.modelo.Texto;
import tienda.modelo.TipoCliente;
import tienda.servicio.Tienda;

public class PanelClientes extends PanelBase {

    private final Tienda tienda;

    private final JTextField txtDpi = Ui.campo();
    private final JTextField txtNombre = Ui.campo();
    private final JTextField txtDireccion = Ui.campo();
    private final JTextField txtFecha = Ui.campo();
    private final JComboBox<TipoCliente> cmbTipo = Ui.combo(new JComboBox<>(TipoCliente.values()));

    private final DefaultTableModel modelo = Ui.modelo("DPI", "Nombre", "Dirección", "Nacimiento", "Tipo");

    public PanelClientes(Tienda tienda) {
        this.tienda = tienda;
        setLayout(new BorderLayout(0, 16));

        JPanel form = Ui.formulario("Nuevo cliente");
        Ui.agregarFila(form, 0, "DPI (13 dígitos):", txtDpi);
        Ui.agregarFila(form, 1, "Nombre:", txtNombre);
        Ui.agregarFila(form, 2, "Dirección:", txtDireccion);
        Ui.agregarFila(form, 3, "Nacimiento (dd/MM/aaaa):", txtFecha);
        Ui.agregarFila(form, 4, "Tipo:", cmbTipo);

        JButton btnGuardar = Ui.boton("Registrar cliente", true);
        btnGuardar.addActionListener(e -> guardar());
        JButton btnLimpiar = Ui.boton("Limpiar", false);
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel botones = Ui.barra();
        botones.add(btnGuardar);
        botones.add(btnLimpiar);
        Ui.agregarFila(form, 5, "", botones);

        add(form, BorderLayout.NORTH);
        add(Ui.enTarjeta(Ui.tabla(modelo, "Todavía no hay clientes registrados")), BorderLayout.CENTER);
        refrescar();
    }

    private void guardar() {
        try {
            LocalDate nacimiento = LocalDate.parse(txtFecha.getText().trim(), Texto.FECHA);
            tienda.registrarCliente(txtDpi.getText(), txtNombre.getText(), txtDireccion.getText(), nacimiento,
                    (TipoCliente) cmbTipo.getSelectedItem());
            limpiar();
            refrescar();
            Ui.info(this, "Cliente registrado correctamente.");
        } catch (DateTimeParseException ex) {
            Ui.error(this, "La fecha debe tener el formato dd/MM/aaaa, por ejemplo 15/03/1998");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void limpiar() {
        txtDpi.setText("");
        txtNombre.setText("");
        txtDireccion.setText("");
        txtFecha.setText("");
        cmbTipo.setSelectedIndex(0);
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Cliente c : tienda.getClientes()) {
            modelo.addRow(new Object[] { c.getDpi(), c.getNombre(), c.getDireccion(),
                    c.getFechaNacimiento().format(Texto.FECHA), c.getTipo() });
        }
    }
}

package tienda.vista;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import tienda.modelo.Producto;
import tienda.modelo.Texto;
import tienda.servicio.Tienda;

public class PanelProductos extends PanelBase {

    private final Tienda tienda;

    private final JTextField txtCodigo = Ui.campo();
    private final JTextField txtNombre = Ui.campo();
    private final JTextField txtDescripcion = Ui.campo();
    private final JTextField txtMarca = Ui.campo();
    private final JTextField txtPrecio = Ui.campo();

    private final DefaultTableModel modelo = Ui.modelo("Código", "Nombre", "Descripción", "Marca", "Precio");

    public PanelProductos(Tienda tienda) {
        this.tienda = tienda;
        setLayout(new BorderLayout(0, 16));

        JPanel form = Ui.formulario("Nuevo producto");
        Ui.agregarFila(form, 0, "Código:", txtCodigo);
        Ui.agregarFila(form, 1, "Nombre:", txtNombre);
        Ui.agregarFila(form, 2, "Descripción:", txtDescripcion);
        Ui.agregarFila(form, 3, "Marca:", txtMarca);
        Ui.agregarFila(form, 4, "Precio (Q):", txtPrecio);

        JButton btnGuardar = Ui.boton("Registrar producto", true);
        btnGuardar.addActionListener(e -> guardar());
        JButton btnLimpiar = Ui.boton("Limpiar", false);
        btnLimpiar.addActionListener(e -> limpiar());
        JPanel botones = Ui.barra();
        botones.add(btnGuardar);
        botones.add(btnLimpiar);
        Ui.agregarFila(form, 5, "", botones);

        add(form, BorderLayout.NORTH);
        add(Ui.enTarjeta(Ui.tabla(modelo, "Todavía no hay productos registrados")), BorderLayout.CENTER);
        refrescar();
    }

    private void guardar() {
        try {
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            tienda.registrarProducto(txtCodigo.getText(), txtNombre.getText(), txtDescripcion.getText(),
                    txtMarca.getText(), precio);
            limpiar();
            refrescar();
            Ui.info(this, "Producto registrado correctamente.");
        } catch (NumberFormatException ex) {
            Ui.error(this, "El precio debe ser un número, por ejemplo 25.50");
        } catch (Exception ex) {
            Ui.error(this, ex.getMessage());
        }
    }

    private void limpiar() {
        txtCodigo.setText("");
        txtNombre.setText("");
        txtDescripcion.setText("");
        txtMarca.setText("");
        txtPrecio.setText("");
    }

    @Override
    public void refrescar() {
        modelo.setRowCount(0);
        for (Producto p : tienda.getProductos()) {
            modelo.addRow(new Object[] { p.getCodigo(), p.getNombre(), p.getDescripcion(), p.getMarca(),
                    Texto.dinero(p.getPrecio()) });
        }
    }
}

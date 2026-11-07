package tienda.vista;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.KeyEvent;

import javax.swing.Box;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.table.DefaultTableModel;

public class Ui {

    private Ui() {
    }

    public static DefaultTableModel modelo(String... columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };
    }

    public static JPanel formulario(String titulo) {
        return formulario(titulo, 2);
    }

    public static JPanel formulario(String titulo, int columnas) {
        Tarjeta tarjeta = new Tarjeta(new GridBagLayout());
        tarjeta.setBorder(BorderFactory.createEmptyBorder(16, 22, 16, 22));
        tarjeta.putClientProperty("columnas", columnas);

        JLabel etiqueta = new JLabel(titulo);
        etiqueta.setFont(Tema.fuente(Font.BOLD, 15));
        etiqueta.setForeground(Tema.TEXTO);

        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = columnas;
        g.anchor = GridBagConstraints.WEST;
        g.insets = new Insets(0, 0, 12, 0);
        tarjeta.add(etiqueta, g);
        return tarjeta;
    }

    
    public static void agregarFila(JPanel panel, int fila, String etiqueta, JComponent campo) {
        Object valor = panel.getClientProperty("columnas");
        int columnas = valor instanceof Integer ? (Integer) valor : 2;

        GridBagConstraints g = new GridBagConstraints();
        g.fill = GridBagConstraints.HORIZONTAL;
        g.anchor = GridBagConstraints.NORTHWEST;
        g.weightx = 1;

        if (etiqueta.isEmpty()) {
            g.gridx = 0;
            g.gridy = 1 + (fila + columnas - 1) / columnas;
            g.gridwidth = columnas;
            g.insets = new Insets(4, 0, 0, 0);
            panel.add(campo, g);
            return;
        }

        int col = fila % columnas;
        g.gridx = col;
        g.gridy = 1 + fila / columnas;
        g.insets = new Insets(0, col == 0 ? 0 : 8, 12, col == columnas - 1 ? 0 : 8);

        String texto = etiqueta.endsWith(":") ? etiqueta.substring(0, etiqueta.length() - 1) : etiqueta;
        JLabel lbl = new JLabel(texto);
        lbl.setFont(Tema.fuente(Font.PLAIN, 12));
        lbl.setForeground(Tema.TEXTO_SUAVE);

        JPanel celda = new JPanel(new BorderLayout(0, 5));
        celda.setOpaque(false);
        celda.add(lbl, BorderLayout.NORTH);
        celda.add(campo, BorderLayout.CENTER);
        panel.add(celda, g);
    }


    public static JTextField campo() {
        JTextField t = new JTextField();
        t.setFont(Tema.fuente(Font.PLAIN, 13));
        t.setForeground(Tema.TEXTO);
        t.setBackground(Tema.SUPERFICIE);
        t.setCaretColor(Tema.TEXTO);
        t.setSelectionColor(Tema.SELECCION);
        t.setSelectedTextColor(Tema.TEXTO);
        t.setBorder(new Campos.Borde(8, 10));
        t.setPreferredSize(new Dimension(120, Tema.ALTO_CAMPO));
        Campos.repintarConFoco(t, t);
        return t;
    }

    public static <T> JComboBox<T> combo(JComboBox<T> combo) {
        combo.setUI(new Campos.ComboUI());
        combo.setFont(Tema.fuente(Font.PLAIN, 13));
        combo.setForeground(Tema.TEXTO);
        combo.setBackground(Tema.SUPERFICIE);
        combo.setBorder(new Campos.Borde(0, 0));
        combo.setRenderer(new Campos.RenderOpcion());
        combo.setMaximumRowCount(8);
        combo.setPreferredSize(new Dimension(120, Tema.ALTO_CAMPO));
        Campos.repintarConFoco(combo, combo);
        return combo;
    }

    public static JSpinner spinner(JSpinner spinner) {
        spinner.setUI(new Campos.SpinnerUI());
        spinner.setFont(Tema.fuente(Font.PLAIN, 13));
        spinner.setBackground(Tema.SUPERFICIE);
        spinner.setBorder(new Campos.Borde(0, 0));

        JComponent editor = spinner.getEditor();
        editor.setOpaque(false);
        editor.setBorder(null);
        if (editor instanceof JSpinner.DefaultEditor) {
            JTextField texto = ((JSpinner.DefaultEditor) editor).getTextField();
            texto.setFont(Tema.fuente(Font.PLAIN, 13));
            texto.setForeground(Tema.TEXTO);
            texto.setBackground(Tema.SUPERFICIE);
            texto.setCaretColor(Tema.TEXTO);
            texto.setSelectionColor(Tema.SELECCION);
            texto.setSelectedTextColor(Tema.TEXTO);
            texto.setHorizontalAlignment(SwingConstants.LEFT);
            texto.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 4));
            Campos.repintarConFoco(texto, spinner);
        }
        spinner.setPreferredSize(new Dimension(120, Tema.ALTO_CAMPO));
        return spinner;
    }


    public static JButton boton(String texto, boolean principal) {
        return new Boton(texto, principal);
    }

    public static JPanel barra() {
        JPanel barra = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)) {
            @Override
            protected void addImpl(Component comp, Object restricciones, int indice) {
                if (getComponentCount() > 0) {
                    super.addImpl(Box.createHorizontalStrut(8), null, -1);
                }
                super.addImpl(comp, restricciones, indice);
            }
        };
        barra.setOpaque(false);
        return barra;
    }

    public static JLabel subtitulo(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(Tema.fuente(Font.BOLD, 15));
        l.setForeground(Tema.TEXTO);
        l.setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 0));
        return l;
    }

    public static void destacar(JLabel etiqueta) {
        etiqueta.setFont(Tema.fuente(Font.BOLD, 15));
        etiqueta.setForeground(Tema.TEXTO);
    }


    public static JTable tabla(DefaultTableModel modelo, String textoVacio) {
        return TablaModerna.crear(modelo, textoVacio);
    }

    public static JComponent enTarjeta(JTable tabla) {
        return TablaModerna.enTarjeta(tabla);
    }

    public static void divisor(JSplitPane division) {
        division.setUI(new BasicSplitPaneUI() {
            @Override
            public BasicSplitPaneDivider createDefaultDivider() {
                return new BasicSplitPaneDivider(this) {
                    @Override
                    public void paint(Graphics g) {
                    }
                };
            }
        });
        division.setBorder(null);
        division.setDividerSize(16);
        division.setContinuousLayout(true);
        division.setOpaque(false);
    }


    public static void error(Component padre, String mensaje) {
        dialogo(padre, "Error", mensaje, new java.awt.Color(0x94503F));
    }

    public static void info(Component padre, String mensaje) {
        dialogo(padre, "Listo", mensaje, Tema.TEXTO);
    }

    private static void dialogo(Component padre, String titulo, String mensaje, java.awt.Color colorTitulo) {
        Window ventana = padre == null ? null : SwingUtilities.getWindowAncestor(padre);
        JDialog d = new JDialog(ventana, titulo, Dialog.ModalityType.APPLICATION_MODAL);

        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(Tema.fuente(Font.BOLD, 15));
        lblTitulo.setForeground(colorTitulo);

        String texto = String.valueOf(mensaje).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
                .replace("\n", "<br>");
        JLabel lblMensaje = new JLabel("<html><body style='width: 300px'>" + texto + "</body></html>");
        lblMensaje.setFont(Tema.fuente(Font.PLAIN, 13));
        lblMensaje.setForeground(Tema.TEXTO);

        JButton aceptar = boton("Aceptar", true);
        aceptar.addActionListener(e -> d.dispose());
        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        pie.setOpaque(false);
        pie.add(aceptar);

        JPanel contenido = new JPanel(new BorderLayout(0, 10));
        contenido.setBackground(Tema.SUPERFICIE);
        contenido.setBorder(BorderFactory.createEmptyBorder(22, 26, 18, 26));
        contenido.add(lblTitulo, BorderLayout.NORTH);
        contenido.add(lblMensaje, BorderLayout.CENTER);
        JPanel abajo = new JPanel(new BorderLayout());
        abajo.setOpaque(false);
        abajo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        abajo.add(pie, BorderLayout.CENTER);
        contenido.add(abajo, BorderLayout.SOUTH);

        d.setContentPane(contenido);
        d.getRootPane().setDefaultButton(aceptar);
        d.getRootPane().registerKeyboardAction(e -> d.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
                JComponent.WHEN_IN_FOCUSED_WINDOW);
        d.setResizable(false);
        d.pack();
        d.setLocationRelativeTo(padre);
        d.setVisible(true);
    }
}

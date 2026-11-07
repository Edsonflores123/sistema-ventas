package tienda.vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.plaf.basic.BasicScrollBarUI;

import java.awt.BorderLayout;

final class TablaModerna {

    private static final Set<String> NUMERICAS = Set.of("Precio", "Salario", "Total", "Subtotal", "Capacidad",
            "Stock", "Cantidad");

    private TablaModerna() {
    }

    private static class Tabla extends JTable {

        private final String vacio;

        Tabla(DefaultTableModel modelo, String vacio) {
            super(modelo);
            this.vacio = vacio;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (getRowCount() == 0 && vacio != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setFont(Tema.fuente(Font.PLAIN, 13));
                g2.setColor(Tema.TEXTO_SUAVE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(vacio)) / 2;
                int y = Math.min(getHeight() / 2, 70) + fm.getAscent() / 2;
                g2.drawString(vacio, x, y);
                g2.dispose();
            }
        }
    }

    static JTable crear(DefaultTableModel modelo, String vacio) {
        JTable tabla = new Tabla(modelo, vacio);
        tabla.setFont(Tema.fuente(Font.PLAIN, 13));
        tabla.setForeground(Tema.TEXTO);
        tabla.setBackground(Tema.SUPERFICIE);
        tabla.setRowHeight(34);
        tabla.setShowHorizontalLines(true);
        tabla.setShowVerticalLines(false);
        tabla.setIntercellSpacing(new Dimension(0, 1));
        tabla.setGridColor(Tema.LINEA);
        tabla.setSelectionBackground(Tema.SELECCION);
        tabla.setSelectionForeground(Tema.TEXTO);
        tabla.setFillsViewportHeight(true);
        tabla.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        tabla.setBorder(null);
        tabla.setPreferredScrollableViewportSize(new Dimension(400, 110));

        tabla.setDefaultRenderer(Object.class, new RenderCelda());
        for (int i = 0; i < tabla.getColumnCount(); i++) {
            if ("Estado".equals(tabla.getColumnName(i))) {
                tabla.getColumnModel().getColumn(i).setCellRenderer(new RenderEstado());
            }
        }

        JTableHeader cabecera = tabla.getTableHeader();
        cabecera.setReorderingAllowed(false);
        cabecera.setDefaultRenderer(new RenderCabecera());
        cabecera.setPreferredSize(new Dimension(0, 36));
        return tabla;
    }

    static JComponent enTarjeta(JTable tabla) {
        JScrollPane scroll = new JScrollPane(tabla);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.setBackground(Tema.SUPERFICIE);
        scroll.getViewport().setBackground(Tema.SUPERFICIE);

        scroll.getVerticalScrollBar().setUI(new BarraUI());
        scroll.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
        scroll.getVerticalScrollBar().setBackground(Tema.SUPERFICIE);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.getHorizontalScrollBar().setUI(new BarraUI());
        scroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 10));
        scroll.getHorizontalScrollBar().setBackground(Tema.SUPERFICIE);

        JPanel esquina = new JPanel();
        esquina.setBackground(Tema.CABECERA);
        scroll.setCorner(JScrollPane.UPPER_RIGHT_CORNER, esquina);

        Tarjeta tarjeta = new Tarjeta(new BorderLayout());
        tarjeta.add(scroll, BorderLayout.CENTER);
        return tarjeta;
    }

    private static boolean esNumerica(JTable tabla, int columna) {
        return NUMERICAS.contains(tabla.getColumnName(columna));
    }

    private static class RenderCabecera extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean sel, boolean foco,
                int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, false, false, fila, columna);
            setFont(Tema.fuente(Font.BOLD, 12));
            setForeground(Tema.TEXTO_SUAVE);
            setBackground(Tema.CABECERA);
            setOpaque(true);
            setHorizontalAlignment(esNumerica(tabla, columna) ? SwingConstants.RIGHT : SwingConstants.LEFT);
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, Tema.BORDE),
                    BorderFactory.createEmptyBorder(0, 14, 0, 14)));
            return this;
        }
    }

    private static class RenderCelda extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean sel, boolean foco,
                int fila, int columna) {
            super.getTableCellRendererComponent(tabla, valor, sel, false, fila, columna);
            setForeground(Tema.TEXTO);
            setBackground(sel ? Tema.SELECCION : Tema.SUPERFICIE);
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
            setHorizontalAlignment(esNumerica(tabla, columna) ? SwingConstants.RIGHT : SwingConstants.LEFT);
            return this;
        }
    }

    private static class RenderEstado extends JLabel implements TableCellRenderer {

        private boolean seleccionada;
        private Color fondoEtiqueta = Tema.HOVER;
        private Color textoEtiqueta = Tema.TEXTO;

        RenderEstado() {
            setOpaque(false);
        }

        @Override
        public Component getTableCellRendererComponent(JTable tabla, Object valor, boolean sel, boolean foco,
                int fila, int columna) {
            String texto = String.valueOf(valor);
            setText(texto);
            seleccionada = sel;
            switch (texto) {
                case "PENDIENTE":
                    fondoEtiqueta = new Color(0xF0E3C2);
                    textoEtiqueta = new Color(0x84601F);
                    break;
                case "COMPLETADA":
                    fondoEtiqueta = new Color(0xDEE7D2);
                    textoEtiqueta = new Color(0x4D6B41);
                    break;
                case "CANCELADA":
                    fondoEtiqueta = new Color(0xEFD9D1);
                    textoEtiqueta = new Color(0x8F4E3E);
                    break;
                default:
                    fondoEtiqueta = Tema.HOVER;
                    textoEtiqueta = Tema.TEXTO;
            }
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setColor(seleccionada ? Tema.SELECCION : Tema.SUPERFICIE);
            g2.fillRect(0, 0, getWidth(), getHeight());

            Font fuente = Tema.fuente(Font.BOLD, 11);
            g2.setFont(fuente);
            FontMetrics fm = g2.getFontMetrics();
            int ancho = fm.stringWidth(getText()) + 20;
            int alto = 22;
            int x = 14;
            int y = (getHeight() - alto) / 2;
            g2.setColor(fondoEtiqueta);
            g2.fillRoundRect(x, y, ancho, alto, alto, alto);
            g2.setColor(textoEtiqueta);
            g2.drawString(getText(), x + 10, y + (alto - fm.getHeight()) / 2 + fm.getAscent());
            g2.dispose();
        }
    }

    static class BarraUI extends BasicScrollBarUI {

        @Override
        protected JButton createDecreaseButton(int orientacion) {
            return botonVacio();
        }

        @Override
        protected JButton createIncreaseButton(int orientacion) {
            return botonVacio();
        }

        private JButton botonVacio() {
            JButton b = new JButton();
            Dimension cero = new Dimension(0, 0);
            b.setPreferredSize(cero);
            b.setMinimumSize(cero);
            b.setMaximumSize(cero);
            return b;
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle r) {
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
            if (r.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(isThumbRollover() || isDragging ? Tema.TEXTO_SUAVE : Tema.BORDE_CAMPO);
            g2.fillRoundRect(r.x + 2, r.y + 2, r.width - 4, r.height - 4, 8, 8);
            g2.dispose();
        }
    }
}

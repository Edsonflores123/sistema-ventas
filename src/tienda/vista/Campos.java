package tienda.vista;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.BasicSpinnerUI;
import javax.swing.plaf.basic.ComboPopup;

final class Campos {

    private Campos() {
    }

    static class Borde extends AbstractBorder {

        private final int vertical;
        private final int horizontal;

        Borde(int vertical, int horizontal) {
            this.vertical = vertical;
            this.horizontal = horizontal;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int ancho, int alto) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(tieneFoco(c) ? Tema.ACENTO : Tema.BORDE_CAMPO);
            g2.drawRoundRect(x, y, ancho - 1, alto - 1, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(vertical + 1, horizontal + 1, vertical + 1, horizontal + 1);
        }

        @Override
        public Insets getBorderInsets(Component c, Insets insets) {
            insets.set(vertical + 1, horizontal + 1, vertical + 1, horizontal + 1);
            return insets;
        }

        private static boolean tieneFoco(Component c) {
            Component foco = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
            return foco != null && SwingUtilities.isDescendingFrom(foco, c);
        }
    }

    static void repintarConFoco(Component conFoco, JComponent aRepintar) {
        conFoco.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                aRepintar.repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                aRepintar.repaint();
            }
        });
    }

    static class Flecha extends JButton {

        private final int direccion;
        private final int tamano;

        Flecha(int direccion, int tamano, int ancho, int alto) {
            this.direccion = direccion;
            this.tamano = tamano;
            setBorder(null);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setFocusable(false);
            setOpaque(false);
            setPreferredSize(new Dimension(ancho, alto));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
            g2.setColor(getModel().isRollover() ? Tema.TEXTO : Tema.TEXTO_SUAVE);
            g2.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int cx = getWidth() / 2;
            int cy = getHeight() / 2;
            int s = tamano;
            if (direccion == SwingConstants.SOUTH) {
                g2.drawPolyline(new int[] { cx - s, cx, cx + s }, new int[] { cy - s / 2, cy + s / 2, cy - s / 2 }, 3);
            } else {
                g2.drawPolyline(new int[] { cx - s, cx, cx + s }, new int[] { cy + s / 2, cy - s / 2, cy + s / 2 }, 3);
            }
            g2.dispose();
        }
    }

    static class ComboUI extends BasicComboBoxUI {

        @Override
        protected JButton createArrowButton() {
            return new Flecha(SwingConstants.SOUTH, 4, 30, 10);
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle r, boolean foco) {
            g.setColor(Tema.SUPERFICIE);
            g.fillRect(r.x, r.y, r.width, r.height);
        }

        @SuppressWarnings({ "rawtypes", "unchecked" })
        @Override
        public void paintCurrentValue(Graphics g, Rectangle r, boolean foco) {
            ListCellRenderer render = comboBox.getRenderer();
            Component c = render.getListCellRendererComponent(listBox, comboBox.getSelectedItem(), -1, false, false);
            c.setFont(comboBox.getFont());
            currentValuePane.paintComponent(g, c, comboBox, r.x, r.y, r.width, r.height, c instanceof JPanel);
        }

        @Override
        protected ComboPopup createPopup() {
            BasicComboPopup popup = new BasicComboPopup(comboBox) {
                @Override
                protected JScrollPane createScroller() {
                    JScrollPane s = super.createScroller();
                    s.setBorder(null);
                    s.getVerticalScrollBar().setUI(new TablaModerna.BarraUI());
                    s.getVerticalScrollBar().setPreferredSize(new Dimension(10, 0));
                    s.getVerticalScrollBar().setBackground(Tema.SUPERFICIE);
                    return s;
                }
            };
            popup.setBorder(BorderFactory.createLineBorder(Tema.BORDE_CAMPO));
            return popup;
        }
    }

    static class RenderOpcion extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object valor, int indice, boolean seleccionada,
                boolean foco) {
            super.getListCellRendererComponent(list, valor, indice, false, false);
            setBorder(BorderFactory.createEmptyBorder(7, 11, 7, 11));
            setForeground(Tema.TEXTO);
            setBackground(seleccionada ? Tema.SELECCION : Tema.SUPERFICIE);
            setOpaque(true);
            return this;
        }
    }

    static class SpinnerUI extends BasicSpinnerUI {

        @Override
        protected Component createNextButton() {
            Flecha b = new Flecha(SwingConstants.NORTH, 3, 24, 10);
            b.setName("Spinner.nextButton");
            installNextButtonListeners(b);
            return b;
        }

        @Override
        protected Component createPreviousButton() {
            Flecha b = new Flecha(SwingConstants.SOUTH, 3, 24, 10);
            b.setName("Spinner.previousButton");
            installPreviousButtonListeners(b);
            return b;
        }
    }
}

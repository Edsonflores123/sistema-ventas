package tienda.vista;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BarraLateral extends JPanel {

    private final IntConsumer alElegir;
    private final List<Item> items = new ArrayList<>();

    public BarraLateral(IntConsumer alElegir) {
        this.alElegir = alElegir;
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Tema.LATERAL);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Tema.BORDE),
                BorderFactory.createEmptyBorder(28, 14, 20, 14)));
        setPreferredSize(new Dimension(228, 0));

        JLabel nombre = new JLabel("Ventas e Inventario");
        nombre.setFont(Tema.fuente(Font.BOLD, 16));
        nombre.setForeground(Tema.TEXTO);
        nombre.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        nombre.setAlignmentX(LEFT_ALIGNMENT);

        JLabel detalle = new JLabel("Panel de gestión");
        detalle.setFont(Tema.fuente(Font.PLAIN, 12));
        detalle.setForeground(Tema.TEXTO_SUAVE);
        detalle.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 0));
        detalle.setAlignmentX(LEFT_ALIGNMENT);

        add(nombre);
        add(Box.createVerticalStrut(3));
        add(detalle);
        add(Box.createVerticalStrut(18));
    }

    public void grupo(String titulo) {
        JLabel l = new JLabel(titulo.toUpperCase());
        l.setFont(Tema.fuente(Font.BOLD, 11));
        l.setForeground(Tema.TEXTO_SUAVE);
        l.setBorder(BorderFactory.createEmptyBorder(14, 12, 6, 0));
        l.setAlignmentX(LEFT_ALIGNMENT);
        add(l);
    }

    public void agregar(String titulo) {
        Item item = new Item(titulo, items.size());
        items.add(item);
        add(item);
        add(Box.createVerticalStrut(2));
    }

    public void seleccionar(int indice) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).activo = (i == indice);
        }
        repaint();
    }

    private class Item extends JButton {

        private boolean activo;

        Item(String titulo, int indice) {
            super(titulo);
            setFont(Tema.fuente(Font.PLAIN, 13));
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setOpaque(false);
            setRolloverEnabled(true);
            setAlignmentX(LEFT_ALIGNMENT);
            setPreferredSize(new Dimension(100, 38));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            addActionListener(e -> alElegir.accept(indice));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth() - 1;
            int h = getHeight() - 1;
            if (activo) {
                g2.setColor(Tema.SUPERFICIE);
                g2.fillRoundRect(0, 0, w, h, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
                g2.setColor(Tema.BORDE);
                g2.drawRoundRect(0, 0, w, h, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
            } else if (getModel().isRollover()) {
                g2.setColor(Tema.HOVER);
                g2.fillRoundRect(0, 0, w, h, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
            }

            g2.setFont(Tema.fuente(activo ? Font.BOLD : Font.PLAIN, 13));
            g2.setColor(activo ? Tema.TEXTO : Tema.ACENTO);
            FontMetrics fm = g2.getFontMetrics();
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(getText(), 12, y);
            g2.dispose();
        }
    }
}

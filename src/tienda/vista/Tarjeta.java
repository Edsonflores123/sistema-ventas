package tienda.vista;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Tarjeta extends JPanel {

    public Tarjeta(LayoutManager layout) {
        super(layout);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Tema.SUPERFICIE);
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Tema.ARCO_TARJETA, Tema.ARCO_TARJETA);
        g2.dispose();
    }

    @Override
    protected void paintChildren(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.clip(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, Tema.ARCO_TARJETA,
                Tema.ARCO_TARJETA));
        super.paintChildren(g2);
        g2.dispose();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Tema.BORDE);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, Tema.ARCO_TARJETA, Tema.ARCO_TARJETA);
        g2.dispose();
    }
}

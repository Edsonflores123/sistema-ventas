package tienda.vista;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class Boton extends JButton {

    private final boolean principal;

    public Boton(String texto, boolean principal) {
        super(texto);
        this.principal = principal;
        setFont(Tema.fuente(Font.BOLD, 13));
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setOpaque(false);
        setRolloverEnabled(true);
        setMargin(new Insets(0, 0, 0, 0));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    @Override
    public Dimension getPreferredSize() {
        FontMetrics fm = getFontMetrics(getFont());
        return new Dimension(fm.stringWidth(getText()) + 36, 36);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        boolean encima = getModel().isRollover();
        boolean presionado = getModel().isArmed() && getModel().isPressed();
        int w = getWidth() - 1;
        int h = getHeight() - 1;

        if (principal) {
            g2.setColor(!isEnabled() ? Tema.BORDE : (presionado || encima) ? Tema.ACENTO_HOVER : Tema.ACENTO);
            g2.fillRoundRect(0, 0, w, h, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
            g2.setColor(isEnabled() ? Tema.SUPERFICIE : Tema.TEXTO_SUAVE);
        } else {
            g2.setColor(presionado ? Tema.BORDE : encima ? Tema.HOVER : Tema.SUPERFICIE);
            g2.fillRoundRect(0, 0, w, h, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
            g2.setColor(Tema.BORDE_CAMPO);
            g2.drawRoundRect(0, 0, w, h, Tema.ARCO_CONTROL, Tema.ARCO_CONTROL);
            g2.setColor(isEnabled() ? Tema.TEXTO : Tema.TEXTO_SUAVE);
        }

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}

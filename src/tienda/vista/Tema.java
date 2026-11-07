package tienda.vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

public final class Tema {

    public static final Color FONDO = new Color(0xF4EFE7);
    public static final Color LATERAL = new Color(0xEBE4D7);
    public static final Color SUPERFICIE = new Color(0xFBF9F5);
    public static final Color CABECERA = new Color(0xF3EEE4);
    public static final Color BORDE = new Color(0xDCD2C1);
    public static final Color BORDE_CAMPO = new Color(0xD0C5B1);
    public static final Color LINEA = new Color(0xECE5D9);
    public static final Color HOVER = new Color(0xE2D9C9);
    public static final Color SELECCION = new Color(0xEBE0CC);
    public static final Color TEXTO = new Color(0x2F2A25);
    public static final Color TEXTO_SUAVE = new Color(0x7B7062);
    public static final Color ACENTO = new Color(0x4A3F35);
    public static final Color ACENTO_HOVER = new Color(0x382F27);

    public static final int ARCO_TARJETA = 16;
    public static final int ARCO_CONTROL = 12;
    public static final int ALTO_CAMPO = 36;

    private static String familia;

    private Tema() {
    }

    public static Font fuente(int estilo, int tamano) {
        if (familia == null) {
            familia = elegirFamilia();
        }
        return new Font(familia, estilo, tamano);
    }

    private static String elegirFamilia() {
        Set<String> disponibles = new HashSet<>(Arrays.asList(
                GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()));
        String[] preferidas = { "Segoe UI", "SF Pro Text", "Helvetica Neue", "Inter", "Noto Sans", "Ubuntu",
                "DejaVu Sans" };
        for (String f : preferidas) {
            if (disponibles.contains(f)) {
                return f;
            }
        }
        return Font.SANS_SERIF;
    }

    public static void instalar() {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        try {
            UIManager.put("swing.boldMetal", Boolean.FALSE);
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
        }

        for (Object clave : Collections.list(UIManager.getDefaults().keys())) {
            Object valor = UIManager.get(clave);
            if (valor instanceof Font) {
                UIManager.put(clave, new FontUIResource(fuente(((Font) valor).getStyle(), 13)));
            }
        }

        UIManager.put("Panel.background", new ColorUIResource(FONDO));
        UIManager.put("Viewport.background", new ColorUIResource(SUPERFICIE));
        UIManager.put("Label.foreground", new ColorUIResource(TEXTO));
        UIManager.put("OptionPane.background", new ColorUIResource(SUPERFICIE));
        UIManager.put("OptionPane.messageForeground", new ColorUIResource(TEXTO));
        UIManager.put("ToolTip.background", new ColorUIResource(SUPERFICIE));
        UIManager.put("ToolTip.foreground", new ColorUIResource(TEXTO));
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(BORDE));
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        UIManager.put("Table.scrollPaneBorder", BorderFactory.createEmptyBorder());
        UIManager.put("TextField.selectionBackground", new ColorUIResource(SELECCION));
        UIManager.put("TextField.selectionForeground", new ColorUIResource(TEXTO));
        UIManager.put("TextField.caretForeground", new ColorUIResource(TEXTO));
        UIManager.put("FormattedTextField.selectionBackground", new ColorUIResource(SELECCION));
        UIManager.put("FormattedTextField.selectionForeground", new ColorUIResource(TEXTO));
        UIManager.put("FormattedTextField.caretForeground", new ColorUIResource(TEXTO));
    }
}

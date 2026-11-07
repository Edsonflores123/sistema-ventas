package tienda.vista;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import tienda.servicio.Tienda;

public class VentanaPrincipal extends JFrame {

    private static final String[] TITULOS = { "Productos", "Clientes", "Empleados", "Bodegas", "Inventario",
            "Órdenes de compra" };
    private static final String[] DETALLES = {
            "Catálogo de artículos disponibles para la venta",
            "Personas registradas como compradoras",
            "Personal de la tienda y su puesto",
            "Espacios de almacenamiento y su capacidad",
            "Existencias de cada producto por bodega",
            "Arma ventas y controla su estado" };

    private final CardLayout cartas = new CardLayout();
    private final JPanel contenido = new JPanel(cartas);
    private final BarraLateral lateral = new BarraLateral(this::mostrar);
    private final JLabel lblTitulo = new JLabel();
    private final JLabel lblDetalle = new JLabel();
    private final PanelBase[] paneles;

    public VentanaPrincipal(Tienda tienda) {
        super("Sistema de Ventas e Inventario");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1120, 740);
        setMinimumSize(new Dimension(980, 660));
        setLocationRelativeTo(null);

        paneles = new PanelBase[] {
                new PanelProductos(tienda),
                new PanelClientes(tienda),
                new PanelEmpleados(tienda),
                new PanelBodegas(tienda),
                new PanelInventario(tienda),
                new PanelOrdenes(tienda)
        };

        contenido.setOpaque(false);
        for (int i = 0; i < paneles.length; i++) {
            contenido.add(paneles[i], TITULOS[i]);
        }

        lateral.grupo("Catálogo");
        lateral.agregar(TITULOS[0]);
        lateral.agregar(TITULOS[1]);
        lateral.agregar(TITULOS[2]);
        lateral.grupo("Almacén");
        lateral.agregar(TITULOS[3]);
        lateral.agregar(TITULOS[4]);
        lateral.grupo("Ventas");
        lateral.agregar(TITULOS[5]);

        lblTitulo.setFont(Tema.fuente(Font.BOLD, 22));
        lblTitulo.setForeground(Tema.TEXTO);
        lblDetalle.setFont(Tema.fuente(Font.PLAIN, 13));
        lblDetalle.setForeground(Tema.TEXTO_SUAVE);
        JPanel encabezado = new JPanel(new BorderLayout(0, 4));
        encabezado.setOpaque(false);
        encabezado.add(lblTitulo, BorderLayout.NORTH);
        encabezado.add(lblDetalle, BorderLayout.CENTER);

        JPanel derecha = new JPanel(new BorderLayout(0, 16));
        derecha.setBackground(Tema.FONDO);
        derecha.setBorder(BorderFactory.createEmptyBorder(22, 32, 22, 32));
        derecha.add(encabezado, BorderLayout.NORTH);
        derecha.add(contenido, BorderLayout.CENTER);

        getContentPane().setBackground(Tema.FONDO);
        add(lateral, BorderLayout.WEST);
        add(derecha, BorderLayout.CENTER);

        mostrar(0);
    }

    private void mostrar(int indice) {
        cartas.show(contenido, TITULOS[indice]);
        paneles[indice].refrescar();
        lateral.seleccionar(indice);
        lblTitulo.setText(TITULOS[indice]);
        lblDetalle.setText(DETALLES[indice]);
    }
}

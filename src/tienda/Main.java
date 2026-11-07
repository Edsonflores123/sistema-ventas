package tienda;

import javax.swing.SwingUtilities;

import tienda.persistencia.Almacen;
import tienda.servicio.Tienda;
import tienda.vista.Tema;
import tienda.vista.VentanaPrincipal;

public class Main {

    public static void main(String[] args) {
        Tema.instalar();
        SwingUtilities.invokeLater(() -> {
            Almacen almacen = new Almacen("datos");
            new VentanaPrincipal(new Tienda(almacen)).setVisible(true);
        });
    }
}

package tienda.modelo;

import java.time.format.DateTimeFormatter;

public class Texto {

    public static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private Texto() {
    }


    public static String limpiar(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("|", " ").replace("\n", " ").replace("\r", " ").trim();
    }

    public static String limpiarCodigo(String s) {
        return limpiar(s).replace(":", "").replace(";", "").replace(" ", "");
    }

    public static String dinero(double valor) {
        return String.format("Q%.2f", valor);
    }
}

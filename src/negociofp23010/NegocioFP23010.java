package negociofp23010;

public class NegocioFP23010 {

    public static void main(String[] args) {

        // Filtrar mensajes de KDE que no son errores de la aplicación
        System.setErr(new java.io.PrintStream(System.err) {
            @Override
            public void println(String x) {
                if (x != null && (x.contains("kf.config") || x.contains("KConfigIni") || x.contains("color-schemes"))) {
                    return; // ignorar silenciosamente
                }
                super.println(x);
            }
        });
    }

}

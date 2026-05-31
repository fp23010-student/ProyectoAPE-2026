package negociofp23010;

import Formularios.frmLogin;

public class NegocioFP23010 {

    public static void main(String[] args) {
        new frmLogin().setVisible(true);
        // Filtrar mensajes de KDE que no son errores de la aplicación
        System.setErr(new java.io.PrintStream(System.err) {
            @Override
            public void println(String x) {
                if (x != null && (x.contains("kf.config") || x.contains("KConfigIni") || x.contains("color-schemes"))) {
                    return; 
                }
                super.println(x);
                
            }
        });
    }
    
}

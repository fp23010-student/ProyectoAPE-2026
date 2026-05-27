package Clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class Conexion {

    private static final String URL = "jdbc:mariadb://localhost:3306/negocio";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "";
    private static Connection conexion = null;

    //Metodo para conectar a la base de datos
    public static Connection conectar() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");

            if (conexion == null || conexion.isClosed()) {
                conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "ERROR DE CONEXIÓN");
            // System.err.println("ERROR DE CONEXIÓN: " + e.getMessage());
            return null;
        }
        return conexion;
    }

    //Metodo para desconectar de la base de datos
    public static void desconectar() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                JOptionPane.showMessageDialog(null, "? Conexión cerrada correctamente.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "? Error al cerrar la conexión: " + e.getMessage());
        }
    }

    // Conexión independiente para transacciones (OrdenService)
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver no encontrado: " + e.getMessage());
        }
    }

}

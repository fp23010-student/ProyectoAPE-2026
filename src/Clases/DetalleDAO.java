package Clases;

import Clases.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import javax.swing.JOptionPane;

public class DetalleDAO {

    public void guardarDetalle(int idCombo, List<Detalle> listaDetalle) {
        String sql = "INSERT INTO detalle (idCombo, idProducto, cantidad) VALUES (?, ?, ?)";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            for (Detalle elemento : listaDetalle) {
                ps.setInt(1, idCombo);
                ps.setInt(2, elemento.getProductos().getIdProducto());
                ps.setInt(3, elemento.getCantidad());
                ps.executeUpdate();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
}

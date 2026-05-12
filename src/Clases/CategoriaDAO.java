package Clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class CategoriaDAO {

    //Metodo para mandar a traer las categorias y setearlas al cmb del formulario
    public List<Categoria> listar() {
        List<Categoria> lista = new ArrayList<>();
        String sql = "SELECT idCategoria, descripcion FROM categoria";
        Connection con = Conexion.conectar();
        if (con == null) {
            return lista;
        }
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                Categoria cat = new Categoria(
                        String.valueOf(rs.getInt("idCategoria")),
                        rs.getString("descripcion")
                );
                lista.add(cat);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al cargar categorías de la base de datos: " + e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException ex) {
                System.err.println("Error al cerrar la conexión: " + ex);
            }
        }
        return lista;
    }
    
    
}

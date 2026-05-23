package Clases;

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class ProductoDAO {

    //Metodo para insertar los datos a la tabla producto
    public boolean insertar(Producto p) {

        String sql = "INSERT INTO producto (idProducto, nombre, precio, idCategoria, descripcion) VALUES (?, ?, ?, ?, ?)";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, p.getIdProducto());
            ps.setString(2, p.getNombreProducto());
            ps.setDouble(3, Double.parseDouble(p.getPrecioProducto()));
            ps.setInt(4, Integer.parseInt(p.getIdCategoria()));
            ps.setString(5, p.getDescripcionProducto());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    //Metodo para generar el IDProducto automaticamente
    public int generarIdAutomatico() {
        String sql = "SELECT MAX(idProducto) FROM producto";
        Connection con = Conexion.conectar();
        try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            System.err.println("Error al generar ID: " + e.getMessage());
        }
        return 1;
    }

    //Metodo para eliminar un producto de la tabla
    public boolean eliminar(int id) {
        String sql = "DELETE FROM producto WHERE idProducto = ?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            int filasAfec = ps.executeUpdate();
            return filasAfec > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar: " + e.getMessage());
            return false;
        }
    }

    //Metodo para buscar un producto por su ID
    public Producto buscar(int id) {
        String sql = "SELECT * FROM producto WHERE idProducto = ?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombreProducto(rs.getString("nombre"));
                p.setPrecioProducto(String.valueOf(rs.getDouble("precio")));
                p.setIdCategoria(String.valueOf(rs.getInt("idCategoria")));
                p.setDescripcionProducto(rs.getString("descripcion"));
                return p;
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar: " + e.getMessage());
        }
        return null;
    }

    //Metodo para hacer update a un producto en la tabla
    public boolean actualizar(Producto p) {
        String sql = "UPDATE producto SET nombre=?, precio=?, idCategoria=?, descripcion=? WHERE idProducto=?";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, p.getNombreProducto());
            ps.setDouble(2, Double.parseDouble(p.getPrecioProducto()));
            ps.setInt(3, Integer.parseInt(p.getIdCategoria()));
            ps.setString(4, p.getDescripcionProducto());
            ps.setInt(5, p.getIdProducto());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    //Metodo para listar los productos en la area de texto del formulario
    public List<Producto> listar() {
        List<Producto> lista = new java.util.ArrayList<>();
        String sql = "SELECT p.idProducto, p.nombre, p.precio, p.idCategoria, "
                + "p.descripcion AS desc_prod, "
                + "c.descripcion AS nombre_cat "
                + "FROM producto p "
                + "INNER JOIN categoria c ON p.idCategoria = c.idCategoria "
                + "ORDER BY p.idProducto ASC";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombreProducto(rs.getString("nombre"));
                p.setPrecioProducto(rs.getString("precio"));
                p.setDescripcionProducto(rs.getString("desc_prod"));

                // ASIGNACIÓN CORRECTA:
                p.setIdCategoria(rs.getString("idCategoria")); // El número (01, 02)
                p.setCategoria(rs.getString("nombre_cat"));    // El nombre (Platos fuertes)

                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar: " + e.getMessage());
        }
        return lista;
    }

    public List<Producto> listarPorCategoria(String idCategoria) {
        List<Producto> lista = new java.util.ArrayList<>();
        String sql = "SELECT idProducto, nombre, precio, idCategoria, descripcion "
                + "FROM producto WHERE idCategoria = ? ORDER BY nombre ASC";
        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idCategoria);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setIdProducto(rs.getInt("idProducto"));
                p.setNombreProducto(rs.getString("nombre"));
                p.setPrecioProducto(String.valueOf(rs.getDouble("precio")));
                p.setIdCategoria(String.valueOf(rs.getInt("idCategoria")));
                p.setDescripcionProducto(rs.getString("descripcion"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.out.println("Error listarPorCategoria: " + e.getMessage());
        }
        return lista;
    }

}

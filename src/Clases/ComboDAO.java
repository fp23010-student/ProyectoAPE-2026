package Clases;

import static Clases.Conexion.conectar;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

public class ComboDAO {

    public int generarIdAutomatico() {
        String sql = "SELECT MAX(idCombo) FROM combo";
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

    public boolean guardarCombo(Combo c) {
        String sql = "INSERT INTO combo (idCombo, combo, precioCombo) VALUES (?, ?, ?)";

        try (Connection con = Conexion.conectar(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, c.getIdCombo());
            ps.setString(2, c.getCombo());
            ps.setDouble(3, c.getPrecioCombo());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al guardar: " + e.getMessage());
            return false;
        }
    }

    public List<Combo> listarCombos(String filtro) {
        List<Combo> lista = new ArrayList<>();
        String sql = "SELECT * FROM combo WHERE combo LIKE ? ORDER BY idCombo DESC";

        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + filtro + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Combo c = new Combo();
                c.setIdCombo(rs.getInt("idCombo"));
                c.setCombo(rs.getString("combo"));
                c.setPrecioCombo(rs.getDouble("precioCombo"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar combos: " + e.getMessage());
        }
        return lista;
    }

    public Combo buscarComboPorId(int id) {
        Combo c = null;
        String sql = "SELECT c.idCombo, c.combo, c.precioCombo, "
                + "d.idProducto, p.nombre, p.precio, d.cantidad "
                + "FROM combo c "
                + "LEFT JOIN detalle d ON c.idCombo = d.idCombo "
                + "LEFT JOIN producto p ON d.idProducto = p.idProducto "
                + "WHERE c.idCombo = ?";

        try (Connection con = conectar(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            List<Detalle> listaDetalles = new ArrayList<>();

            while (rs.next()) {
                if (c == null) {
                    c = new Combo();
                    c.setIdCombo(rs.getInt("idCombo"));
                    c.setCombo(rs.getString("combo"));
                    c.setPrecioCombo(rs.getDouble("precioCombo"));
                }
                if (rs.getObject("idProducto") != null) {
                    Producto prod = new Producto();
                    prod.setIdProducto(rs.getInt("idProducto"));
                    prod.setNombreProducto(rs.getString("nombre"));
                    prod.setPrecioProducto(String.valueOf(rs.getDouble("precio")));

                    Detalle det = new Detalle();
                    det.setProductos(prod);
                    det.setCantidad(rs.getInt("cantidad"));
                    listaDetalles.add(det);
                }
            }

            if (c != null) {
                c.setDetalle(listaDetalles);
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar combo: " + e.getMessage());
        }
        return c;
    }

    public boolean actualizarCombo(Combo c) {
        String sqlCombo = "UPDATE combo SET combo = ?, precioCombo = ? WHERE idCombo = ?";
        String sqlDeleteDetalle = "DELETE FROM detalle WHERE idCombo = ?";
        String sqlInsertDetalle = "INSERT INTO detalle (idCombo, idProducto, cantidad) VALUES (?, ?, ?)";

        Connection con = null;
        try {
            con = conectar();
            con.setAutoCommit(false);

            PreparedStatement psCombo = con.prepareStatement(sqlCombo);
            psCombo.setString(1, c.getCombo());
            psCombo.setDouble(2, c.getPrecioCombo());
            psCombo.setInt(3, c.getIdCombo());
            psCombo.executeUpdate();

            PreparedStatement psDel = con.prepareStatement(sqlDeleteDetalle);
            psDel.setInt(1, c.getIdCombo());
            psDel.executeUpdate();

            PreparedStatement psIns = con.prepareStatement(sqlInsertDetalle);
            for (Detalle det : c.getDetalle()) {
                psIns.setInt(1, c.getIdCombo());
                psIns.setInt(2, det.getProductos().getIdProducto());
                psIns.setInt(3, det.getCantidad());
                psIns.executeUpdate();
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Error al actualizar: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminarCombo(int id) {
        String sqlDetalle = "DELETE FROM detalle WHERE idCombo = ?";
        String sqlCombo = "DELETE FROM combo WHERE idCombo = ?";

        Connection con = null;
        try {
            con = conectar();
            con.setAutoCommit(false);

            PreparedStatement psDet = con.prepareStatement(sqlDetalle);
            psDet.setInt(1, id);
            psDet.executeUpdate();

            PreparedStatement psCom = con.prepareStatement(sqlCombo);
            psCom.setInt(1, id);
            psCom.executeUpdate();

            con.commit();
            return true;
        } catch (SQLException e) {
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Error al eliminar combo: " + e.getMessage());
            return false;
        }
    }

}

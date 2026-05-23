package Clases;

import java.sql.*;
import java.util.ArrayList;

public class DetalleOrdenDAO {

    public boolean insertarLote(int idOrden, ArrayList<DetalleOrden> detalles, Connection cn)
            throws SQLException {
        String sql = "INSERT INTO detalleorden (idOrden, idLinea, idProducto, idCombo, cantidad, precioUnitario) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = cn.prepareStatement(sql);

        for (DetalleOrden d : detalles) {
            ps.setInt(1, idOrden);
            ps.setInt(2, d.getIdLinea());

            if (d.getIdProducto() != null) {
                ps.setInt(3, d.getIdProducto());
            } else {
                ps.setNull(3, Types.INTEGER);
            }

            if (d.getIdCombo() != null) {
                ps.setInt(4, d.getIdCombo());
            } else {
                ps.setNull(4, Types.INTEGER);
            }

            ps.setInt(5, d.getCantidad());
            ps.setDouble(6, d.getPrecioUnitario());
            ps.addBatch();
        }

        int[] resultados = ps.executeBatch();
        // Verifica que todas las filas se insertaron
        for (int r : resultados) {
            if (r == 0) {
                return false;
            }
        }
        return true;
    }

    // Para consultas futuras (historial, ticket de reimpresión)
    public ArrayList<DetalleOrden> listarPorOrden(int idOrden) {
        ArrayList<DetalleOrden> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT d.idLinea, d.idProducto, d.idCombo, d.cantidad, d.precioUnitario, "
                    + "COALESCE(p.nombre, c.combo) AS nombre "
                    + "FROM detalleorden d "
                    + "LEFT JOIN producto p ON d.idProducto = p.idProducto "
                    + "LEFT JOIN combo c ON d.idCombo = c.idCombo "
                    + "WHERE d.idOrden = ? ORDER BY d.idLinea";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                DetalleOrden d = new DetalleOrden();
                d.setIdOrden(idOrden);
                d.setIdLinea(rs.getInt("idLinea"));
                d.setNombre(rs.getString("nombre"));
                d.setCantidad(rs.getInt("cantidad"));
                d.setPrecioUnitario(rs.getDouble("precioUnitario"));
                int idProd = rs.getInt("idProducto");
                d.setIdProducto(rs.wasNull() ? null : idProd);
                int idComb = rs.getInt("idCombo");
                d.setIdCombo(rs.wasNull() ? null : idComb);
                lista.add(d);
            }
        } catch (Exception e) {
            System.out.println("Error listarPorOrden: " + e.getMessage());
        }
        return lista;
    }
}

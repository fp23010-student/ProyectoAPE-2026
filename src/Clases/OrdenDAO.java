package Clases;

import java.sql.*;

public class OrdenDAO {
    // Recibe la conexión abierta para poder participar en la transacción de OrdenService

    public int insertar(Orden orden, Connection cn) throws SQLException {
        String sql = "INSERT INTO orden (idUsuario, idMesa, total) VALUES (?, ?, ?)";
        PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, orden.getIdUsuario());
        ps.setInt(2, orden.getIdMesa());
        ps.setDouble(3, orden.getTotal());
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1); // devuelve el idOrden generado
        }
        throw new SQLException("No se pudo obtener el ID de la orden generada.");
    }

    // Para mostrar en el campo ID al abrir el formulario (próximo ID)
    public int obtenerProximoId() {
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES "
                    + "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'orden'";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("Error obtenerProximoId: " + e.getMessage());
        }
        return 0;
    }

    // Para recuperar una orden completa (reimpresión de ticket)
    public Orden obtenerPorId(int idOrden) {
        Orden orden = null;
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT o.idOrden, o.fechaHora, o.total, "
                    + "u.nombre, u.apellido, m.numero AS numeroMesa "
                    + "FROM orden o "
                    + "JOIN usuario u ON o.idUsuario = u.idUsuario "
                    + "JOIN mesa m    ON o.idMesa    = m.idMesa "
                    + "WHERE o.idOrden = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                orden = new Orden();
                orden.setIdOrden(rs.getInt("idOrden"));
                orden.setFechaHora(rs.getTimestamp("fechaHora"));
//                orden.setTotal(rs.getDouble("total"));
                orden.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellido"));
                orden.setNumeroMesa(rs.getInt("numeroMesa"));

                DetalleOrdenDAO detalleDAO = new DetalleOrdenDAO();
                orden.setDetalles(detalleDAO.listarPorOrden(idOrden));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorId: " + e.getMessage());
        }
        return orden;
    }
}

package Clases;

import java.sql.*;
import java.util.ArrayList;

public class OrdenDAO {

    public int insertar(Orden orden, Connection cn) throws SQLException {
        String sql = "INSERT INTO orden (idUsuario, idMesa, total, estado) VALUES (?, ?, ?, ?)";
        PreparedStatement ps = cn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, orden.getIdUsuario());
        ps.setInt(2, orden.getIdMesa());
        ps.setDouble(3, orden.getTotal());
        ps.setString(4, "PENDIENTE");
        ps.executeUpdate();

        ResultSet keys = ps.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }
        throw new SQLException("No se pudo obtener el ID de la orden generada.");
    }

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

    public Orden obtenerPorId(int idOrden) {
        Orden orden = null;
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT o.idOrden, o.fechaHora, o.total, o.estado, "
                    + "o.idMesa, u.nombre, u.apellido, m.numero AS numeroMesa "
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
                orden.setEstado(rs.getString("estado"));
                orden.setIdMesa(rs.getInt("idMesa"));
                orden.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellido"));
                orden.setNumeroMesa(rs.getInt("numeroMesa"));
                orden.setTotalGuardado(rs.getDouble("total"));

                DetalleOrdenDAO detalleDAO = new DetalleOrdenDAO();
                orden.setDetalles(detalleDAO.listarPorOrden(idOrden));
            }
        } catch (Exception e) {
            System.out.println("Error obtenerPorId: " + e.getMessage());
        }
        return orden;
    }

    public ArrayList<Orden> listarPendientes() {
        ArrayList<Orden> lista = new ArrayList<>();
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT o.idOrden, o.fechaHora, o.total, o.estado, "
                    + "o.idMesa, u.nombre, u.apellido, m.numero AS numeroMesa "
                    + "FROM orden o "
                    + "JOIN usuario u ON o.idUsuario = u.idUsuario "
                    + "JOIN mesa m    ON o.idMesa    = m.idMesa "
                    + "WHERE o.estado = 'PENDIENTE' "
                    + "ORDER BY o.fechaHora ASC";
            PreparedStatement ps = cn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Orden o = new Orden();
                o.setIdOrden(rs.getInt("idOrden"));
                o.setFechaHora(rs.getTimestamp("fechaHora"));
                o.setEstado(rs.getString("estado"));
                o.setIdMesa(rs.getInt("idMesa"));
                o.setNombreUsuario(rs.getString("nombre") + " " + rs.getString("apellido"));
                o.setNumeroMesa(rs.getInt("numeroMesa"));
                o.setTotalGuardado(rs.getDouble("total"));
                lista.add(o);
            }
        } catch (Exception e) {
            System.out.println("Error listarPendientes: " + e.getMessage());
        }
        return lista;
    }

    public boolean actualizarEstado(int idOrden, String estado) {
        try (Connection cn = Conexion.conectar()) {
            String sql = "UPDATE orden SET estado = ? WHERE idOrden = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setString(1, estado);
            ps.setInt(2, idOrden);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error actualizarEstado: " + e.getMessage());
            return false;
        }
    }

    public int obtenerIdMesa(int idOrden) {
        try (Connection cn = Conexion.conectar()) {
            String sql = "SELECT idMesa FROM orden WHERE idOrden = ?";
            PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idOrden);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("idMesa");
            }
        } catch (Exception e) {
            System.out.println("Error obtenerIdMesa: " + e.getMessage());
        }
        return -1;
    }
}

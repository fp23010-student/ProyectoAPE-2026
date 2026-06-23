package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class OrdenService {

    private final OrdenDAO ordenDAO = new OrdenDAO();
    private final DetalleOrdenDAO detalleDAO = new DetalleOrdenDAO();
    private final MesaDAO mesaDAO = new MesaDAO();

    public int procesarOrden(Orden orden) {
        if (!orden.tieneDetalles()) {
            throw new IllegalArgumentException("La orden no tiene productos.");
        }

        try (Connection cn = Conexion.conectar()) {
            cn.setAutoCommit(false);

            int idOrden = ordenDAO.insertar(orden, cn);
            orden.setIdOrden(idOrden);

            for (DetalleOrden d : orden.getDetalles()) {
                d.setIdOrden(idOrden);
            }

            boolean detallesOk = detalleDAO.insertarLote(idOrden, orden.getDetalles(), cn);
            if (!detallesOk) {
                cn.rollback();
                return -1;
            }

            cn.commit();

            mesaDAO.actualizarEstado(orden.getIdMesa(), "OCUPADA");

            return idOrden;

        } catch (Exception e) {
            System.out.println("Error procesarOrden: " + e.getMessage());
            return -1;
        }
    }

    public void liberarMesa(int idMesa) {
        mesaDAO.actualizarEstado(idMesa, "LIBRE");
    }

    public int registrarOrden(Orden orden) {
        if (!orden.tieneDetalles()) {
            throw new IllegalArgumentException("La orden no tiene productos.");
        }
        try (Connection cn = Conexion.getConnection()) {
            cn.setAutoCommit(false);

            int idOrden = ordenDAO.insertar(orden, cn);
            orden.setIdOrden(idOrden);

            boolean detallesOk = detalleDAO.insertarLote(idOrden, orden.getDetalles(), cn);
            if (!detallesOk) {
                cn.rollback();
                return -1;
            }

            cn.commit();

            mesaDAO.actualizarEstado(orden.getIdMesa(), "OCUPADA");

            return idOrden;

        } catch (Exception e) {
            System.out.println("Error registrarOrden: " + e.getMessage());
            return -1;
        }
    }

    public boolean despacharOrden(int idOrden) {
        int idMesa = ordenDAO.obtenerIdMesa(idOrden);
        boolean ok = ordenDAO.actualizarEstado(idOrden, "DESPACHADA");
        if (ok) {
            mesaDAO.actualizarEstado(idMesa, "LIBRE");
            new TicketPDF().generarTicket(idOrden);
        }
        return ok;
    }

    public boolean cancelarOrden(int idOrden) {
        int idMesa = ordenDAO.obtenerIdMesa(idOrden);
        boolean ok = ordenDAO.actualizarEstado(idOrden, "CANCELADA");
        if (ok) {
            mesaDAO.actualizarEstado(idMesa, "LIBRE");
        }
        return ok;
    }

    public boolean actualizarOrden(int idOrden, Orden orden) {
        try (Connection cn = Conexion.getConnection()) {
            cn.setAutoCommit(false);

            String sqlDelete = "DELETE FROM detalleorden WHERE idOrden = ?";
            PreparedStatement psDel = cn.prepareStatement(sqlDelete);
            psDel.setInt(1, idOrden);
            psDel.executeUpdate();

            boolean ok = detalleDAO.insertarLote(idOrden, orden.getDetalles(), cn);
            if (!ok) {
                cn.rollback();
                return false;
            }

            String sqlTotal = "UPDATE orden SET total = ? WHERE idOrden = ?";
            PreparedStatement psTotal = cn.prepareStatement(sqlTotal);
            psTotal.setDouble(1, orden.getTotal());
            psTotal.setInt(2, idOrden);
            psTotal.executeUpdate();

            cn.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Error actualizarOrden: " + e.getMessage());
            return false;
        }
    }

    public boolean anularOrden(int idOrden) {
       
        try (java.sql.Connection cn = Conexion.conectar()) {
            String sql = """
            SELECT COUNT(*) FROM orden
            WHERE idOrden = ?
              AND DATE(fechaHora) = CURDATE()
              AND estado IN ('PROCESADA', 'DESPACHADA')
        """;
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, idOrden);
            java.sql.ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) == 0) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        int idMesa = ordenDAO.obtenerIdMesa(idOrden);
        boolean ok = ordenDAO.actualizarEstado(idOrden, "ANULADA");
        if (ok) {
            mesaDAO.actualizarEstado(idMesa, "LIBRE");
        }
        return ok;
    }

}

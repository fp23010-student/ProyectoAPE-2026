package Clases;

import java.sql.Connection;
import java.sql.PreparedStatement;

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

            // 1. Insertar cabecera
            int idOrden = ordenDAO.insertar(orden, cn);
            orden.setIdOrden(idOrden);

            // Asignar idOrden a cada detalle
            for (DetalleOrden d : orden.getDetalles()) {
                d.setIdOrden(idOrden);
            }

            // 2. Insertar detalles en lote
            boolean detallesOk = detalleDAO.insertarLote(idOrden, orden.getDetalles(), cn);
            if (!detallesOk) {
                cn.rollback();
                return -1;
            }

            cn.commit();

            // 3. Marcar mesa como OCUPADA (fuera de la transacción, es un UPDATE simple)
            mesaDAO.actualizarEstado(orden.getIdMesa(), "OCUPADA");

            return idOrden;

        } catch (Exception e) {
            System.out.println("Error procesarOrden: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Libera la mesa al cerrar/entregar el ticket.
     */
    public void liberarMesa(int idMesa) {
        mesaDAO.actualizarEstado(idMesa, "LIBRE");
    }

    // Solo guarda la orden como PENDIENTE, sin ticket ni liberar mesa
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

            // Marcar mesa como OCUPADA
            mesaDAO.actualizarEstado(orden.getIdMesa(), "OCUPADA");

            return idOrden;

        } catch (Exception e) {
            System.out.println("Error registrarOrden: " + e.getMessage());
            return -1;
        }
    }

// Pagar: marca como PAGADA, libera mesa, genera ticket
    public boolean pagarOrden(int idOrden) {
        int idMesa = ordenDAO.obtenerIdMesa(idOrden);
        boolean ok = ordenDAO.actualizarEstado(idOrden, "PAGADA");
        if (ok) {
            mesaDAO.actualizarEstado(idMesa, "LIBRE");
            new TicketPDF().generarTicket(idOrden);
        }
        return ok;
    }

// Cancelar: marca como CANCELADA, libera mesa, sin ticket
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

            // 1. Eliminar los detalles anteriores
            String sqlDelete = "DELETE FROM detalleorden WHERE idOrden = ?";
            PreparedStatement psDel = cn.prepareStatement(sqlDelete);
            psDel.setInt(1, idOrden);
            psDel.executeUpdate();

            // 2. Insertar los detalles nuevos
            boolean ok = detalleDAO.insertarLote(idOrden, orden.getDetalles(), cn);
            if (!ok) {
                cn.rollback();
                return false;
            }

            // 3. Actualizar el total en la cabecera
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

}

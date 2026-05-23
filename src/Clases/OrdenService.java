package Clases;

import java.sql.Connection;

public class OrdenService {

    private final OrdenDAO ordenDAO = new OrdenDAO();
    private final DetalleOrdenDAO detalleDAO = new DetalleOrdenDAO();
    private final MesaDAO mesaDAO = new MesaDAO();

    /**
     * Procesa la orden completa en una sola transacción: 1. Inserta la cabecera
     * en `orden` 2. Inserta todas las líneas en `detalleorden` 3. Marca la mesa
     * como OCUPADA
     *
     * @return el idOrden generado, o -1 si falló
     */
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
}

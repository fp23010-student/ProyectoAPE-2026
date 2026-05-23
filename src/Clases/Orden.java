package Clases;

import java.util.ArrayList;
import java.util.Date;

public class Orden {

    private int idOrden;
    private Date fechaHora;
    private int idUsuario;
    private String nombreUsuario;
    private int idMesa;
    private int numeroMesa;
    private double total;
    private ArrayList<DetalleOrden> detalles;

    public Orden() {
        this.detalles = new ArrayList<>();
    }

    public void agregarDetalle(DetalleOrden detalle) {
        detalle.setIdLinea(detalles.size() + 1);
        detalles.add(detalle);
        recalcularTotal();
    }

    public void eliminarDetalle(int index) {
        detalles.remove(index);
        // Reajustar números de línea
        for (int i = 0; i < detalles.size(); i++) {
            detalles.get(i).setIdLinea(i + 1);
        }
        recalcularTotal();
    }

    private void recalcularTotal() {
        this.total = detalles.stream()
                .mapToDouble(DetalleOrden::getSubtotal)
                .sum();
    }

    public boolean tieneDetalles() {
        return !detalles.isEmpty();
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdMesa() {
        return idMesa;
    }

    public void setIdMesa(int idMesa) {
        this.idMesa = idMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    public void setNumeroMesa(int numeroMesa) {
        this.numeroMesa = numeroMesa;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public ArrayList<DetalleOrden> getDetalles() {
        return detalles;
    }

    public void setDetalles(ArrayList<DetalleOrden> detalles) {
        this.detalles = detalles;
        recalcularTotal();
    }
}

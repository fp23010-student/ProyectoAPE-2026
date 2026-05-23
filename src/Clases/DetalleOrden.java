package Clases;

public class DetalleOrden {

    private int idOrden;
    private int idLinea;
    private Integer idProducto;  // null si es combo
    private Integer idCombo;     // null si es producto
    private String nombre;       // para mostrar en tabla y ticket
    private int cantidad;
    private double precioUnitario;

    public DetalleOrden() {
    }

    public DetalleOrden(int idLinea, Item item, int cantidad) {
        this.idLinea = idLinea;
        this.nombre = item.getNombre();
        this.cantidad = cantidad;
        this.precioUnitario = item.getPrecio();
        if (item.isEsCombo()) {
            this.idCombo = item.getId();
            this.idProducto = null;
        } else {
            this.idProducto = item.getId();
            this.idCombo = null;
        }
    }

    public double getSubtotal() {
        return cantidad * precioUnitario;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(int idLinea) {
        this.idLinea = idLinea;
    }

    public Integer getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(Integer idProducto) {
        this.idProducto = idProducto;
    }

    public Integer getIdCombo() {
        return idCombo;
    }

    public void setIdCombo(Integer idCombo) {
        this.idCombo = idCombo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }
}

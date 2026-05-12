package Clases;

public class Detalle {
    private Producto productos;
    private int cantidad;

    public Detalle(Producto productos, int cantidad) {
        this.productos = productos;
        this.cantidad = cantidad;
    }

    public Detalle() {
    }

    public Producto getProductos() {
        return productos;
    }

    public void setProductos(Producto productos) {
        this.productos = productos;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
    
    
    
}

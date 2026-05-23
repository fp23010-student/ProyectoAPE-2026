package Clases;

public class Item {

    private int id;
    private String nombre;
    private double precio;
    private boolean esCombo;

    public Item() {
    }

    public Item(int id, String nombre, double precio, boolean esCombo) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.esCombo = esCombo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEsCombo() {
        return esCombo;
    }

    public void setEsCombo(boolean esCombo) {
        this.esCombo = esCombo;
    }

    @Override
    public String toString() {
        return nombre + " - $" + String.format("%.2f", precio);
    }
}

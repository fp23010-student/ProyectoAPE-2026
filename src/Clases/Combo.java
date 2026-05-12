package Clases;

import java.util.List;

public class Combo {
    private int idCombo;
    private String combo;
    private double precioCombo;
    private List<Detalle> detalle;

    public Combo(int idCombo, String combo, double precioCombo, List<Detalle> detalle) {
        this.idCombo = idCombo;
        this.combo = combo;
        this.precioCombo = precioCombo;
        this.detalle = detalle;
    }

    public Combo() {
    }

    public int getIdCombo()  {
        return idCombo; 
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public String getCombo() {
        return combo;
    }

    public void setCombo(String combo) {
        this.combo = combo;
    }

    public double getPrecioCombo() {
        return precioCombo;
    }

    public void setPrecioCombo(double precioCombo) {
        this.precioCombo = precioCombo;
    }

    public List<Detalle> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<Detalle> detalle) {
        this.detalle = detalle;
    }
    
}

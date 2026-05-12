package Clases;

public class Categoria {

    private String idCategoria, descripcion;

    public Categoria(String idCategoria, String descripcion) {
        this.idCategoria = idCategoria;
        this.descripcion = descripcion;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        if (this.idCategoria.equals("00")) {
            return this.descripcion;
        }
        return this.idCategoria + " - " + this.descripcion;
    }

    public Categoria() {
    }

}

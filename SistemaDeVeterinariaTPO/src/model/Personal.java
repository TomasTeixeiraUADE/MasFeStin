package model;

public abstract class Personal {
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;

    public Personal(String nombre, String apellido, String documento, String telefono) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String obtenerNombreCompleto() {
        return nombre + " " + apellido;
    }
}
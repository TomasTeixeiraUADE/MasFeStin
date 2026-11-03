package model;

public abstract class Mascota {
    private String nombre;
    private String especie;
    private String sexo;
    private String fechaNacimiento;
    private double pesoActual;

    public Mascota(String nombre, String especie, String sexo, String fechaNacimiento, double pesoActual) {
        this.nombre = nombre;
        this.especie = especie;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.pesoActual = pesoActual;
    }

    // Getters necesarios
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public String getSexo() { return sexo; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public double getPesoActual() { return pesoActual; }
}



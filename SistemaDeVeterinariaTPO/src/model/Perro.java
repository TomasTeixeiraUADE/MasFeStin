package model;

public class Perro extends Mascota {
    private String numeroChip;
    private String tamanio;

    public Perro(String nombre, String especie, String sexo, String fechaNacimiento, double pesoActual, String numeroChip, String tamanio) {
        super(nombre, especie, sexo, fechaNacimiento, pesoActual);
        this.numeroChip = numeroChip;
        this.tamanio = tamanio;
    }

    public String getNumeroChip() { return numeroChip; }
    public String getTamanio() { return tamanio; }
}

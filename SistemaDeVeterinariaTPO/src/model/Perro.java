package model;

import java.time.LocalDate;

public class Perro extends Mascota {
    private String numeroChip;
    private String tamanio;

    public Perro(String nombre, String raza, String sexo, LocalDate fechaNacimiento, double pesoActual, String numeroChip, String tamanio) {
        super(nombre, raza, sexo, fechaNacimiento, pesoActual);

        this.numeroChip = numeroChip;
        this.tamanio = tamanio;
    }

    public String getNumeroChip() {
        return numeroChip;
    }

    public String getTamanio() {
        return tamanio;
    }

    @Override
    public String obtenerDatos() {

        return "Perro: " + getNombre()
                + " - Raza: " + getRaza()
                + " - Chip: " + numeroChip
                + " - Tamaño: " + tamanio;
    }

    @Override
    public String getEspecie() {
        return "PERRO";
    }
}

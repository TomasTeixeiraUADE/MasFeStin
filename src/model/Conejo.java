package src.model;

import java.time.LocalDate;

public class Conejo extends Mascota {

    public Conejo(String nombre, String raza, String sexo, LocalDate fechaNacimiento, double pesoActual) {
        super(nombre, raza, sexo, fechaNacimiento, pesoActual);
    }

    @Override
    public String obtenerDatos() {

        return "Conejo: " + getNombre()
                + " - Raza: " + getRaza();
    }
}
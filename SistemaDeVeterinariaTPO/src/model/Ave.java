package src.model;

import java.time.LocalDate;

public class Ave extends Mascota {
    private boolean puedeVolar;

    public Ave(String nombre, String raza, String sexo, LocalDate fechaNacimiento, double pesoActual, boolean puedeVolar) {
        super(nombre, raza, sexo, fechaNacimiento, pesoActual);

        this.puedeVolar = puedeVolar;
    }

    public boolean isPuedeVolar() {
        return puedeVolar;
    }

    @Override
    public String obtenerDatos() {

        String vuelo;

        if (puedeVolar) {
            vuelo = "Puede volar";
        } else {
            vuelo = "No puede volar";
        }

        return "Ave: " + getNombre()
                + " - Raza: " + getRaza()
                + " - " + vuelo;
    }

    @Override
    public String getEspecie() {
        return "AVE";
    }
}
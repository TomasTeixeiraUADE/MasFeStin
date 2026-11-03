package src.model;

import java.time.LocalDate;

public class Gato extends Mascota {

    private boolean viveEnInterior;

    public Gato(String nombre, String raza, String sexo, LocalDate fechaNacimiento, double pesoActual, boolean viveEnInterior) {
        super(nombre, raza, sexo, fechaNacimiento, pesoActual);

        this.viveEnInterior = viveEnInterior;
    }

    public boolean isViveEnInterior() {
        return viveEnInterior;
    }

    @Override
    public String obtenerDatos() {

        String tipoVida;

        if (viveEnInterior) {
            tipoVida = "Interior";
        } else {
            tipoVida = "Exterior";
        }

        return "Gato: " + getNombre()
                + " - Raza: " + getRaza()
                + " - Vive en: " + tipoVida;
    }
}
package controller;

import model.Clinica;
import model.Dueno;
import model.Mascota;

public class ControladorRegistro {
    private Clinica clinica;

    public ControladorRegistro(Clinica clinica) {
        this.clinica = clinica;
    }

    public Dueno registrarDueno(String nombre, String apellido, String documento,
                                String telefono, String correo, boolean planSalud) {
        for (Dueno existente : clinica.getDuenos()) {
            if (existente.getDocumento().equalsIgnoreCase(documento)) {
                throw new IllegalArgumentException("Ya existe un dueño con ese documento.");
            }
        }

        Dueno dueno = new Dueno(nombre, apellido, documento, telefono, correo, planSalud);
        clinica.getDuenos().add(dueno);
        return dueno;
    }

    public Mascota registrarMascota(Dueno dueno, Mascota mascota) {
        if (dueno == null || mascota == null) {
            throw new IllegalArgumentException("Dueño y mascota son obligatorios.");
        }
        for (Mascota existente : clinica.getMascotas()) {
            if (existente.getNombre().equalsIgnoreCase(mascota.getNombre())
                    && existente.getDueno() == dueno) {
                throw new IllegalArgumentException("Ese dueño ya tiene una mascota con ese nombre.");
            }
        }
        dueno.agregarMascota(mascota);
        clinica.getMascotas().add(mascota);
        return mascota;
    }

    public void transferirMascota(Mascota mascota, Dueno nuevoDueno) {
        if (mascota == null || nuevoDueno == null) {
            throw new IllegalArgumentException("Mascota y nuevo dueño son obligatorios.");
        }
        Dueno anterior = mascota.getDueno();
        if (anterior == nuevoDueno) {
            return;
        }
        if (anterior != null) {
            anterior.quitarMascota(mascota);
        }
        nuevoDueno.agregarMascota(mascota);
    }
}

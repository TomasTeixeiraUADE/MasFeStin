package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class RecordatorioVacuna {
    private Mascota mascota;
    private VacunaAplicada aplicacion;

    public RecordatorioVacuna(Mascota mascota, VacunaAplicada aplicacion) {
        this.mascota = mascota;
        this.aplicacion = aplicacion;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public VacunaAplicada getAplicacion() {
        return aplicacion;
    }

    public long diasHastaRefuerzo(LocalDate hoy) {
        return ChronoUnit.DAYS.between(hoy, aplicacion.getFechaProximoRefuerzo());
    }

    public String descripcion(LocalDate hoy) {
        String dueno = mascota.getDueno() == null
                ? "sin dueño"
                : mascota.getDueno().obtenerNombreCompleto();

        if (aplicacion.estaVencida(hoy)) {
            return mascota.getNombre() + " - " + aplicacion.getVacuna().getNombre()
                    + " - VENCIDA - dueño: " + dueno;
        }

        return mascota.getNombre() + " - " + aplicacion.getVacuna().getNombre()
                + " - refuerzo: " + aplicacion.getFechaProximoRefuerzo()
                + " - dueño: " + dueno;
    }
}

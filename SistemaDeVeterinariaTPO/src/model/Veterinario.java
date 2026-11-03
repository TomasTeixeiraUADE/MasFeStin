package model;

import java.time.LocalTime;

public class Veterinario extends Personal {
    private String matricula;
    private String especialidad;
    private LocalTime horaInicio;
    private LocalTime horaFin;

    public Veterinario(String nombre, String apellido, String documento, String telefono, String matricula, String especialidad, LocalTime horaInicio, LocalTime horaFin) {
        super(nombre, apellido, documento, telefono);

        this.matricula = matricula;
        this.especialidad = especialidad;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
    }

    public String getMatricula() {
        return matricula;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public boolean atiendeEnHorario(LocalTime hora, int duracionMinutos) {

        LocalTime horaFinal = hora.plusMinutes(duracionMinutos);

        return !hora.isBefore(horaInicio) && !horaFinal.isAfter(horaFin);
    }
}
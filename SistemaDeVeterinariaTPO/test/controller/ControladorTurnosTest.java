package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import model.Clinica;
import model.Dueno;
import model.Perro;
import model.Turno;
import model.Veterinario;
import model.enums.EstadoTurno;
import model.enums.TipoAtencion;

public class ControladorTurnosTest {

    @Test
    void rechazaSuperposicionDelMismoVeterinario() {
        Clinica clinica = new Clinica();
        ControladorTurnos controlador = new ControladorTurnos(clinica, clinica.getPrecios());
        Dueno dueno = new Dueno("Juan", "Perez", "1", "1", "a@a.com", false);
        Perro mascota = new Perro("Toby", "Lab", "Macho", LocalDate.now().minusYears(2), 20, "C", "G");
        dueno.agregarMascota(mascota);
        Veterinario vet = new Veterinario("Ana", "Vet", "2", "2", "MP", "Clínica", LocalTime.of(9,0), LocalTime.of(18,0));

        LocalDateTime inicio = LocalDate.now().plusDays(1).atTime(10, 0);
        controlador.asignarTurno(mascota, vet, inicio, TipoAtencion.CONSULTA_GENERAL, false, false);

        assertThrows(IllegalStateException.class, () ->
                controlador.asignarTurno(mascota, vet, inicio.plusMinutes(15),
                        TipoAtencion.CONSULTA_GENERAL, false, false));
    }

    @Test
    void marcarAusenteIncrementaAusenciasDelDueno() {
        Clinica clinica = new Clinica();
        ControladorTurnos controlador = new ControladorTurnos(clinica, clinica.getPrecios());
        Dueno dueno = new Dueno("Juan", "Perez", "1", "1", "a@a.com", false);
        Perro mascota = new Perro("Toby", "Lab", "Macho", LocalDate.now().minusYears(2), 20, "C", "G");
        dueno.agregarMascota(mascota);
        Veterinario vet = new Veterinario("Ana", "Vet", "2", "2", "MP", "Clínica", LocalTime.of(9,0), LocalTime.of(18,0));
        Turno turno = controlador.asignarTurno(
                mascota, vet, LocalDate.now().plusDays(1).atTime(10,0),
                TipoAtencion.CONSULTA_GENERAL, false, false
        );

        controlador.marcarAusente(turno);

        assertEquals(EstadoTurno.AUSENTE, turno.getEstado());
        assertEquals(1, dueno.getAusenciasConsecutivas());
    }
}

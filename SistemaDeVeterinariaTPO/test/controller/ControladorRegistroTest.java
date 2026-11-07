package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import model.Clinica;
import model.Dueno;
import model.Perro;

public class ControladorRegistroTest {
    @Test
    void transferirMascotaCambiaDuenoYConservaHistoria() {
        Clinica clinica = new Clinica();
        ControladorRegistro controlador = new ControladorRegistro(clinica);
        Dueno a = controlador.registrarDueno("A", "Uno", "1", "1", "a@a.com", false);
        Dueno b = controlador.registrarDueno("B", "Dos", "2", "2", "b@b.com", false);
        Perro perro = new Perro("Toby", "Lab", "Macho", LocalDate.now().minusYears(2), 20, "C", "G");
        controlador.registrarMascota(a, perro);
        Object historiaOriginal = perro.getHistoriaClinica();

        controlador.transferirMascota(perro, b);

        assertSame(b, perro.getDueno());
        assertSame(historiaOriginal, perro.getHistoriaClinica());
        assertEquals(0, a.getMascotas().size());
        assertEquals(1, b.getMascotas().size());
    }
}

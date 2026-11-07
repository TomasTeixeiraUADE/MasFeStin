package model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

public class ConsultaTest {

    private Veterinario veterinario() {
        return new Veterinario("Ana", "Vet", "1", "1", "MP", "Clínica",
                LocalTime.of(9,0), LocalTime.of(18,0));
    }

    @Test
    void noPermiteMedicamentoSinPeso() {
        Perro perro = new Perro("Toby", "Lab", "Macho", LocalDate.now().minusYears(2), 20, "C", "G");
        Consulta consulta = new Consulta(LocalDateTime.now(), "Dolor", 0.0, "Dx", "", veterinario(), perro);
        Medicamento medicamento = new Medicamento("M1", "Amoxicilina", 2);

        assertThrows(IllegalStateException.class, () ->
                consulta.agregarTratamiento(medicamento, 5, "8 hs", "5 días"));
    }

    @Test
    void consultaConfirmadaSoloSeCorrigeConNotaRectificatoria() throws Exception {
        Perro perro = new Perro("Toby", "Lab", "Macho", LocalDate.now().minusYears(2), 20, "C", "G");
        Veterinario vet = veterinario();
        Consulta consulta = new Consulta(LocalDateTime.now(), "Control", 20.0, "Dx", "", vet, perro);
        consulta.confirmar();

        consulta.agregarNotaRectificatoria("Se corrige el diagnóstico", vet);
        assertEquals(1, consulta.getNotasRectificatorias().size());
    }
}

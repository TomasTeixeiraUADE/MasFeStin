package model;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import exception.StockInsuficienteException;

public class VacunaTest {

    @Test
    void validaEspeciePermitida() {
        Vacuna vacuna = new Vacuna("Antirrábica", 12, 365);
        vacuna.agregarEspecie("PERRO");
        Perro perro = new Perro("Toby", "Lab", "Macho", LocalDate.now().minusYears(2), 20, "C", "G");
        Gato gato = new Gato("Luna", "Siamés", "Hembra", LocalDate.now().minusYears(2), 4, true);

        assertTrue(vacuna.aplicaA(perro));
        assertFalse(vacuna.aplicaA(gato));
    }

    @Test
    void rechazaCuandoNoHayLoteVigente() {
        Vacuna vacuna = new Vacuna("Antirrábica", 12, 365);
        vacuna.agregarLote(new Lote("VENCIDO", LocalDate.now().minusDays(1), 20));
        assertThrows(StockInsuficienteException.class, vacuna::obtenerLoteDisponible);
    }

    @Test
    void recordatorioSeActivaSieteDiasAntesYVenceTrasTreintaDias() {
        Vacuna vacuna = new Vacuna("Antirrábica", 1, 365);
        Lote lote = new Lote("L1", LocalDate.now().plusYears(2), 10);
        LocalDate aplicacion = LocalDate.now().minusDays(358);
        VacunaAplicada aplicada = new VacunaAplicada(vacuna, aplicacion, lote);

        assertTrue(aplicada.debeEmitirRecordatorio(LocalDate.now()));
        assertTrue(aplicada.estaVencida(aplicada.getFechaProximoRefuerzo().plusDays(31)));
    }
}

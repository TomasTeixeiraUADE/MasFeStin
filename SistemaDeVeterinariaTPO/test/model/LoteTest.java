package model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import exception.StockInsuficienteException;

import java.time.LocalDate;

public class LoteTest {

    @Test
    void testDescontarStockExitoso() throws StockInsuficienteException{
        Lote lote = new Lote("L001", LocalDate.now().plusMonths(6),10);
        lote.descontar(3);
        assertEquals(7, lote.getCantidad());
    }
    @Test
    void testDescontarStockInsuficienteLanzaExcepcion() {
        Lote lote = new Lote("L001", LocalDate.now().plusMonths(6), 5);
        assertThrows(StockInsuficienteException.class, () -> {
            lote.descontar(10);
        });
    }

    @Test
    void testDescontarCantidadNegativaLanzaIllegalArgument() {
        Lote lote = new Lote("L001", LocalDate.now().plusMonths(6), 5);
        assertThrows(IllegalArgumentException.class, () -> {
            lote.descontar(-2);
        });
    }
}

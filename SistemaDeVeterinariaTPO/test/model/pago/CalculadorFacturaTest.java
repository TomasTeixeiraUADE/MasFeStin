package model.pago;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;

import model.Consulta;
import model.Perro;
import model.Veterinario;

public class CalculadorFacturaTest {

    private Factura facturaBase() {
        Perro mascota = new Perro(
                "Toby", "Labrador", "Macho", LocalDate.now().minusYears(3),
                20, "CHIP", "Grande"
        );
        Veterinario veterinario = new Veterinario(
                "Ana", "Vet", "1", "111", "MP1", "Clínica",
                LocalTime.of(9, 0), LocalTime.of(18, 0)
        );
        Consulta consulta = new Consulta(
                LocalDateTime.now(), "Control", 20.0, "OK", "", veterinario, mascota
        );
        Factura factura = new Factura(1, LocalDate.now(), consulta);
        factura.agregarLinea("Consulta", 1, 10000);
        return factura;
    }

    @Test
    void efectivoAplicaDiezPorCiento() {
        double total = new CalculadorFactura().calcularTotal(
                facturaBase(), new Efectivo(), false
        );
        assertEquals(9000, total, 0.01);
    }

    @Test
    void planYefectivoNoSeAcumulanSeUsaMayor() {
        double total = new CalculadorFactura().calcularTotal(
                facturaBase(), new Efectivo(), true
        );
        assertEquals(8500, total, 0.01);
    }

    @Test
    void creditoAplicaRecargoDelOchoPorCiento() {
        double total = new CalculadorFactura().calcularTotal(
                facturaBase(), new Credito(), false
        );
        assertEquals(10800, total, 0.01);
    }
}

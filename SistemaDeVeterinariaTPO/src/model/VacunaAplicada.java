package model;

import java.time.LocalDate;

import exception.StockInsuficienteException;

public class VacunaAplicada {
    private Vacuna vacuna;
    private LocalDate fechaAplicacion;
    private LocalDate fechaProximoRefuerzo;

    private Lote lote;

    public VacunaAplicada(Vacuna vacuna, LocalDate fechaAplicacion, Lote lote) {
        this.vacuna = vacuna;
        this.fechaAplicacion =
                fechaAplicacion;

        this.fechaProximoRefuerzo =
                vacuna.calcularProximoRefuerzo(
                        fechaAplicacion
                );

        this.lote = lote;
    }

    public Vacuna getVacuna() {
        return vacuna;
    }

    public LocalDate getFechaAplicacion() {
        return fechaAplicacion;
    }

    public LocalDate getFechaProximoRefuerzo() {
        return fechaProximoRefuerzo;
    }

    public Lote getLote() {
        return lote;
    }

    public void confirmarAplicacion()
            throws StockInsuficienteException {

        lote.descontar(1);
    }
}
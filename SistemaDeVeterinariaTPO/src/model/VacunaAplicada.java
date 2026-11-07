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

    public boolean debeEmitirRecordatorio(LocalDate hoy) {
        LocalDate inicioRecordatorio = fechaProximoRefuerzo.minusDays(7);
        return !hoy.isBefore(inicioRecordatorio)
                && !hoy.isAfter(fechaProximoRefuerzo);
    }

    public boolean estaVencida(LocalDate hoy) {
        return hoy.isAfter(fechaProximoRefuerzo.plusDays(30));
    }

    public void confirmarAplicacion()
            throws StockInsuficienteException {

        lote.descontar(1);
    }
}
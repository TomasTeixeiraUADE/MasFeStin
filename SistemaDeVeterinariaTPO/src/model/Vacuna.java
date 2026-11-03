package src.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.exception.StockInsuficienteException;

public class Vacuna {
    private String nombre;
    private List<String> especiesAplicables;

    private int edadMinimaSemanas;
    private int intervaloRefuerzoDias;

    private List<Lote> lotes;

    public Vacuna(String nombre, int edadMinimaSemanas, int intervaloRefuerzoDias) {
        this.nombre = nombre;
        this.edadMinimaSemanas = edadMinimaSemanas;
        this.intervaloRefuerzoDias =
                intervaloRefuerzoDias;

        this.especiesAplicables =
                new ArrayList<>();

        this.lotes =
                new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public int getEdadMinimaSemanas() {
        return edadMinimaSemanas;
    }

    public int getIntervaloRefuerzoDias() {
        return intervaloRefuerzoDias;
    }

    public void agregarEspecie(
            String especie) {

        if (especie != null
                && !especiesAplicables.contains(
                        especie.toUpperCase())) {

            especiesAplicables.add(
                    especie.toUpperCase()
            );
        }
    }

    public void agregarLote(
            Lote lote) {

        if (lote != null) {
            lotes.add(lote);
        }
    }

    public boolean aplicaA(
            Mascota mascota) {

        return especiesAplicables.contains(
                mascota.getEspecie()
                        .toUpperCase()
        );
    }

    public boolean permiteEdad(
            Mascota mascota) {

        return mascota.calcularEdadSemanas()
                >= edadMinimaSemanas;
    }

    public LocalDate calcularProximoRefuerzo(
            LocalDate fechaAplicacion) {

        return fechaAplicacion.plusDays(
                intervaloRefuerzoDias
        );
    }

    public Lote obtenerLoteDisponible()
            throws StockInsuficienteException {

        Lote seleccionado = null;

        for (Lote lote : lotes) {

            if (lote.tieneStockDisponible()) {

                if (seleccionado == null
                        || lote.getFechaVencimiento()
                        .isBefore(
                                seleccionado
                                        .getFechaVencimiento()
                        )) {

                    seleccionado = lote;
                }
            }
        }

        if (seleccionado == null) {

            throw new StockInsuficienteException(
                    "No hay lotes vigentes disponibles "
                    + "para la vacuna "
                    + nombre
            );
        }

        return seleccionado;
    }

    public List<String> getEspeciesAplicables() {
        return new ArrayList<>(
                especiesAplicables
        );
    }

    public List<Lote> getLotes() {
        return new ArrayList<>(
                lotes
        );
    }
}
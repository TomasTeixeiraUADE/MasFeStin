package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import model.comparator.ConsultaPorFechaComparator;

public class HistoriaClinica {
    private Mascota mascota;

    private List<Consulta> consultas;

    public HistoriaClinica(Mascota mascota) {

        this.mascota = mascota;

        this.consultas =
                new ArrayList<>();
    }

    public Mascota getMascota() {
        return mascota;
    }

    public List<Consulta> getConsultas() {

        return new ArrayList<>(
                consultas
        );
    }

    public Consulta registrarConsulta(LocalDateTime fecha, String motivo, Double pesoDelDia, String diagnostico, String observaciones, Veterinario veterinario) {
        Consulta consulta =
                new Consulta(fecha, motivo, pesoDelDia, diagnostico, observaciones, veterinario, mascota);

        consultas.add(consulta);

        return consulta;
    }

    public List<Consulta> obtenerConsultasOrdenadasPorFecha() {

        List<Consulta> copia = new ArrayList<>(consultas);

        copia.sort(
                new ConsultaPorFechaComparator()
        );

        return copia;
    }
}
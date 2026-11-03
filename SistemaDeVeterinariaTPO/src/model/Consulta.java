package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import exception.StockInsuficienteException;

public class Consulta {
    private LocalDateTime fecha;
    private String motivo;

    private Double pesoDelDia;

    private String diagnostico;
    private String observaciones;

    private Veterinario veterinario;
    private Mascota mascota;

    private List<Tratamiento> tratamientos;
    private List<VacunaAplicada> vacunasAplicadas;

    private boolean confirmada;

    public Consulta(LocalDateTime fecha, String motivo, Double pesoDelDia, String diagnostico, String observaciones, Veterinario veterinario, Mascota mascota) {
        this.fecha = fecha;
        this.motivo = motivo;
        this.pesoDelDia = pesoDelDia;
        this.diagnostico = diagnostico;
        this.observaciones = observaciones;
        this.veterinario = veterinario;
        this.mascota = mascota;

        this.tratamientos =
                new ArrayList<>();

        this.vacunasAplicadas =
                new ArrayList<>();

        this.confirmada = false;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getMotivo() {
        return motivo;
    }

    public Double getPesoDelDia() {
        return pesoDelDia;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public boolean isConfirmada() {
        return confirmada;
    }

    public List<Tratamiento> getTratamientos() {
        return new ArrayList<>(
                tratamientos
        );
    }

    public List<VacunaAplicada>
            getVacunasAplicadas() {

        return new ArrayList<>(
                vacunasAplicadas
        );
    }

    public void agregarTratamiento(Medicamento medicamento, double dosis, String frecuencia, String duracion) {

        if (pesoDelDia == null
                || pesoDelDia <= 0) {

            throw new IllegalStateException(
                    "No se puede indicar "
                    + "un medicamento sin registrar "
                    + "el peso del animal."
            );
        }

        Tratamiento tratamiento =
                new Tratamiento(medicamento, dosis, frecuencia, duracion);

        tratamientos.add(tratamiento);
    }

    public VacunaAplicada aplicarVacuna(
            Vacuna vacuna,
            LocalDate fechaAplicacion)
            throws StockInsuficienteException {

        if (!vacuna.aplicaA(mascota)) {

            throw new IllegalArgumentException(
                    "La vacuna no se puede aplicar "
                    + "a la especie "
                    + mascota.getEspecie()
            );
        }

        if (!vacuna.permiteEdad(mascota)) {

            throw new IllegalArgumentException(
                    "El animal no tiene "
                    + "la edad minima requerida."
            );
        }

        Lote lote =
                vacuna.obtenerLoteDisponible();

        VacunaAplicada aplicada =
                new VacunaAplicada(
                        vacuna,
                        fechaAplicacion,
                        lote
                );

        vacunasAplicadas.add(aplicada);

        return aplicada;
    }

    public void confirmar()
            throws StockInsuficienteException {

        if (confirmada) {
            return;
        }

        for (VacunaAplicada vacuna
                : vacunasAplicadas) {

            vacuna.confirmarAplicacion();
        }

        confirmada = true;
    }

    public void registrarPeso(double peso) {
        if (peso <= 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser mayor a cero."
            );
        }

        this.pesoDelDia = peso;

        mascota.setPesoActual(peso);
    }
}
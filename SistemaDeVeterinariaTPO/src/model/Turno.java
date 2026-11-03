package src.model;

import java.time.LocalDateTime;

import src.model.enums.EstadoTurno;
import src.model.enums.TipoAtencion;

public class Turno {
    private Mascota mascota;
    private Veterinario veterinario;

    private LocalDateTime fechaHora;
    private TipoAtencion tipoAtencion;
    private EstadoTurno estado;

    private boolean urgencia;
    private boolean senaPagada;

    public Turno(Mascota mascota, Veterinario veterinario, LocalDateTime fechaHora, TipoAtencion tipoAtencion, boolean urgencia) {
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.fechaHora = fechaHora;
        this.tipoAtencion = tipoAtencion;
        this.urgencia = urgencia;

        this.estado = EstadoTurno.PROGRAMADO;
        this.senaPagada = false;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public TipoAtencion getTipoAtencion() {
        return tipoAtencion;
    }

    public EstadoTurno getEstado() {
        return estado;
    }

    public boolean isUrgencia() {
        return urgencia;
    }

    public boolean isSenaPagada() {
        return senaPagada;
    }

    public int calcularDuracion() {

        switch (tipoAtencion) {

            case CONSULTA_GENERAL:
                return 30;

            case VACUNACION:
                return 15;

            case CIRUGIA:
                return 120;

            default:
                return 0;
        }
    }

    public LocalDateTime calcularHoraFin() {

        return fechaHora.plusMinutes(
                calcularDuracion()
        );
    }

    public void reprogramar(
            LocalDateTime nuevaFechaHora) {

        if (estado == EstadoTurno.PROGRAMADO) {
            this.fechaHora = nuevaFechaHora;
        }
    }

    public void cancelar() {

        if (estado == EstadoTurno.PROGRAMADO) {
            estado = EstadoTurno.CANCELADO;
        }
    }

    public void marcarAtendido() {

        estado = EstadoTurno.ATENDIDO;

        if (mascota.getDueno() != null) {
            mascota.getDueno().reiniciarAusencias();
        }
    }

    public void marcarAusente() {

        estado = EstadoTurno.AUSENTE;

        if (mascota.getDueno() != null) {
            mascota.getDueno().registrarAusencia();
        }
    }

    public void registrarSena() {
        senaPagada = true;
    }

    public boolean seSuperponeCon(Turno otroTurno) {
        LocalDateTime inicio1 =
                this.fechaHora;

        LocalDateTime fin1 =
                this.calcularHoraFin();

        LocalDateTime inicio2 =
                otroTurno.getFechaHora();

        LocalDateTime fin2 =
                otroTurno.calcularHoraFin();

        return inicio1.isBefore(fin2)
                && inicio2.isBefore(fin1);
    }
}
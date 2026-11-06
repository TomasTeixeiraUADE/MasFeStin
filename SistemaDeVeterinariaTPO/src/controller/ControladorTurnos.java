package controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import model.Clinica;
import model.Mascota;
import model.Turno;
import model.Veterinario;
import model.enums.EstadoTurno;
import model.enums.TipoAtencion;
import model.pago.ListaPrecios;

public class ControladorTurnos {
    public static final double SENA = 5000;
    public static final double CARGO_CANCELACION_TARDIA = 0.20;

    private static final int HORAS_SIN_CARGO = 2;
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Clinica clinica;
    private ListaPrecios precios;

    public ControladorTurnos(Clinica clinica, ListaPrecios precios) {
        this.clinica = clinica;
        this.precios = precios;
    }

    public Turno asignarTurno(Mascota mascota, Veterinario veterinario,
                              LocalDateTime fechaHora, TipoAtencion tipo,
                              boolean urgencia, boolean senaPagada) {

        if (mascota.getDueno() == null) {
            throw new IllegalStateException(
                    "La mascota no tiene un dueño asignado."
            );
        }

        if (mascota.getDueno().tieneRestriccionPorAusencias() && !senaPagada) {
            throw new IllegalStateException(
                    "Seña requerida: el dueño acumula 3 ausencias seguidas. "
                    + "Cobrar la seña de $" + (int) SENA
                    + " para habilitar el turno."
            );
        }

        Turno turno = new Turno(mascota, veterinario, fechaHora, tipo, urgencia);

        if (senaPagada) {
            turno.registrarSena();
        }

        if (urgencia) {
            return asignarUrgencia(turno);
        }

        validarHorario(veterinario, fechaHora, turno.calcularDuracion(), null);

        clinica.getTurnos().add(turno);

        return turno;
    }

    public double cancelarTurno(Turno turno, LocalDateTime ahora) {

        exigirProgramado(turno);

        turno.cancelar();

        boolean tarde = ahora.isAfter(
                turno.getFechaHora().minusHours(HORAS_SIN_CARGO)
        );

        if (!tarde) {
            return 0;
        }

        return precios.precioAtencion(turno.getTipoAtencion())
                * CARGO_CANCELACION_TARDIA;
    }

    public void reprogramarTurno(Turno turno, LocalDateTime nuevaFechaHora) {

        exigirProgramado(turno);

        validarHorario(
                turno.getVeterinario(),
                nuevaFechaHora,
                turno.calcularDuracion(),
                turno
        );

        turno.reprogramar(nuevaFechaHora);
    }

    public void marcarAusente(Turno turno) {

        exigirProgramado(turno);

        turno.marcarAusente();
    }

    public void marcarAtendido(Turno turno) {

        exigirProgramado(turno);

        turno.marcarAtendido();
    }

    public List<Turno> turnosProgramados(Veterinario veterinario) {

        return clinica.getTurnos().stream()
                .filter(t -> t.getVeterinario() == veterinario
                        && t.getEstado() == EstadoTurno.PROGRAMADO)
                .sorted(Comparator.comparing(Turno::getFechaHora))
                .collect(Collectors.toList());
    }

    private Turno asignarUrgencia(Turno urgente) {

        Veterinario veterinario = urgente.getVeterinario();

        if (!atiende(veterinario, urgente.getFechaHora(), urgente.calcularDuracion())) {
            throw new IllegalStateException(
                    "El veterinario de guardia no atiende en ese horario."
            );
        }

        List<Turno> desplazados = turnosProgramados(veterinario).stream()
                .filter(urgente::seSuperponeCon)
                .collect(Collectors.toList());

        clinica.getTurnos().add(urgente);

        for (Turno desplazado : desplazados) {

            LocalDateTime nuevo = proximoHorarioLibre(
                    veterinario,
                    urgente.calcularHoraFin(),
                    desplazado.calcularDuracion(),
                    desplazado
            );

            desplazado.reprogramar(nuevo);

            System.out.println(
                    "[NOTIFICACIÓN] "
                    + desplazado.getMascota().getDueno().obtenerNombreCompleto()
                    + ": el turno de " + desplazado.getMascota().getNombre()
                    + " se reprogramó al " + nuevo.format(FORMATO)
                    + " por una urgencia."
            );
        }

        return urgente;
    }

    private void validarHorario(Veterinario veterinario, LocalDateTime inicio,
                                int duracion, Turno ignorar) {

        if (!atiende(veterinario, inicio, duracion)) {
            throw new IllegalStateException(
                    "El veterinario no atiende en ese horario. "
                    + "Próximo horario libre: "
                    + proximoHorarioLibre(veterinario, inicio, duracion, ignorar)
                            .format(FORMATO)
            );
        }

        if (!estaLibre(veterinario, inicio, duracion, ignorar)) {
            throw new IllegalStateException(
                    "El veterinario ya tiene un turno superpuesto. "
                    + "Próximo horario libre: "
                    + proximoHorarioLibre(veterinario, inicio, duracion, ignorar)
                            .format(FORMATO)
            );
        }
    }

    private boolean atiende(Veterinario veterinario, LocalDateTime inicio, int duracion) {

        LocalDateTime fin = inicio.plusMinutes(duracion);

        return fin.toLocalDate().equals(inicio.toLocalDate())
                && veterinario.atiendeEnHorario(inicio.toLocalTime(), duracion);
    }

    private boolean estaLibre(Veterinario veterinario, LocalDateTime inicio,
                              int duracion, Turno ignorar) {

        LocalDateTime fin = inicio.plusMinutes(duracion);

        for (Turno otro : turnosProgramados(veterinario)) {

            if (otro != ignorar
                    && inicio.isBefore(otro.calcularHoraFin())
                    && otro.getFechaHora().isBefore(fin)) {

                return false;
            }
        }

        return true;
    }

    private LocalDateTime proximoHorarioLibre(Veterinario veterinario,
                                              LocalDateTime desde, int duracion,
                                              Turno ignorar) {

        LocalDateTime candidato = desde;

        for (int i = 0; i < 14 * 24 * 4; i++) {

            if (atiende(veterinario, candidato, duracion)
                    && estaLibre(veterinario, candidato, duracion, ignorar)) {

                return candidato;
            }

            candidato = candidato.plusMinutes(15);
        }

        throw new IllegalStateException(
                "No hay horarios libres en los próximos 14 días."
        );
    }

    private void exigirProgramado(Turno turno) {

        if (turno.getEstado() != EstadoTurno.PROGRAMADO) {
            throw new IllegalStateException(
                    "El turno no está programado (estado: "
                    + turno.getEstado() + ")."
            );
        }
    }
}

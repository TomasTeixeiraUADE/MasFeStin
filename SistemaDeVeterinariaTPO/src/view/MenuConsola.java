package view;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.function.Function;

import controller.ControladorAtencion;
import controller.ControladorDatos;
import controller.ControladorFacturacion;
import controller.ControladorTurnos;
import exception.StockInsuficienteException;
import model.Clinica;
import model.Consulta;
import model.Dueno;
import model.Lote;
import model.Mascota;
import model.Medicamento;
import model.Turno;
import model.Vacuna;
import model.VacunaAplicada;
import model.Veterinario;
import model.enums.TipoAtencion;
import model.pago.Credito;
import model.pago.Debito;
import model.pago.Efectivo;
import model.pago.Factura;
import model.pago.LineaFactura;
import model.pago.PoliticaPago;

public class MenuConsola {
    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale ARGENTINA = Locale.forLanguageTag("es-AR");

    private Clinica clinica;
    private ControladorTurnos turnos;
    private ControladorAtencion atencion;
    private ControladorFacturacion facturacion;
    private ControladorDatos datos;
    private Scanner entrada = new Scanner(System.in);

    public MenuConsola(Clinica clinica, ControladorTurnos turnos,
                       ControladorAtencion atencion,
                       ControladorFacturacion facturacion,
                       ControladorDatos datos) {
        this.clinica = clinica;
        this.turnos = turnos;
        this.atencion = atencion;
        this.facturacion = facturacion;
        this.datos = datos;
    }

    public void iniciar() {

        int opcion;

        do {
            System.out.println("\n===== CLÍNICA VETERINARIA PATITAS =====");
            System.out.println("1. Agendar Turno");
            System.out.println("2. Registrar Consulta Veterinaria");
            System.out.println("3. Cobrar Factura");
            System.out.println("4. Cargar/Guardar datos");
            System.out.println("0. Salir");

            opcion = leerEntero("Opción: ");

            ejecutar(opcion);

        } while (opcion != 0);

        System.out.println("Hasta luego.");
    }

    private void ejecutar(int opcion) {

        try {
            switch (opcion) {
                case 1:
                    menuTurnos();
                    break;
                case 2:
                    registrarConsulta();
                    break;
                case 3:
                    cobrarFactura();
                    break;
                case 4:
                    menuDatos();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } catch (IllegalStateException | IllegalArgumentException e) {

            System.out.println("Error: " + e.getMessage());

        } catch (DateTimeParseException e) {

            System.out.println(
                    "Error: fecha inválida. Usá el formato dd/MM/yyyy HH:mm "
                    + "(ejemplo: 05/11/2026 09:30)."
            );
        }
    }

    private void menuTurnos() {

        System.out.println("\n--- Turnos ---");
        System.out.println("1. Agendar turno");
        System.out.println("2. Cancelar turno");
        System.out.println("3. Reprogramar turno");
        System.out.println("4. Ver agenda de un veterinario");
        System.out.println("5. Marcar turno como ausente");
        System.out.println("6. Marcar turno como atendido");
        System.out.println("0. Volver");

        switch (leerEntero("Opción: ")) {
            case 1:
                agendar();
                break;
            case 2:
                cancelar();
                break;
            case 3:
                reprogramar();
                break;
            case 4:
                verAgenda();
                break;
            case 5:
                marcarAusente();
                break;
            case 6:
                marcarAtendido();
                break;
            default:
                break;
        }
    }

    private void agendar() {

        Mascota mascota = elegir("Mascota", clinica.getMascotas(), this::describir);
        Veterinario veterinario = elegir("Veterinario", clinica.getVeterinarios(), this::describir);
        TipoAtencion tipo = elegir("Tipo de atención", Arrays.asList(TipoAtencion.values()), TipoAtencion::name);

        if (mascota == null || veterinario == null || tipo == null) {
            return;
        }

        boolean urgencia = leerSiNo("¿Es una urgencia?");

        LocalDateTime inicio;

        if (urgencia) {
            inicio = LocalDateTime.now().withSecond(0).withNano(0);
        } else {
            inicio = LocalDateTime.parse(
                    leerTexto("Fecha y hora (dd/MM/yyyy HH:mm): "),
                    FORMATO
            );
        }

        boolean sena = false;

        if (mascota.getDueno() != null
                && mascota.getDueno().tieneRestriccionPorAusencias()) {

            System.out.println(
                    "El dueño tiene 3 ausencias seguidas: debe pagar una seña de $"
                    + (int) ControladorTurnos.SENA + "."
            );

            sena = leerSiNo("¿Se cobró la seña?");
        }

        Turno turno = turnos.asignarTurno(
                mascota, veterinario, inicio, tipo, urgencia, sena
        );

        System.out.println("Turno agendado: " + describir(turno));
    }

    private void cancelar() {

        Turno turno = elegirTurnoProgramado();

        if (turno == null) {
            return;
        }

        double cargo = turnos.cancelarTurno(turno, LocalDateTime.now());

        if (cargo > 0) {
            System.out.println(
                    "Turno cancelado fuera de plazo. Cargo del 20 %: "
                    + plata(cargo)
            );
        } else {
            System.out.println("Turno cancelado sin cargo.");
        }
    }

    private void reprogramar() {

        Turno turno = elegirTurnoProgramado();

        if (turno == null) {
            return;
        }

        LocalDateTime nuevo = LocalDateTime.parse(
                leerTexto("Nueva fecha y hora (dd/MM/yyyy HH:mm): "),
                FORMATO
        );

        turnos.reprogramarTurno(turno, nuevo);

        System.out.println("Turno reprogramado: " + describir(turno));
    }

    private void verAgenda() {

        Veterinario veterinario =
                elegir("Veterinario", clinica.getVeterinarios(), this::describir);

        if (veterinario == null) {
            return;
        }

        List<Turno> lista = turnos.turnosProgramados(veterinario);

        if (lista.isEmpty()) {
            System.out.println("Sin turnos programados.");
        }

        for (Turno turno : lista) {
            System.out.println(" - " + describir(turno));
        }
    }

    private void marcarAusente() {

        Turno turno = elegirTurnoProgramado();

        if (turno == null) {
            return;
        }

        turnos.marcarAusente(turno);

        System.out.println(
                "Turno marcado como ausente. Ausencias seguidas del dueño: "
                + turno.getMascota().getDueno().getAusenciasConsecutivas()
        );
    }

    private void marcarAtendido() {

        Turno turno = elegirTurnoProgramado();

        if (turno == null) {
            return;
        }

        turnos.marcarAtendido(turno);

        System.out.println("Turno marcado como atendido.");
    }

    private Turno elegirTurnoProgramado() {

        List<Turno> todos = new ArrayList<>();

        for (Veterinario veterinario : clinica.getVeterinarios()) {
            todos.addAll(turnos.turnosProgramados(veterinario));
        }

        if (todos.isEmpty()) {
            System.out.println("No hay turnos programados.");
            return null;
        }

        return elegir("Turno", todos, this::describir);
    }

    private void registrarConsulta() {

        System.out.println("\n--- Consulta veterinaria ---");
        System.out.println("1. Nueva consulta");
        System.out.println("2. Continuar una consulta sin confirmar");
        System.out.println("0. Volver");

        Consulta consulta = null;

        switch (leerEntero("Opción: ")) {
            case 1:
                consulta = nuevaConsulta();
                break;
            case 2:
                consulta = elegir(
                        "Consulta sin confirmar",
                        atencion.consultasEnCurso(),
                        this::describir
                );
                break;
            default:
                return;
        }

        if (consulta != null) {
            trabajarConsulta(consulta);
        }
    }

    private Consulta nuevaConsulta() {

        Mascota mascota = elegir("Mascota", clinica.getMascotas(), this::describir);
        Veterinario veterinario = elegir("Veterinario que atiende", clinica.getVeterinarios(), this::describir);

        if (mascota == null || veterinario == null) {
            return null;
        }

        String motivo = leerTexto("Motivo: ");
        double peso = leerDecimal("Peso del día en kg (0 si no se pesó): ");
        String diagnostico = leerTexto("Diagnóstico: ");
        String observaciones = leerTexto("Observaciones: ");

        return atencion.registrarConsulta(
                mascota, veterinario, motivo, peso, diagnostico, observaciones
        );
    }

    private void trabajarConsulta(Consulta consulta) {

        int opcion;

        do {
            System.out.println(
                    "\n--- Consulta de " + consulta.getMascota().getNombre() + " ---"
            );
            System.out.println("1. Agregar tratamiento");
            System.out.println("2. Agregar vacuna");
            System.out.println("3. Registrar o corregir peso del día");
            System.out.println("4. Confirmar consulta");
            System.out.println("0. Salir (la consulta queda sin confirmar)");

            opcion = leerEntero("Opción: ");

            try {
                switch (opcion) {
                    case 1:
                        agregarTratamiento(consulta);
                        break;
                    case 2:
                        agregarVacuna(consulta);
                        break;
                    case 3:
                        atencion.registrarPeso(
                                consulta,
                                leerDecimal("Peso del día en kg: ")
                        );
                        System.out.println("Peso registrado.");
                        break;
                    case 4:
                        atencion.confirmarConsulta(consulta);
                        System.out.println(
                                "Consulta confirmada y guardada en la historia clínica. "
                                + "Stock descontado."
                        );
                        return;
                    default:
                        break;
                }
            } catch (IllegalStateException | IllegalArgumentException
                     | StockInsuficienteException e) {

                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void agregarTratamiento(Consulta consulta) {

        Medicamento medicamento =
                elegir("Medicamento", clinica.getMedicamentos(), this::describir);

        if (medicamento == null) {
            return;
        }

        double dosis = leerDecimal("Dosis (mg/ml): ");
        String frecuencia = leerTexto("Frecuencia (ej: cada 8 hs): ");
        String duracion = leerTexto("Duración (ej: 7 días): ");

        atencion.agregarTratamiento(consulta, medicamento, dosis, frecuencia, duracion);

        System.out.println("Tratamiento agregado.");
    }

    private void agregarVacuna(Consulta consulta) throws StockInsuficienteException {

        Vacuna vacuna = elegir("Vacuna", clinica.getVacunas(), this::describir);

        if (vacuna == null) {
            return;
        }

        atencion.agregarVacuna(consulta, vacuna);

        List<VacunaAplicada> aplicadas = consulta.getVacunasAplicadas();

        System.out.println(
                "Vacuna agregada. Próximo refuerzo: "
                + aplicadas.get(aplicadas.size() - 1).getFechaProximoRefuerzo()
        );
    }

    private void cobrarFactura() {

        List<Consulta> pendientes = facturacion.consultasPendientesDeCobro();

        if (pendientes.isEmpty()) {
            System.out.println("No hay consultas pendientes de cobro.");
            return;
        }

        Consulta consulta = elegir("Consulta a facturar", pendientes, this::describir);

        if (consulta == null) {
            return;
        }

        Factura factura = facturacion.generarFactura(consulta);

        System.out.println(
                "\n--- Factura N° " + factura.getNumero()
                + " (" + consulta.getMascota().getNombre() + ") ---"
        );

        for (LineaFactura linea : factura.getLineas()) {
            System.out.println(String.format(
                    " %-45s x%d  %s",
                    linea.getDescripcion(),
                    linea.getCantidad(),
                    plata(linea.calcularSubtotal())
            ));
        }

        System.out.println(" Subtotal: " + plata(factura.calcularSubtotal()));

        System.out.println(
                "Medio de pago: 1. Efectivo (10 % desc.)  2. Débito  "
                + "3. Crédito en cuotas (8 % rec.)"
        );

        PoliticaPago politica;

        switch (leerEntero("Opción: ")) {
            case 1:
                politica = new Efectivo();
                break;
            case 2:
                politica = new Debito();
                break;
            case 3:
                politica = new Credito();
                break;
            default:
                System.out.println("Medio de pago inválido.");
                return;
        }

        boolean plan = consulta.getMascota().getDueno() != null
                && consulta.getMascota().getDueno().isPoseePlanSalud();

        if (plan) {
            System.out.println(
                    "Plan de salud animal: SÍ (15 % desc., no se acumula con efectivo)"
            );
        } else {
            System.out.println("Plan de salud animal: NO");
        }

        double total = facturacion.calcularTotal(factura, politica, plan);

        System.out.println("Total a cobrar: " + plata(total));

        if (leerSiNo("¿Confirmar cobro?")) {

            facturacion.cobrar(factura, politica, plan);

            System.out.println(
                    "Factura N° " + factura.getNumero() + " emitida y pagada."
            );

        } else {
            System.out.println("Cobro cancelado.");
        }
    }

    private void menuDatos() {

        System.out.println("\n--- Datos ---");
        System.out.println("1. Guardar mascotas en archivo");
        System.out.println("2. Cargar mascotas desde archivo");
        System.out.println("3. Guardar los lotes de un producto");
        System.out.println("4. Cargar los lotes de un producto (repone stock)");
        System.out.println("0. Volver");

        int opcion = leerEntero("Opción: ");

        try {
            switch (opcion) {
                case 1:
                    System.out.println(
                            datos.guardarMascotas() + " mascotas guardadas en data/mascotas.csv"
                    );
                    break;
                case 2:
                    cargarMascotas();
                    break;
                case 3:
                    lotes(true);
                    break;
                case 4:
                    lotes(false);
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            System.out.println(
                    "Error: no se pudo completar la operación: " + e.getMessage()
            );
        }
    }

    private void cargarMascotas() throws IOException {

        List<Mascota> nuevas = datos.cargarMascotas();

        System.out.println(
                nuevas.size() + " mascotas nuevas cargadas "
                + "(las que ya existían no se duplican)."
        );

        for (Mascota mascota : nuevas) {

            System.out.println("\nMascota cargada: " + mascota.getNombre()
                    + " (" + mascota.getEspecie() + "), sin dueño.");

            Dueno dueno = elegir(
                    "Dueño para " + mascota.getNombre(),
                    clinica.getDuenos(),
                    Dueno::obtenerNombreCompleto
            );

            if (dueno != null) {
                datos.asignarDueno(mascota, dueno);
            }
        }
    }

    private void lotes(boolean guardar) throws IOException {

        System.out.println("1. Medicamento  2. Vacuna");

        int tipo = leerEntero("Opción: ");

        int cantidad;

        if (tipo == 1) {

            Medicamento medicamento =
                    elegir("Medicamento", clinica.getMedicamentos(), this::describir);

            if (medicamento == null) {
                return;
            }

            if (guardar) {
                cantidad = datos.guardarLotes(medicamento);
            } else {
                cantidad = datos.cargarLotes(medicamento);
            }

        } else if (tipo == 2) {

            Vacuna vacuna = elegir("Vacuna", clinica.getVacunas(), this::describir);

            if (vacuna == null) {
                return;
            }

            if (guardar) {
                cantidad = datos.guardarLotes(vacuna);
            } else {
                cantidad = datos.cargarLotes(vacuna);
            }

        } else {
            return;
        }

        if (guardar) {
            System.out.println(cantidad + " lotes guardados.");
        } else {
            System.out.println(
                    cantidad + " lotes nuevos agregados al stock "
                    + "(los que ya estaban no se duplican)."
            );
        }
    }

    private String describir(Mascota mascota) {

        String dueno = "sin dueño";

        if (mascota.getDueno() != null) {
            dueno = mascota.getDueno().obtenerNombreCompleto();
        }

        return mascota.getNombre() + " (" + mascota.getEspecie()
                + ") - dueño: " + dueno;
    }

    private String describir(Veterinario veterinario) {

        return "Dr/a. " + veterinario.obtenerNombreCompleto()
                + " (" + veterinario.getEspecialidad() + ", "
                + veterinario.getHoraInicio() + "-" + veterinario.getHoraFin() + ")";
    }

    private String describir(Turno turno) {

        String urgencia = "";

        if (turno.isUrgencia()) {
            urgencia = " [URGENCIA]";
        }

        return turno.getFechaHora().format(FORMATO)
                + " | " + turno.getTipoAtencion() + urgencia
                + " | " + turno.getMascota().getNombre()
                + " con " + turno.getVeterinario().obtenerNombreCompleto()
                + " | " + turno.getEstado();
    }

    private String describir(Consulta consulta) {

        String estado = "SIN CONFIRMAR";

        if (consulta.isConfirmada()) {
            estado = "CONFIRMADA";
        }

        return consulta.getFecha().format(FORMATO)
                + " | " + consulta.getMascota().getNombre()
                + " | " + consulta.getMotivo()
                + " | " + estado;
    }

    private String describir(Medicamento medicamento) {

        String alerta = "";

        if (medicamento.tieneStockBajo()) {
            alerta = " - STOCK BAJO";
        }

        return medicamento.getNombre()
                + " (stock " + medicamento.calcularStock() + alerta + ")";
    }

    private String describir(Vacuna vacuna) {

        int stock = 0;

        for (Lote lote : vacuna.getLotes()) {

            if (!lote.estaVencido()) {
                stock = stock + lote.getCantidad();
            }
        }

        return vacuna.getNombre() + " (stock " + stock + ")";
    }

    private <T> T elegir(String titulo, List<T> opciones, Function<T, String> texto) {

        if (opciones.isEmpty()) {
            System.out.println("No hay opciones disponibles para: " + titulo);
            return null;
        }

        System.out.println("\n" + titulo + ":");

        for (int i = 0; i < opciones.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + texto.apply(opciones.get(i)));
        }

        int numero = leerEntero("Elegí un número (0 para cancelar): ");

        if (numero >= 1 && numero <= opciones.size()) {
            return opciones.get(numero - 1);
        }

        return null;
    }

    private String leerTexto(String mensaje) {

        System.out.print(mensaje);

        return entrada.nextLine().trim();
    }

    private int leerEntero(String mensaje) {

        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje));
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número entero.");
            }
        }
    }

    private double leerDecimal(String mensaje) {

        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingresá un número (ejemplo: 12.5).");
            }
        }
    }

    private boolean leerSiNo(String mensaje) {
        return leerTexto(mensaje + " (s/n): ").toLowerCase().startsWith("s");
    }

    private String plata(double monto) {
        return String.format(ARGENTINA, "$ %,.2f", monto);
    }
}

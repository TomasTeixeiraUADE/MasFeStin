package src.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.exception.StockInsuficienteException;
import src.model.comparator.ConsultaPorFechaComparator;
import src.model.enums.EstadoTurno;
import src.model.enums.TipoAtencion;

public class PruebaCompletaDominio {

    public static void main(String[] args) {

        tituloPrincipal("PRUEBA COMPLETA - CLINICA VETERINARIA PATITAS");

        pruebaMascotas();
        pruebaComparableMascotas();
        pruebaPersonal();
        pruebaVeterinarioHorario();
        pruebaDuenoMascotas();
        pruebaAusenciasDueno();
        pruebaTurnos();
        pruebaSuperposicionTurnos();
        pruebaLotes();
        pruebaProductos();
        pruebaConsumoStock();
        pruebaStockInsuficiente();
        pruebaHistoriaClinica();
        pruebaTratamientos();
        pruebaTratamientoSinPeso();
        pruebaVacunas();
        pruebaVacunaEspecieIncorrecta();
        pruebaVacunaEdadInsuficiente();
        pruebaVacunaSinStock();
        pruebaConfirmacionVacuna();
        pruebaComparatorConsultas();

        tituloPrincipal("FIN DE TODAS LAS PRUEBAS");
    }


    // =========================================================
    // 1. MASCOTAS
    // =========================================================

    private static void pruebaMascotas() {

        titulo("1. JERARQUIA DE MASCOTAS");

        Perro perro = new Perro(
                "Toby",
                "Labrador",
                "Macho",
                LocalDate.now().minusYears(4),
                25.5,
                "CHIP-001",
                "Grande"
        );

        Gato gato = new Gato(
                "Luna",
                "Siames",
                "Hembra",
                LocalDate.now().minusYears(2),
                4.3,
                true
        );

        Ave ave = new Ave(
                "Piolin",
                "Canario",
                "Macho",
                LocalDate.now().minusYears(1),
                0.4,
                true
        );

        Conejo conejo = new Conejo(
                "Nube",
                "Mini Lop",
                "Hembra",
                LocalDate.now().minusYears(3),
                2.1
        );

        System.out.println(perro.obtenerDatos());
        System.out.println(gato.obtenerDatos());
        System.out.println(ave.obtenerDatos());
        System.out.println(conejo.obtenerDatos());

        System.out.println();

        System.out.println("Especie perro: " + perro.getEspecie());
        System.out.println("Especie gato: " + gato.getEspecie());
        System.out.println("Especie ave: " + ave.getEspecie());
        System.out.println("Especie conejo: " + conejo.getEspecie());

        System.out.println();

        System.out.println(
                "Edad de Toby en meses: "
                + perro.calcularEdadMeses()
        );

        System.out.println(
                "Edad de Toby en semanas: "
                + perro.calcularEdadSemanas()
        );

        correcto("Jerarquia de Mascota funcionando");
    }


    // =========================================================
    // 2. COMPARABLE
    // =========================================================

    private static void pruebaComparableMascotas() {

        titulo("2. COMPARABLE<MASCOTA> - ORDEN POR NOMBRE");

        List<Mascota> mascotas = new ArrayList<>();

        mascotas.add(
                new Conejo(
                        "Zoe",
                        "Mini Lop",
                        "Hembra",
                        LocalDate.now().minusYears(2),
                        2
                )
        );

        mascotas.add(
                new Gato(
                        "Luna",
                        "Siames",
                        "Hembra",
                        LocalDate.now().minusYears(3),
                        4,
                        true
                )
        );

        mascotas.add(
                new Perro(
                        "Firulais",
                        "Labrador",
                        "Macho",
                        LocalDate.now().minusYears(5),
                        30,
                        "ABC123",
                        "Grande"
                )
        );

        mascotas.add(
                new Ave(
                        "Alfredo",
                        "Canario",
                        "Macho",
                        LocalDate.now().minusYears(1),
                        0.5,
                        true
                )
        );

        System.out.println("ANTES DE ORDENAR:");

        for (Mascota mascota : mascotas) {
            System.out.println("- " + mascota.getNombre());
        }

        Collections.sort(mascotas);

        System.out.println();
        System.out.println("DESPUES DE ORDENAR:");

        for (Mascota mascota : mascotas) {
            System.out.println("- " + mascota.getNombre());
        }

        System.out.println();
        System.out.println(
                "Esperado: Alfredo, Firulais, Luna, Zoe"
        );

        correcto("Comparable<Mascota> funcionando");
    }


    // =========================================================
    // 3. PERSONAL
    // =========================================================

    private static void pruebaPersonal() {

        titulo("3. JERARQUIA DE PERSONAL");

        Veterinario veterinario = crearVeterinario();

        Recepcionista recepcionista =
                new Recepcionista(
                        "Ana",
                        "Martinez",
                        "40111222",
                        "1111111111"
                );

        Administrador administrador =
                new Administrador(
                        "Carlos",
                        "Lopez",
                        "30123456",
                        "1122222222"
                );

        System.out.println(
                "Veterinario: "
                + veterinario.obtenerNombreCompleto()
        );

        System.out.println(
                "Matricula: "
                + veterinario.getMatricula()
        );

        System.out.println(
                "Especialidad: "
                + veterinario.getEspecialidad()
        );

        System.out.println(
                "Recepcionista: "
                + recepcionista.obtenerNombreCompleto()
        );

        System.out.println(
                "Administrador: "
                + administrador.obtenerNombreCompleto()
        );

        correcto("Jerarquia Personal funcionando");
    }


    // =========================================================
    // 4. HORARIO VETERINARIO
    // =========================================================

    private static void pruebaVeterinarioHorario() {

        titulo("4. HORARIO DEL VETERINARIO");

        Veterinario veterinario =
                crearVeterinario();

        boolean consulta =
                veterinario.atiendeEnHorario(
                        LocalTime.of(18, 30),
                        30
                );

        boolean cirugia =
                veterinario.atiendeEnHorario(
                        LocalTime.of(18, 30),
                        120
                );

        System.out.println(
                "Consulta 18:30 a 19:00: "
                + consulta
        );

        System.out.println(
                "Esperado: true"
        );

        System.out.println();

        System.out.println(
                "Cirugia 18:30 a 20:30: "
                + cirugia
        );

        System.out.println(
                "Esperado: false"
        );

        correcto("Validacion de horario funcionando");
    }


    // =========================================================
    // 5. DUENO Y MASCOTAS
    // =========================================================

    private static void pruebaDuenoMascotas() {

        titulo("5. RELACION DUENO - MASCOTAS");

        Dueno dueno = crearDueno();

        Perro perro = crearPerro("Toby");
        Gato gato = crearGato("Luna");

        dueno.agregarMascota(perro);
        dueno.agregarMascota(gato);

        System.out.println(
                "Dueño: "
                + dueno.obtenerNombreCompleto()
        );

        System.out.println(
                "Cantidad de mascotas: "
                + dueno.getMascotas().size()
        );

        System.out.println("Mascotas:");

        for (Mascota mascota : dueno.getMascotas()) {
            System.out.println(
                    "- "
                    + mascota.getNombre()
                    + " / "
                    + mascota.getEspecie()
            );
        }

        System.out.println();

        System.out.println(
                "Dueño de Toby desde Mascota: "
                + perro.getDueno()
                       .obtenerNombreCompleto()
        );

        correcto("Asociacion Dueno-Mascota funcionando");
    }


    // =========================================================
    // 6. AUSENCIAS
    // =========================================================

    private static void pruebaAusenciasDueno() {

        titulo("6. AUSENCIAS CONSECUTIVAS DEL DUENO");

        Dueno dueno = crearDueno();

        System.out.println(
                "Ausencias iniciales: "
                + dueno.getAusenciasConsecutivas()
        );

        dueno.registrarAusencia();
        dueno.registrarAusencia();

        System.out.println(
                "Con 2 ausencias tiene restriccion: "
                + dueno.tieneRestriccionPorAusencias()
        );

        System.out.println(
                "Esperado: false"
        );

        dueno.registrarAusencia();

        System.out.println(
                "Con 3 ausencias tiene restriccion: "
                + dueno.tieneRestriccionPorAusencias()
        );

        System.out.println(
                "Esperado: true"
        );

        dueno.reiniciarAusencias();

        System.out.println(
                "Despues de reiniciar: "
                + dueno.getAusenciasConsecutivas()
        );

        correcto("Regla de ausencias funcionando");
    }


    // =========================================================
    // 7. TURNOS
    // =========================================================

    private static void pruebaTurnos() {

        titulo("7. TURNOS");

        Dueno dueno = crearDueno();
        Perro perro = crearPerro("Toby");
        dueno.agregarMascota(perro);

        Veterinario veterinario =
                crearVeterinario();

        Turno consultaGeneral =
                new Turno(
                        perro,
                        veterinario,
                        LocalDateTime.now()
                                .plusDays(1)
                                .withHour(10)
                                .withMinute(0),
                        TipoAtencion.CONSULTA_GENERAL,
                        false
                );

        Turno vacunacion =
                new Turno(
                        perro,
                        veterinario,
                        LocalDateTime.now()
                                .plusDays(2)
                                .withHour(11)
                                .withMinute(0),
                        TipoAtencion.VACUNACION,
                        false
                );

        Turno cirugia =
                new Turno(
                        perro,
                        veterinario,
                        LocalDateTime.now()
                                .plusDays(3)
                                .withHour(12)
                                .withMinute(0),
                        TipoAtencion.CIRUGIA,
                        false
                );

        System.out.println(
                "Consulta general: "
                + consultaGeneral.calcularDuracion()
                + " minutos"
        );

        System.out.println(
                "Vacunacion: "
                + vacunacion.calcularDuracion()
                + " minutos"
        );

        System.out.println(
                "Cirugia: "
                + cirugia.calcularDuracion()
                + " minutos"
        );

        System.out.println();

        System.out.println(
                "Estado inicial: "
                + consultaGeneral.getEstado()
        );

        System.out.println(
                "Esperado: "
                + EstadoTurno.PROGRAMADO
        );

        LocalDateTime nuevaFecha =
                consultaGeneral.getFechaHora()
                        .plusDays(2);

        consultaGeneral.reprogramar(nuevaFecha);

        System.out.println(
                "Fecha despues de reprogramar: "
                + consultaGeneral.getFechaHora()
        );

        consultaGeneral.registrarSena();

        System.out.println(
                "Seña pagada: "
                + consultaGeneral.isSenaPagada()
        );

        consultaGeneral.marcarAtendido();

        System.out.println(
                "Estado final: "
                + consultaGeneral.getEstado()
        );

        correcto("Turnos funcionando");
    }


    // =========================================================
    // 8. SUPERPOSICION
    // =========================================================

    private static void pruebaSuperposicionTurnos() {

        titulo("8. SUPERPOSICION DE TURNOS");

        Dueno dueno = crearDueno();
        Perro perro = crearPerro("Toby");

        dueno.agregarMascota(perro);

        Veterinario veterinario =
                crearVeterinario();

        LocalDateTime fecha =
                LocalDateTime.now()
                        .plusDays(1)
                        .withHour(10)
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0);

        Turno turno1 =
                new Turno(
                        perro,
                        veterinario,
                        fecha,
                        TipoAtencion.CONSULTA_GENERAL,
                        false
                );

        Turno turno2 =
                new Turno(
                        perro,
                        veterinario,
                        fecha.plusMinutes(15),
                        TipoAtencion.CONSULTA_GENERAL,
                        false
                );

        Turno turno3 =
                new Turno(
                        perro,
                        veterinario,
                        fecha.plusMinutes(30),
                        TipoAtencion.CONSULTA_GENERAL,
                        false
                );

        System.out.println(
                "Turno 1: 10:00 - 10:30"
        );

        System.out.println(
                "Turno 2: 10:15 - 10:45"
        );

        System.out.println(
                "¿Se superponen?: "
                + turno1.seSuperponeCon(turno2)
        );

        System.out.println(
                "Esperado: true"
        );

        System.out.println();

        System.out.println(
                "Turno 3: 10:30 - 11:00"
        );

        System.out.println(
                "¿Turno 1 se superpone con Turno 3?: "
                + turno1.seSuperponeCon(turno3)
        );

        System.out.println(
                "Esperado: false"
        );

        correcto("Deteccion de superposicion funcionando");
    }


    // =========================================================
    // 9. LOTES
    // =========================================================

    private static void pruebaLotes() {

        titulo("9. LOTES");

        Lote loteVigente =
                new Lote(
                        "L-001",
                        LocalDate.now().plusMonths(6),
                        20
                );

        Lote loteVencido =
                new Lote(
                        "L-002",
                        LocalDate.now().minusDays(1),
                        50
                );

        System.out.println(
                "Lote vigente vencido?: "
                + loteVigente.estaVencido()
        );

        System.out.println(
                "Esperado: false"
        );

        System.out.println();

        System.out.println(
                "Lote vencido vencido?: "
                + loteVencido.estaVencido()
        );

        System.out.println(
                "Esperado: true"
        );

        correcto("Control de vencimiento funcionando");
    }


    // =========================================================
    // 10. PRODUCTOS
    // =========================================================

    private static void pruebaProductos() {

        titulo("10. PRODUCTOS Y STOCK");

        Medicamento medicamento =
                new Medicamento(
                        "MED001",
                        "Amoxicilina",
                        10
                );

        medicamento.agregarLote(
                new Lote(
                        "A1",
                        LocalDate.now().plusMonths(1),
                        5
                )
        );

        medicamento.agregarLote(
                new Lote(
                        "A2",
                        LocalDate.now().plusMonths(6),
                        20
                )
        );

        medicamento.agregarLote(
                new Lote(
                        "A3",
                        LocalDate.now().minusMonths(1),
                        100
                )
        );

        System.out.println(
                "Stock disponible: "
                + medicamento.calcularStock()
        );

        System.out.println(
                "Esperado: 25"
        );

        System.out.println(
                "¿Stock bajo?: "
                + medicamento.tieneStockBajo()
        );

        System.out.println(
                "Esperado: false"
        );

        correcto("Calculo de stock funcionando");
    }


    // =========================================================
    // 11. CONSUMO FEFO
    // =========================================================

    private static void pruebaConsumoStock() {

        titulo("11. CONSUMO DEL LOTE QUE VENCE PRIMERO");

        Medicamento medicamento =
                new Medicamento(
                        "MED002",
                        "Antibiotico",
                        2
                );

        Lote lote1 =
                new Lote(
                        "PROXIMO",
                        LocalDate.now().plusMonths(1),
                        3
                );

        Lote lote2 =
                new Lote(
                        "LEJANO",
                        LocalDate.now().plusMonths(8),
                        10
                );

        medicamento.agregarLote(lote1);
        medicamento.agregarLote(lote2);

        System.out.println(
                "Antes:"
        );

        System.out.println(
                "PROXIMO = "
                + lote1.getCantidad()
        );

        System.out.println(
                "LEJANO = "
                + lote2.getCantidad()
        );

        try {

            medicamento.consumirStock(5);

            System.out.println();
            System.out.println("Despues de consumir 5:");

            System.out.println(
                    "PROXIMO = "
                    + lote1.getCantidad()
            );

            System.out.println(
                    "LEJANO = "
                    + lote2.getCantidad()
            );

            System.out.println();

            System.out.println(
                    "Esperado: PROXIMO = 0"
            );

            System.out.println(
                    "Esperado: LEJANO = 8"
            );

        } catch (StockInsuficienteException e) {

            error(e.getMessage());
        }

        correcto("Consumo por vencimiento funcionando");
    }


    // =========================================================
    // 12. STOCK INSUFICIENTE
    // =========================================================

    private static void pruebaStockInsuficiente() {

        titulo("12. STOCK INSUFICIENTE");

        Medicamento medicamento =
                new Medicamento(
                        "MED003",
                        "Medicamento escaso",
                        1
                );

        medicamento.agregarLote(
                new Lote(
                        "ESCASO-01",
                        LocalDate.now().plusMonths(1),
                        2
                )
        );

        try {

            medicamento.consumirStock(50);

            error(
                    "ERROR: deberia haber lanzado "
                    + "StockInsuficienteException"
            );

        } catch (StockInsuficienteException e) {

            System.out.println(
                    "Excepcion capturada correctamente:"
            );

            System.out.println(
                    e.getMessage()
            );

            correcto(
                    "StockInsuficienteException funcionando"
            );
        }
    }


    // =========================================================
    // 13. HISTORIA CLINICA
    // =========================================================

    private static void pruebaHistoriaClinica() {

        titulo("13. HISTORIA CLINICA");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        HistoriaClinica historia =
                perro.getHistoriaClinica();

        historia.registrarConsulta(
                LocalDateTime.now(),
                "Control general",
                25.0,
                "Mascota saludable",
                "Sin observaciones",
                veterinario
        );

        historia.registrarConsulta(
                LocalDateTime.now().plusMonths(1),
                "Control",
                25.5,
                "Sin novedades",
                "Todo normal",
                veterinario
        );

        System.out.println(
                "Cantidad de consultas: "
                + historia.getConsultas().size()
        );

        System.out.println(
                "Esperado: 2"
        );

        correcto("Historia clinica funcionando");
    }


    // =========================================================
    // 14. TRATAMIENTOS
    // =========================================================

    private static void pruebaTratamientos() {

        titulo("14. TRATAMIENTO CON PESO REGISTRADO");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        Consulta consulta =
                perro.getHistoriaClinica()
                        .registrarConsulta(
                                LocalDateTime.now(),
                                "Infeccion",
                                25.0,
                                "Infeccion leve",
                                "Control en 7 dias",
                                veterinario
                        );

        Medicamento medicamento =
                new Medicamento(
                        "MED100",
                        "Amoxicilina",
                        5
                );

        consulta.agregarTratamiento(
                medicamento,
                5,
                "Cada 8 horas",
                "7 dias"
        );

        System.out.println(
                "Tratamientos registrados: "
                + consulta.getTratamientos().size()
        );

        System.out.println(
                "Esperado: 1"
        );

        Tratamiento tratamiento =
                consulta.getTratamientos().get(0);

        System.out.println(
                tratamiento.obtenerDescripcion()
        );

        correcto("Registro de tratamientos funcionando");
    }


    // =========================================================
    // 15. TRATAMIENTO SIN PESO
    // =========================================================

    private static void pruebaTratamientoSinPeso() {

        titulo("15. ERROR AL INDICAR MEDICAMENTO SIN PESO");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        Consulta consulta =
                perro.getHistoriaClinica()
                        .registrarConsulta(
                                LocalDateTime.now(),
                                "Dolor",
                                null,
                                "Pendiente",
                                "Sin peso",
                                veterinario
                        );

        Medicamento medicamento =
                new Medicamento(
                        "MED200",
                        "Medicamento X",
                        5
                );

        try {

            consulta.agregarTratamiento(
                    medicamento,
                    10,
                    "Cada 12 horas",
                    "3 dias"
            );

            error(
                    "ERROR: permitio tratamiento sin peso"
            );

        } catch (IllegalStateException e) {

            System.out.println(
                    "Error capturado correctamente:"
            );

            System.out.println(
                    e.getMessage()
            );

            correcto(
                    "Regla de medicamento sujeto a peso funcionando"
            );
        }

        System.out.println();
        System.out.println(
                "Ahora registramos peso..."
        );

        consulta.registrarPeso(20);

        consulta.agregarTratamiento(
                medicamento,
                10,
                "Cada 12 horas",
                "3 dias"
        );

        System.out.println(
                "Tratamientos despues de registrar peso: "
                + consulta.getTratamientos().size()
        );

        System.out.println(
                "Peso actual de la mascota: "
                + perro.getPesoActual()
        );
    }


    // =========================================================
    // 16. VACUNA CORRECTA
    // =========================================================

    private static void pruebaVacunas() {

        titulo("16. VACUNA - ESPECIE Y EDAD VALIDAS");

        Perro perro = crearPerro("Toby");

        Vacuna vacuna =
                new Vacuna(
                        "Antirrabica",
                        12,
                        365
                );

        vacuna.agregarEspecie("PERRO");
        vacuna.agregarEspecie("GATO");

        System.out.println(
                "¿Aplica a Toby?: "
                + vacuna.aplicaA(perro)
        );

        System.out.println(
                "Esperado: true"
        );

        System.out.println(
                "¿Tiene edad suficiente?: "
                + vacuna.permiteEdad(perro)
        );

        System.out.println(
                "Esperado: true"
        );

        LocalDate hoy = LocalDate.now();

        System.out.println(
                "Fecha aplicacion: "
                + hoy
        );

        System.out.println(
                "Proximo refuerzo: "
                + vacuna.calcularProximoRefuerzo(hoy)
        );

        correcto("Validacion basica de vacunas funcionando");
    }


    // =========================================================
    // 17. ESPECIE INCORRECTA
    // =========================================================

    private static void pruebaVacunaEspecieIncorrecta() {

        titulo("17. ERROR - VACUNA NO APLICA A LA ESPECIE");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        Vacuna vacuna =
                new Vacuna(
                        "Vacuna exclusiva gatos",
                        5,
                        365
                );

        vacuna.agregarEspecie("GATO");

        vacuna.agregarLote(
                new Lote(
                        "VG01",
                        LocalDate.now().plusYears(1),
                        10
                )
        );

        Consulta consulta =
                perro.getHistoriaClinica()
                        .registrarConsulta(
                                LocalDateTime.now(),
                                "Vacunacion",
                                25.0,
                                "Sin diagnostico",
                                "",
                                veterinario
                        );

        try {

            consulta.aplicarVacuna(
                    vacuna,
                    LocalDate.now()
            );

            error(
                    "ERROR: permitio aplicar vacuna de gato a perro"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Error capturado correctamente:"
            );

            System.out.println(
                    e.getMessage()
            );

            correcto(
                    "Validacion de especie funcionando"
            );

        } catch (StockInsuficienteException e) {

            error(e.getMessage());
        }
    }


    // =========================================================
    // 18. EDAD INSUFICIENTE
    // =========================================================

    private static void pruebaVacunaEdadInsuficiente() {

        titulo("18. ERROR - EDAD INSUFICIENTE PARA VACUNA");

        Perro cachorro =
                new Perro(
                        "Cachorro",
                        "Labrador",
                        "Macho",
                        LocalDate.now().minusWeeks(4),
                        5,
                        "CACH01",
                        "Pequeno"
                );

        Veterinario veterinario =
                crearVeterinario();

        Vacuna vacuna =
                new Vacuna(
                        "Vacuna mayores de 12 semanas",
                        12,
                        365
                );

        vacuna.agregarEspecie("PERRO");

        vacuna.agregarLote(
                new Lote(
                        "VAC-EDAD",
                        LocalDate.now().plusYears(1),
                        10
                )
        );

        Consulta consulta =
                cachorro.getHistoriaClinica()
                        .registrarConsulta(
                                LocalDateTime.now(),
                                "Vacunacion",
                                5.0,
                                "",
                                "",
                                veterinario
                        );

        try {

            consulta.aplicarVacuna(
                    vacuna,
                    LocalDate.now()
            );

            error(
                    "ERROR: permitio vacuna con edad insuficiente"
            );

        } catch (IllegalArgumentException e) {

            System.out.println(
                    "Error capturado correctamente:"
            );

            System.out.println(
                    e.getMessage()
            );

            correcto(
                    "Validacion de edad funcionando"
            );

        } catch (StockInsuficienteException e) {

            error(e.getMessage());
        }
    }


    // =========================================================
    // 19. VACUNA SIN STOCK
    // =========================================================

    private static void pruebaVacunaSinStock() {

        titulo("19. ERROR - VACUNA SIN STOCK VIGENTE");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        Vacuna vacuna =
                new Vacuna(
                        "Vacuna sin stock",
                        1,
                        365
                );

        vacuna.agregarEspecie("PERRO");

        vacuna.agregarLote(
                new Lote(
                        "VENCIDO",
                        LocalDate.now().minusDays(1),
                        100
                )
        );

        vacuna.agregarLote(
                new Lote(
                        "VACIO",
                        LocalDate.now().plusYears(1),
                        0
                )
        );

        Consulta consulta =
                perro.getHistoriaClinica()
                        .registrarConsulta(
                                LocalDateTime.now(),
                                "Vacunacion",
                                25.0,
                                "",
                                "",
                                veterinario
                        );

        try {

            consulta.aplicarVacuna(
                    vacuna,
                    LocalDate.now()
            );

            error(
                    "ERROR: deberia detectar falta de stock"
            );

        } catch (StockInsuficienteException e) {

            System.out.println(
                    "Stock insuficiente detectado:"
            );

            System.out.println(
                    e.getMessage()
            );

            correcto(
                    "Validacion de stock de vacuna funcionando"
            );

        } catch (IllegalArgumentException e) {

            error(e.getMessage());
        }
    }


    // =========================================================
    // 20. CONFIRMAR VACUNA
    // =========================================================

    private static void pruebaConfirmacionVacuna() {

        titulo("20. CONFIRMACION DE VACUNA Y DESCUENTO DE STOCK");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        Vacuna vacuna =
                new Vacuna(
                        "Antirrabica",
                        12,
                        365
                );

        vacuna.agregarEspecie("PERRO");

        Lote lote =
                new Lote(
                        "ANT-01",
                        LocalDate.now().plusYears(1),
                        10
                );

        vacuna.agregarLote(lote);

        Consulta consulta =
                perro.getHistoriaClinica()
                        .registrarConsulta(
                                LocalDateTime.now(),
                                "Vacunacion",
                                25.0,
                                "",
                                "",
                                veterinario
                        );

        try {

            VacunaAplicada aplicada =
                    consulta.aplicarVacuna(
                            vacuna,
                            LocalDate.now()
                    );

            System.out.println(
                    "Vacuna agregada a consulta."
            );

            System.out.println(
                    "Stock ANTES de confirmar: "
                    + lote.getCantidad()
            );

            System.out.println(
                    "Fecha proximo refuerzo: "
                    + aplicada.getFechaProximoRefuerzo()
            );

            consulta.confirmar();

            System.out.println(
                    "Stock DESPUES de confirmar: "
                    + lote.getCantidad()
            );

            System.out.println(
                    "Esperado: 9"
            );

            System.out.println(
                    "Consulta confirmada: "
                    + consulta.isConfirmada()
            );

            correcto(
                    "Confirmacion y descuento de vacuna funcionando"
            );

        } catch (StockInsuficienteException e) {

            error(e.getMessage());
        }
    }


    // =========================================================
    // 21. COMPARATOR CONSULTAS
    // =========================================================

    private static void pruebaComparatorConsultas() {

        titulo("21. COMPARATOR<CONSULTA> - ORDEN POR FECHA");

        Perro perro = crearPerro("Toby");

        Veterinario veterinario =
                crearVeterinario();

        HistoriaClinica historia =
                perro.getHistoriaClinica();

        historia.registrarConsulta(
                LocalDateTime.now().plusDays(20),
                "Consulta futura",
                25.0,
                "",
                "",
                veterinario
        );

        historia.registrarConsulta(
                LocalDateTime.now().minusDays(30),
                "Consulta anterior",
                24.0,
                "",
                "",
                veterinario
        );

        historia.registrarConsulta(
                LocalDateTime.now(),
                "Consulta actual",
                25.0,
                "",
                "",
                veterinario
        );

        System.out.println(
                "ORDEN ORIGINAL:"
        );

        for (Consulta consulta :
                historia.getConsultas()) {

            System.out.println(
                    consulta.getFecha()
                    + " - "
                    + consulta.getMotivo()
            );
        }

        List<Consulta> copia =
                new ArrayList<>(
                        historia.getConsultas()
                );

        copia.sort(
                new ConsultaPorFechaComparator()
        );

        System.out.println();
        System.out.println(
                "ORDENADAS POR FECHA:"
        );

        for (Consulta consulta : copia) {

            System.out.println(
                    consulta.getFecha()
                    + " - "
                    + consulta.getMotivo()
            );
        }

        System.out.println();
        System.out.println(
                "Tambien usando el metodo de HistoriaClinica:"
        );

        for (Consulta consulta :
                historia
                    .obtenerConsultasOrdenadasPorFecha()) {

            System.out.println(
                    consulta.getFecha()
                    + " - "
                    + consulta.getMotivo()
            );
        }

        correcto(
                "Comparator<Consulta> funcionando"
        );
    }


    // =========================================================
    // OBJETOS AUXILIARES
    // =========================================================

    private static Dueno crearDueno() {

        return new Dueno(
                "Juan",
                "Perez",
                "30111222",
                "1155555555",
                "juan@mail.com",
                true
        );
    }


    private static Perro crearPerro(
            String nombre) {

        return new Perro(
                nombre,
                "Labrador",
                "Macho",
                LocalDate.now().minusYears(4),
                25.0,
                "CHIP-" + nombre,
                "Grande"
        );
    }


    private static Gato crearGato(
            String nombre) {

        return new Gato(
                nombre,
                "Siames",
                "Hembra",
                LocalDate.now().minusYears(3),
                4.0,
                true
        );
    }


    private static Veterinario crearVeterinario() {

        return new Veterinario(
                "Laura",
                "Gomez",
                "30222333",
                "1144444444",
                "MAT-001",
                "Clinica General",
                LocalTime.of(9, 0),
                LocalTime.of(19, 0)
        );
    }


    // =========================================================
    // FORMATO DE CONSOLA
    // =========================================================

    private static void titulo(
            String texto) {

        System.out.println();
        System.out.println(
                "============================================================"
        );

        System.out.println(texto);

        System.out.println(
                "============================================================"
        );
    }


    private static void tituloPrincipal(
            String texto) {

        System.out.println();
        System.out.println();
        System.out.println(
                "############################################################"
        );

        System.out.println(
                "### " + texto
        );

        System.out.println(
                "############################################################"
        );
        System.out.println();
    }


    private static void correcto(
            String mensaje) {

        System.out.println();
        System.out.println(
                "[OK] " + mensaje
        );
    }


    private static void error(
            String mensaje) {

        System.out.println();
        System.out.println(
                "[ERROR] " + mensaje
        );
    }
}
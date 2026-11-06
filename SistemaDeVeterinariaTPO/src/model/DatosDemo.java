package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class DatosDemo {

    public static void cargar(Clinica clinica) {

        LocalDate hoy = LocalDate.now();

        clinica.getVeterinarios().add(new Veterinario(
                "Lucía", "Fernández", "30111000", "1155550001",
                "MP-1001", "Clínica general",
                LocalTime.of(8, 0), LocalTime.of(14, 0)));

        clinica.getVeterinarios().add(new Veterinario(
                "Martín", "Gómez", "29222000", "1155550002",
                "MP-1002", "Cirugía",
                LocalTime.of(12, 0), LocalTime.of(20, 0)));

        Dueno ana = new Dueno(
                "Ana", "Pérez", "30111222", "1166660001",
                "ana@mail.com", true);

        Dueno juan = new Dueno(
                "Juan", "Rodríguez", "28999888", "1166660002",
                "juan@mail.com", false);

        clinica.getDuenos().add(ana);
        clinica.getDuenos().add(juan);

        Perro firulais = new Perro(
                "Firulais", "Labrador", "Macho",
                hoy.minusYears(3), 28.5, "CHIP-123", "Grande");

        Gato misha = new Gato(
                "Misha", "Siamés", "Hembra",
                hoy.minusYears(2), 4.2, true);

        Perro rocky = new Perro(
                "Rocky", "Mestizo", "Macho",
                hoy.minusWeeks(8), 5.0, "CHIP-456", "Mediano");

        Conejo copito = new Conejo(
                "Copito", "Bélier", "Macho",
                hoy.minusYears(1), 2.1);

        ana.agregarMascota(firulais);
        ana.agregarMascota(misha);
        juan.agregarMascota(rocky);
        juan.agregarMascota(copito);

        clinica.getMascotas().add(firulais);
        clinica.getMascotas().add(misha);
        clinica.getMascotas().add(rocky);
        clinica.getMascotas().add(copito);

        Medicamento amoxicilina = new Medicamento("MED-01", "Amoxicilina 250mg", 10);
        amoxicilina.agregarLote(new Lote("A-001", hoy.plusMonths(2), 15));
        amoxicilina.agregarLote(new Lote("A-002", hoy.plusMonths(8), 30));

        Medicamento antiparasitario = new Medicamento("MED-02", "Antiparasitario", 5);
        antiparasitario.agregarLote(new Lote("P-001", hoy.plusMonths(6), 8));

        Medicamento antiinflamatorio = new Medicamento("MED-03", "Antiinflamatorio", 5);
        antiinflamatorio.agregarLote(new Lote("I-001", hoy.minusDays(10), 20));

        clinica.getMedicamentos().add(amoxicilina);
        clinica.getMedicamentos().add(antiparasitario);
        clinica.getMedicamentos().add(antiinflamatorio);

        Vacuna antirrabica = new Vacuna("Antirrábica", 12, 365);
        antirrabica.agregarEspecie("PERRO");
        antirrabica.agregarEspecie("GATO");
        antirrabica.agregarLote(new Lote("R-100", hoy.plusMonths(1), 6));
        antirrabica.agregarLote(new Lote("R-101", hoy.plusMonths(10), 20));

        Vacuna quintuple = new Vacuna("Quíntuple canina", 6, 365);
        quintuple.agregarEspecie("PERRO");
        quintuple.agregarLote(new Lote("Q-200", hoy.plusMonths(5), 10));

        clinica.getVacunas().add(antirrabica);
        clinica.getVacunas().add(quintuple);

        clinica.getPrecios().definirPrecioProducto("Amoxicilina 250mg", 1800);
        clinica.getPrecios().definirPrecioProducto("Antiparasitario", 2500);
        clinica.getPrecios().definirPrecioProducto("Antiinflamatorio", 3200);
        clinica.getPrecios().definirPrecioProducto("Antirrábica", 6000);
        clinica.getPrecios().definirPrecioProducto("Quíntuple canina", 9000);
    }
}

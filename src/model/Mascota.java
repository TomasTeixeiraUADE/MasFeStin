package src.model;

import java.time.LocalDate;
import java.time.Period;

public abstract class Mascota implements Comparable<Mascota> {
// Los objetos Mascota saben compararse entre sí.

    private String nombre;
    private String raza;
    private String sexo;
    private LocalDate fechaNacimiento;
    private double pesoActual;

    private Dueno dueno;

    private HistoriaClinica historiaClinica;

    public Mascota(String nombre, String raza, String sexo, LocalDate fechaNacimiento, double pesoActual) {
        this.nombre = nombre;
        this.raza = raza;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
        this.pesoActual = pesoActual;

        this.historiaClinica =
            new HistoriaClinica(this);
    }

    public String getNombre() {
        return nombre;
    }

    public String getRaza() {
        return raza;
    }

    public String getSexo() {
        return sexo;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public double getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(double pesoActual) {
        this.pesoActual = pesoActual;
    }

    public int calcularEdadMeses() {
        LocalDate hoy = LocalDate.now();

        Period edad = Period.between(fechaNacimiento, hoy);

        return edad.getYears() * 12 + edad.getMonths();
    }

    public abstract String obtenerDatos();

    @Override
    public int compareTo(Mascota otraMascota) {

        return this.nombre.compareToIgnoreCase(
                otraMascota.getNombre()
        );
    }

    public Dueno getDueno() {
        return dueno;
    }

    public void asignarDueno(Dueno dueno) {
        this.dueno = dueno;
    }

    public long calcularEdadSemanas() {

        long dias =
                java.time.temporal.ChronoUnit.DAYS.between(
                        fechaNacimiento,
                        LocalDate.now()
                );

        return dias / 7;
    }

    public abstract String getEspecie();

    public HistoriaClinica getHistoriaClinica() {
        return historiaClinica;
    }
}

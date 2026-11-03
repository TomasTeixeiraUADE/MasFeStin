package model;

public class Tratamiento {
    private Medicamento medicamento;
    private double dosis;
    private String frecuencia;
    private String duracion;

    public Tratamiento(Medicamento medicamento, double dosis, String frecuencia, String duracion) {
        this.medicamento = medicamento;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.duracion = duracion;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public double getDosis() {
        return dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public String getDuracion() {
        return duracion;
    }

    public String obtenerDescripcion() {

        return medicamento.getNombre()
                + " - Dosis: " + dosis
                + " - Frecuencia: " + frecuencia
                + " - Duracion: " + duracion;
    }
}
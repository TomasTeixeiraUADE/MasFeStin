package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import exception.StockInsuficienteException;
import model.Clinica;
import model.Consulta;
import model.Insumo;
import model.Mascota;
import model.Medicamento;
import model.NotaRectificatoria;
import model.Tratamiento;
import model.Vacuna;
import model.Veterinario;

public class ControladorAtencion {
    private Clinica clinica;

    public ControladorAtencion(Clinica clinica) {
        this.clinica = clinica;
    }

    public Consulta registrarConsulta(Mascota mascota, Veterinario veterinario,
                                      String motivo, double peso,
                                      String diagnostico, String observaciones) {

        return mascota.getHistoriaClinica().registrarConsulta(
                LocalDateTime.now(),
                motivo,
                peso,
                diagnostico,
                observaciones,
                veterinario
        );
    }

    public List<Consulta> consultasEnCurso() {

        List<Consulta> resultado = new ArrayList<>();

        for (Mascota mascota : clinica.getMascotas()) {

            for (Consulta consulta : mascota.getHistoriaClinica().getConsultas()) {

                if (!consulta.isConfirmada()) {
                    resultado.add(consulta);
                }
            }
        }

        return resultado;
    }

    public void registrarPeso(Consulta consulta, double peso) {

        exigirEnCurso(consulta);

        consulta.registrarPeso(peso);
    }

    public void agregarTratamiento(Consulta consulta, Medicamento medicamento,
                                   double dosis, String frecuencia, String duracion) {

        exigirEnCurso(consulta);

        consulta.agregarTratamiento(medicamento, dosis, frecuencia, duracion);
    }

    public void agregarVacuna(Consulta consulta, Vacuna vacuna)
            throws StockInsuficienteException {

        exigirEnCurso(consulta);

        consulta.aplicarVacuna(vacuna, LocalDate.now());
    }

    public void agregarInsumo(Consulta consulta, Insumo insumo, int cantidad) {
        exigirEnCurso(consulta);
        consulta.agregarInsumo(insumo, cantidad);
    }

    public NotaRectificatoria rectificarConsulta(Consulta consulta, String texto, Veterinario veterinario) {
        return consulta.agregarNotaRectificatoria(texto, veterinario);
    }

    public void confirmarConsulta(Consulta consulta)
            throws StockInsuficienteException {

        exigirEnCurso(consulta);

        Map<Medicamento, Integer> requerido = new LinkedHashMap<>();

        for (Tratamiento tratamiento : consulta.getTratamientos()) {
            requerido.merge(tratamiento.getMedicamento(), 1, Integer::sum);
        }

        for (Map.Entry<Medicamento, Integer> entrada : requerido.entrySet()) {

            if (entrada.getKey().calcularStock() < entrada.getValue()) {
                throw new StockInsuficienteException(
                        "Sin stock suficiente de " + entrada.getKey().getNombre()
                );
            }
        }

        for (Map.Entry<Insumo, Integer> entrada : consulta.getInsumosUtilizados().entrySet()) {
            if (entrada.getKey().calcularStock() < entrada.getValue()) {
                throw new StockInsuficienteException(
                        "Sin stock suficiente de " + entrada.getKey().getNombre()
                );
            }
        }

        consulta.confirmar();

        for (Map.Entry<Medicamento, Integer> entrada : requerido.entrySet()) {
            entrada.getKey().consumirStock(entrada.getValue());
        }

        for (Map.Entry<Insumo, Integer> entrada : consulta.getInsumosUtilizados().entrySet()) {
            entrada.getKey().consumirStock(entrada.getValue());
        }
    }

    private void exigirEnCurso(Consulta consulta) {

        if (consulta.isConfirmada()) {
            throw new IllegalStateException(
                    "La consulta ya fue confirmada y no se puede modificar."
            );
        }
    }
}

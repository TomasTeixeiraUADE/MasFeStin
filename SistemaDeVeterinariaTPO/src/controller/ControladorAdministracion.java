package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Clinica;
import model.Consulta;
import model.Insumo;
import model.Mascota;
import model.Medicamento;
import model.Producto;
import model.RecordatorioVacuna;
import model.VacunaAplicada;

public class ControladorAdministracion {
    private Clinica clinica;

    public ControladorAdministracion(Clinica clinica) {
        this.clinica = clinica;
    }

    public List<Producto> productosConStockBajo() {
        List<Producto> resultado = new ArrayList<>();

        for (Medicamento medicamento : clinica.getMedicamentos()) {
            if (medicamento.tieneStockBajo()) {
                resultado.add(medicamento);
            }
        }

        for (Insumo insumo : clinica.getInsumos()) {
            if (insumo.tieneStockBajo()) {
                resultado.add(insumo);
            }
        }

        return resultado;
    }

    public List<RecordatorioVacuna> vacunasProximasARefuerzo(LocalDate hoy) {
        return buscarVacunas(hoy, false);
    }

    public List<RecordatorioVacuna> vacunasVencidas(LocalDate hoy) {
        return buscarVacunas(hoy, true);
    }

    private List<RecordatorioVacuna> buscarVacunas(LocalDate hoy, boolean vencidas) {
        List<RecordatorioVacuna> resultado = new ArrayList<>();

        for (Mascota mascota : clinica.getMascotas()) {
            for (Consulta consulta : mascota.getHistoriaClinica().getConsultas()) {
                for (VacunaAplicada aplicada : consulta.getVacunasAplicadas()) {
                    boolean coincide = vencidas
                            ? aplicada.estaVencida(hoy)
                            : aplicada.debeEmitirRecordatorio(hoy);
                    if (coincide) {
                        resultado.add(new RecordatorioVacuna(mascota, aplicada));
                    }
                }
            }
        }

        return resultado;
    }

    public int emitirRecordatorios(LocalDate hoy) {
        List<RecordatorioVacuna> recordatorios = vacunasProximasARefuerzo(hoy);

        for (RecordatorioVacuna recordatorio : recordatorios) {
            String destinatario = recordatorio.getMascota().getDueno() == null
                    ? "dueño no asignado"
                    : recordatorio.getMascota().getDueno().getCorreo();

            System.out.println("[RECORDATORIO] " + destinatario + ": "
                    + recordatorio.descripcion(hoy));
        }

        return recordatorios.size();
    }
}

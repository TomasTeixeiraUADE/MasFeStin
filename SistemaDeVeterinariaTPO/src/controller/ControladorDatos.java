package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import model.Clinica;
import model.Dueno;
import model.Insumo;
import model.Lote;
import model.Mascota;
import model.Medicamento;
import model.Vacuna;
import persistence.RepositorioInsumos;
import persistence.RepositorioMascotas;

public class ControladorDatos {
    private Clinica clinica;
    private Path carpeta;
    private RepositorioMascotas repositorioMascotas;

    public ControladorDatos(Clinica clinica, Path carpeta) {
        this.clinica = clinica;
        this.carpeta = carpeta;
        this.repositorioMascotas = new RepositorioMascotas(carpeta.resolve("mascotas.csv"));
    }

    public int guardarMascotas() throws IOException {

        Files.createDirectories(carpeta);

        repositorioMascotas.guardarTodas(clinica.getMascotas());

        return clinica.getMascotas().size();
    }

    public List<Mascota> cargarMascotas() throws IOException {

        List<Mascota> nuevas = new ArrayList<>();

        for (Mascota cargada : repositorioMascotas.cargarTodas()) {

            if (!existe(cargada)) {
                clinica.getMascotas().add(cargada);
                nuevas.add(cargada);
            }
        }

        return nuevas;
    }

    public void asignarDueno(Mascota mascota, Dueno dueno) {
        dueno.agregarMascota(mascota);
    }

    public int guardarLotes(Medicamento medicamento) throws IOException {
        return guardarLotes(archivoLotes(medicamento.getNombre()), medicamento.getLotes());
    }

    public int guardarLotes(Insumo insumo) throws IOException {
        return guardarLotes(archivoLotes(insumo.getNombre()), insumo.getLotes());
    }

    public int guardarLotes(Vacuna vacuna) throws IOException {
        return guardarLotes(archivoLotes(vacuna.getNombre()), vacuna.getLotes());
    }

    public int cargarLotes(Medicamento medicamento) throws IOException {
        return cargarLotes(
                archivoLotes(medicamento.getNombre()),
                medicamento.getLotes(),
                medicamento::agregarLote
        );
    }

    public int cargarLotes(Insumo insumo) throws IOException {
        return cargarLotes(
                archivoLotes(insumo.getNombre()),
                insumo.getLotes(),
                insumo::agregarLote
        );
    }

    public int cargarLotes(Vacuna vacuna) throws IOException {
        return cargarLotes(
                archivoLotes(vacuna.getNombre()),
                vacuna.getLotes(),
                vacuna::agregarLote
        );
    }

    private int guardarLotes(Path archivo, List<Lote> lotes) throws IOException {

        Files.createDirectories(carpeta);

        new RepositorioInsumos(archivo).guardarTodos(lotes);

        return lotes.size();
    }

    private int cargarLotes(Path archivo, List<Lote> existentes,
                            Consumer<Lote> agregar) throws IOException {

        int agregados = 0;

        for (Lote lote : new RepositorioInsumos(archivo).cargarTodos()) {

            if (!existeLote(existentes, lote)) {
                agregar.accept(lote);
                agregados++;
            }
        }

        return agregados;
    }

    private boolean existe(Mascota cargada) {

        for (Mascota mascota : clinica.getMascotas()) {

            if (mascota.getNombre().equalsIgnoreCase(cargada.getNombre())
                    && mascota.getEspecie().equalsIgnoreCase(cargada.getEspecie())) {

                return true;
            }
        }

        return false;
    }

    private boolean existeLote(List<Lote> existentes, Lote lote) {

        for (Lote existente : existentes) {

            if (existente.getNumero().equals(lote.getNumero())) {
                return true;
            }
        }

        return false;
    }

    private Path archivoLotes(String nombreProducto) {

        String limpio = Normalizer.normalize(nombreProducto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_|_$", "");

        return carpeta.resolve("lotes_" + limpio + ".csv");
    }
}

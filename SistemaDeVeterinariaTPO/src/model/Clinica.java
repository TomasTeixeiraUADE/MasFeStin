package model;

import java.util.ArrayList;
import java.util.List;

import model.pago.ListaPrecios;
import model.pago.RepositorioFacturaMemoria;

public class Clinica {
    private List<Dueno> duenos = new ArrayList<>();
    private List<Mascota> mascotas = new ArrayList<>();
    private List<Veterinario> veterinarios = new ArrayList<>();
    private List<Turno> turnos = new ArrayList<>();
    private List<Medicamento> medicamentos = new ArrayList<>();
    private List<Insumo> insumos = new ArrayList<>();
    private List<Vacuna> vacunas = new ArrayList<>();

    private RepositorioFacturaMemoria facturas = new RepositorioFacturaMemoria();
    private ListaPrecios precios = new ListaPrecios();

    public List<Dueno> getDuenos() {
        return duenos;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public List<Veterinario> getVeterinarios() {
        return veterinarios;
    }

    public List<Turno> getTurnos() {
        return turnos;
    }

    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }

    public List<Insumo> getInsumos() {
        return insumos;
    }

    public List<Vacuna> getVacunas() {
        return vacunas;
    }

    public RepositorioFacturaMemoria getFacturas() {
        return facturas;
    }

    public ListaPrecios getPrecios() {
        return precios;
    }
}

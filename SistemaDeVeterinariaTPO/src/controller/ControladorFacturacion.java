package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Clinica;
import model.Consulta;
import model.Insumo;
import model.Mascota;
import model.Tratamiento;
import model.VacunaAplicada;
import model.enums.EstadoFactura;
import model.pago.CalculadorFactura;
import model.pago.Factura;
import model.pago.ListaPrecios;
import model.pago.NotaCredito;
import model.pago.PoliticaPago;
import model.pago.RepositorioFactura;
import model.enums.TipoAtencion;

public class ControladorFacturacion {
    private Clinica clinica;
    private RepositorioFactura repositorio;
    private CalculadorFactura calculador;
    private ListaPrecios precios;

    public ControladorFacturacion(Clinica clinica, RepositorioFactura repositorio,
                                  CalculadorFactura calculador, ListaPrecios precios) {
        this.clinica = clinica;
        this.repositorio = repositorio;
        this.calculador = calculador;
        this.precios = precios;
    }

    public List<Consulta> consultasPendientesDeCobro() {

        List<Consulta> pendientes = new ArrayList<>();

        for (Mascota mascota : clinica.getMascotas()) {

            for (Consulta consulta : mascota.getHistoriaClinica().getConsultas()) {

                if (consulta.isConfirmada() && !estaFacturada(consulta)) {
                    pendientes.add(consulta);
                }
            }
        }

        return pendientes;
    }

    public Factura generarFactura(Consulta consulta) {

        if (!consulta.isConfirmada()) {
            throw new IllegalStateException(
                    "La consulta debe estar confirmada para facturarse."
            );
        }

        if (estaFacturada(consulta)) {
            throw new IllegalStateException(
                    "La consulta ya fue facturada."
            );
        }

        Factura factura = new Factura(
                repositorio.siguienteNumero(),
                LocalDate.now(),
                consulta
        );

        factura.agregarLinea(
                "Consulta: " + consulta.getMotivo(),
                1,
                precios.precioAtencion(TipoAtencion.CONSULTA_GENERAL)
        );

        for (Tratamiento tratamiento : consulta.getTratamientos()) {

            String nombre = tratamiento.getMedicamento().getNombre();

            factura.agregarLinea(
                    "Medicamento: " + nombre,
                    1,
                    precios.precioProducto(nombre)
            );
        }

        for (VacunaAplicada aplicada : consulta.getVacunasAplicadas()) {

            String nombre = aplicada.getVacuna().getNombre();

            factura.agregarLinea(
                    "Vacuna: " + nombre + " (lote " + aplicada.getLote().getNumero() + ")",
                    1,
                    precios.precioProducto(nombre)
            );
        }

        for (java.util.Map.Entry<Insumo, Integer> entrada : consulta.getInsumosUtilizados().entrySet()) {
            String nombre = entrada.getKey().getNombre();
            factura.agregarLinea(
                    "Insumo: " + nombre,
                    entrada.getValue(),
                    precios.precioProducto(nombre)
            );
        }

        return factura;
    }

    public double calcularTotal(Factura factura, PoliticaPago politica, boolean tienePlan) {
        return calculador.calcularTotal(factura, politica, tienePlan);
    }

    public double cobrar(Factura factura, PoliticaPago politica, boolean tienePlan) {

        double total = calculador.calcularTotal(factura, politica, tienePlan);

        factura.registrarPago(total, politica.nombre());

        repositorio.guardar(factura);

        return total;
    }

    public NotaCredito anularFactura(Factura factura, String motivo) {

        NotaCredito nota = factura.anular(motivo);
        repositorio.guardar(factura);
        return nota;
    }

    public void anularFactura(Factura factura) {
        anularFactura(factura, "Corrección de factura emitida");
    }

    public List<Factura> listarFacturas() {
        return repositorio.listar();
    }

    private boolean estaFacturada(Consulta consulta) {

        for (Factura factura : repositorio.listar()) {

            if (factura.getConsulta() == consulta
                    && factura.getEstado() != EstadoFactura.ANULADA) {

                return true;
            }
        }

        return false;
    }
}

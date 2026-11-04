package model.pago;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import model.Consulta;
import model.enums.EstadoFactura;

public class Factura {
    private int numero;
    private LocalDate fecha;
    private Consulta consulta;

    private EstadoFactura estado;
    private List<LineaFactura> lineas;

    private double total;
    private String medioPago;

    public Factura(int numero, LocalDate fecha, Consulta consulta) {
        this.numero = numero;
        this.fecha = fecha;
        this.consulta = consulta;

        this.estado = EstadoFactura.EMITIDA;
        this.lineas = new ArrayList<>();
    }

    public int getNumero() {
        return numero;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public String getMedioPago() {
        return medioPago;
    }

    public List<LineaFactura> getLineas() {
        return new ArrayList<>(lineas);
    }

    public void agregarLinea(String descripcion, int cantidad, double precioUnitario) {

        if (estado != EstadoFactura.EMITIDA) {
            throw new IllegalStateException(
                    "Una factura cobrada o anulada no se modifica."
            );
        }

        lineas.add(new LineaFactura(descripcion, cantidad, precioUnitario));
    }

    public double calcularSubtotal() {

        double subtotal = 0;

        for (LineaFactura linea : lineas) {
            subtotal = subtotal + linea.calcularSubtotal();
        }

        return subtotal;
    }

    public double calcularTotal() {

        if (estado == EstadoFactura.PAGADA) {
            return total;
        }

        return calcularSubtotal();
    }

    public void registrarPago(double totalFinal, String medio) {

        if (estado != EstadoFactura.EMITIDA) {
            throw new IllegalStateException(
                    "La factura ya fue cobrada o anulada."
            );
        }

        this.total = totalFinal;
        this.medioPago = medio;
        this.estado = EstadoFactura.PAGADA;
    }

    public void anular() {

        if (estado != EstadoFactura.PAGADA) {
            throw new IllegalStateException(
                    "Solo se puede anular una factura cobrada."
            );
        }

        estado = EstadoFactura.ANULADA;
    }
}

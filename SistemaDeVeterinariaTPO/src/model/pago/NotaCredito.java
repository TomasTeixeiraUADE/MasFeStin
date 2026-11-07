package model.pago;

import java.time.LocalDate;

public class NotaCredito {
    private String numero;
    private LocalDate fecha;
    private int facturaOrigen;
    private String motivo;
    private double importe;

    public NotaCredito(Factura factura, String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo de la nota de crédito.");
        }
        this.numero = "NC-" + factura.getNumero();
        this.fecha = LocalDate.now();
        this.facturaOrigen = factura.getNumero();
        this.motivo = motivo;
        this.importe = factura.calcularTotal();
    }

    public String getNumero() { return numero; }
    public LocalDate getFecha() { return fecha; }
    public int getFacturaOrigen() { return facturaOrigen; }
    public String getMotivo() { return motivo; }
    public double getImporte() { return importe; }
}

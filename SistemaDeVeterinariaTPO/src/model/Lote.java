package src.model;

import java.time.LocalDate;
import src.exception.StockInsuficienteException;

public class Lote {
    private String numero;
    private LocalDate fechaVencimiento;
    private int cantidad;

    public Lote(String numero, LocalDate fechaVencimiento, int cantidad) {
        this.numero = numero;
        this.fechaVencimiento = fechaVencimiento;
        this.cantidad = cantidad;
    }

    public String getNumero() {
        return numero;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public boolean estaVencido() {
        return fechaVencimiento.isBefore(LocalDate.now());
    }

    public boolean tieneStockDisponible() {
        return cantidad > 0 && !estaVencido();
    }

    public void descontar(int cantidadADescontar)
            throws StockInsuficienteException {

        if (cantidadADescontar <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero."
            );
        }

        if (estaVencido()) {
            throw new StockInsuficienteException(
                    "El lote " + numero + " esta vencido."
            );
        }

        if (cantidad < cantidadADescontar) {
            throw new StockInsuficienteException(
                    "Stock insuficiente en el lote "
                    + numero
            );
        }

        cantidad = cantidad - cantidadADescontar;
    }
}

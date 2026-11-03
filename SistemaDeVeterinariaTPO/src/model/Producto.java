package src.model;

import java.util.ArrayList;
import java.util.List;

import src.exception.StockInsuficienteException;

public abstract class Producto {
    private String codigo;
    private String nombre;
    private int stockMinimo;

    private List<Lote> lotes;

    public Producto(String codigo, String nombre, int stockMinimo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.stockMinimo = stockMinimo;

        this.lotes = new ArrayList<>();
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStockMinimo() {
        return stockMinimo;
    }

    public List<Lote> getLotes() {
        return lotes;
    }

    public void agregarLote(Lote lote) {

        if (lote != null) {
            lotes.add(lote);
        }
    }

    public int calcularStock() {

        int stock = 0;

        for (Lote lote : lotes) {

            if (!lote.estaVencido()) {
                stock = stock + lote.getCantidad();
            }
        }

        return stock;
    }

    public boolean tieneStockBajo() {
        return calcularStock() <= stockMinimo;
    }

    public Lote obtenerLoteProximoAVencer()
            throws StockInsuficienteException {

        Lote seleccionado = null;

        for (Lote lote : lotes) {

            if (lote.tieneStockDisponible()) {

                if (seleccionado == null
                        || lote.getFechaVencimiento()
                        .isBefore(
                                seleccionado.getFechaVencimiento()
                        )) {

                    seleccionado = lote;
                }
            }
        }

        if (seleccionado == null) {

            throw new StockInsuficienteException(
                    "No hay lotes vigentes con stock para "
                    + nombre
            );
        }

        return seleccionado;
    }

    public void consumirStock(int cantidad)throws StockInsuficienteException {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor a cero."
            );
        }

        if (calcularStock() < cantidad) {
            throw new StockInsuficienteException(
                    "Stock insuficiente de " + nombre
            );
        }

        int restante = cantidad;

        while (restante > 0) {

            Lote lote =
                    obtenerLoteProximoAVencer();

            int cantidadLote =
                    lote.getCantidad();

            if (cantidadLote >= restante) {

                lote.descontar(restante);

                restante = 0;

            } else {

                lote.descontar(cantidadLote);

                restante =
                        restante - cantidadLote;
            }
        }
    }
}
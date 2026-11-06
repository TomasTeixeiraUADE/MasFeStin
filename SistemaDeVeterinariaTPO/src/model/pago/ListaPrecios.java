package model.pago;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import model.enums.TipoAtencion;

public class ListaPrecios {
    private Map<TipoAtencion, Double> atenciones;
    private Map<String, Double> productos;

    public ListaPrecios() {
        this.atenciones = new EnumMap<>(TipoAtencion.class);
        this.productos = new HashMap<>();

        atenciones.put(TipoAtencion.CONSULTA_GENERAL, 15000.0);
        atenciones.put(TipoAtencion.VACUNACION, 8000.0);
        atenciones.put(TipoAtencion.CIRUGIA, 90000.0);
    }

    public void definirPrecioAtencion(TipoAtencion tipo, double precio) {
        atenciones.put(tipo, precio);
    }

    public void definirPrecioProducto(String nombre, double precio) {
        productos.put(nombre, precio);
    }

    public double precioAtencion(TipoAtencion tipo) {
        return atenciones.get(tipo);
    }

    public double precioProducto(String nombre) {
        Double precio = productos.get(nombre);

        if (precio == null) {
            throw new IllegalStateException(
                    "No hay precio cargado para " + nombre
            );
        }

        return precio;
    }
}

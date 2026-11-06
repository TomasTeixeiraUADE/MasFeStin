package model.pago;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RepositorioFacturaMemoria implements RepositorioFactura {
    private Map<Integer, Factura> facturas = new TreeMap<>();

    @Override
    public void guardar(Factura factura) {
        facturas.put(factura.getNumero(), factura);
    }

    @Override
    public Factura recuperar(int numero) {
        return facturas.get(numero);
    }

    @Override
    public List<Factura> listar() {
        return new ArrayList<>(facturas.values());
    }

    @Override
    public int siguienteNumero() {
        return facturas.size() + 1;
    }
}

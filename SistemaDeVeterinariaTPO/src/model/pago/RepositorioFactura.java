package model.pago;

import java.util.List;

public interface RepositorioFactura {

    void guardar(Factura factura);

    Factura recuperar(int numero);

    List<Factura> listar();

    int siguienteNumero();
}

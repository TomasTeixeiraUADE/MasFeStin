package model.pago;

public class Debito implements PoliticaPago {

    @Override
    public double aplicar(double subtotal) {
        return subtotal;
    }
}

package model.pago;

public class Credito implements PoliticaPago {

    @Override
    public double aplicar(double subtotal) {
        return subtotal * 1.08;
    }
}

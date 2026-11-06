package model.pago;

public class Efectivo implements PoliticaPago {

    @Override
    public double aplicar(double subtotal) {
        return subtotal * 0.90;
    }

    @Override
    public boolean acumulaConPlan() {
        return false;
    }
}

package model.pago;

public class CalculadorFactura {
    public static final double DESCUENTO_PLAN = 0.15;

    public double calcularTotal(Factura factura, PoliticaPago pago, boolean tienePlan) {

        double subtotal = factura.calcularSubtotal();

        if (!tienePlan) {
            return pago.aplicar(subtotal);
        }

        double conPlan = subtotal * (1 - DESCUENTO_PLAN);

        if (pago.acumulaConPlan()) {
            return pago.aplicar(conPlan);
        }

        return Math.min(pago.aplicar(subtotal), conPlan);
    }
}

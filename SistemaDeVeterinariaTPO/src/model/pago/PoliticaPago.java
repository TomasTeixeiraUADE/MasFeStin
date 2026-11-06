package model.pago;

public interface PoliticaPago {

    double aplicar(double subtotal);

    default String nombre() {
        return getClass().getSimpleName();
    }

    default boolean acumulaConPlan() {
        return true;
    }
}

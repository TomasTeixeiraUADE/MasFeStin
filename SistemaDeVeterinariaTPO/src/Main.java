import java.nio.file.Path;

import controller.ControladorAtencion;
import controller.ControladorDatos;
import controller.ControladorFacturacion;
import controller.ControladorTurnos;
import model.Clinica;
import model.DatosDemo;
import model.pago.CalculadorFactura;
import view.MenuConsola;

public class Main {

    public static void main(String[] args) {

        Clinica clinica = new Clinica();

        DatosDemo.cargar(clinica);

        ControladorTurnos turnos =
                new ControladorTurnos(clinica, clinica.getPrecios());

        ControladorAtencion atencion =
                new ControladorAtencion(clinica);

        ControladorFacturacion facturacion =
                new ControladorFacturacion(
                        clinica,
                        clinica.getFacturas(),
                        new CalculadorFactura(),
                        clinica.getPrecios()
                );

        ControladorDatos datos =
                new ControladorDatos(clinica, Path.of("data"));

        new MenuConsola(clinica, turnos, atencion, facturacion, datos).iniciar();
    }
}

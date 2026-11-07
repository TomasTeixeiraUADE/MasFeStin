import java.nio.file.Path;
import java.time.LocalDate;

import controller.ControladorAdministracion;
import controller.ControladorAtencion;
import controller.ControladorDatos;
import controller.ControladorFacturacion;
import controller.ControladorRegistro;
import controller.ControladorTurnos;
import model.Clinica;
import model.DatosDemo;
import model.pago.CalculadorFactura;
import view.MenuConsola;

public class Main {

    public static void main(String[] args) {

        Clinica clinica = new Clinica();

        DatosDemo.cargar(clinica);

        ControladorRegistro registro =
                new ControladorRegistro(clinica);

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

        ControladorAdministracion administracion =
                new ControladorAdministracion(clinica);

        ControladorDatos datos =
                new ControladorDatos(clinica, Path.of("data"));

        // Simula el servicio externo de notificaciones al iniciar el sistema.
        administracion.emitirRecordatorios(LocalDate.now());

        new MenuConsola(
                clinica, registro, turnos, atencion, facturacion, administracion, datos
        ).iniciar();
    }
}

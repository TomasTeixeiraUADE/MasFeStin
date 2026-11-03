package persistence;

import model.Mascota;
import model.Perro;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
public class RepositorioMascotaTest {
        @Test
        void testGuardarYCargarMascotas() throws IOException {
            RepositorioMascotas repo = new RepositorioMascotas(Path.of("data/test_mascotas.csv"));
            List<Mascota> listaOriginal = List.of(
                    new Perro("Rocco", "Perro", "Macho", LocalDate.of(2022, 1, 1), 15.0, "CHIP99", "Grande")
            );

            repo.guardarTodas(listaOriginal);
            List<Mascota> listaCargada = repo.cargarTodas();

            assertEquals(1, listaCargada.size());
            assertEquals("Rocco", listaCargada.getFirst().getNombre());
        }
}

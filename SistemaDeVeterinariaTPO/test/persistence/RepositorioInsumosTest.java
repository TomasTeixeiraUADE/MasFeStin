package persistence;

import model.Lote;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RepositorioInsumosTest {

    @Test
    void testGuardaryCargarLotes() throws IOException{
        RepositorioInsumos repo = new RepositorioInsumos(Path.of("data/test_lotes.csv"));
        List<Lote> lotes = List.of(new Lote("L001", LocalDate.of(2026,12,31), 50));

        repo.guardarTodos(lotes);
        List<Lote> lotesCargados = repo.cargarTodos();

        assertEquals(1, lotesCargados.size());
        assertEquals("L001", lotesCargados.get(0).getNumero());
        assertEquals(50, lotesCargados.get(0).getCantidad());
    }
}

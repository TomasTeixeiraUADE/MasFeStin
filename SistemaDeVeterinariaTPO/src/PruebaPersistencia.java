package test; // o en tu Main de prueba

//import model.Gato;
import model.Mascota;
import model.Perro;
import persistence.RepositorioMascotas;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PruebaPersistencia {
    public static void main(String[] args) {
        RepositorioMascotas repo = new RepositorioMascotas(Path.of("data/mascotas.csv"));

        // 1. Creamos 2 mascotas de prueba
        List<Mascota> listaGuardar = new ArrayList<>();
        listaGuardar.add(new Perro("Fido", "Perro", "Macho", LocalDate.of(2022,05,10), 12.5, "CHIP123", "Grande"));
        //listaGuardar.add(new Gato("Michi", "Gato", "Hembra", "2021-08-15", 4.2, true));

        try {
            // 2. Guardamos en archivo
            repo.guardarTodas(listaGuardar);
            System.out.println("✅ Mascotas guardadas correctamente en mascotas.csv");

            // 3. Volvemos a cargar desde el archivo
            List<Mascota> listaCargada = repo.cargarTodas();
            System.out.println("📖 Mascotas leídas desde el archivo: " + listaCargada.size());
            for (Mascota m : listaCargada) {
                System.out.println(" - " + m.getNombre() + " (" + m.getEspecie() + ")");
            }

        } catch (IOException e) {
            System.err.println("❌ Error de E/S: " + e.getMessage());
        }
    }
}
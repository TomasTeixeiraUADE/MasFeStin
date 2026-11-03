package persistence;

import model.Lote;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioInsumos {
    private Path rutaArchivos;

    public RepositorioInsumos(Path rutaArchivos) {
        this.rutaArchivos = rutaArchivos;
    }

    public void guardarTodos(List<Lote> lotes) throws IOException{
        try (BufferedWriter bw = Files.newBufferedWriter(rutaArchivos)){
            for (Lote l : lotes){
                StringBuilder sb = new StringBuilder();
                sb.append(l.getNumero()).append(";")
                        .append(l.getFechaVencimiento()).append(";")
                        .append(l.getCantidad());

                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    public List<Lote> cargarTodos() throws IOException{
        List<Lote> lotes = new ArrayList<>();
        if (!Files.exists(rutaArchivos)){
            return lotes;
        }

        try(BufferedReader br = Files.newBufferedReader(rutaArchivos)){
            String linea;
            while((linea = br.readLine()) != null){
                if(!linea.trim().isEmpty()){
                    String[] datos = linea.split(";");
                    String numero = datos[0];
                    LocalDate fechaVencimiento = LocalDate.parse(datos[1]);
                    int cantidad = Integer.parseInt(datos[2]);

                    lotes.add(new Lote(numero, fechaVencimiento, cantidad));
                }
            }
        }
        return lotes;
    }
}

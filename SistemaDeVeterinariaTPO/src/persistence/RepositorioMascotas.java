package persistence;

import model.Mascota;
import model.Perro;
import model.Gato;
import model.Ave;
import model.Conejo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class RepositorioMascotas {
    private Path rutaArchivo;

    public RepositorioMascotas(Path rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public void guardarTodas(List<Mascota> mascotas) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(rutaArchivo)) {
            for (Mascota m : mascotas) {
                StringBuilder sb = new StringBuilder();
                sb.append(m.getNombre()).append(";")
                        .append(m.getEspecie()).append(";")
                        .append(m.getRaza()).append(";")
                        .append(m.getSexo()).append(";")
                        .append(m.getFechaNacimiento()).append(";")
                        .append(m.getPesoActual());

                if (m instanceof Perro) {
                    Perro p = (Perro) m;
                    sb.append(";").append(p.getNumeroChip()).append(";")
                            .append(p.getTamanio());
                } else if (m instanceof Gato) {
                    Gato g = (Gato) m;
                    sb.append(";").append(g.isViveEnInterior());
                } else if (m instanceof Ave) {
                    Ave a = (Ave) m;
                    sb.append(";").append(a.isPuedeVolar());
                }

                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    public List<Mascota> cargarTodas() throws IOException {
        List<Mascota> mascotas = new ArrayList<>();
        if (!Files.exists(rutaArchivo)) {
            return mascotas;
        }

        try (BufferedReader br = Files.newBufferedReader(rutaArchivo)) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(";");

                // Formato actual:
                // nombre;especie;raza;sexo;fechaNacimiento;peso;datosEspecificos...
                // También soporta el formato viejo sin raza para no romper archivos existentes.
                boolean formatoNuevo = datos.length >= 6
                        && esFecha(datos[4]);

                String nombre = datos[0];
                String especie = datos[1];
                String raza;
                String sexo;
                LocalDate fechaNacimiento;
                double pesoActual;
                int extra;

                if (formatoNuevo) {
                    raza = datos[2];
                    sexo = datos[3];
                    fechaNacimiento = LocalDate.parse(datos[4]);
                    pesoActual = Double.parseDouble(datos[5]);
                    extra = 6;
                } else {
                    raza = especie;
                    sexo = datos[2];
                    fechaNacimiento = LocalDate.parse(datos[3]);
                    pesoActual = Double.parseDouble(datos[4]);
                    extra = 5;
                }

                if (especie.equalsIgnoreCase("Perro")) {
                    String chip = datos[extra];
                    String tamanio = datos[extra + 1];
                    mascotas.add(new Perro(nombre, raza, sexo, fechaNacimiento, pesoActual, chip, tamanio));
                } else if (especie.equalsIgnoreCase("Gato")) {
                    boolean interior = Boolean.parseBoolean(datos[extra]);
                    mascotas.add(new Gato(nombre, raza, sexo, fechaNacimiento, pesoActual, interior));
                } else if (especie.equalsIgnoreCase("Ave")) {
                    boolean volar = Boolean.parseBoolean(datos[extra]);
                    mascotas.add(new Ave(nombre, raza, sexo, fechaNacimiento, pesoActual, volar));
                } else if (especie.equalsIgnoreCase("Conejo")) {
                    mascotas.add(new Conejo(nombre, raza, sexo, fechaNacimiento, pesoActual));
                }
            }
        }
        return mascotas;
    }

    private boolean esFecha(String valor) {
        try {
            LocalDate.parse(valor);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

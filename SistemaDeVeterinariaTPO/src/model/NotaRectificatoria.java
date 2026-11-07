package model;

import java.time.LocalDateTime;

public class NotaRectificatoria {
    private LocalDateTime fecha;
    private String texto;
    private Veterinario veterinario;

    public NotaRectificatoria(String texto, Veterinario veterinario) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("La nota rectificatoria no puede estar vacía.");
        }
        if (veterinario == null) {
            throw new IllegalArgumentException("La nota debe estar firmada por un veterinario.");
        }
        this.fecha = LocalDateTime.now();
        this.texto = texto;
        this.veterinario = veterinario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getTexto() {
        return texto;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public String getFirma() {
        return veterinario.obtenerNombreCompleto() + " - Matrícula " + veterinario.getMatricula();
    }
}

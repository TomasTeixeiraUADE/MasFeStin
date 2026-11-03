package model;

import java.util.ArrayList;
import java.util.List;

public class Dueno {
    private String nombre;
    private String apellido;
    private String documento;
    private String telefono;
    private String correo;
    private boolean poseePlanSalud;

    private int ausenciasConsecutivas;

    private List<Mascota> mascotas;

    public Dueno(String nombre, String apellido, String documento, String telefono, String correo, boolean poseePlanSalud) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
        this.poseePlanSalud = poseePlanSalud;

        this.ausenciasConsecutivas = 0;
        this.mascotas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public boolean isPoseePlanSalud() {
        return poseePlanSalud;
    }

    public int getAusenciasConsecutivas() {
        return ausenciasConsecutivas;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    public void agregarMascota(Mascota mascota) {

        if (mascota != null && !mascotas.contains(mascota)) {

            mascotas.add(mascota);
            mascota.asignarDueno(this);
        }
    }

    public void registrarAusencia() {
        ausenciasConsecutivas++;
    }

    public void reiniciarAusencias() {
        ausenciasConsecutivas = 0;
    }

    public boolean tieneRestriccionPorAusencias() {
        return ausenciasConsecutivas >= 3;
    }

    public String obtenerNombreCompleto() {
        return nombre + " " + apellido;
    }
}
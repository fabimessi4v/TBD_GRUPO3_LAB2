package Grupo3TBD.ClimateViewer.entities;


import jakarta.persistence.Table;

import java.time.LocalDate;

@Table(name = "usuarios")
public class Usuario {
    private Long id;
    private String nombre;
    private String email;
    private String conHash;
    private String rol;
    private LocalDate fechaRegistro;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConHash() {
        return conHash;
    }
    public void setConHash(String conHash) {
        this.conHash = conHash;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }
}
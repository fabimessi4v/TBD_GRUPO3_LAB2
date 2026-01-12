package Grupo3TBD.ClimateViewer.DTO;

import java.time.LocalDate;

public class DatasetDTO {
    Long id;
    String nombre;
    String descripcion;
    String fuente;
    LocalDate fechaActualizacion;

    public DatasetDTO(Long id, String nombre, String descripcion, String fuente, LocalDate fechaActualizacion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fuente = fuente;
        this.fechaActualizacion = fechaActualizacion;
    }

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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    public LocalDate getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }
}

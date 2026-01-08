package Grupo3TBD.ClimateViewer.DTO;

import java.time.LocalDateTime;

public class PuntoUltimaMedicionDTO {
    private Long idPunto;
    private String nombrePunto;
    private Double latitud;
    private Double longitud;
    private LocalDateTime ultimaMedicion;

    public PuntoUltimaMedicionDTO() {}

    public PuntoUltimaMedicionDTO(Long idPunto, String nombrePunto, Double latitud, Double longitud, LocalDateTime ultimaMedicion) {
        this.idPunto = idPunto;
        this.nombrePunto = nombrePunto;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ultimaMedicion = ultimaMedicion;
    }

    public Long getIdPunto() {
        return idPunto;
    }

    public void setIdPunto(Long idPunto) {
        this.idPunto = idPunto;
    }

    public String getNombrePunto() {
        return nombrePunto;
    }

    public void setNombrePunto(String nombrePunto) {
        this.nombrePunto = nombrePunto;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public LocalDateTime getUltimaMedicion() {
        return ultimaMedicion;
    }

    public void setUltimaMedicion(LocalDateTime ultimaMedicion) {
        this.ultimaMedicion = ultimaMedicion;
    }
}

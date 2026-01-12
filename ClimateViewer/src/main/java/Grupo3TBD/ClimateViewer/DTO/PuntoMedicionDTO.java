package Grupo3TBD.ClimateViewer.DTO;

public class PuntoMedicionDTO {
    private Long id;
    private String nombre;
    private double latitud;
    private double longitud;
    private String tipoSensor;
    private boolean activo;
    private String geom; // WKT

    public PuntoMedicionDTO(Long id, String nombre, double latitud, double longitud, String tipoSensor, boolean activo, String geom) {
        this.id = id;
        this.nombre = nombre;
        this.latitud = latitud;
        this.longitud = longitud;
        this.tipoSensor = tipoSensor;
        this.activo = activo;
        this.geom = geom;
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

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public String getTipoSensor() {
        return tipoSensor;
    }

    public void setTipoSensor(String tipoSensor) {
        this.tipoSensor = tipoSensor;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}

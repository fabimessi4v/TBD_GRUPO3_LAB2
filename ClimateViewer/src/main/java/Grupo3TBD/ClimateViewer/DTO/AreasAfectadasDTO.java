package Grupo3TBD.ClimateViewer.DTO;

public class AreasAfectadasDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipoRiesgo;
    private String geom;

    public AreasAfectadasDTO(Long id, String nombre, String descripcion, String tipoRiesgo, String geom) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoRiesgo = tipoRiesgo;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoRiesgo() {
        return tipoRiesgo;
    }

    public void setTipoRiesgo(String tipoRiesgo) {
        this.tipoRiesgo = tipoRiesgo;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }
}

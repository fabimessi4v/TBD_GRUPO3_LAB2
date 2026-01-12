package Grupo3TBD.ClimateViewer.entities;




import java.time.LocalDate;
import java.time.LocalDateTime;

public class Medicion {
    private Long id;
    private PuntoMedicion puntoMedicion;
    private Dataset dataset;
    private Long valor;
    private LocalDate fechahora;

    public LocalDate getFechahora() {
        return fechahora;
    }

    public void setFechahora(LocalDate fechahora) {
        this.fechahora = fechahora;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public PuntoMedicion getPuntoMedicion() {
        return puntoMedicion;
    }

    public void setPuntoMedicion(PuntoMedicion puntoMedicion) {
        this.puntoMedicion = puntoMedicion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
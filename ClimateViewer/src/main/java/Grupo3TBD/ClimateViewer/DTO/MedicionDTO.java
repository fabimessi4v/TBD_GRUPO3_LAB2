package Grupo3TBD.ClimateViewer.DTO;

import java.time.LocalDate;

public class MedicionDTO {
    private Long id;
    private Long idPuntoMedicion;
    private Long idDataset;
    private Long valor;
    private LocalDate fechahora;

    public MedicionDTO(Long id, Long idPuntoMedicion, Long idDataset, Long valor, LocalDate fechahora) {
        this.id = id;
        this.idPuntoMedicion = idPuntoMedicion;
        this.idDataset = idDataset;
        this.valor = valor;
        this.fechahora = fechahora;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdPuntoMedicion() {
        return idPuntoMedicion;
    }

    public void setIdPuntoMedicion(Long idPuntoMedicion) {
        this.idPuntoMedicion = idPuntoMedicion;
    }

    public Long getIdDataset() {
        return idDataset;
    }

    public void setIdDataset(Long idDataset) {
        this.idDataset = idDataset;
    }

    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public LocalDate getFechahora() {
        return fechahora;
    }

    public void setFechahora(LocalDate fechahora) {
        this.fechahora = fechahora;
    }
}

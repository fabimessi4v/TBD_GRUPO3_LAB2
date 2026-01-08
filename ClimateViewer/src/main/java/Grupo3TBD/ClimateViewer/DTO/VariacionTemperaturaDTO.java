package Grupo3TBD.ClimateViewer.DTO;

public class VariacionTemperaturaDTO {
    private Integer idPunto;
    private Double desviacionStd;

    public VariacionTemperaturaDTO() {}

    public VariacionTemperaturaDTO(Integer idPunto, Double desviacionStd) {
        this.idPunto = idPunto;
        this.desviacionStd = desviacionStd;
    }

    public Integer getIdPunto() { return idPunto; }
    public void setIdPunto(Integer idPunto) { this.idPunto = idPunto; }

    public Double getDesviacionStd() { return desviacionStd; }
    public void setDesviacionStd(Double desviacionStd) { this.desviacionStd = desviacionStd; }
}

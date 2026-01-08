package Grupo3TBD.ClimateViewer.DTO;


public class AnomaliaTemperaturaDTO {
    private Integer idPunto;
    private Double anomalia;

    public AnomaliaTemperaturaDTO() {}

    public AnomaliaTemperaturaDTO(Integer idPunto, Double anomalia) {
        this.idPunto = idPunto;
        this.anomalia = anomalia;
    }

    public Integer getIdPunto() { return idPunto; }
    public void setIdPunto(Integer idPunto) { this.idPunto = idPunto; }

    public Double getAnomalia() { return anomalia; }
    public void setAnomalia(Double anomalia) { this.anomalia = anomalia; }
}

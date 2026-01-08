package Grupo3TBD.ClimateViewer.DTO;

public class CorrelacionDTO {
    private Long idPuntoTemperatura;
    private Long idPuntoCO2;
    private Double distanciaKm;

    public CorrelacionDTO() {}

    public CorrelacionDTO(Long idPuntoTemperatura, Long idPuntoCO2, Double distanciaKm) {
        this.idPuntoTemperatura = idPuntoTemperatura;
        this.idPuntoCO2 = idPuntoCO2;
        this.distanciaKm = distanciaKm;
    }

    public Long getIdPuntoTemperatura() {
        return idPuntoTemperatura;
    }

    public void setIdPuntoTemperatura(Long idPuntoTemperatura) {
        this.idPuntoTemperatura = idPuntoTemperatura;
    }

    public Long getIdPuntoCO2() {
        return idPuntoCO2;
    }

    public void setIdPuntoCO2(Long idPuntoCO2) {
        this.idPuntoCO2 = idPuntoCO2;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}
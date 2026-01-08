package Grupo3TBD.ClimateViewer.DTO;

import java.time.LocalDate;

public class EventoExtremoDTO {
    private LocalDate fecha;
    private Double maximaTemperatura;

    public EventoExtremoDTO() {
    }

    public EventoExtremoDTO(LocalDate fecha, Double maximaTemperatura) {
        this.fecha = fecha;
        this.maximaTemperatura = maximaTemperatura;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Double getMaximaTemperatura() {
        return maximaTemperatura;
    }

    public void setMaximaTemperatura(Double maximaTemperatura) {
        this.maximaTemperatura = maximaTemperatura;
    }
}
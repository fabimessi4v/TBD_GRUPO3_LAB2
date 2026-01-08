package Grupo3TBD.ClimateViewer.DTO;

import java.time.LocalDate;

public class AgregacionDeDatosDTO {
    private Integer idDataset;
    private LocalDate periodo;
    private Double valor;

    public AgregacionDeDatosDTO(Integer idDataset, LocalDate periodo, Double valor) {
        this.idDataset = idDataset;
        this.periodo = periodo;
        this.valor = valor;
    }

    public AgregacionDeDatosDTO() {
    }

    public Integer getIdDataset() {
        return idDataset;
    }

    public LocalDate getPeriodo() {
        return periodo;
    }

    public Double getValor() {
        return valor;
    }
}

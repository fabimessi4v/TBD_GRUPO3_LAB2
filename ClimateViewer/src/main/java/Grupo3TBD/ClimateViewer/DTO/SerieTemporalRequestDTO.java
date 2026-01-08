package Grupo3TBD.ClimateViewer.DTO;

import java.time.LocalDate;


public class SerieTemporalRequestDTO {

    private Integer idDataset;
    private LocalDate fechaComienzo;
    private LocalDate fechaTermino;

    public SerieTemporalRequestDTO() {
    }

    public SerieTemporalRequestDTO(Integer idDataset, LocalDate fechaComienzo, LocalDate fechaTermino) {
        this.idDataset = idDataset;
        this.fechaComienzo = fechaComienzo;
        this.fechaTermino = fechaTermino;
    }

    public Integer getIdDataset() {
        return idDataset;
    }

    public LocalDate getFechaComienzo() {
        return fechaComienzo;
    }

    public LocalDate getFechaTermino() {
        return fechaTermino;
    }
}

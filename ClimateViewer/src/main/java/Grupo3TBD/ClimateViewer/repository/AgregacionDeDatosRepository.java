package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.DTO.AgregacionDeDatosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class AgregacionDeDatosRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<AgregacionDeDatosDTO> obtenerSerieTemporal(Integer idDataset, LocalDate fechaComienzo, LocalDate fechaTermino){

        jdbcTemplate.update(
                "CALL agregacion_de_datos(?, ?, ?)",
                idDataset,
                Date.valueOf(fechaComienzo),
                Date.valueOf(fechaTermino)
        );
        return jdbcTemplate.query(
                "SELECT * FROM vista_serie_temporal ORDER BY periodo",
                (rs, rowNum) -> new AgregacionDeDatosDTO(
                        rs.getInt("iddataset"),
                        rs.getObject("periodo", LocalDate.class),
                        rs.getDouble("valor")
                )
        );


    }

    public void llamarProcedimiento(Integer idDataset, LocalDate fechaComienzo, LocalDate fechaTermino) {
        jdbcTemplate.update(
                "CALL agregacion_de_datos(?, ?, ?)",
                idDataset,
                Date.valueOf(fechaComienzo),
                Date.valueOf(fechaTermino)
        );
    }

    public List<AgregacionDeDatosDTO> obtenerSerieTemporal() {
        String sql = "SELECT * FROM vista_serie_temporal ORDER BY periodo";
        return jdbcTemplate.query(sql,
                (rs, rowNum) -> new AgregacionDeDatosDTO(
                        rs.getInt("iddataset"),
                        rs.getObject("periodo", LocalDate.class),
                        rs.getDouble("valor")
                )
        );
    }
}

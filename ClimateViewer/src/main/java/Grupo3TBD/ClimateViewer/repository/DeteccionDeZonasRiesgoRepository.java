package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.DTO.DeteccionDeZonasRiesgoDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DeteccionDeZonasRiesgoRepository {
    private final JdbcTemplate jdbcTemplate;

    public DeteccionDeZonasRiesgoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<DeteccionDeZonasRiesgoDTO> obtenerPuntosEnZonasRiesgo() {
        String sql = """
                    SELECT
                        p.IdPunto,
                        p.Nombre AS NombrePunto,
                        p.TipoSensor,
                        a.IdArea,
                        a.Nombre AS NombreArea,
                        a.TipoRiesgo
                    FROM PuntosMedicion p
                    JOIN AreasAfectadas a
                    ON ST_Intersects(p.geom, a.geom)
                """;

        return jdbcTemplate.query(sql, (resultado, numeroFila) -> {
            DeteccionDeZonasRiesgoDTO punto = new DeteccionDeZonasRiesgoDTO();
            punto.setIdPunto(resultado.getInt("IdPunto"));
            punto.setNombrePunto(resultado.getString("NombrePunto"));
            punto.setTipoSensor(resultado.getString("TipoSensor"));
            punto.setIdArea(resultado.getInt("IdArea"));
            punto.setNombreArea(resultado.getString("NombreArea"));
            punto.setTipoRiesgo(resultado.getString("TipoRiesgo"));
            return punto;
        });

    }
}

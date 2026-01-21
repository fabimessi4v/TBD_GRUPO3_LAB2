package Grupo3TBD.ClimateViewer.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class InterpolarizacionRepository {

    private final NamedParameterJdbcTemplate jdbc;

    public InterpolarizacionRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // mv_mediciones_nulas_interp
    private static final String SQL_SELECT_MV_NULAS_SIMPLE = """
        SELECT
          nombre_punto  AS "nombre",
          sensor        AS "sensor",
          longitud      AS "longitud",
          latitud       AS "latitud",
          valor_final   AS "valor",
          es_interpolado AS "interpolacion"
        FROM mv_mediciones_nulas_interp
        ORDER BY iddataset, fechahora, idpunto;
        """;

    public List<Map<String, Object>> listarNulasParaFront() {
        return jdbc.queryForList(SQL_SELECT_MV_NULAS_SIMPLE, new MapSqlParameterSource());
    }

    // limpiar interpolacion
    private static final String SQL_SET_FLAG = """
        UPDATE app_flags
        SET interpolacion_activa = :activa
        WHERE id = 1;
        """;

    private static final String SQL_REFRESH_MV =
            "REFRESH MATERIALIZED VIEW CONCURRENTLY mv_mediciones_nulas_interp;";

    public void aplicarInterpolacion() {
        jdbc.update(SQL_SET_FLAG, new MapSqlParameterSource().addValue("activa", true));
        jdbc.getJdbcTemplate().execute(SQL_REFRESH_MV);
    }

    public void limpiarInterpolacion() {
        jdbc.update(SQL_SET_FLAG, new MapSqlParameterSource().addValue("activa", false));
        jdbc.getJdbcTemplate().execute(SQL_REFRESH_MV);
    }
}
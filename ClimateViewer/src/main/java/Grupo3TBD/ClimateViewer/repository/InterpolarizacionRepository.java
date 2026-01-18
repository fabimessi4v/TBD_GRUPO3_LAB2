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

    private static final String SQL_SELECT_MV_PUNTOS_ALL = """
        SELECT
          iddataset,
          idpunto,
          nombre,
          tiposensor,
          activo,
          lon,
          lat,
          valor_real,
          fechahora_real,
          valor_estimado,
          valor_final,
          es_interpolado
        FROM mv_puntos_valor
        ORDER BY iddataset, idpunto;
        """;

    public List<Map<String, Object>> listarDesdeMV() {
        return jdbc.queryForList(SQL_SELECT_MV_PUNTOS_ALL, new MapSqlParameterSource());
    }

    private static final String SQL_SELECT_MV_SIMPLE_ALL = """
        SELECT
          nombre         AS "nombre",
          tiposensor     AS "sensor",
          lon            AS "longitud",
          lat            AS "latitud",
          TRUNC(valor_final::numeric, 2) AS "valor",
          es_interpolado AS "interpolacion"
        FROM mv_puntos_valor
        ORDER BY iddataset, idpunto;
        """;

    public List<Map<String, Object>> listarTablaSimpleAll() {
        return jdbc.queryForList(SQL_SELECT_MV_SIMPLE_ALL, new MapSqlParameterSource());
    }

    private static final String SQL_SET_FLAG = """
        UPDATE app_flags
        SET interpolacion_activa = :activa
        WHERE id = 1;
        """;

    private static final String SQL_REFRESH_ULTIMA = "REFRESH MATERIALIZED VIEW CONCURRENTLY mv_ultima_medicion;";
    private static final String SQL_REFRESH_PUNTOS = "REFRESH MATERIALIZED VIEW CONCURRENTLY mv_puntos_valor;";


    public void aplicarInterpolacion() {
        jdbc.update(SQL_SET_FLAG, new MapSqlParameterSource().addValue("activa", true));
        jdbc.getJdbcTemplate().execute(SQL_REFRESH_ULTIMA);
        jdbc.getJdbcTemplate().execute(SQL_REFRESH_PUNTOS);
    }

    public void limpiarInterpolacion() {
        jdbc.update(SQL_SET_FLAG, new MapSqlParameterSource().addValue("activa", false));
        jdbc.getJdbcTemplate().execute(SQL_REFRESH_ULTIMA);
        jdbc.getJdbcTemplate().execute(SQL_REFRESH_PUNTOS);
    }

}
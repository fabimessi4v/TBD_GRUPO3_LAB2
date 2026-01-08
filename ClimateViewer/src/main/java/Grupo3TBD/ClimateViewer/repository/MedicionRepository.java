package Grupo3TBD.ClimateViewer.repository;


import Grupo3TBD.ClimateViewer.DTO.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MedicionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    /**
     * Obtiene una lista de eventos extremos de temperatura registrados en el último año.
     *
     * Cada evento corresponde a un día en el que la temperatura máxima registrada por
     * cualquier punto de medición de tipo "Termómetro" superó los 35°C.
     *
     * @return Lista de objetos EventoExtremoDTO, cada uno con la fecha y el valor máximo de temperatura de ese día.
     */
    public List<EventoExtremoDTO> findEventosExtremosTemperaturaUltimoAno() {
        String sql = """
            SELECT
                DATE(m.fechahora) AS fecha,
                MAX(m.valor) AS maxima_temperatura
            FROM
                mediciones m
                INNER JOIN puntosmedicion p ON m.idpunto = p.idpunto
            WHERE
                p.tiposensor = 'Termómetro'
                AND m.valor > 35
                AND m.fechahora >= CURRENT_DATE - INTERVAL '1 year'
            GROUP BY
                DATE(m.fechahora)
            ORDER BY
                fecha ASC
            """;
        // Ejecuta la consulta y mapea cada fila del resultado a un objeto EventoExtremoDTO.
        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    EventoExtremoDTO dto = new EventoExtremoDTO();
                    dto.setFecha(rs.getDate("fecha").toLocalDate());
                    dto.setMaximaTemperatura(rs.getDouble("maxima_temperatura"));
                    return dto;
                }
        );
    }

    public List<TendenciaMensualDTO> obtenerTendenciaMensual(String dataset, String sensor, String estacion) {
        StringBuilder sql = new StringBuilder("""
   SELECT
       tipo_sensor,
       nombre_dataset,
       nombre_punto,
       TO_CHAR(mes, 'YYYY-MM') AS mes,
       promedio_mensual,
       cantidad_mediciones
   FROM tendencia_mensual
   WHERE 1=1
   """);


        List<Object> params = new ArrayList<>();


        if (dataset != null && !dataset.isBlank()) {
            sql.append(" AND nombre_dataset = ? ");
            params.add(dataset);
        }
        if (sensor != null && !sensor.isBlank()) {
            sql.append(" AND tipo_sensor = ? ");
            params.add(sensor);
        }
        if (estacion != null && !estacion.isBlank()) {
            sql.append(" AND nombre_punto = ? ");
            params.add(estacion);
        }

        sql.append(" ORDER BY tipo_sensor, nombre_dataset, nombre_punto, mes;");

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new TendenciaMensualDTO(
                        rs.getString("tipo_sensor"),
                        rs.getString("nombre_dataset"),
                        rs.getString("nombre_punto"),
                        rs.getString("mes"),
                        rs.getDouble("promedio_mensual"),
                        rs.getInt("cantidad_mediciones")
                )
        );
    }

    public List<AnomaliaTemperaturaDTO> findAnomaliaTemperaturaPorPunto() {
        String sql = """
        SELECT
          m.idpunto,
          (AVG(m.valor) FILTER (WHERE m.fechahora >= CURRENT_DATE - INTERVAL '1 year')
           - AVG(m.valor)) AS anomalia
        FROM mediciones m
        INNER JOIN puntosmedicion p ON m.idpunto = p.idpunto
        WHERE p.tiposensor = 'Termómetro'
        GROUP BY m.idpunto
        HAVING COUNT(*) FILTER (WHERE m.fechahora >= CURRENT_DATE - INTERVAL '1 year') > 0
        ORDER BY anomalia DESC
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new AnomaliaTemperaturaDTO(
                        rs.getInt("idpunto"),
                        rs.getObject("anomalia") == null ? null : rs.getDouble("anomalia")
                )
        );
    }


    public List<VariacionTemperaturaDTO> findTop10MayorVariacionTemperatura5Anios() {
        String sql = """
        WITH t AS (
          SELECT
            m.idpunto,
            STDDEV_SAMP(m.valor) OVER (PARTITION BY m.idpunto) AS desviacion_std
          FROM mediciones m
          INNER JOIN puntosmedicion p ON m.idpunto = p.idpunto
          WHERE p.tiposensor = 'Termómetro'
            AND m.fechahora >= CURRENT_DATE - INTERVAL '5 years'
        )
        SELECT DISTINCT
          idpunto,
          desviacion_std
        FROM t
        ORDER BY desviacion_std DESC NULLS LAST
        LIMIT 10
        """;

        return jdbcTemplate.query(sql, (rs, rowNum) ->
                new VariacionTemperaturaDTO(
                        rs.getInt("idpunto"),
                        rs.getObject("desviacion_std") == null ? null : rs.getDouble("desviacion_std")
                )
        );
    }






}
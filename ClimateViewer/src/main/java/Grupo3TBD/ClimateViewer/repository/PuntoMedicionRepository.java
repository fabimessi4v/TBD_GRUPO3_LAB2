package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.DTO.CorrelacionDTO;
import Grupo3TBD.ClimateViewer.DTO.PuntoUltimaMedicionDTO;
import Grupo3TBD.ClimateViewer.entities.PuntoMedicion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PuntoMedicionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    // --- RowMapper ---
    private final RowMapper<PuntoMedicion> rowMapper = (rs, rowNum) -> {
        PuntoMedicion punto = new PuntoMedicion();
        punto.setId(rs.getLong("IdPunto"));
        punto.setNombre(rs.getString("Nombre"));
        punto.setLatitud(rs.getDouble("Latitud"));
        punto.setLongitud(rs.getDouble("Longitud"));
        punto.setTipoSensor(rs.getString("TipoSensor"));
        punto.setActivo(rs.getBoolean("Activo"));
        punto.setGeom(rs.getString("geomWKT")); // WKT obtenido con ST_AsText
        return punto;
    };


    // --- CREATE ---
    public int save(PuntoMedicion punto) {
        String sql = "INSERT INTO PuntosMedicion (Nombre, Latitud, Longitud, TipoSensor, Activo, geom) " +
                "VALUES (?, ?, ?, ?, ?, ST_SetSRID(ST_MakePoint(?, ?), 4326))";
        return jdbcTemplate.update(
                sql,
                punto.getNombre(),
                punto.getLatitud(),
                punto.getLongitud(),
                punto.getTipoSensor(),
                punto.isActivo(),
                punto.getLongitud(), // x = longitud
                punto.getLatitud()   // y = latitud
        );
    }

    // --- READ (list paginated) ---
    public List<PuntoMedicion> findAllPaged(int page, int size, String nombreFiltro) {
        int offset = page * size;
        String sql = "SELECT IdPunto, Nombre, Latitud, Longitud, TipoSensor, Activo, ST_AsText(geom) AS geomWKT " +
                "FROM PuntosMedicion " +
                "WHERE Nombre LIKE ? " +
                "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, "%" + nombreFiltro + "%", size, offset);
    }

    // --- UPDATE ---
    public int update(PuntoMedicion punto) {
        String sql = "UPDATE PuntosMedicion SET Nombre = ?, Latitud = ?, Longitud = ?, TipoSensor = ?, Activo = ?, " +
                "geom = ST_SetSRID(ST_MakePoint(?, ?), 4326) WHERE IdPunto = ?";
        return jdbcTemplate.update(
                sql,
                punto.getNombre(),
                punto.getLatitud(),
                punto.getLongitud(),
                punto.getTipoSensor(),
                punto.isActivo(),
                punto.getLongitud(), // x = longitud
                punto.getLatitud(),  // y = latitud
                punto.getId()
        );
    }

    // --- DELETE ---
    public int deleteById(Long id) {
        String sql = "DELETE FROM PuntosMedicion WHERE IdPunto = ?";
        return jdbcTemplate.update(sql, id);
    }

    /**
     * Busca los puntos de medición de tipo "Sensor CO2" que se encuentran a menos de 50 km
     * de un punto de medición de temperatura específico.
     *
     * Utiliza la fórmula del haversine para calcular la distancia geodésica entre los puntos.
     *
     * @param idPuntoTemperatura El ID del punto de medición de temperatura.
     * @return Lista de objetos CorrelacionDTO que contienen los IDs de los puntos y la distancia entre ellos.
     */

    public List<CorrelacionDTO> findCO2CercanosPorPuntoTemperatura(Long idPuntoTemperatura) {
        String sql = """
        SELECT
            temp.idpunto AS idPuntoTemperatura,
            co2.idpunto AS idPuntoCO2,
            ROUND((ST_Distance(temp.geom::geography, co2.geom::geography) / 1000)::numeric, 2) AS distanciaKm
        FROM puntosmedicion temp
        JOIN puntosmedicion co2 ON co2.tiposensor = 'Sensor CO2'
        WHERE temp.tiposensor = 'Termómetro'
          AND temp.idpunto = ?
          AND temp.idpunto <> co2.idpunto
          AND ST_DWithin(temp.geom::geography, co2.geom::geography, 50000)
    """;
        // Ejecuta la consulta y mapea cada fila del resultado a un objeto CorrelacionDTO.
        return jdbcTemplate.query(sql, new Object[]{idPuntoTemperatura}, (rs, rowNum) -> mapCorrelacion(rs));
    }
    /**
     * Mapea el resultado de la consulta SQL a un objeto CorrelacionDTO.
     *
     * @param rs ResultSet con los datos de la consulta.
     * @return CorrelacionDTO con los datos de la correlación geoespacial.
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet.
     */
    private CorrelacionDTO mapCorrelacion(ResultSet rs) throws SQLException {
        CorrelacionDTO dto = new CorrelacionDTO();
        dto.setIdPuntoTemperatura(rs.getLong("idPuntoTemperatura"));
        dto.setIdPuntoCO2(rs.getLong("idPuntoCO2"));
        dto.setDistanciaKm(rs.getDouble("distanciaKm"));
        return dto;
    }

    // 7. Listado de Medidas sin Georreferenciación
    public List<PuntoUltimaMedicionDTO> findPuntosSinGeorreferenciacion() {
        String sql = """
            SELECT 
                p.idpunto AS idPunto,
                p.nombre AS nombrePunto,
                p.latitud,
                p.longitud,
                MAX(m.fechahora) AS ultimaMedicion
            FROM puntosmedicion p
            LEFT JOIN mediciones m ON p.idpunto = m.idpunto
            WHERE 
                p.latitud IS NULL 
                OR p.longitud IS NULL 
                OR (p.latitud = 0 AND p.longitud = 0)
            GROUP BY 
                p.idpunto, p.nombre, p.latitud, p.longitud
            ORDER BY 
                ultimaMedicion DESC NULLS LAST
        """;
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapPuntoUltimaMedicion(rs));
    }

    /**
     * Mapea el resultado del ResultSet a un objeto PuntoUltimaMedicionDTO
     * @param rs ResultSet con los datos de la consulta SQL
     * @return Objeto PuntoUltimaMedicionDTO con la información del punto
     * @throws SQLException Si ocurre un error al acceder a los datos del ResultSet
     */
    private PuntoUltimaMedicionDTO mapPuntoUltimaMedicion(ResultSet rs) throws SQLException {
        PuntoUltimaMedicionDTO dto = new PuntoUltimaMedicionDTO();
        dto.setIdPunto(rs.getLong("idPunto"));
        dto.setNombrePunto(rs.getString("nombrePunto"));
        BigDecimal lat = rs.getBigDecimal("latitud");
        BigDecimal lon = rs.getBigDecimal("longitud");
        dto.setLatitud(lat != null ? lat.doubleValue() : null);
        dto.setLongitud(lon != null ? lon.doubleValue() : null);

        Timestamp ts = rs.getTimestamp("ultimaMedicion");
        dto.setUltimaMedicion(ts != null ? ts.toLocalDateTime() : null);

        return dto;
    }


}

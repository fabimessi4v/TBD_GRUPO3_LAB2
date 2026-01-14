package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.entities.AreasAfectadas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AreasAfectadasRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // RowMapper
    private final RowMapper<AreasAfectadas> rowMapper = (rs, rowNum) -> {
        AreasAfectadas a = new AreasAfectadas();
        a.setId(rs.getLong("IdArea"));
        a.setNombre(rs.getString("Nombre"));
        a.setDescripcion(rs.getString("Descripcion"));
        a.setTipoRiesgo(rs.getString("TipoRiesgo"));
        a.setGeom(rs.getString("geomGeoJSON")); // GeoJson
        return a;
    };


    // CREATE
    public int save(AreasAfectadas area) {
        String sql = "INSERT INTO AreasAfectadas (Nombre, Descripcion, TipoRiesgo, geom) " +
                "VALUES (?, ?, ?, ST_SetSRID(ST_GeomFromText(?), 4326))";
        return jdbcTemplate.update(sql,
                area.getNombre(),
                area.getDescripcion(),
                area.getTipoRiesgo(),
                area.getGeom()
        );
    }

    // READ paginado + filtro por nombre
    public List<AreasAfectadas> findAllPaged(int page, int size, String nombreFiltro) {
        int offset = page * size;
        String sql =
                "SELECT IdArea, Nombre, Descripcion, TipoRiesgo, ST_AsGeoJSON(geom) AS geomGeoJSON " +
                        "FROM AreasAfectadas WHERE Nombre LIKE ? LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, rowMapper, "%" + nombreFiltro + "%", size, offset);
    }

    // UPDATE
    public int update(AreasAfectadas area) {
        String sql = "UPDATE AreasAfectadas SET Nombre = ?, Descripcion = ?, TipoRiesgo = ?, " +
                "geom = ST_SetSRID(ST_GeomFromText(?), 4326) WHERE IdArea = ?";
        return jdbcTemplate.update(sql,
                area.getNombre(),
                area.getDescripcion(),
                area.getTipoRiesgo(),
                area.getGeom(),
                area.getId()
        );
    }

    // DELETE
    public int deleteById(Long id) {
        String sql = "DELETE FROM AreasAfectadas WHERE IdArea = ?";
        return jdbcTemplate.update(sql, id);
    }









}

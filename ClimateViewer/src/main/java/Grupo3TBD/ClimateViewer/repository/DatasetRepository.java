package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.entities.Dataset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DatasetRepository {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    // Mapeador para convertir filas de la BD a objetos Java
    private final RowMapper<Dataset> rowMapper = (rs, rowNum) -> {
        Dataset dataset = new Dataset();
        dataset.setId(rs.getLong("iddataset"));
        dataset.setNombre(rs.getString("nombre"));
        dataset.setDescripcion(rs.getString("descripcion"));
        dataset.setFuente(rs.getString("fuente"));
        dataset.setFechaActualizacion(rs.getDate("fechaactualizacion").toLocalDate());
        return dataset;
    };

    // --- CREATE ---
    public int save(Dataset dataset) {
        return jdbcTemplate.update(
                "INSERT INTO datasets (nombre, descripcion, fuente, fechaactualizacion) VALUES (?, ?, ?, ?)",
                dataset.getNombre(), dataset.getDescripcion(), dataset.getFuente(), dataset.getFechaActualizacion()
        );
    }

    // --- READ (Con Paginación y Query Simple por nombre) ---
    public List<Dataset> findAllPaged(int page, int size, String nombreFiltro) {
        int offset = page * size;
        String sql = "SELECT * FROM datasets WHERE nombre LIKE ? LIMIT ? OFFSET ?";
        List<Dataset> resultados = jdbcTemplate.query(sql, rowMapper, "%" + nombreFiltro + "%", size, offset);
        return resultados;
    }

    // --- READ (Por ID) ---
    public Dataset findById(Long id) {
        return jdbcTemplate.queryForObject("SELECT * FROM datasets WHERE iddataset = ?", rowMapper, id);
    }

    // --- UPDATE ---
    public int update(Dataset dataset) {
        return jdbcTemplate.update(
                "UPDATE datasets SET nombre = ?, descripcion = ?, fuente = ?, fechaactualizacion = ? WHERE iddataset = ?",
                dataset.getNombre(), dataset.getDescripcion(), dataset.getFuente(), dataset.getFechaActualizacion(), dataset.getId()
        );
    }

    // --- DELETE ---
    public int deleteById(Long id) {
        return jdbcTemplate.update("DELETE FROM datasets WHERE iddataset = ?", id);
    }
}

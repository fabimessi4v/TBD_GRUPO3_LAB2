package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.DTO.ResumenSemanalDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Repository
public class ResumenSemanalRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public ResumenSemanalRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /** Sube los datos del dataset según el id proporcionado a la tabla
     * resumen semanal
     @param idDataset que se está consultando
     */
    public void recalcularResumenSemanal(Integer idDataset) {
        String sql = "CALL interpolar_datos_semanales(?);";
        jdbcTemplate.update(sql, idDataset);
    }

    /** Devuelve una lista del resumen semanal (id, resumen y promedio)
     @return Lista de objetos ResumenSemanal
     */
    public List<ResumenSemanalDTO> obtenerResumen() {
         String sql = """
        SELECT iddataset, semana, promedio
        FROM resumen_semanal
        ORDER BY iddataset, semana;
        """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new ResumenSemanalDTO(
                        rs.getInt("iddataset"),
                        rs.getDate("semana").toLocalDate(),
                        rs.getDouble("promedio")
                )
        );
    }

    /** Según un id devuelve una lista del resumen semanal (id, resumen y promedio)
     * que se solicitó
     @param idDataset que se está consultando
     @return Lista de objetos ResumenSemanal
     */
    public List<ResumenSemanalDTO> obtenerResumenEspecifico(Integer idDataset) {
        String sql = """
        SELECT iddataset, semana, promedio
        FROM resumen_semanal
        WHERE iddataset = ?
        ORDER BY iddataset,semana;
        """;

        return jdbcTemplate.query(
                sql,
                ps -> ps.setInt(1, idDataset),
                (rs, rowNum) -> new ResumenSemanalDTO(
                        rs.getInt("iddataset"),
                        rs.getDate("semana").toLocalDate(),
                        rs.getDouble("promedio")
                )
        );
    }
}

package Grupo3TBD.ClimateViewer.repository;

import Grupo3TBD.ClimateViewer.entities.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
/**
 * Repositorio para la entidad Usuario.
 *
 * Esta clase maneja la interacción con la base de datos para operaciones relacionadas
 * con los usuarios, utilizando JDBC a través de JdbcTemplate.
 *
 * Cada método realiza una consulta SQL específica y mapea los resultados a la entidad Usuario.
 */
@Repository
public class UserRepository {
    // JdbcTemplate es el componente de Spring para interactuar con la base de datos usando SQL.
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // RowMapper para convertir de ResultSet a Usuario
    // Este objeto mapea los nombres de las columnas de la tabla 'usuarios' a los
     // atributos correspondientes de la clase Usuario.
    private final RowMapper<Usuario> usuarioRowMapper = new RowMapper<Usuario>() {
        @Override
        public Usuario mapRow(ResultSet rs, int rowNum) throws SQLException {
            Usuario usuario = new Usuario();
            usuario.setId(rs.getLong("idusuario"));
            usuario.setNombre(rs.getString("nombre"));
            usuario.setEmail(rs.getString("email"));
            usuario.setConHash(rs.getString("conhash"));
            usuario.setRol(rs.getString("rol"));
            usuario.setFechaRegistro(rs.getObject("fecharegistro", LocalDate.class));
            return usuario;
        }
    };

    /**
     * Busca un usuario por su email.
     *
     * @param email Email del usuario a buscar.
     * @return Optional<Usuario> con el usuario encontrado o vacío si no existe.
     */
    public Optional<Usuario> findByEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, email);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Verifica si existe un usuario con el email proporcionado.
     *
     * @param email Email a verificar.
     * @return true si existe, false si no.
     */
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    /**
     * Guarda un nuevo usuario en la base de datos.
     *
     * @param usuario El objeto Usuario con los datos a guardar.
     */
    public void save(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, conhash, rol, fecharegistro) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                usuario.getNombre(),
                usuario.getEmail(),
                usuario.getConHash(),
                usuario.getRol(),
                usuario.getFechaRegistro()
        );
    }

    /**
     * Busca un usuario por su ID.
     *
     * @param id ID del usuario a buscar.
     * @return Optional<Usuario> con el usuario encontrado o vacío si no existe.
     */
    public Optional<Usuario> findById(Long id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, usuarioRowMapper, id);
            return Optional.ofNullable(usuario);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
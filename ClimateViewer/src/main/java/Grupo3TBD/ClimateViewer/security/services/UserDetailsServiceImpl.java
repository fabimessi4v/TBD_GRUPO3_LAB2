package Grupo3TBD.ClimateViewer.security.services;

import Grupo3TBD.ClimateViewer.entities.Usuario;
import Grupo3TBD.ClimateViewer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Servicio para la autenticación de usuarios.
 *
 * Esta clase implementa la interfaz UserDetailsService de Spring Security,
 * que es utilizada por el sistema de autenticación para cargar los datos del usuario
 * (por ejemplo, cuando alguien intenta iniciar sesión).
 *
 * El método loadUserByUsername es llamado automáticamente por Spring Security
 * durante el proceso de autenticación.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    /**
     * Busca el usuario por email y construye el objeto UserDetails requerido por Spring Security.
     *
     * @param email El email del usuario que intenta autenticarse.
     * @return UserDetails Implementación que representa los datos del usuario.
     * @throws UsernameNotFoundException Si no se encuentra el usuario con ese email.
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));
        return UserDetailsImpl.build(usuario);
    }
}
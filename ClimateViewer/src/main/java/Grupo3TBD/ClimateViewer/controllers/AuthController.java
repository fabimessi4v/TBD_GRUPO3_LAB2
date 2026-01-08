package Grupo3TBD.ClimateViewer.controllers;

import Grupo3TBD.ClimateViewer.DTO.LoginRequest;
import Grupo3TBD.ClimateViewer.DTO.MensajeRespuesta;
import Grupo3TBD.ClimateViewer.DTO.SignupRequest;
import Grupo3TBD.ClimateViewer.entities.Usuario;
import Grupo3TBD.ClimateViewer.repository.UserRepository;
import Grupo3TBD.ClimateViewer.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;


    @RestController
    @RequestMapping("/api/auth")
    // Permite conexiones desde el frontend en localhost:3000
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
    public class AuthController {
        //Componente de Spring Security para manejar autenticaciones
        @Autowired
        AuthenticationManager authenticationManager;
        // Repositorio para acceder a datos de usuarios en la base de datos
        @Autowired
        UserRepository usuarioRepository;
        // Componente para codificar/validar contraseñas
        @Autowired
        PasswordEncoder encoder;
        // Utilidad para generar y validar tokens JWT
        @Autowired
        JwtUtils jwtUtils;

        // ----------------- LOGIN -----------------
        @PostMapping("/login")
        public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
            // Busca usuario por email en la base de datos
            Optional<Usuario> userOptional = usuarioRepository.findByEmail(loginRequest.getEmail());
            if (userOptional.isEmpty()) {
                // Si no existe el usuario, retorna error 401 (no autorizado)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new MensajeRespuesta("Error: Invalid email or password!"));
            }

            Usuario usuario = userOptional.get();

            // Autenticación con Spring Security
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            usuario.getEmail(), loginRequest.getPassword()
                    )
            );
            // Guarda la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // Genera un token JWT para el usuario autenticado
            String jwt = jwtUtils.generateJwtToken(authentication);
            // Retornar un JSON de respuesta con el token y datos del usuario
            return ResponseEntity.ok(Map.of(
                    "token", jwt,
                    "id", usuario.getId(),
                    "nombre", usuario.getNombre(),
                    "email", usuario.getEmail(),
                    "rol", usuario.getRol(),
                    "fechaRegistro", usuario.getFechaRegistro()
            ));
        }

        // ----------------- SIGNUP -----------------
        @PostMapping("/signup")
        public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
            // Verifica si el email ya existe
            if (usuarioRepository.existsByEmail(signUpRequest.getEmail())) {
                return ResponseEntity.badRequest()
                        .body(new MensajeRespuesta("Error: Email is already in use!"));
            }

            // Crea nuevo usuario
            Usuario usuario = new Usuario();
            usuario.setNombre(signUpRequest.getNombre());
            usuario.setEmail(signUpRequest.getEmail());
            // Guarda la contraseña encriptada
            usuario.setConHash(encoder.encode(signUpRequest.getPassword()));
            usuario.setRol("usuario"); // rol por defecto
            usuario.setFechaRegistro(LocalDate.now());
            // Guarda el usuario en la base de datos
            usuarioRepository.save(usuario);

            return ResponseEntity.ok(new MensajeRespuesta("User registered successfully!"));
        }
    }


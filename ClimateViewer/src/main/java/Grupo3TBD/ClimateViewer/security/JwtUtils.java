package Grupo3TBD.ClimateViewer.security;

import Grupo3TBD.ClimateViewer.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component
public class JwtUtils {
    // Logger para mostrar mensajes en consola relacionados con JWT.
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    // Se obtiene el valor de la clave secreta para firmar el JWT desde application.properties.
    @Value("${jwt.secret}")
    private String jwtSecret;
    // Tiempo de expiración del JWT (en milisegundos), también configurado en application.properties.
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;
    // Duración del refresh token (en milisegundos).
    @Value("${jwt.refreshExpiration}")
    private int refreshTokenDurationMs;
    // Genera un JWT usando la información de autenticación (usuario autenticado).
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }
    // Otra sobrecarga del método para generar un JWT solo a partir del nombre de usuario.
    public String generateJwtToken(
            String username
    ) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(getSigningKey())
                .compact();
    }
    // Genera un refresh token como un UUID aleatorio.
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }
    // Devuelve el tiempo de expiración del refresh token (timestamp en milisegundos).
    public long getRefreshTokenExpiryTime() {
        return System.currentTimeMillis() + refreshTokenDurationMs;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }
    // Extrae el nombre de usuario (subject) del token JWT.
    public String getUsernameFromJwtToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    // Valida si el JWT es correcto y no ha sido alterado ni expirado.
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Firma JWT inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token JWT inválido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Cadena de claims JWT vacía: {}", e.getMessage());
        }

        return false;
    }
    // Verifica si el token JWT ya expiró.
    public boolean isTokenExpired(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return false;
        } catch (ExpiredJwtException ex) {
            return true;
        } catch (Exception ex) {
            logger.error("Error al verificar expiración del token: {}", ex.getMessage());
            return true;
        }
    }
}
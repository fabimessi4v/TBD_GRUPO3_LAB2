package Grupo3TBD.ClimateViewer.DTO;
/**
 * DTO para la petición de inicio de sesión.
 *  * Esta clase sirve como modelo de datos para recibir información desde el frontend
 *  * cuando un usuario intenta iniciar sesión en el sistema.
 */
public class LoginRequest {
    private String username;
    private String password;
    private String email;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;}
}

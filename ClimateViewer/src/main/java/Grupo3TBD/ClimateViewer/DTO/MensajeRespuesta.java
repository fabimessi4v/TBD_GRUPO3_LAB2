package Grupo3TBD.ClimateViewer.DTO;

/**
 * DTO para la respuesta de mensajes simples.
 *  * Esta clase se utiliza para enviar mensajes simples, como respuesta en los endpoints de la API.
 */
public class MensajeRespuesta {
    private String mensaje;

    public MensajeRespuesta(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

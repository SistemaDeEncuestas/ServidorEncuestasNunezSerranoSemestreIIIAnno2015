package domain;

/**
 * @author adriansb3105
 */
public class Correo {
    
    private String usuario;
    private String contrasenia;
    private String rutaArchivo;
    private String nombreArchivo;
    private String destinatario;
    private String asunto;
    private String mensaje;

    public Correo() {
        this.usuario = "";
        this.contrasenia = "";
        this.rutaArchivo = "";
        this.nombreArchivo = "";
        this.destinatario = "";
        this.asunto = "";
        this.mensaje = "";
    }

    /**
     * @return the usuario
     */
    public String getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the contrasenia
     */
    public String getContrasenia() {
        return contrasenia;
    }

    /**
     * @param contrasenia the contrasenia to set
     */
    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    /**
     * @return the rutaArchivo
     */
    public String getRutaArchivo() {
        return rutaArchivo;
    }

    /**
     * @param rutaArchivo the rutaArchivo to set
     */
    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    /**
     * @return the nombreArchivo
     */
    public String getNombreArchivo() {
        return nombreArchivo;
    }

    /**
     * @param nombreArchivo the nombreArchivo to set
     */
    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    /**
     * @return the destinatario
     */
    public String getDestinatario() {
        return destinatario;
    }

    /**
     * @param destinatario the destinatario to set
     */
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    /**
     * @return the asunto
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * @param asunto the asunto to set
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    @Override
    public String toString() {
        return "Correo{" + "usuario=" + usuario + ", contrasenia=" + contrasenia + ", rutaArchivo=" + rutaArchivo + ", nombreArchivo=" + nombreArchivo + ", destinatario=" + destinatario + ", asunto=" + asunto + ", mensaje=" + mensaje + '}';
    }
    
}

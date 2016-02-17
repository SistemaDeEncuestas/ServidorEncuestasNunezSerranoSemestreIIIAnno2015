package domain;

import java.io.Serializable;

/**
 *
 * @author Daniel
 * 
 */
public class Usuario implements Serializable {
    
    private String nombre;
    private String nickname;
    private String contrasenna;
    private String correoElectronico;

    public Usuario() {
        this.nombre = "";
        this.nickname = "";
        this.contrasenna = "";
        this.correoElectronico = "";
    }

    
    public Usuario(String nombre, String nickname, String contrasenna, String correoElectronico) {
        this.nombre = nombre;
        this.nickname = nickname;
        this.contrasenna = contrasenna;
        this.correoElectronico = correoElectronico;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nombreUsuario) {
        this.nickname = nombreUsuario;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public void setCorreoElectronico(String correoElectronico) {
        this.correoElectronico = correoElectronico;
    }

    @Override
    public String toString() {
        return "Usuario{" + "nombre=" + nombre + ", nombreUsuario=" + nickname + 
                ", contrasenna=" + contrasenna + ", correoElectronico=" + correoElectronico + '}';
    }
    
}

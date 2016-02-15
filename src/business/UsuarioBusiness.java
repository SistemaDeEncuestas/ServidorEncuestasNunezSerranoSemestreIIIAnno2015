package business;

import data.UsuarioData;
import domain.Encuestado;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author Daniel
 */
public class UsuarioBusiness {

    private UsuarioData userData;
    private List<String> usuariosConectados;

    public UsuarioBusiness() {
    }

    public UsuarioBusiness(String nombreArchivo) {
        this.usuariosConectados = new ArrayList<>();
        try {
            this.userData = new UsuarioData(nombreArchivo);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(UsuarioBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean insertar(Encuestado encuestado) {
        boolean existe = false;
        try {

            Encuestado[] encuestados = getEncuestados();
            for (int i = 0; i < encuestados.length; i++) {
                if (encuestados[i].getNombreUsuario().equals(encuestado.getNombreUsuario())) {
                    existe = true;
                }
            }
            if (!existe) {
                this.userData.insertar(encuestado);
                return true;
            }

        } catch (IOException ex) {
            Logger.getLogger(UsuarioBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Encuestado[] getEncuestados() {
        return this.userData.getEncuestados();
    }

    public Encuestado getEncuestado(String nickname, String contrasenna) {

        if (this.userData.getEncuestado(nickname).getContrasenna().equals(contrasenna)) {
            return this.userData.getEncuestado(nickname);
        } else {
            return null;
        }
    }

    public boolean eliminaEncuestado(String nickname) {
        try {
            return this.userData.eliminaEncuestado(nickname);
        } catch (IOException ex) {
            Logger.getLogger(UsuarioBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public boolean editaEncuestado(Encuestado encuestado) {
        try {
            return this.userData.editaEncuestado(encuestado);
        } catch (IOException ex) {
            Logger.getLogger(UsuarioBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String[] getUsuariosConectados() {
        
        String[] usuarios = new String[this.usuariosConectados.size()];
        for (int i = 0; i < usuarios.length; i++) {
            usuarios[i] = this.usuariosConectados.get(i);
        }
        
        return usuarios;
    }

    public void setUsuariosConectados(String nombreusuario) {
        this.usuariosConectados.add(nombreusuario);
    }
}

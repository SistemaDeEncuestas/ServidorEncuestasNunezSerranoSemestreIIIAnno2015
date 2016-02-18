package business;

import data.UsuarioData;
import domain.Encuestado;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author Daniel
 */
public class UsuarioBusiness {

    private UsuarioData userData;

    public UsuarioBusiness() {
        try {
            this.userData = new UsuarioData();
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(UsuarioBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean insertar(Encuestado encuestado) {
        boolean existe = false;
        try {

            Encuestado[] encuestados = getEncuestados();
            for (int i = 0; i < encuestados.length; i++) {
                if (encuestados[i].getNickname().equals(encuestado.getNickname())) {
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

        if (this.userData.getEncuestado(nickname) != null) {
            if (this.userData.getEncuestado(nickname).getContrasenna().equals(contrasenna)) {
                return this.userData.getEncuestado(nickname);
            }
        }
        return null;
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
    
    public List<String> getNombresEncuestados(){
        return this.userData.getNombresEncuestados();
    }
}

package business;

import data.UsuarioData;
import domain.Encuestado;
import java.io.FileNotFoundException;
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

    public UsuarioBusiness(String nombreArchivo) {

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

    public Encuestado getEncuestado(String nickname) {
        return this.userData.getEncuestado(nickname);
    }

    public boolean eliminaEncuestado(String nickname) {
        try {
            return this.userData.eliminaEncuestado(nickname);
        } catch (IOException ex) {
            Logger.getLogger(UsuarioBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

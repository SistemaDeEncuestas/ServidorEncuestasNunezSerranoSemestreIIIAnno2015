package business;

import data.AdministradorData;
import domain.Administrador;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 *
 * @author Daniel
 */
public class AdministradorBusiness {

    private AdministradorData adminData;

    public AdministradorBusiness(String nombreArchivo) {
        try {
            this.adminData = new AdministradorData(nombreArchivo);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertar(Administrador administrador) {
        boolean existe = false;
        Administrador[] administradores = getAdministradores();
        for (int i = 0; i < administradores.length; i++) {
            if (administradores[i].getNombreUsuario().equals(administrador.getNombreUsuario())) {
                existe = true;
            }
        }
        try {
            if (!existe) {
                this.adminData.insertar(administrador);
                return true;
            }

        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public Administrador[] getAdministradores() {
        return this.adminData.getAdministradores();
    }

    public Administrador getAdministrador(String nickname) {
        return this.adminData.getAdministrador(nickname);
    }

    public boolean eliminaAdministrador(String nickname) {
        try {
            return this.adminData.eliminaAdministrador(nickname);
        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public String[] getNombresAdministradores() {
        return this.adminData.getNombresAdministradores();
    }
}

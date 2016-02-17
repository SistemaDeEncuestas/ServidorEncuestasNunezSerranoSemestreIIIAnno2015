package business;

import data.AdministradorData;
import domain.Administrador;
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

    public AdministradorBusiness() {
        try {
            this.adminData = new AdministradorData();
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean insertar(Administrador administrador) {
        boolean existe = false;
        Administrador[] administradores = getAdministradores();
        for (int i = 0; i < administradores.length; i++) {
            if (administradores[i].getNickname().equals(administrador.getNickname())) {
                existe = true;
                break;
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

    public boolean insertarEncuesta(String nombreEncuesta, String nickName){
        try {
            return this.adminData.insertarEncuesta(nombreEncuesta, nickName);
        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Administrador[] getAdministradores() {
        try {
            return this.adminData.getAdministradores();
        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Administrador getAdministrador(String nickname, String contrasenna) {

        try {
            if (this.adminData.getAdministrador(nickname) != null) {
                Administrador admin = this.adminData.getAdministrador(nickname);
                if (admin.getContrasenna().equals(contrasenna)) {
                    return admin;
                }
            }
            
            return null;
        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        try {
            return this.adminData.getNombresParaConsola();
        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public boolean editaAdministrador(Administrador administrador) {
        try {
            return this.adminData.editaAdministrador(administrador);
        } catch (IOException ex) {
            Logger.getLogger(AdministradorBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }
}

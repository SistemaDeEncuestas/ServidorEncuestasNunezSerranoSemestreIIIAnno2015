package business;

import data.AdministradorData;
import domain.Administrador;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jdom.JDOMException;

/**
 *
 * @author Daniel
 */
public class AdministradorBusiness {

    private AdministradorData adminData;

    public AdministradorBusiness(String nombreArchivo) throws JDOMException, IOException {

        this.adminData = new AdministradorData(nombreArchivo);

    }

    public void insertar(Administrador administrador) throws IOException {
        this.adminData.insertar(administrador);
    }

    public Administrador[] getAdministradores() {
        return this.adminData.getAdministradores();
    }

    public Administrador getAdministrador(String nickname) {
        return this.adminData.getAdministrador(nickname);
    }
    
     public boolean eliminaAdministrador(String nickname) throws FileNotFoundException, IOException{
         return this.adminData.eliminaAdministrador(nickname);
     }
    
}

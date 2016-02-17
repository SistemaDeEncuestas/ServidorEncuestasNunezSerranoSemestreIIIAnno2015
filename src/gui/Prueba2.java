package gui;

import business.AdministradorBusiness;
import data.AdministradorData;
import domain.Administrador;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 *
 * @author adriansb3105
 */
public class Prueba2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
        AdministradorBusiness administradorBusiness = new AdministradorBusiness();
        Administrador administrador = administradorBusiness.getAdministrador("adriansb3105", "Serranobrenes");
        System.out.println(administrador.getEncuestasCreadas().get(0));
    }

}

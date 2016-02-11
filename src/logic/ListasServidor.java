package logic;

import business.AdministradorBusiness;
import business.NombresDeArchivosBusiness;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 * @author adriansb3105
 */
public class ListasServidor implements Runnable{

    private JList<String> listaAdmins;
    private JList<String> listaEncuestas;
    private AdministradorBusiness administradorBusiness;
    private NombresDeArchivosBusiness nombresDeArchivosBusiness;
        
    public ListasServidor(JList listaAdmins, JList listaEncuestas) {
        this.listaAdmins = listaAdmins;
        this.listaEncuestas = listaEncuestas;
    }
    
    @Override
    public void run() {
        
        while(true){
            this.administradorBusiness = new AdministradorBusiness("admin");
            this.listaAdmins.setListData(this.administradorBusiness.getNombresAdministradores());
            
            this.nombresDeArchivosBusiness = new NombresDeArchivosBusiness();
            this.listaEncuestas.setListData(this.nombresDeArchivosBusiness.listaNombresArchivos());
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ListasServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}

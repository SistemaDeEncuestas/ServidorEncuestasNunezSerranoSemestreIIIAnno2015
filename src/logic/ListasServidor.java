package logic;

import business.AdministradorBusiness;
import business.NombresDeArchivosBusiness;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 * @author adriansb3105
 */
public class ListasServidor implements Runnable {

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

        while (true) {

            iniciar();

            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ListasServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void iniciar() {
        this.administradorBusiness = new AdministradorBusiness();
        this.nombresDeArchivosBusiness = new NombresDeArchivosBusiness();

        String nombresAdmins[] = new String[(this.administradorBusiness.getNombresAdministradores().length) + (1)];
        nombresAdmins[0] = "Administradores";
        for (int i = 0; i < this.administradorBusiness.getNombresAdministradores().length; i++) {
            nombresAdmins[i + 1] = this.administradorBusiness.getNombresAdministradores()[i];
        }

        String[] nombresEncuestas = new String[this.nombresDeArchivosBusiness.getNombresDeEncuestas().size() + 1];
        nombresEncuestas[0] = "Encuestas";
        for (int i = 0; i < this.nombresDeArchivosBusiness.getNombresDeEncuestas().size(); i++) {
            nombresEncuestas[i + 1] = this.nombresDeArchivosBusiness.getNombresDeEncuestas().get(i);
        }

        this.listaAdmins.setListData(nombresAdmins);
        this.listaEncuestas.setListData(nombresEncuestas);
    }
}

package logic;

import business.UsuarioBusiness;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 * @author adriansb3105
 */
public class UsuariosConectados implements Runnable{
    
    private UsuarioBusiness usuarioBusiness;
    private JList<String> listaUsuariosConectados;
    
    public UsuariosConectados(JList listaUsuariosConectados) {
        this.listaUsuariosConectados = listaUsuariosConectados;
        this.usuarioBusiness = new UsuarioBusiness();
    }

    @Override
    public void run() {
        
        while(true){
            
        this.listaUsuariosConectados.setListData(this.usuarioBusiness.getUsuariosConectados());
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UsuariosConectados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

package logic;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;

/**
 * @author adriansb3105
 */
public class UsuariosConectados implements Runnable{
    
    private JList<String> listaUsuariosConectados;
    String[] usuarios = {"Usuarios conectados", "usuario 1", "usuario 2", "usuario 3", "usuario 4", "usuario 5"};
    
    public UsuariosConectados(JList listaUsuariosConectados) {
        this.listaUsuariosConectados = listaUsuariosConectados;
    }

    @Override
    public void run() {
        
        while(true){
            
        this.listaUsuariosConectados.setListData(usuarios);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UsuariosConectados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

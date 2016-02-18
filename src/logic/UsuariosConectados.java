package logic;

import business.UsuarioBusiness;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JList;
import util.Strings;

/**
 * @author adriansb3105
 */
public class UsuariosConectados implements Runnable{
    
    private JList<String> listaUsuariosConectados;
    
    public UsuariosConectados(JList listaUsuariosConectados) {
        this.listaUsuariosConectados = listaUsuariosConectados;
    }

    @Override
    public void run() {
        
        while(true){
        List<String> usuariosLista = Strings.LISTA_USUARIOS_CONECTADOS;
        String[] usuarios = new String[usuariosLista.size()+1];
        
        usuarios[0] = "Usuarios conectados";    
            
            for (int i = 0; i < usuariosLista.size(); i++) {
                usuarios[i+1] = usuariosLista.get(i);
            }
        
        
        this.listaUsuariosConectados.setListData(usuarios);
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Logger.getLogger(UsuariosConectados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
}

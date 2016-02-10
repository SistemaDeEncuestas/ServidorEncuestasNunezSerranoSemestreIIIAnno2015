package business;

import data.UsuarioData;
import domain.Encuestado;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.jdom.JDOMException;

/**
 * @author Daniel
 */
public class UsuarioBusiness {
    
    private UsuarioData userData;
    
    public UsuarioBusiness(String nombreArchivo) throws JDOMException, IOException {
        
        this.userData = new UsuarioData(nombreArchivo);
        
    }
    
    public void insertar(Encuestado encuestado) throws IOException {
        this.userData.insertar(encuestado);
    }

    public Encuestado[] getEncuestados() {
        return this.userData.getEncuestados();
    }
    
    public Encuestado getEncuestado(String nickname) {
        return this.userData.getEncuestado(nickname);
    }
    
    public boolean eliminaEncuestado(String nickname) throws FileNotFoundException, IOException {
        return this.userData.eliminaEncuestado(nickname);
    }
}

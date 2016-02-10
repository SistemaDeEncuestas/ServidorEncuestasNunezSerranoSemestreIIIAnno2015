package business;

import data.NombresDeArchivosData;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author adriansb3105
 */
public class NombresDeArchivosBusiness {

    private NombresDeArchivosData nombresDeArchivosData;

    public NombresDeArchivosBusiness() {
        try {
            this.nombresDeArchivosData = new NombresDeArchivosData();
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(NombresDeArchivosBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertarNombre(String nombreArchivo){
        try {
            this.nombresDeArchivosData.insertarNombre(nombreArchivo);
        } catch (IOException ex) {
            Logger.getLogger(NombresDeArchivosBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> getNombres(){
        return this.nombresDeArchivosData.getNombres();
    }
    
    public boolean existeArchivo(String nombreArchivo){
        return this.nombresDeArchivosData.existeArchivo(nombreArchivo);
    }
}

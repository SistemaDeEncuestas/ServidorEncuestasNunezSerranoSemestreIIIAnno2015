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
    
    public void insertarNombre(String nombreArchivo, String creador){
        try {
            this.nombresDeArchivosData.insertarNombre(nombreArchivo, creador);
        } catch (IOException ex) {
            Logger.getLogger(NombresDeArchivosBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> getNombresDeEncuestas(){
        return this.nombresDeArchivosData.getNombresDeEncuestas();
    }
    
    public boolean existeArchivo(String nombreArchivo){
        return this.nombresDeArchivosData.existeArchivo(nombreArchivo);
    }
    
    public boolean borrarNombreArchivo(String nombreArchivo){
        try {
            return this.nombresDeArchivosData.borrarNombreArchivo(nombreArchivo);
        } catch (IOException ex) {
            Logger.getLogger(NombresDeArchivosBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

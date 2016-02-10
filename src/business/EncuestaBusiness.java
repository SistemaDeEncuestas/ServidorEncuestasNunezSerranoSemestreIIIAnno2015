package business;

import data.EncuestaData;
import domain.Encuesta;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author adriansb3105
 */
public class EncuestaBusiness {

    private EncuestaData encuestaData;

    public EncuestaBusiness(String nombreArchivo) {
        try {
            this.encuestaData = new EncuestaData(nombreArchivo);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean insertar(Encuesta encuesta){
        try {
            return this.encuestaData.insertar(encuesta);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public List<Encuesta> getTodasLasEncuestas(){
        try {
            return this.encuestaData.getTodasLasEncuestas();
        } catch (IOException | JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Encuesta getEncuesta(String nombreEncuesta){
        try {
            return this.encuestaData.getEncuesta(nombreEncuesta);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}

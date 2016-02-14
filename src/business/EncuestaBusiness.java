package business;

import data.EncuestaData;
import domain.Encuesta;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author adriansb3105
 */
public class EncuestaBusiness {

    private EncuestaData encuestaData;

    /**
     * Este constructor es para los metodos de escritura como insertar, editar y borrar
     * @param nombreArchivo  Recibe el nombre de la encuesta
     **/
    public EncuestaBusiness(String nombreArchivo) {
        try {
            this.encuestaData = new EncuestaData(nombreArchivo);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Este constructor es solo para los metodos de lectura como los get
     **/
    public EncuestaBusiness() {
        this.encuestaData = new EncuestaData();
    }
    
    
    public boolean insertar(Encuesta encuesta){
        try {
            return this.encuestaData.insertar(encuesta);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public Encuesta[] getTodasLasEncuestas(){
        try {
            return this.encuestaData.getTodasLasEncuestas();
        } catch (IOException | JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Encuesta getEncuesta(){
        try {
            return this.encuestaData.getEncuesta();
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public Encuesta getEncuesta(String nombreEncuesta){
        
        for (int i = 0; i < getTodasLasEncuestas().length; i++) {
            if (getTodasLasEncuestas()[i].getTitulo().equals(nombreEncuesta)) {
                return getTodasLasEncuestas()[i];
            }
        }
        return null;
    }
    
    public Encuesta[] getEncuestasPorAdmin(String nickname){
        try {
            return this.encuestaData.getEncuestasPorAdmin(nickname);
        } catch (IOException | JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public boolean borrarEncuesta(){
        try {
            return this.encuestaData.borrarEncuesta();
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
   }
    
    public boolean editarEncuesta(Encuesta encuesta){
        try {
            return this.encuestaData.editarEncuesta(encuesta);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
}

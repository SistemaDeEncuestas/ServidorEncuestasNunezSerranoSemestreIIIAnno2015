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

    public EncuestaBusiness() {
        try {
            this.encuestaData = new EncuestaData();
        } catch (JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void iniciar(String rutaArchivo){
        try {
            this.encuestaData.iniciar(rutaArchivo);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void iniciarEncuestaRespondida(String rutaArchivo){
        try {
            this.encuestaData.iniciarEncuestaRespondida(rutaArchivo);
        } catch (JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean insertar(Encuesta encuesta){
        try {
            return this.encuestaData.insertar(encuesta);
        } catch (IOException | JDOMException ex) {
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
    
    public List<String> getPreguntasPorEncuesta(String nombreEncuesta){
        try {
            return this.encuestaData.getPreguntasPorEncuesta(nombreEncuesta);
        } catch (JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
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
    
    public List<String> getNombresDeEncuestasPorAdmin(String nickname){
        try {
            return this.encuestaData.getNombresDeEncuestasPorAdmin(nickname);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    /**
     * @deprecated Se debe usar con el titulo de la encuesta si no da null pointer
     **/
    public Encuesta getEncuesta(String nombreEncuesta){
        
        for (int i = 0; i < getTodasLasEncuestas().length; i++) {
//            System.out.println(getTodasLasEncuestas()[i].getNombreArchivo()+ "----"+(nombreEncuesta));
            if (getTodasLasEncuestas()[i].getNombreArchivo().equals(nombreEncuesta)) {
                return getTodasLasEncuestas()[i];
            }
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
        } catch (JDOMException ex) {
            Logger.getLogger(EncuestaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    
}

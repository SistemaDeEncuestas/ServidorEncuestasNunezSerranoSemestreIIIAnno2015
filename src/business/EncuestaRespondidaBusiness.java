package business;

import data.EncuestaRespondidaData;
import domain.Encuesta;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author adriansb3105
 */
public class EncuestaRespondidaBusiness {

    private EncuestaRespondidaData encuestaRespondidaData;

    public EncuestaRespondidaBusiness() {
        this.encuestaRespondidaData = new EncuestaRespondidaData();
    }
    
    public boolean insertar(Encuesta encuesta){
        try {
            return this.encuestaRespondidaData.insertar(encuesta);
        } catch (IOException ex) {
            Logger.getLogger(EncuestaRespondidaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JDOMException ex) {
            Logger.getLogger(EncuestaRespondidaBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
}

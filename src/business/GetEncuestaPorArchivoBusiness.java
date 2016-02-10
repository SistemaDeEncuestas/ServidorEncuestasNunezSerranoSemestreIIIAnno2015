package business;

import data.GetEncuestaPorArchivoData;
import domain.Encuesta;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author adriansb3105
 */
public class GetEncuestaPorArchivoBusiness {

    private GetEncuestaPorArchivoData getEncuestaPorArchivoData;

    public GetEncuestaPorArchivoBusiness(String nombreArchivo) {
        try {
            this.getEncuestaPorArchivoData = new GetEncuestaPorArchivoData(nombreArchivo);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(GetEncuestaPorArchivoBusiness.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public Encuesta getEncuesta(){
        return this.getEncuestaPorArchivoData.getEncuesta();
    }
    
}

package domain;

import java.util.List;
import util.Strings;

/**
 *
 * @author Daniel
 */
public class PreguntaRespuestaMultiple extends Pregunta{
    
    public PreguntaRespuestaMultiple(String enunciado) {
        super(enunciado, Strings.TIPO_MULTIPLE);
    }
    
    public PreguntaRespuestaMultiple() {
        super("", Strings.TIPO_MULTIPLE);
    }
    
    @Override
    public List getListaRespuestas() {
        return super.getListaRespuestas();
    }

    @Override
    public void setListaRespuestas(List listaRespuestas) {
        super.setListaRespuestas(listaRespuestas);
    }
    
     @Override
    public void cargaComponentes(){
        //TODO
    }

    
}

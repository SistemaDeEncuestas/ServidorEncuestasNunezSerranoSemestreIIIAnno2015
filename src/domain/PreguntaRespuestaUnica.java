package domain;

import java.util.List;
import util.Strings;

/**
 *
 * @author Daniel
 */
public class PreguntaRespuestaUnica extends Pregunta {

    public PreguntaRespuestaUnica(String enunciado) {
        super(enunciado, Strings.TIPO_UNICA);
    }
    

    public PreguntaRespuestaUnica() {
        super("", Strings.TIPO_UNICA);
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

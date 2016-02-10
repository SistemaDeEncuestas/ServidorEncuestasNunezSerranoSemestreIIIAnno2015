package domain;

import java.util.List;
import util.Strings;

/**
 *
 * @author Daniel
 */
public class PreguntaAbierta extends Pregunta{

    public PreguntaAbierta(String enunciado) {
        super(enunciado, Strings.TIPO_ABIERTA);
    }

    public PreguntaAbierta() {
        super("", Strings.TIPO_ABIERTA);
    }

    @Override
    public List<String> getListaRespuestas() {
        return super.getListaRespuestas();
    }

    @Override
    public void setListaRespuestas(List<String> listaRespuestas) {
        super.setListaRespuestas(listaRespuestas);
    }

    @Override
    public void cargaComponentes(){
        //TODO
    }

}

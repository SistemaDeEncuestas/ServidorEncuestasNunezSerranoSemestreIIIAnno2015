package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public abstract class Pregunta {

    private String enunciado;
    private String tipo;
    private List<String> listaRespuestas;

    public Pregunta(String enunciado, String tipo) {
        this.enunciado = enunciado;
        this.tipo = tipo;
        this.listaRespuestas = new ArrayList<>();
    }
    
    public Pregunta() {
        this.enunciado = "";
        this.tipo = "";
        this.listaRespuestas = new ArrayList<>();
    }

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public abstract void cargaComponentes();

    public List<String> getListaRespuestas() {
        return listaRespuestas;
    }

    public void setListaRespuestas(List<String> listaRespuestas) {
        this.listaRespuestas = listaRespuestas;
    }

    @Override
    public String toString() {
        return "Pregunta{" + "enunciado=" + enunciado + ", tipo=" + tipo + '}';
    }

}

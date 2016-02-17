package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Encuestado extends Usuario{
    
   private List<Encuesta> listaEncuestas;

    public Encuestado(String nombre, String nickName, String contrasenna, String correoElectronico) {
        super(nombre, nickName, contrasenna, correoElectronico);
        this.listaEncuestas = new ArrayList<>();
    }

    public Encuestado() {
        super("", "", "", "");
        this.listaEncuestas = new ArrayList<>();
    }
    

    public List<Encuesta> getListaEncuestas() {
        return listaEncuestas;
    }

    public void setListaEncuestas(List<Encuesta> listaEncuestas) {
        this.listaEncuestas = listaEncuestas;
    }
    
    public void agregaEncuesta(Encuesta encuesta){
        this.listaEncuestas.add(encuesta);
    }

    public void eliminaEncuesta(Encuesta encuesta){
        this.listaEncuestas.remove(encuesta);
    }
    
    @Override
    public String toString() {
        return "Encuestado{" + "listaEncuestas=" + listaEncuestas + '}';
    }
    
}

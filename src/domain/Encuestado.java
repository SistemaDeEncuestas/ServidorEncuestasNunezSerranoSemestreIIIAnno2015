package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Encuestado extends Usuario{
    
   private List<String> listaEncuestas;

    public Encuestado(String nombre, String nickName, String contrasenna, String correoElectronico) {
        super(nombre, nickName, contrasenna, correoElectronico);
        this.listaEncuestas = new ArrayList<>();
    }

    public Encuestado() {
        super("", "", "", "");
        this.listaEncuestas = new ArrayList<>();
    }
    

    public List<String> getListaEncuestas() {
        return listaEncuestas;
    }

    public void setListaEncuestas(List<String> listaEncuestas) {
        this.listaEncuestas = listaEncuestas;
    }
    
    public void agregaEncuesta(String nombreEncuesta){
        this.listaEncuestas.add(nombreEncuesta);
    }

    public void eliminaEncuesta(String nombreEncuesta){
        this.listaEncuestas.remove(nombreEncuesta);
    }
    
    @Override
    public String toString() {
        return "Encuestado{" + "listaEncuestas=" + listaEncuestas + '}';
    }
    
}

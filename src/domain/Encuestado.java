package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Encuestado extends Usuario{
    
   private List<Encuesta> listaEncuestas;

    public Encuestado(String nombre, String nombreUsuario, String contrasenna, String correoElectronico) {
        super(nombre, nombreUsuario, contrasenna, correoElectronico);
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

    @Override
    public String toString() {
        return "Encuestado{" + "listaEncuestas=" + listaEncuestas + '}';
    }
    
}

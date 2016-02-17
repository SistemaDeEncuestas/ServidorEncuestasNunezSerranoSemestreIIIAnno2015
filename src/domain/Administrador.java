package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Administrador extends Usuario {

    private List<String> encuestasCreadas;
    private boolean primeraVez;

    public Administrador(String nombre, String nickname, String contrasenna,String correoElectronico) {
        super(nombre, nickname, contrasenna, correoElectronico);
        this.encuestasCreadas = new ArrayList<>();
        this.primeraVez = true;
    }

    public Administrador() {
        super("", "", "", "");
        this.encuestasCreadas = new ArrayList<>();
        this.primeraVez = true;
    }
    
    public List<String> getEncuestasCreadas() {
        return encuestasCreadas;
    }

    public void setEncuestasCreadas(List<String> encuestasCreadas) {
        this.encuestasCreadas = encuestasCreadas;
    }

    public void addEncuestasCreadas(String nombreEncuesta) {
        this.encuestasCreadas.add(nombreEncuesta);
    }

   public void agregaEncuesta(String nombreEncuesta){
       this.encuestasCreadas.add(nombreEncuesta);
   }
   
   public void eliminaEncuesta(String nombreEncuesta){
       this.encuestasCreadas.remove(nombreEncuesta);
   }

    public boolean isPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    @Override
    public String toString() {
        return super.toString() + " Administrador{" + "encuestasCreadas=" + encuestasCreadas + ", primeraVez=" + primeraVez + '}';
    }

}

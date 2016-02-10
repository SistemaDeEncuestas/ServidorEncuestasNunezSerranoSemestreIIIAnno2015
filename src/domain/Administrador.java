package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Administrador extends Usuario {

    private List<Encuesta> encuestasCreadas;
    private boolean primeraVez;

    public Administrador(String nombre, String nombreUsuario, String contrasenna,String correoElectronico) {
        super(nombre, nombreUsuario, contrasenna, correoElectronico);
        this.encuestasCreadas = new ArrayList<>();
        this.primeraVez = true;
    }

    public Administrador() {
        super("", "", "", "");
        this.encuestasCreadas = new ArrayList<>();
        this.primeraVez = true;
    }
    
    public List<Encuesta> getEncuestasCreadas() {
        return encuestasCreadas;
    }

    public void setEncuestasCreadas(List<Encuesta> encuestasCreadas) {
        this.encuestasCreadas = encuestasCreadas;
    }

   

    public boolean isPrimeraVez() {
        return primeraVez;
    }

    public void setPrimeraVez(boolean primeraVez) {
        this.primeraVez = primeraVez;
    }

    @Override
    public String toString() {
        return "Administrador{" + "encuestasCreadas=" + encuestasCreadas + ", primeraVez=" + primeraVez + '}';
    }

}

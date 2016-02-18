package gui;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.UsuarioBusiness;
import data.AdministradorData;
import domain.Administrador;
import domain.Encuesta;
import domain.Encuestado;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.jdom.JDOMException;

/**
 *
 * @author adriansb3105
 */
public class Prueba2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

//        AdministradorBusiness administradorBusiness = new AdministradorBusiness();
//        Administrador administrador = administradorBusiness.getAdministrador("adriansb3105", "Serranobrenes");
//        System.out.println(administrador.getEncuestasCreadas().get(0));
        Prueba2 p = new Prueba2();
        p.i();

    }

    public void i() {

//        UsuarioBusiness u = new UsuarioBusiness();
//        EncuestaBusiness eb = new EncuestaBusiness();
//        eb.iniciar("encuesta_1");

//        if (u.getEncuestado("panqu7221", "a") != null) {
//            Encuestado e = u.getEncuestado("panqui7221", "a");

//            List<String> lista = new ArrayList<>();
//            Encuesta enc = eb.getEncuesta();
//            lista.add(enc.getNombreArchivo());
//
//            e.setListaEncuestas(lista);
//
//            u.editaEncuestado(e);
//        }else{
//            System.out.println("El usuario no existe");
//        }

//        System.out.println(e.getListaEncuestas());
    }

}

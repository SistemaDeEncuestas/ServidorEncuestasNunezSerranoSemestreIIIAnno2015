package main;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.UsuarioBusiness;
import domain.Administrador;
import domain.Encuesta;
import domain.Encuestado;
import domain.Pregunta;
import domain.PreguntaAbierta;
import domain.PreguntaRespuestaMultiple;
import domain.PreguntaRespuestaUnica;
import gui.ServidorInterfaz;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.JDOMException;

/**
 * @author adriansb3105
 */
public class MainServidor {

    public static void main(String[] args){
        
        try {
            //        int puerto = 5700;
//
//        ServidorInterfaz servidorInterfaz = new ServidorInterfaz(puerto);
//        servidorInterfaz.setVisible(true);
            //============================================================
        
            EncuestaBusiness eb = new EncuestaBusiness("miArchivo1");
            Encuestado a = new Encuestado("AdrianSerrano", "Adrian", "qweryty", "c@c.com");
            List<Encuesta> lista = eb.getTodasLasEncuestas();
            
            a.setListaEncuestas(lista);
            
            UsuarioBusiness ab = new UsuarioBusiness("src/files/users.xml");
            ab.insertar(a);
            
             Encuestado[] admins = ab.getEncuestados();
             
             for (int i = 0; i < admins.length; i++) {
                 System.out.println(admins[i]);
                
            }
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(MainServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
        
        
    }
    
}

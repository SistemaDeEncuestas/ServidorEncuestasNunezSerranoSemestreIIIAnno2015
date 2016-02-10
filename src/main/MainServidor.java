package main;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import domain.Administrador;
import domain.Encuesta;
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
        
//        EncuestaBusiness encuestaBusiness = new EncuestaBusiness("miArchivo2");
//        
//        String r1 = "resp 1 - pregunt";
//        String r2 = "resp 2 - pregunt";
//        String r3 = "resp 3 - pregunt";
//        String r4 = "resp 4 - pregunt";
//        List<String> respAbierta = new ArrayList<>();
//        respAbierta.add(r1);
//        List<String> respuestas =  new ArrayList<>();
//        respuestas.add(r1);
//        respuestas.add(r2);
//        respuestas.add(r3);
//        respuestas.add(r4);
//        
//        Pregunta p1 = new PreguntaAbierta("Enun para la preg 1");p1.setListaRespuestas(respAbierta);
//        Pregunta p2 = new PreguntaRespuestaMultiple("Enun para la preg 2");p2.setListaRespuestas(respuestas);
//        Pregunta p3 = new PreguntaAbierta("Enun para la preg 3");p3.setListaRespuestas(respuestas);
//        Pregunta p4 = new PreguntaAbierta("Enun para la preg 4");p4.setListaRespuestas(respAbierta);
//        Pregunta p5 = new PreguntaRespuestaUnica("Enun para la preg 5");p5.setListaRespuestas(respuestas);
//        Pregunta p6 = new PreguntaRespuestaMultiple("Enun para la preg 6");p6.setListaRespuestas(respuestas);
//        Pregunta p7 = new PreguntaRespuestaUnica("Enun para la preg 7");p7.setListaRespuestas(respuestas);
//        
//        List<Pregunta> preguntas = new ArrayList<>();
//        preguntas.add(p1);
////        preguntas.add(p2);
//        preguntas.add(p3);
//        preguntas.add(p4);
////        preguntas.add(p5);
//        preguntas.add(p6);
////        preguntas.add(p7);
//        
//        Encuesta encuesta2 = new Encuesta("Adrian", "Encuesta para la prueba titulo 2",
//                                          "Esta es la descripcion de la encuesta 2", preguntas);
//
//
//        
//        if (encuestaBusiness.insertar(encuesta2)) {
//            System.out.println("insertada la "+encuesta2.getTitulo());
//        }else{
//            System.out.println("la encuesta "+encuesta2.getTitulo() + " ya existe");
//        }
//
            
            EncuestaBusiness eb = new EncuestaBusiness("miArchivo1");
            Administrador a = new Administrador("AdrianSerrano", "Adrian", "qweryty", "c@c.com");
            List<Encuesta> lista = eb.getTodasLasEncuestas();
            
             
        
        
//         List<Encuesta> lista = eb.getTodasLasEncuestas();
//         lista.add();
            
            a.setEncuestasCreadas(lista);
            
            AdministradorBusiness ab = new AdministradorBusiness("admin.xml");
            ab.insertar(a);
            
             Administrador[] admins = ab.getAdministradores();
             
             for (int i = 0; i < admins.length; i++) {
                 System.out.println(admins[i]);
                
            }
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(MainServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
        
        
        
    }
    
}

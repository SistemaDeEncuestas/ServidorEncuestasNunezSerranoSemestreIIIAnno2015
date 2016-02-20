/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.EncuestaRespondidaBusiness;
import business.UsuarioBusiness;
import data.EncuestaRespondidaData;
import domain.Administrador;
import domain.Encuesta;
import domain.Pregunta;
import domain.PreguntaAbierta;
import domain.PreguntaRespuestaMultiple;
import domain.PreguntaRespuestaUnica;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import logic.Servidor;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import util.Strings;

/**
 *
 * @author adriansb3105
 */
public class PruebaServidor extends javax.swing.JFrame {

    private String peticion = Strings.PETICION_LOGIN_ADMIN;
    private String nick;
    private String contrasenna;
    private Administrador administrador;
    private AdministradorBusiness administradorBusiness = new AdministradorBusiness();
    
    public PruebaServidor() {
//        initComponents();
//        
        try {
            ServerSocket serverSocket = new ServerSocket(5700);

            System.out.println("iniciado");
            
            do {
                Socket socket = serverSocket.accept();
                PrintStream enviar = new PrintStream(socket.getOutputStream());
                BufferedReader recibir = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                this.peticion = recibir.readLine();

                switch (this.peticion) {

                    case Strings.PETICION_LOGIN_ADMIN:
                        this.nick = recibir.readLine();
                        this.contrasenna = recibir.readLine();

                        if (this.administradorBusiness.getAdministrador(this.nick, this.contrasenna) == null) {
                            enviar.println("null");
                            System.out.println("Intento de inicio de sesion fallido");
                        } else {
                            this.administrador = this.administradorBusiness.getAdministrador(this.nick, this.contrasenna);
                            enviar.println(enviarPeticionLoginAdmin(this.administrador));
                            System.out.println(this.nick + " ha iniciado sesión");
                        }
                        break;
                }
                socket.close();
            } while (true);
        } catch (BindException e) {
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 546, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
////
//////        java.awt.EventQueue.invokeLater(new Runnable() {
//////            public void run() {
//////                new PruebaServidor();
//////            }
//////        });
        
//        EncuestaRespondidaData e = new EncuestaRespondidaData();
//        EncuestaBusiness enc = new EncuestaBusiness();
//        
////        try {
//            enc.iniciar("encuesta_3");
//            
////            e.insertaNuevaEncuesta("abcefg");
////            System.out.println(e.getNumeroPorEncuesta());
//            Encuesta encuesta = enc.getEncuesta();
            
        
//        UsuarioBusiness u = new UsuarioBusiness();
//        
//        u.eliminarEncuestaEnUsuario("encuesta_2_a");
            
            
        EncuestaRespondidaBusiness erb = new EncuestaRespondidaBusiness();
            
        System.out.println(erb.getPreguntas("encuesta_2_a", "preugnta 1"));
            
        
//        } catch (IOException ex) {
//            Logger.getLogger(PruebaServidor.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (JDOMException ex) {
//            Logger.getLogger(PruebaServidor.class.getName()).log(Level.SEVERE, null, ex);
//        }
     }

    public void iniciar(){
        String r1 = "resp 1 - pregunt";
        String r2 = "resp 2 - pregunt";
        String r3 = "resp 3 - pregunt";
        String r4 = "resp 4 - pregunt";
        List<String> respAbierta = new ArrayList<>();
        respAbierta.add(r1);
        List<String> respuestas =  new ArrayList<>();
        respuestas.add(r1);
        respuestas.add(r2);
        respuestas.add(r3);
        respuestas.add(r4);
        
        Pregunta p1 = new PreguntaAbierta("Enun para la preg 1");p1.setListaRespuestas(respAbierta);
        Pregunta p2 = new PreguntaRespuestaMultiple("Enun para la preg 2");p2.setListaRespuestas(respuestas);
        Pregunta p3 = new PreguntaAbierta("Enun para la preg 3");p3.setListaRespuestas(respuestas);
        Pregunta p4 = new PreguntaAbierta("Enun para la preg 4");p4.setListaRespuestas(respAbierta);
        Pregunta p5 = new PreguntaRespuestaUnica("Enun para la preg 5");p5.setListaRespuestas(respuestas);
        Pregunta p6 = new PreguntaRespuestaMultiple("Enun para la preg 6");p6.setListaRespuestas(respuestas);
        Pregunta p7 = new PreguntaRespuestaUnica("Enun para la preg 7");p7.setListaRespuestas(respuestas);
        
        List<Pregunta> preguntas = new ArrayList<>();
        preguntas.add(p1);
        preguntas.add(p2);
        preguntas.add(p3);
        preguntas.add(p4);
        preguntas.add(p5);
        preguntas.add(p6);
        preguntas.add(p7);
        
        
//        AdministradorBusiness admin = new AdministradorBusiness();
//        Administrador a = new Administrador("Adrian Serrano", "adriansb3105", "Serranobrenes", "adrian@gmail.com");
//        System.out.println("inserto: "+admin.insertar(a));
//        System.out.println("edito: "+admin.editaAdministrador(a));
////        System.out.println("getAdmin: "+admin.getAdministrador("adriansb3105", "Serranobrenes"));
//        System.out.println("todosAdmins"+Arrays.toString(admin.getAdministradores()));
//        System.out.println("NombresAdmins: "+Arrays.toString(admin.getNombresAdministradores()));
//        admin.eliminaAdministrador("adriansb3105");
        
        
        Encuesta encuesta = new Encuesta("adriansb3105", "Encuesta - este es el titulo233",
                                         "Esta es la descripcion de la encuesta 1", "encuesta_1", preguntas);
//        encuesta.setNombreArchivo("encuesta_1");
//        
        EncuestaBusiness encuestaBusiness = new EncuestaBusiness();
        encuestaBusiness.iniciar(encuesta.getNombreArchivo());
//        
        encuestaBusiness.insertar(encuesta);
//        System.out.println("lo hizo: "+administradorBusiness.insertarEncuesta(encuesta.getNombreArchivo(), encuesta.getNickname()));
//        encuestaBusiness.borrarEncuesta();
        
//        System.out.println("inserto: "+encuestaBusiness.insertar(encuesta));
//        System.out.println(Arrays.toString(encuestaBusiness.getTodasLasEncuestas()));
//        System.out.println(encuestaBusiness.getEncuestasPorAdmin("adriansb3105").length);
        
//        Encuesta encuesta = new Encuesta("Adrian", "Encuesta para la PruebaServidor titulo 1",
//                                          "Esta es la descripcion de la encuesta 1", preguntas);
//        a.agregaEncuesta(encuesta);
//        System.out.println(encuestaBusiness.editarEncuesta(encuesta));
    }
    
    private String enviarPeticionLoginAdmin(Administrador admin) {

        Element elemAdmin = new Element("administrador");
        elemAdmin.setAttribute("nickname", admin.getNickname());

        Element elemNombre = new Element("nombre");
        elemNombre.addContent(admin.getNombre());

        Element elemContrasenna = new Element("contrasenna");
        elemContrasenna.addContent(admin.getContrasenna());

        Element elemCorreo = new Element("correo");
        elemCorreo.addContent(admin.getCorreoElectronico());
        
        Element elemPrimeraVez = new Element("primeraVez");
        elemPrimeraVez.addContent(String.valueOf(admin.isPrimeraVez()));
        
        Element elemEncuestas = new Element("encuestas");
        
        for (int i = 0; i < admin.getEncuestasCreadas().size(); i++) {
            Element elemEncuesta = new Element("encuesta"+i);
            elemEncuesta.addContent(admin.getEncuestasCreadas().get(i));
            elemEncuestas.addContent(elemEncuesta);
        }
        
        
        
        elemAdmin.addContent(elemNombre);
        elemAdmin.addContent(elemContrasenna);
        elemAdmin.addContent(elemCorreo);
        elemAdmin.addContent(elemPrimeraVez);
        elemAdmin.addContent(elemEncuestas);

        XMLOutputter output = new XMLOutputter(Format.getCompactFormat());
        String adminXML = output.outputString(elemAdmin);

        adminXML = adminXML.replace("\n", "");
        
        return adminXML;
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}

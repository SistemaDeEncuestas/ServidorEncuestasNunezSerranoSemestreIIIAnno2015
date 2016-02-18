package logic;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.UsuarioBusiness;
import domain.Administrador;
import domain.Correo;
import domain.Encuesta;
import domain.Encuestado;
import domain.Pregunta;
import domain.PreguntaAbierta;
import domain.PreguntaRespuestaMultiple;
import domain.PreguntaRespuestaUnica;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import util.Strings;

/**
 *
 * @author adriansb3105
 */
public class Servidor implements Runnable {

    private String peticion;
    private String nick;
    private String contrasenna;
    private Administrador administrador;
    private Encuestado encuestado;
    private Encuesta encuesta;
    private String nombreEncuesta;
    private List<String> listaEncuestados;
    private List<Correo> listaCorreos;
    private int puerto;
    private JLabel jlEstado;
    private JTextArea jtaConsola;
    private boolean flag;
    private AdministradorBusiness administradorBusiness;
    private EncuestaBusiness encuestaBusiness;
    private UsuarioBusiness usuarioBusiness;
    private boolean insertado;
    private boolean editado;

    public Servidor(int puerto) {
        super();
        this.puerto = puerto;
        this.flag = true;
        this.encuestaBusiness = new EncuestaBusiness();
        this.usuarioBusiness = new UsuarioBusiness();
        this.administradorBusiness = new AdministradorBusiness();
    }

    public void correHilo(JLabel jlEstado, JTextArea jtaConsola) {
        this.jlEstado = jlEstado;
        this.jtaConsola = jtaConsola;
    }

    @Override
    public void run() {

        try {
            ServerSocket serverSocket = new ServerSocket(this.puerto);

            this.jlEstado.setText("Estado: Iniciado");
            this.jtaConsola.append("Servidor iniciado\n");
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
                            this.jtaConsola.append("Intento de inicio de sesion de un administrador fallido\n");
                        } else {
                            this.administrador = this.administradorBusiness.getAdministrador(this.nick, this.contrasenna);
                            enviar.println(enviarPeticionLoginAdmin(this.administrador));
                            this.jtaConsola.append(this.nick + " ha iniciado sesión\n");
                        }
                        break;

                    case Strings.PETICION_LOGIN_USER:
                        this.nick = recibir.readLine();
                        this.contrasenna = recibir.readLine();
                        
                        if (this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna) == null) {
                            enviar.println("null");
                            this.jtaConsola.append("Intento de inicio de sesion de un encuestado fallido\n");
                        }else{
                            this.encuestado = this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna);
                            enviar.println(enviarPeticionLoginUser(this.encuestado));
                            Strings.LISTA_USUARIOS_CONECTADOS.add(this.nick);
                            this.jtaConsola.append(this.nick + " ha iniciado sesión\n");
                        }
                        break;
                        
                    case Strings.PETICION_REGISTRA_ADMIN:
                        this.administrador = recibirRegistraAdmin(recibir.readLine());
                        this.insertado = this.administradorBusiness.insertar(this.administrador);
                        if (this.insertado) {
                            this.jtaConsola.append("El administrador "+this.administrador.getNickname()+" se ha registrado con éxito\n");
                            enviar.println("insertado");
                        }else{
                            enviar.println("yaExiste");
                        }
                        
                        break;
                        
                    case Strings.PETICION_REGISTRAR_USER:
                        this.encuestado = recibirRegistraUser(recibir.readLine());
                        this.insertado = this.usuarioBusiness.insertar(this.encuestado);
                        if (this.insertado) {
                            this.jtaConsola.append("El encuestado "+this.encuestado.getNickname()+" se ha registrado con éxito\n");
                            enviar.println("insertado");
                        }else{
                            enviar.println("yaExiste");
                        }
                        break;
                        
//                    case Strings.PETICION_GET_ENCUESTADOS:
//                        enviar.writeObject(this.usuarioBusiness.getEncuestados());
//                        this.jtaConsola.append("Se ha solicitado la lista de los encuestados registrados\n");
//                        break;
                        
                    case Strings.PETICION_CREAR_ENCUESTA:
                        this.encuesta = recibirEncuesta(recibir.readLine());
                        
                        this.encuestaBusiness.iniciar(this.encuesta.getNombreArchivo());
                        
                        this.insertado = this.encuestaBusiness.insertar(this.encuesta);
                        if (this.insertado) {
                            enviar.println("insertada");
                            this.jtaConsola.append("La encuesta '"+this.encuesta.getTitulo()+"' se ha creado con éxito\n");
                        }else{
                            enviar.println("yaExiste");
                        }
                        break;
                        
//                    case Strings.PETICION_EDITA_ENCUESTA:
//                        this.nombreEncuesta = recibir.readUTF();
//                        this.encuestaBusiness = new EncuestaBusiness();
//                        this.encuesta = this.encuestaBusiness.getEncuesta(this.nombreEncuesta);
//                        enviar.writeObject(this.encuesta);
//                        this.jtaConsola.append("Se ha solicitado editar la encuesta "+this.nombreEncuesta+"\n");
//                        break;
//                        
//                    
//                    case Strings.PETICION_GUARDA_EDICION:
//                        this.encuesta = (Encuesta) recibir.readObject();
//                        this.encuestaBusiness =  new EncuestaBusiness(this.encuesta.getTitulo());
//                        this.editado = this.encuestaBusiness.editarEncuesta(this.encuesta);
//                        if (this.editado) {
//                            this.jtaConsola.append("La encuesta "+this.encuesta.getTitulo()+" se ha editado con éxito\n");
//                        }
//                        break;
//                        
//                        //admin
//                    case Strings.PETICION_ENVIAR_ENCUESTA:
//                        this.nombreEncuesta = recibir.readUTF();
//                        this.listaEncuestados = (List<String>) recibir.readObject();
//                        
//                        this.encuestaBusiness = new EncuestaBusiness(this.nombreEncuesta);
//                        this.encuesta = this.encuestaBusiness.getEncuesta(this.nombreEncuesta);
//                        List<String> enviados = new ArrayList<>();
//                        //enviar a los usuarios conectados
//                        
//                        for (int i = 0; i < this.listaEncuestados.size(); i++) {
//                            for (int j = 0; j < this.usuarioBusiness.getEncuestados().length; j++) {
//                                if (this.usuarioBusiness.getEncuestados()[j].getNickname().equals(this.listaEncuestados.get(i))) {
//                                    this.usuarioBusiness.getEncuestados()[j].agregaEncuesta(this.encuesta);
//                                    enviados.add(this.usuarioBusiness.getEncuestados()[j].getNickname());
//                                }
//                            }
//                        }
//                        this.jtaConsola.append("La encuesta "+this.nombreEncuesta+" ha sido enviada a los usuarios "+enviados+"\n");
//                        
//                        break;
//                        
//                        //encuestado
//                    case Strings.PETICION_DEVOLVER_ENCUESTA:
//                        this.encuesta = (Encuesta) recibir.readObject();
//                        
//                        //Hacer metodo de quitar encuesta al usuario
//                        //TODO
//                        
//                        break;
                        
                        
                        
                        
                        
                        
//                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ADMIN:
//                        this.administrador = (Administrador) recibir.readObject();
//                        this.editado = this.administradorBusiness.editaAdministrador(this.administrador);
//                        if (this.editado) {
//                            this.jtaConsola.append("La contraseña de "+this.administrador.getNickname()+" ha sido cambiada\n");
//                        }
//                        break;
                        
//                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ENCUESTADO:
//                        this.encuestado = (Encuestado) recibir.readObject();
//                        this.editado = this.usuarioBusiness.editaEncuestado(this.encuestado);
//                        if (this.editado) {
//                            this.jtaConsola.append("La contraseña de "+this.encuestado.getNickname()+" ha sido cambiada\n");
//                        }
//                        break;
                }
                socket.close();
            } while (this.flag);
        } catch (BindException e) {
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
    private String enviarPeticionLoginUser(Encuestado encuestado) {

        Element elemEncuestado = new Element("encuestado");
        elemEncuestado.setAttribute("nickname", encuestado.getNickname());

        Element elemNombre = new Element("nombre");
        elemNombre.addContent(encuestado.getNombre());

        Element elemContrasenna = new Element("contrasenna");
        elemContrasenna.addContent(encuestado.getContrasenna());

        Element elemCorreo = new Element("correo");
        elemCorreo.addContent(encuestado.getCorreoElectronico());
        
        Element elemEncuestas = new Element("encuestas");
        
        for (int i = 0; i < encuestado.getListaEncuestas().size(); i++) {
            Element elemEncuesta = new Element("encuesta"+i);
            elemEncuesta.addContent(encuestado.getListaEncuestas().get(i));
            elemEncuestas.addContent(elemEncuesta);
        }
        
        elemEncuestado.addContent(elemNombre);
        elemEncuestado.addContent(elemContrasenna);
        elemEncuestado.addContent(elemCorreo);
        elemEncuestado.addContent(elemEncuestas);

        XMLOutputter output = new XMLOutputter(Format.getCompactFormat());
        String userXML = output.outputString(elemEncuestado);

        userXML = userXML.replace("\n", "");
        
        return userXML;
    }
    
    private Administrador recibirRegistraAdmin(String adminXML){
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(adminXML);
            Document doc = saxBuilder.build(stringReader);
            Element rootAdmin = doc.getRootElement();
            
            Administrador admin = new Administrador(rootAdmin.getChildText("nombre"),
                                                    rootAdmin.getAttributeValue("nickname"),
                                                    rootAdmin.getChildText("contrasenna"),
                                                    rootAdmin.getChildText("correo"));
            
            admin.setPrimeraVez(Boolean.getBoolean(rootAdmin.getChildText("primeraVez")));

            List<String> listaEncuestas = new ArrayList<>();
            
            for (int i = 0; i < rootAdmin.getChild("encuestas").getChildren().size(); i++) {
                listaEncuestas.add(rootAdmin.getChild("encuestas").getChild("encuesta"+i).getValue());
            }
            
            admin.setEncuestasCreadas(listaEncuestas);
            
            return admin;
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Encuestado recibirRegistraUser(String userXML) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(userXML);
            Document doc = saxBuilder.build(stringReader);
            Element rootAdmin = doc.getRootElement();
            
            Encuestado user = new Encuestado(   rootAdmin.getChildText("nombre"),
                                                rootAdmin.getAttributeValue("nickname"),
                                                rootAdmin.getChildText("contrasenna"),
                                                rootAdmin.getChildText("correo"));
            
            List<String> listaEncuestas = new ArrayList<>();
            
            for (int i = 0; i < rootAdmin.getChild("encuestas").getChildren().size(); i++) {
                listaEncuestas.add(rootAdmin.getChild("encuestas").getChild("encuesta"+i).getValue());
            }
            
            user.setListaEncuestas(listaEncuestas);
            
            return user;
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    private Encuesta recibirEncuesta(String encuestaXML) {
        
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(encuestaXML);
            Document doc = saxBuilder.build(stringReader);
            Element rootAdmin = doc.getRootElement();
            
            List<Pregunta> listaPreguntas = new ArrayList<>();
            Element elemPreguntas = rootAdmin.getChild("preguntas");
            List lista = elemPreguntas.getChildren();
            
            for (Object objetoActualPreguntas : lista) {
                List<String> listaRespuestas = new ArrayList<>();
                Element elemActualPregunta = (Element) objetoActualPreguntas;
                
                Pregunta pregunta;
                switch (elemActualPregunta.getAttributeValue("tipo")) {
                    case Strings.TIPO_MULTIPLE:
                        pregunta = new PreguntaRespuestaMultiple(elemActualPregunta.getAttributeValue("enunciado"));
                        break;
                    case Strings.TIPO_UNICA:
                        pregunta = new PreguntaRespuestaUnica(elemActualPregunta.getAttributeValue("enunciado"));
                        break;
                    default://TIPO_ABIERTA
                        pregunta = new PreguntaAbierta(elemActualPregunta.getAttributeValue("enunciado"));
                        break;
                }

                List listaElementosRespuestas = elemActualPregunta.getChildren();

                for (Object objetoActualRespuesta : listaElementosRespuestas) {
                    Element elemRespuesta = (Element) objetoActualRespuesta;
                    listaRespuestas.add(elemRespuesta.getValue());
                }

                pregunta.setListaRespuestas(listaRespuestas);

                listaPreguntas.add(pregunta);
            
            }
            
            Encuesta encuesta = new Encuesta(   rootAdmin.getChildText("creador"),
                                                rootAdmin.getChildText("titulo"),
                                                rootAdmin.getChildText("descripcion"),
                                                rootAdmin.getChildText("nombreArchivo"),
                                                listaPreguntas);
        
        return encuesta;
        
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
}

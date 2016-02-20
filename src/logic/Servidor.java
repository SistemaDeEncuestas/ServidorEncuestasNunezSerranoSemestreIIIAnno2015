package logic;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.EncuestaRespondidaBusiness;
import business.UsuarioBusiness;
import domain.Administrador;
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
 * Servidor del sistema
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
    private String nombre;
    private int puerto;
    private JLabel jlEstado;
    private JTextArea jtaConsola;
    private boolean flag;
    private AdministradorBusiness administradorBusiness;
    private EncuestaBusiness encuestaBusiness;
    private UsuarioBusiness usuarioBusiness;
    private EncuestaRespondidaBusiness encuestaRespondidaBusiness;
    private boolean insertado;
    private boolean editado;
    private List<String> lista;
    private String parte;

    /**
     *
     * @param puerto
     */
    public Servidor(int puerto) {
        super();
        this.puerto = puerto;
        this.flag = true;
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

                this.encuestaRespondidaBusiness = new EncuestaRespondidaBusiness();
                this.administradorBusiness = new AdministradorBusiness();
                this.usuarioBusiness = new UsuarioBusiness();
                this.encuestaBusiness = new EncuestaBusiness();
                /*De acuerdo a la peticion que el servidor
                 recibe del cliente, el server se comporta de manera diferente
                 respondiendo a las peticiones del cliente*/
                switch (this.peticion) {
                    case Strings.PETICION_LOGIN_ADMIN:
                        this.nick = recibir.readLine();
                        this.contrasenna = recibir.readLine();

                        if (this.administradorBusiness.getAdministrador(this.nick, this.contrasenna) == null) {
                            enviar.println("null");
                            this.jtaConsola.append("Intento de inicio de sesion de un administrador fallido\n");
                        } else {
                            this.administrador = this.administradorBusiness.getAdministrador(this.nick, this.contrasenna);
                            enviar.println(enviarAdministrador(this.administrador));
                            this.jtaConsola.append("El administrador " + this.nick + " ha iniciado sesión\n");
                        }
                        break;

                    case Strings.PETICION_LOGIN_USER:
                        this.nick = recibir.readLine();
                        this.contrasenna = recibir.readLine();
                        if (this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna) == null) {
                            enviar.println("null");
                            this.jtaConsola.append("Intento de inicio de sesion de un encuestado fallido\n");
                        } else {
                            this.encuestado = this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna);
                            enviar.println(enviarEncuestado(this.encuestado));
                            Strings.LISTA_USUARIOS_CONECTADOS.add(this.nick);
                            this.jtaConsola.append("El usuario " + this.nick + " ha iniciado sesión\n");
                        }
                        break;

                    case Strings.PETICION_REGISTRA_ADMIN:
                        this.administrador = recibirAdministrador(recibir.readLine());
                        this.insertado = this.administradorBusiness.insertar(this.administrador);
                        if (this.insertado) {
                            this.jtaConsola.append("El administrador " + this.administrador.getNickname() + " se ha registrado con éxito\n");
                            enviar.println("insertado");
                        } else {
                            enviar.println("yaExiste");
                            this.jtaConsola.append("Intento de registro de un administrador fallido\n");
                        }
                        break;

                    case Strings.PETICION_REGISTRAR_USER:
                        this.encuestado = recibirEncuestado(recibir.readLine());
                        this.insertado = this.usuarioBusiness.insertar(this.encuestado);
                        if (this.insertado) {
                            this.jtaConsola.append("El encuestado " + this.encuestado.getNickname() + " se ha registrado con éxito\n");
                            enviar.println("insertado");
                        } else {
                            enviar.println("yaExiste");
                            this.jtaConsola.append("Intento de registro de un encuestado fallido\n");
                        }
                        break;

                    case Strings.PETICION_CREAR_ENCUESTA:
                        this.encuesta = recibirEncuesta(recibir.readLine());
                        this.encuestaBusiness.iniciar(this.encuesta.getNombreArchivo());

                        this.insertado = this.encuestaBusiness.insertar(this.encuesta);
                        if (this.insertado) {
                            enviar.println("insertada");
                            this.jtaConsola.append("La encuesta '" + this.encuesta.getTitulo() + "' se ha creado con éxito\n");
                        } else {
                            enviar.println("yaExiste");
                            this.jtaConsola.append("La encuesta '" + this.encuesta.getTitulo() + "' no se pudo ingresar al sistema\n");
                        }
                        break;

                    case Strings.PETICION_PREGUNTAS_POR_ENCUESTA:
                        this.nombre = recibir.readLine();//nombre encuesta
                        this.encuestaBusiness.iniciar(this.nombre);
                        this.lista = this.encuestaBusiness.getPreguntasPorEncuesta(this.nombre);
                        enviar.println(enviarLista(this.lista));
                        break;

                    case Strings.PETICION_GET_ENCUESTA:
                        this.nombre = recibir.readLine();
                        this.encuestaBusiness.iniciar(this.nombre);
                        this.encuesta = this.encuestaBusiness.getEncuesta();
                        enviar.println(enviarEncuesta(this.encuesta));
                        this.jtaConsola.append("El administrador " + this.encuesta.getNickname() + " ha abierto la encuesta '" + this.nombre + "'\n");
                        break;

                    case Strings.PETICION_ELIMINA_ENCUESTA:
                        this.nombre = recibir.readLine();
                        this.encuestaBusiness.iniciar(this.nombre);
                        boolean eliminado = this.encuestaBusiness.borrarEncuesta();
                        if (eliminado) {
                            enviar.println("eliminado");
                            this.jtaConsola.append("La encuesta '" + this.nombre + "' ha sido eliminada con éxito\n");
                        } else {
                            enviar.println("noSePudoEliminar");
                            this.jtaConsola.append("La solicitud de eliminar una encuesta ha fallado\n");
                        }
                        break;

                    case Strings.PETICION_GUARDA_EDICION:
                        this.encuesta = recibirEncuesta(recibir.readLine());
                        this.encuestaBusiness.iniciar(this.encuesta.getNombreArchivo());

                        this.editado = this.encuestaBusiness.editarEncuesta(this.encuesta);
                        if (this.editado) {
                            enviar.println("listo");
                            this.jtaConsola.append("La encuesta " + this.encuesta.getTitulo() + " se ha editado con éxito\n");
                        } else {
                            enviar.println("noSePudoEditar");
                            this.jtaConsola.append("Ha fallado la edición de la encuesta '" + this.encuesta.getTitulo() + "'\n");
                        }
                        break;

                    /*admin*/
                    case Strings.PETICION_ENVIAR_ENCUESTA:
                        this.nombre = recibir.readLine();
                        this.lista = recibirLista(recibir.readLine());

                        for (int i = 0; i < this.lista.size(); i++) {
                            this.encuestado = this.usuarioBusiness.getEncuestado(this.lista.get(i));
                            this.encuestado.agregaEncuesta(this.nombre);
                            this.usuarioBusiness.editaEncuestado(this.encuestado);
                        }
                        this.jtaConsola.append("La encuesta '" + this.nombre + "' ha sido enviada a los usuarios: " + this.lista + "\n");
                        enviar.println("listo");
                        break;

                    case Strings.PETICION_SOLICITA_ENCUESTA:
                        this.nombre = recibir.readLine();
                        this.encuestaBusiness.iniciar(this.nombre);
                        if (this.encuestaBusiness.getEncuesta() != null) {
                            this.encuesta = this.encuestaBusiness.getEncuesta();
                            enviar.println(enviarEncuesta(this.encuesta));
                            this.jtaConsola.append("La encuesta '" + this.nombre + "' ha sido solicitada para ser llenada\n");
                        } else {
                            enviar.println("null");
                            this.jtaConsola.append("La encuesta '" + this.nombre + "' no existe\n");
                        }
                        break;

                    case Strings.PETICION_DEVOLVER_ENCUESTA:
                        this.encuesta = recibirEncuesta(recibir.readLine());
//                        this.usuarioBusiness.eliminarEncuestaEnUsuario(this.encuesta.getNombreArchivo());
                        this.insertado = this.encuestaRespondidaBusiness.insertar(this.encuesta);
                        if (this.insertado) {
                            enviar.println("listo");
                            this.jtaConsola.append("La respuesta de la encuesta '" + this.nombre + "' ha sido guardada con éxito\n");
                        } else {
                            enviar.println("noSePudoInsertar");
                            this.jtaConsola.append("No se ha podido guardar la encuesta '" + this.nombre + "'\n");
                        }
                        break;

                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ADMIN:
                        this.administrador = recibirAdministrador(recibir.readLine());
                        this.editado = this.administradorBusiness.editaAdministrador(this.administrador);
                        if (this.editado) {
                            enviar.println("listo");
                            this.jtaConsola.append("La contraseña de " + this.administrador.getNickname() + " ha sido cambiada\n");
                        } else {
                            enviar.println("noSePudoEditar");
                            this.jtaConsola.append("No se pudo cambiar la contraseña de " + this.administrador.getNickname() + "\n");
                        }
                        break;

                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ENCUESTADO:
                        this.encuestado = recibirEncuestado(recibir.readLine());
                        this.editado = this.usuarioBusiness.editaEncuestado(this.encuestado);
                        if (this.editado) {
                            enviar.println("listo");
                            this.jtaConsola.append("La contraseña de " + this.encuestado.getNickname() + " ha sido cambiada\n");
                        } else {
                            enviar.println("noSePudoEditar");
                            this.jtaConsola.append("No se pudo cambiar la contraseña de " + this.encuestado.getNickname() + "\n");
                        }
                        break;

                    case Strings.PETICION_ENVIAR_CORREO:
                        this.nombre = recibir.readLine();
                        this.lista = recibirLista(recibir.readLine());
                        EnviaCorreos enviaCorreos = new EnviaCorreos(this.nombre, this.lista);
                        enviaCorreos.start();
                        enviar.println("listo");
                        this.jtaConsola.append("La encuesta '" + this.nombre + "' ha sido correctamente enviada por correo electrónico\n");
                        break;

                    case Strings.PETICION_CERRAR_SESION:
                        this.nombre = recibir.readLine();
                        Strings.LISTA_USUARIOS_CONECTADOS.remove(this.nombre);
                        this.jtaConsola.append("El usuario " + this.nombre + " ha cerrado la sesion\n");
                        break;

                    case Strings.PETICION_LISTAS_USUARIOS:
                        enviar.println(enviarLista(this.usuarioBusiness.getNombresEncuestados()));
                        break;

                    case Strings.PETICION_PREGUNTAS_ESTADISTICA:
                        this.nombre = recibir.readLine();
                        this.parte = recibir.readLine();
                        this.lista = this.encuestaRespondidaBusiness.getPreguntas(this.nombre, this.parte);
                        enviar.println(enviarLista(this.lista));
                        break;
                }
                socket.close();
            } while (this.flag);
        } catch (BindException e) {
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Metodo que me transforma un objeto tipo administrador en un String con
     * formato xml para enviar al server
     *
     * @param admin el objeto a convertir
     * @return el string a enviar
     */
    private String enviarAdministrador(Administrador admin) {
        Element elemAdmin = new Element("administrador");
        elemAdmin.setAttribute("nickname", admin.getNickname());

        Element elemNombre = new Element("nombre");
        elemNombre.addContent(admin.getNombre());

        Element elemContrasenna = new Element("contrasenna");
        elemContrasenna.addContent(admin.getContrasenna());

        Element elemCorreo = new Element("correo");
        elemCorreo.addContent(admin.getCorreoElectronico());

        Element elemPrimeraVez = new Element("primeraVez");
        elemPrimeraVez.addContent(admin.isPrimeraVez());

        Element elemEncuestas = new Element("encuestas");

        for (int i = 0; i < admin.getEncuestasCreadas().size(); i++) {
            Element elemEncuesta = new Element("encuesta" + i);
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

    /**
     * Metodo que me transforma un objeto tipo encuestado en un String con
     * formato xml para enviar al server
     *
     * @param encuestado el objeto a convertir
     * @return el string a enviar
     */
    private String enviarEncuestado(Encuestado encuestado) {

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
            Element elemEncuesta = new Element("encuesta" + i);
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

    /**
     * Metodo que me transforma una lista en un String con formato xml para
     * enviar al server
     *
     * @param lista la lista convertir
     * @return el String a enviar
     */
    private String enviarLista(List<String> lista) {

        Element elemNombres = new Element("nombres");

        for (int i = 0; i < lista.size(); i++) {
            Element elemNombre = new Element("nombre");
            elemNombre.addContent(lista.get(i));
            elemNombres.addContent(elemNombre);
        }

        XMLOutputter output = new XMLOutputter(Format.getCompactFormat());
        String nombresXML = output.outputString(elemNombres);

        nombresXML = nombresXML.replace("\n", "");

        return nombresXML;
    }

    /**
     * Metodo que me transforma un objeto tipo encuesta en un String con formato
     * xml para enviar al server
     *
     * @param encuesta el objeto a convertir
     * @return el string a a enviar
     */
    private String enviarEncuesta(Encuesta encuesta) {
        Element elemEncuesta = new Element("encuesta");
        Element elemCreador = new Element("creador");
        elemCreador.addContent(encuesta.getNickname());

        Element elemTitulo = new Element("titulo");
        elemTitulo.addContent(encuesta.getTitulo());

        Element elemDescripcion = new Element("descripcion");
        elemDescripcion.addContent(encuesta.getDescripcion());

        Element elemNombreArchivo = new Element("nombreArchivo");
        elemNombreArchivo.addContent(encuesta.getNombreArchivo());

        Element elemPreguntas = new Element("preguntas");

        List<Pregunta> listaPreguntas = encuesta.getPreguntas();

        for (int i = 0; i < listaPreguntas.size(); i++) {

            Element elemPregunta = new Element("pregunta");
            elemPregunta.setAttribute("tipo", listaPreguntas.get(i).getTipo());
            elemPregunta.setAttribute("enunciado", listaPreguntas.get(i).getEnunciado());

            List<String> listaRespuestas = encuesta.getPreguntas().get(i).getListaRespuestas();

            for (String respuesta : listaRespuestas) {
                Element elemRespuesta = new Element("respuesta");
                elemRespuesta.addContent(respuesta);
                elemPregunta.addContent(elemRespuesta);
            }

            elemPreguntas.addContent(elemPregunta);
        }

        elemEncuesta.addContent(elemCreador);
        elemEncuesta.addContent(elemTitulo);
        elemEncuesta.addContent(elemDescripcion);
        elemEncuesta.addContent(elemNombreArchivo);
        elemEncuesta.addContent(elemPreguntas);

        XMLOutputter output = new XMLOutputter(Format.getCompactFormat());
        String encuestaXML = output.outputString(elemEncuesta);

        encuestaXML = encuestaXML.replace("\n", "");

        return encuestaXML;
    }

    /**
     * Método que a partir de un String con formato XML me obtiene una lista
     *
     * @param listaString
     * @return la lista que se genera del string
     */
    private List<String> recibirLista(String listaString) {

        List<String> nombres = new ArrayList<>();

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(listaString);
            Document doc;
            doc = saxBuilder.build(stringReader);
            Element root = doc.getRootElement();

            List listaRoot = root.getChildren();

            for (Object objActual : listaRoot) {
                Element elemActual = (Element) objActual;
                nombres.add(elemActual.getValue());
            }

            return nombres;
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método que a partir de un String con formato XML me obtiene un objeto
     * tipo encuestado.
     *
     * @param userXML el String con formato XML.
     * @return el objeto encuestado que se genera.
     */
    private Encuestado recibirEncuestado(String userXML) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(userXML);
            Document doc = saxBuilder.build(stringReader);
            Element rootUser = doc.getRootElement();

            Encuestado user = new Encuestado(rootUser.getChildText("nombre"),
                    rootUser.getAttributeValue("nickname"),
                    rootUser.getChildText("contrasenna"),
                    rootUser.getChildText("correo"));

            List<String> listaEncuestas = new ArrayList<>();

            for (int i = 0; i < rootUser.getChild("encuestas").getChildren().size(); i++) {
                listaEncuestas.add(rootUser.getChild("encuestas").getChild("encuesta" + i).getValue());
            }

            user.setListaEncuestas(listaEncuestas);

            return user;
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método que a partir de un String con formato XML me obtiene un objeto
     * tipo encuesta.
     *
     * @param encuestaXML el String con formato XML.
     * @return El objeto encuesta que se forma.
     */
    private Encuesta recibirEncuesta(String encuestaXML) {

        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(encuestaXML);
            Document doc = saxBuilder.build(stringReader);
            Element rootEncuesta = doc.getRootElement();

            List<Pregunta> listaPreguntas = new ArrayList<>();
            Element elemPreguntas = rootEncuesta.getChild("preguntas");
            this.lista = elemPreguntas.getChildren();

            for (Object objetoActualPreguntas : this.lista) {
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

            this.encuesta = new Encuesta(rootEncuesta.getChildText("creador"),
                    rootEncuesta.getChildText("titulo"),
                    rootEncuesta.getChildText("descripcion"),
                    rootEncuesta.getChildText("nombreArchivo"),
                    listaPreguntas);

            return encuesta;

        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Método que a partir de un String con formato XML me obtiene un objeto
     * tipo administrador.
     *
     * @param adminXML el String con formato XML.
     * @return el objeto admnistrador que se genera.
     */
    private Administrador recibirAdministrador(String adminXML) {
        try {
            SAXBuilder saxBuilder = new SAXBuilder();
            StringReader stringReader = new StringReader(adminXML);
            Document doc = saxBuilder.build(stringReader);
            Element rootAdmin = doc.getRootElement();

            Administrador admin = new Administrador(rootAdmin.getChildText("nombre"),
                    rootAdmin.getAttributeValue("nickname"),
                    rootAdmin.getChildText("contrasenna"),
                    rootAdmin.getChildText("correo"));

            admin.setPrimeraVez(rootAdmin.getChildText("primeraVez"));

            List<String> listaEncuestas = new ArrayList<>();

            for (int i = 0; i < rootAdmin.getChild("encuestas").getChildren().size(); i++) {
                listaEncuestas.add(rootAdmin.getChild("encuestas").getChild("encuesta" + i).getValue());
            }

            admin.setEncuestasCreadas(listaEncuestas);

            return admin;
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

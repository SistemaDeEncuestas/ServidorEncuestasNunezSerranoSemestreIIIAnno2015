package logic;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.UsuarioBusiness;
import domain.Administrador;
import domain.Correo;
import domain.Encuesta;
import domain.Encuestado;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import org.jdom.Element;
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
                            this.jtaConsola.append("Intento de inicio de sesion fallido\n");
                        } else {
                            this.administrador = this.administradorBusiness.getAdministrador(this.nick, this.contrasenna);
                            enviar.println(enviarPeticionLoginAdmin(this.administrador));
                            this.jtaConsola.append(this.nick + " ha iniciado sesión");
                        }
                        break;

//                    case Strings.PETICION_LOGIN_USER:
//                        this.nick = recibir.readUTF();
//                        this.contrasenna = recibir.readUTF();
//                        this.encuestado = this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna);
//                        enviar.writeObject(this.encuestado);
//                        if (this.encuestado != null) {
//                            this.usuarioBusiness.setUsuariosConectados(this.nick);
//                            this.jtaConsola.append(this.nick+" ha iniciado sesión\n");
//                        }
//                        break;
//                        
//                    case Strings.PETICION_REGISTRA_ADMIN:
//                        this.administrador = (Administrador) recibir.readObject();
//                        this.insertado = this.administradorBusiness.insertar(this.administrador);
//                        if (this.insertado) {
//                            this.jtaConsola.append(this.administrador.getNickname()+" se ha registrado con éxito\n");
//                        }
//                        break;
//                        
//                    case Strings.PETICION_REGISTRAR_USER:
//                        this.encuestado = (Encuestado) recibir.readObject();
//                        this.insertado = this.usuarioBusiness.insertar(this.encuestado);
//                        if (this.insertado) {
//                            this.jtaConsola.append(this.encuestado.getNickname()+" se ha registrado con éxito\n");
//                        }
//                        break;
//                        
//                    case Strings.PETICION_GET_ENCUESTADOS:
//                        enviar.writeObject(this.usuarioBusiness.getEncuestados());
//                        this.jtaConsola.append("Se ha solicitado la lista de los encuestados registrados\n");
//                        break;
//                        
//                    case Strings.PETICION_CREAR_ENCUESTA:
//                        this.encuesta = (Encuesta) recibir.readObject();
//                        this.encuestaBusiness = new EncuestaBusiness(this.encuesta.getTitulo());
//                        this.insertado = this.encuestaBusiness.insertar(this.encuesta);
//                        if (this.insertado) {
//                            this.jtaConsola.append("La encuesta "+this.encuesta.getTitulo()+" se ha creado con éxito\n");
//                        }
//                        break;
//                        
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
//                        
//                        
//                        
//                        
//                        
//                        
//                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ADMIN:
//                        this.administrador = (Administrador) recibir.readObject();
//                        this.editado = this.administradorBusiness.editaAdministrador(this.administrador);
//                        if (this.editado) {
//                            this.jtaConsola.append("La contraseña de "+this.administrador.getNickname()+" ha sido cambiada\n");
//                        }
//                        break;
//                        
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
//            socket.close();
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
    
}

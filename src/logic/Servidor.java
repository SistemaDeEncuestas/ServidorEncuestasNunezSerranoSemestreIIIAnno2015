package logic;

import business.AdministradorBusiness;
import business.EncuestaBusiness;
import business.UsuarioBusiness;
import domain.Administrador;
import domain.Correo;
import domain.Encuesta;
import domain.Encuestado;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextArea;
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
    private List<Encuestado> listaEncuestados;
    private List<Correo> listaCorreos;
    private int puerto;
    private JLabel jlEstado;
    private JTextArea jtaConsola;
    private boolean flag;
    private AdministradorBusiness administradorBusiness;
    private EncuestaBusiness encuestaBusiness;
    private UsuarioBusiness usuarioBusiness;
    private boolean insertado;

    public Servidor(int puerto) {
        super();
        this.puerto = puerto;
        this.flag = true;
        this.usuarioBusiness = new UsuarioBusiness("users");
        this.administradorBusiness = new AdministradorBusiness("admin");
    }

    public void correHilo(JLabel jlEstado, JTextArea jtaConsola) {
        this.jlEstado = jlEstado;
        this.jtaConsola = jtaConsola;
    }

    @Override
    public void run() {

        try {
            Socket socket;
            ServerSocket serverSocket = new ServerSocket(this.puerto);
            ObjectInputStream recibir;

            this.jlEstado.setText("Estado: Iniciado");
            this.jtaConsola.append("Servidor iniciado\n");
            do {
                socket = serverSocket.accept();
                recibir = new ObjectInputStream(new DataInputStream(socket.getInputStream()));

                this.peticion = recibir.readUTF();

                switch (peticion) {
                    case Strings.PETICION_LOGIN_ADMIN:
                        this.nick = recibir.readUTF();
                        this.contrasenna = recibir.readUTF();
                        this.administradorBusiness.getAdministrador(this.nick, this.contrasenna);

                        break;

                    case Strings.PETICION_LOGIN_USER:
                        this.nick = recibir.readUTF();
                        this.contrasenna = recibir.readUTF();
                        this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna);
                        
                        break;
                        
                    case Strings.PETICION_REGISTRA_ADMIN:
                        this.administrador = (Administrador) recibir.readObject();
                        this.insertado = this.administradorBusiness.insertar(this.administrador);
                        System.out.println("Admin insertado: "+this.insertado);
                        
                        break;
                        
                    case Strings.PETICION_REGISTRAR_USER:
                        this.encuestado = (Encuestado) recibir.readObject();
                        this.insertado = this.usuarioBusiness.insertar(this.encuestado);
                        System.out.println("Encuestado insertado: "+this.insertado);
                        
                        break;
                        
                    case Strings.PETICION_GET_ENCUESTADOS:
                        //TODO
                        
                        break;
                        
                        
                    case Strings.PETICION_CREAR_ENCUESTA:
                        this.encuesta = (Encuesta) recibir.readObject();
                        this.encuestaBusiness = new EncuestaBusiness(this.encuesta.getTitulo());
                        this.insertado = this.encuestaBusiness.insertar(this.encuesta);
                        System.out.println("Encuesta insertada: "+this.insertado);
                        
                        break;
                        
                    case Strings.PETICION_EDITA_ENCUESTA:
                        this.nombreEncuesta = recibir.readUTF();
                        this.encuestaBusiness = new EncuestaBusiness();
                        this.encuesta = this.encuestaBusiness.getEncuesta(this.nombreEncuesta);
                        boolean editado = this.encuestaBusiness.editarEncuesta(this.encuesta);
                        System.out.println("Encuesta editada: "+editado);
                        
                        break;
                        
                    
                    case Strings.PETICION_GUARDA_EDICION:
                        this.encuesta = (Encuesta) recibir.readObject();
                        this.encuestaBusiness =  new EncuestaBusiness(this.encuesta.getTitulo());
                        this.encuestaBusiness.editarEncuesta(this.encuesta);
                        
                        break;
                        
                        //admin
                    case Strings.PETICION_ENVIAR_ENCUESTA:
                        this.nombreEncuesta = recibir.readUTF();
                        this.listaEncuestados = (List<Encuestado>) recibir.readObject();
                        
                        //TODO
                        
                        break;
                        
                        //encuestado
                    case Strings.PETICION_DEVOLVER_ENCUESTA:
                        this.encuesta = (Encuesta) recibir.readObject();
                        
                        //TODO
                        
                        break;
                        
                        
                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ADMIN:
                        this.administrador = (Administrador) recibir.readObject();
                        
                        //editar admin
                        
                        break;
                        
                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ENCUESTADO:
                        this.encuestado = (Encuestado) recibir.readObject();
                        
                        //editar usuario
                        
                        break;
                }
                
                recibir.close();
            } while (this.flag);

            socket.close();

        } catch (BindException e) {
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

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
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
            ObjectOutputStream enviar;

            this.jlEstado.setText("Estado: Iniciado");
            this.jtaConsola.append("Servidor iniciado\n");
            do {
                socket = serverSocket.accept();
                recibir = new ObjectInputStream(new DataInputStream(socket.getInputStream()));
                enviar = new ObjectOutputStream(socket.getOutputStream());
                
                this.peticion = recibir.readUTF();

                switch (peticion) {
                    case Strings.PETICION_LOGIN_ADMIN:
                        this.nick = recibir.readUTF();
                        this.contrasenna = recibir.readUTF();
                        this.administrador = this.administradorBusiness.getAdministrador(this.nick, this.contrasenna);
                        enviar.writeObject(this.administrador);
                        if (this.administrador != null) {
                            this.jtaConsola.append(this.nick+" ha iniciado sesión\n");
                        }
                        break;

                    case Strings.PETICION_LOGIN_USER:
                        this.nick = recibir.readUTF();
                        this.contrasenna = recibir.readUTF();
                        this.encuestado = this.usuarioBusiness.getEncuestado(this.nick, this.contrasenna);
                        enviar.writeObject(this.encuestado);
                        if (this.encuestado != null) {
                            this.usuarioBusiness.setUsuariosConectados(this.nick);
                            this.jtaConsola.append(this.nick+" ha iniciado sesión\n");
                        }
                        break;
                        
                    case Strings.PETICION_REGISTRA_ADMIN:
                        this.administrador = (Administrador) recibir.readObject();
                        this.insertado = this.administradorBusiness.insertar(this.administrador);
                        if (this.insertado) {
                            this.jtaConsola.append(this.administrador.getNombreUsuario()+" se ha registrado con éxito\n");
                        }
                        break;
                        
                    case Strings.PETICION_REGISTRAR_USER:
                        this.encuestado = (Encuestado) recibir.readObject();
                        this.insertado = this.usuarioBusiness.insertar(this.encuestado);
                        if (this.insertado) {
                            this.jtaConsola.append(this.encuestado.getNombreUsuario()+" se ha registrado con éxito\n");
                        }
                        break;
                        
                    case Strings.PETICION_GET_ENCUESTADOS:
                        enviar.writeObject(this.usuarioBusiness.getEncuestados());
                        this.jtaConsola.append("Se ha solicitado la lista de los encuestados registrados\n");
                        break;
                        
                    case Strings.PETICION_CREAR_ENCUESTA:
                        this.encuesta = (Encuesta) recibir.readObject();
                        this.encuestaBusiness = new EncuestaBusiness(this.encuesta.getTitulo());
                        this.insertado = this.encuestaBusiness.insertar(this.encuesta);
                        if (this.insertado) {
                            this.jtaConsola.append("La encuesta "+this.encuesta.getTitulo()+" se ha creado con éxito\n");
                        }
                        break;
                        
                    case Strings.PETICION_EDITA_ENCUESTA:
                        this.nombreEncuesta = recibir.readUTF();
                        this.encuestaBusiness = new EncuestaBusiness();
                        this.encuesta = this.encuestaBusiness.getEncuesta(this.nombreEncuesta);
                        enviar.writeObject(this.encuesta);
                        this.jtaConsola.append("Se ha solicitado editar la encuesta "+this.nombreEncuesta+"\n");
                        break;
                        
                    
                    case Strings.PETICION_GUARDA_EDICION:
                        this.encuesta = (Encuesta) recibir.readObject();
                        this.encuestaBusiness =  new EncuestaBusiness(this.encuesta.getTitulo());
                        this.editado = this.encuestaBusiness.editarEncuesta(this.encuesta);
                        if (this.editado) {
                            this.jtaConsola.append("La encuesta "+this.encuesta.getTitulo()+" se ha editado con éxito\n");
                        }
                        break;
                        
                        //admin
                    case Strings.PETICION_ENVIAR_ENCUESTA:
                        this.nombreEncuesta = recibir.readUTF();
                        this.listaEncuestados = (List<String>) recibir.readObject();
                        
                        this.encuestaBusiness = new EncuestaBusiness(this.nombreEncuesta);
                        this.encuesta = this.encuestaBusiness.getEncuesta(this.nombreEncuesta);
                        List<String> enviados = new ArrayList<>();
                        //enviar a los usuarios conectados
                        
                        for (int i = 0; i < this.listaEncuestados.size(); i++) {
                            for (int j = 0; j < this.usuarioBusiness.getEncuestados().length; j++) {
                                if (this.usuarioBusiness.getEncuestados()[j].getNombreUsuario().equals(this.listaEncuestados.get(i))) {
                                    this.usuarioBusiness.getEncuestados()[j].agregaEncuesta(this.encuesta);
                                    enviados.add(this.usuarioBusiness.getEncuestados()[j].getNombreUsuario());
                                }
                            }
                        }
                        this.jtaConsola.append("La encuesta "+this.nombreEncuesta+" ha sido enviada a los usuarios "+enviados+"\n");
                        
                        break;
                        
                        //encuestado
                    case Strings.PETICION_DEVOLVER_ENCUESTA:
                        this.encuesta = (Encuesta) recibir.readObject();
                        
                        //Hacer metodo de quitar encuesta al usuario
                        //TODO
                        
                        break;
                        
                        
                        
                        
                        
                        
                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ADMIN:
                        this.administrador = (Administrador) recibir.readObject();
                        this.editado = this.administradorBusiness.editaAdministrador(this.administrador);
                        if (this.editado) {
                            this.jtaConsola.append("La contraseña de "+this.administrador.getNombreUsuario()+" ha sido cambiada\n");
                        }
                        break;
                        
                    case Strings.PETICION_CAMBIAR_CONTRASENNA_ENCUESTADO:
                        this.encuestado = (Encuestado) recibir.readObject();
                        this.editado = this.usuarioBusiness.editaEncuestado(this.encuestado);
                        if (this.editado) {
                            this.jtaConsola.append("La contraseña de "+this.encuestado.getNombreUsuario()+" ha sido cambiada\n");
                        }
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

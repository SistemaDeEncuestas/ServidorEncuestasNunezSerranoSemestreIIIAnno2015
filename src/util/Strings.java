package util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author adriansb3105
 */
public final class Strings {

    public final static int PUERTO = 5700;
    public static List<String> LISTA_USUARIOS_CONECTADOS = new ArrayList<>();
    
    public final static String TIPO_MULTIPLE = "multiple";
    public final static String TIPO_UNICA = "unica";
    public final static String TIPO_ABIERTA = "abierta";
    public final static String RUTA_ARCHIVOS = "src/files/nombresDeArchivos.xml";
    
    /*Constantes para las peticiones del cliente*/
    public final static String PETICION_LISTAS_USUARIOS = "listasUsuarios";
    public final static String PETICION_LOGIN_ADMIN = "loginAdministrador";
    public final static String PETICION_LOGIN_USER = "loginEncuestado";
    public final static String PETICION_REGISTRA_ADMIN = "registrarAdministrador";
    public final static String PETICION_REGISTRAR_USER = "registarUsuario";
    public final static String PETICION_GET_ENCUESTADOS = "getEncuestados";
    public final static String PETICION_CREAR_ENCUESTA = "crearEncuesta";
    public final static String PETICION_GUARDA_EDICION = "guardarEdicion";
    public final static String PETICION_ENVIAR_ENCUESTA = "enviarEncuesta";
    public final static String PETICION_DEVOLVER_ENCUESTA = "devolverEncuesta";
    public final static String PETICION_ENVIAR_CORREO = "enviarCorreo";
    public final static String PETICION_CAMBIAR_CONTRASENNA_ADMIN = "cambiarContrasennaAdministrador";
    public final static String PETICION_CAMBIAR_CONTRASENNA_ENCUESTADO = "cambiarContrasennaEncuestado";
    public final static String PETICION_PREGUNTAS_POR_ENCUESTA = "nombresPorEncuesta";
    public final static String PETICION_GET_ENCUESTA = "abrirEncuesta";
    public final static String PETICION_ELIMINA_ENCUESTA = "eliminaEncuesta";
    public final static String PETICION_SOLICITA_ENCUESTA = "solicitaEncuesta";
    public final static String PETICION_CERRAR_SESION = "cerrarSesion";
}

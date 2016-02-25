package main;

import gui.ServidorInterfaz;
import util.Strings;

/**
 * @author adriansb3105
 */
public class MainServidor {

    public static void main(String[] args){
        
        int puerto = Strings.PUERTO;

        ServidorInterfaz servidorInterfaz = new ServidorInterfaz(puerto);
    }
}

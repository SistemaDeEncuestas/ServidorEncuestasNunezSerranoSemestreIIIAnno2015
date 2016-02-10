package main;

import gui.ServidorInterfaz;

/**
 * @author adriansb3105
 */
public class MainServidor {

    public static void main(String[] args){
        
        int puerto = 5700;
        
        ServidorInterfaz servidorInterfaz = new ServidorInterfaz(puerto);
        servidorInterfaz.setVisible(true);
    }
    
}

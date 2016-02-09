package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.StringReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 *
 * @author adriansb3105
 */
public class Servidor implements Runnable{
    private int puerto;

    public Servidor(int puerto) {
        super();
        this.puerto= puerto;
    }
    
    @Override
    public void run(){
        try {
            ServerSocket serverSocket= new ServerSocket(this.puerto);
            System.out.println("Iniciado");
            do{
                Socket socket= serverSocket.accept();
                PrintStream enviar= new PrintStream(socket.getOutputStream());
                BufferedReader recibir = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                enviar.println("Este es el sevidor(Adrian)");
                System.out.println("El cliente me envio: "+recibir.readLine());
                enviar.println("Listo");
                
                String estudianteString = recibir.readLine();
                
                System.out.println(estudianteString);
                SAXBuilder saxBuilder = new SAXBuilder();
                
                StringReader stringReader= new StringReader(estudianteString);
                Document doc= saxBuilder.build(stringReader);
                Element rootEstudiante= doc.getRootElement();
                
                
////                EstudianteXMLBusiness eXMLB= new EstudianteXMLBusiness();
////                eXMLB.insertarEstaudiante(new Estudiante(rootEstudiante.getAttributeValue("cedula"), rootEstudiante.getChildText("nombre"), Integer.parseInt(rootEstudiante.getChildText("edad"))));
                
               socket.close();
            
            }while(true);
            
        } catch (IOException | JDOMException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   
}
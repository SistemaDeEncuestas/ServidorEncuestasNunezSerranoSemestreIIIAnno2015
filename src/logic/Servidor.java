package logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 *
 * @author adriansb3105
 */
public class Servidor implements Runnable{
    private int puerto;
    private JLabel jlEstado;
    private JTextArea jtaConsola;
    private boolean flag;

    public Servidor(int puerto) {
        super();
        this.puerto= puerto;
        this.flag = true;
    }
    
    public void correHilo(JLabel jlEstado, JTextArea jtaConsola){
        this.jlEstado = jlEstado;
        this.jtaConsola = jtaConsola;
    }
    
    @Override
    public void run(){
        
        try {
            Socket socket;
            ServerSocket serverSocket = new ServerSocket(this.puerto);

            this.jlEstado.setText("Estado: Iniciado");
            this.jtaConsola.append("Servidor iniciado\n");
            do {
                socket = serverSocket.accept();
                PrintStream enviar = new PrintStream(socket.getOutputStream());
                BufferedReader recibir = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                
                Scanner s = new Scanner(System.in);
                String b = s.nextLine();
                
                jtaConsola.append("Yo: "+b+"\n");
                
                enviar.println(b);
                this.jtaConsola.append("Daniel dice: "+recibir.readLine()+"\n");
                
                socket.close(); 
                
            } while (this.flag);
            
            
        }catch(BindException e){
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }   
}

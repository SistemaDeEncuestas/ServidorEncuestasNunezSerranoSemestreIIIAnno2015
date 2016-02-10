package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import logic.Servidor;

/**
 * @author adriansb3105
 */
public class ServidorInterfaz extends JFrame implements ActionListener, Runnable {

    private JPanel panelGrande;
    private JPanel panelUsuarios;
    private JPanel panelEncuentas;
    private JMenuBar jmbBarraMenu;
    private JMenu jmInicio;
    private JMenu jmSalir;
    private JMenuItem jmiSalir;
    private JMenuItem jmiIniciar;
    private JMenuItem jmiDetener;
    private JLabel jlEstado;
    private JTextArea jtaConsola;
    private JScrollPane scroll;
    private JList<String> listaAdmins;
    private JList<String> listaUsuariosConectados;
    private JList<String> listaEncuestas;
    private Font fuente;
    private int puerto;
    private Thread hilo;
    private boolean flag;
    int num;

    public ServidorInterfaz(int puerto) {
        super("Servidor");

        this.setSize(this.getToolkit().getScreenSize());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());

        init(puerto);
    }

    private void init(int puerto) {

        this.flag = true;
        this.puerto = puerto;
        this.panelGrande = new JPanel();
        this.panelGrande.setLayout(new BorderLayout());
        this.panelUsuarios = new JPanel();
        this.panelUsuarios.setLayout(new GridLayout(1, 2));
        this.panelEncuentas = new JPanel();
        this.panelEncuentas.setLayout(new GridLayout(1, 1));
        this.jlEstado = new JLabel("Estado: Detenido", SwingConstants.CENTER);
        this.jmbBarraMenu = new JMenuBar();
        this.jmInicio = new JMenu("Inicio");
        this.jmSalir = new JMenu("Salir");
        this.jmiIniciar = new JMenuItem("Iniciar Servidor");
        this.jmiDetener = new JMenuItem("Detener Servidor");
        this.jmiSalir = new JMenuItem("Salir");
        this.jtaConsola = new JTextArea();
        this.jmiDetener.setEnabled(false);
        this.hilo = new Thread(this);

        String[] admins = {"Administradores", "admi 1", "admi 2", "admi 3", "admi 4"};
        String[] encuestas = {"Encuestas Creadas", "encuesta 1", "encuesta 2", "encuesta 3", "encuesta 4"};
        String[] usuarios = {"Usuarios conectados", "usuario 1", "usuario 2", "usuario 3", "usuario 4", "usuario 5"};

        this.listaAdmins = new JList<>();
        this.listaEncuestas = new JList<>();
        this.listaUsuariosConectados = new JList<>();
        this.scroll = new JScrollPane(this.jtaConsola);
        this.jtaConsola.setLineWrap(true);
        this.fuente = new Font("Dialog", Font.BOLD, 12);
        this.jtaConsola.setFont(fuente);
        this.jtaConsola.setForeground(Color.white);
        this.jtaConsola.setEditable(false);

        this.jmInicio.add(this.jmiIniciar);
        this.jmInicio.add(this.jmiDetener);
        this.jmSalir.add(this.jmiSalir);
        this.jmbBarraMenu.add(this.jmInicio);
        this.jmbBarraMenu.add(this.jmSalir);

        this.jmiIniciar.addActionListener(this);
        this.jmiDetener.addActionListener(this);
        this.jmiSalir.addActionListener(this);

        this.jtaConsola.setBackground(new Color(80, 80, 80));
        this.listaUsuariosConectados.setBackground(new Color(192, 192, 192));
        this.listaAdmins.setBackground(new Color(169, 169, 169));
        this.listaEncuestas.setBackground(new Color(169, 169, 169));

        this.panelGrande.add(this.scroll, BorderLayout.CENTER);
        this.panelGrande.add(this.panelUsuarios, BorderLayout.EAST);
        this.panelGrande.add(this.panelEncuentas, BorderLayout.WEST);

        this.jmbBarraMenu.setBounds(0, 0, this.getWidth(), 20);
        this.panelGrande.setBounds(0, 50, this.getWidth(), this.getHeight() - 50);
        this.listaEncuestas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.listaAdmins.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.listaUsuariosConectados.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        this.panelUsuarios.add(this.listaUsuariosConectados);
        this.panelUsuarios.add(this.listaAdmins);
        this.panelEncuentas.add(this.listaEncuestas);

        this.add(this.jlEstado, BorderLayout.SOUTH);
        this.add(this.jmbBarraMenu, BorderLayout.NORTH);
        this.add(this.panelGrande, BorderLayout.CENTER);

        this.listaAdmins.setListData(admins);
        this.listaEncuestas.setListData(encuestas);
        this.listaUsuariosConectados.setListData(usuarios);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.jmiIniciar) {
            this.jmiIniciar.setEnabled(false);
            this.jmiDetener.setEnabled(true);
            
            
            
            this.hilo.start();
        }

        if (e.getSource() == this.jmiDetener) {
            this.hilo.stop();
            this.flag = false;
            
            this.jlEstado.setText("Estado: Detenido");
            this.jtaConsola.append("Servidor detenido\n");
            
            this.jmiDetener.setEnabled(false);
            this.jmiIniciar.setEnabled(true);
        }

        if (e.getSource() == this.jmiSalir) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Confirma que desea cerrar el servidor?");

            if (opcion == 0) {
                System.exit(0);
            }
        }
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
                enviar.println("Este es el sevidor(Adrian)");
                System.out.println("El cliente me envio: " + recibir.readLine());
                enviar.println("Listo");
                socket.close();

            } while (this.flag);
            System.out.println("termino");
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import logic.UsuariosConectados;
import logic.ListasServidor;

/**
 * @author adriansb3105
 */
public class ServidorInterfaz extends JFrame implements ActionListener{

    private JPanel panelGrande;
    private JPanel panelUsuarios;
    private JPanel panelEncuentas;
    private JMenuBar jmbBarraMenu;
    private JMenu jmInicio;
    private JMenu jmSalir;
    private JMenuItem jmiSalir;
    private JMenuItem jmiIniciar;
    private JLabel jlEstado;
    private JTextArea jtaConsola;
    private JScrollPane scroll;
    private JList<String> listaAdmins;
    private JList<String> listaUsuariosConectados;
    private JList<String> listaEncuestas;
    private Font fuente;
    private int puerto;
    private Thread hilo;
    private Servidor servidor;
    private UsuariosConectados usuariosConectados;
    private ListasServidor listasServidor;

    public ServidorInterfaz(int puerto) {
        super("Servidor");

        this.setSize(this.getToolkit().getScreenSize());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLayout(new BorderLayout());

        init(puerto);
    }

    private void init(int puerto) {

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
        this.jmiSalir = new JMenuItem("Salir");
        this.jtaConsola = new JTextArea();
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
        this.jmSalir.add(this.jmiSalir);
        this.jmbBarraMenu.add(this.jmInicio);
        this.jmbBarraMenu.add(this.jmSalir);

        this.jmiIniciar.addActionListener(this);
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
        
        this.usuariosConectados = new UsuariosConectados(this.listaUsuariosConectados);
        Thread hiloUsuarios = new Thread(this.usuariosConectados);
        hiloUsuarios.start();
        
        this.listasServidor = new ListasServidor(listaAdmins, listaEncuestas);
        Thread hiloListas = new Thread(this.listasServidor);
        hiloListas.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == this.jmiIniciar) 
            this.jmiIniciar.setEnabled(false);{
            this.servidor = new Servidor(this.puerto);
            servidor.correHilo(this.jlEstado, this.jtaConsola);
            
            this.hilo = new Thread(servidor);
            this.hilo.start();
        }
        
        if (e.getSource() == this.jmiSalir) {
            int opcion = JOptionPane.showConfirmDialog(null, "¿Confirma que desea cerrar el servidor? se cerrara toda conexion existente");

            if (opcion == 0) {
                System.exit(0);
            }
        }
    }
}

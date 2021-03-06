package logic;

import business.EncuestaBusiness;
import domain.Encuesta;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import util.Exportar;

/**
 * Clase utilizada para enviar correos electrónicos a distintos usuarios con un
 * pfd de la encuesta a llenar
 *
 * @author adriansb3105
 */
public class EnviaCorreos extends Thread {

    List<String> listaCorreos;
    private String nombreEncuesta;
    private EncuestaBusiness encuestaBusiness;
    private Encuesta encuesta;
    /**
     * 
     * @param nombreEncuesta el nombre de la encuesta a exportar
     * @param listaCorreos la lista de correos electrónicos de los receptores
     */
    public EnviaCorreos(String nombreEncuesta, List<String> listaCorreos) {
        super();
        this.encuestaBusiness = new EncuestaBusiness();
        this.nombreEncuesta = nombreEncuesta;
        this.listaCorreos = listaCorreos;
    }

    @Override
    public void run() {

        this.encuestaBusiness.iniciar(this.nombreEncuesta);
        this.encuesta = this.encuestaBusiness.getEncuesta();

        Exportar exportar = new Exportar();
        exportar.exportarAPDF(encuesta);

        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(EnviaCorreos.class.getName()).log(Level.SEVERE, null, ex);
        }

        enviarCorreo();
    }

    public void enviarCorreo() {

        /*
         hotmail
         host = smtp.live.com
         port = 25
        
         gmail
         host = smtp.gmail.com
         port = 587
         */
        try {
            Properties p = new Properties();
            p.put("mail.smtp.host", "smtp.live.com");
            p.setProperty("mail.smtp.starttls.enable", "true");
            p.setProperty("mail.smtp.port", "25");
            p.setProperty("mail.smtp.user", "adrian-3105@hotmail.com");
            p.setProperty("mail.smtp.auth", "true");

            Session s = Session.getDefaultInstance(p, null);
            BodyPart texto = new MimeBodyPart();
            texto.setText("Mensaje automatico: Se le solicita que llene la encuesta adjunta");

            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource("src/files/emailSent/" + this.encuesta.getNombreArchivo() + ".pdf")));
            adjunto.setFileName(this.nombreEncuesta);

            MimeMultipart m = new MimeMultipart();
            m.addBodyPart(texto);
            m.addBodyPart(adjunto);

            MimeMessage mensaje = new MimeMessage(s);
            mensaje.setFrom(new InternetAddress("adrian-3105@hotmail.com"));

            for (String correo : this.listaCorreos) {
                mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress(correo));
            }

            mensaje.setSubject(this.nombreEncuesta);
            mensaje.setContent(m);

            Transport t = s.getTransport("smtp");
            t.connect("adrian-3105@hotmail.com", "Serrano310594");
            t.sendMessage(mensaje, mensaje.getAllRecipients());
            t.close();

        } catch (MessagingException ex) {
            Logger.getLogger(EnviaCorreos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

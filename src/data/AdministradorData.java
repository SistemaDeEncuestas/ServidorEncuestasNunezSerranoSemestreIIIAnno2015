package data;

import business.EncuestaBusiness;
import business.NombresDeArchivosBusiness;
import domain.Administrador;
import domain.Encuesta;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Daniel
 */
public class AdministradorData {

    private EncuestaData encuestaData;
    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public AdministradorData() throws JDOMException, IOException {
        this.encuestaData = new EncuestaData();
        this.rutaArchivo = "src/files/administradores.xml";
        File archivo = new File(this.rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element("administradores");
            this.documento = new Document(this.raiz);

            guardarXML();
        }
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }

    public void insertar(Administrador administrador) throws IOException {

        Element eAdministrador = new Element("administrador");
        eAdministrador.setAttribute("nickname", administrador.getNickname());
        Element eNombre = new Element("nombre");
        eNombre.addContent(administrador.getNombre());
        eAdministrador.addContent(eNombre);
        Element eContrasenna = new Element("contrasenna");
        eContrasenna.addContent(administrador.getContrasenna());
        eAdministrador.addContent(eContrasenna);
        Element eCorreo = new Element("correo");
        eCorreo.addContent(administrador.getCorreoElectronico());
        eAdministrador.addContent(eCorreo);

//        System.out.println("num: "+administrador.getEncuestasCreadas() + "--- tam: "+ administrador.getEncuestasCreadas().size());
//        
//        List<String> listaEncuestas = administrador.getEncuestasCreadas();
////        Element eEncuestas = new Element("encuestas");
//
//        for (int i = 0; i < listaEncuestas.size(); i++) {
//            Element eEncuesta = new Element("encuesta" + i);
//            eEncuesta.addContent(listaEncuestas.get(i));
//            eEncuestas.addContent(eEncuesta);
//        }
//        eAdministrador.addContent(eEncuestas);
        
        
        Element ePrimeraVez = new Element("primeraVez");
        String tipo = String.valueOf(administrador.isPrimeraVez());
        ePrimeraVez.addContent(tipo);
        eAdministrador.addContent(ePrimeraVez);

        this.raiz.addContent(eAdministrador);
        guardarXML();
    }

    public boolean insertarEncuesta(String nombreEncuesta, String nickName) throws IOException, JDOMException{
        Administrador admin = getAdministrador(nickName);
        admin.addEncuestasCreadas(nombreEncuesta);
        return editaAdministrador(admin);
    }
    
    public Administrador[] getAdministradores() throws IOException, JDOMException {

        int cantidadProyectos = this.raiz.getContentSize();
        Administrador[] administradores = new Administrador[cantidadProyectos];
        int contador = 0;

        List listaElementosAdmins = this.raiz.getChildren();

        for (Object objetoActual : listaElementosAdmins) {

//            List<Encuesta> listaEncuestasCreadas = new ArrayList<>();

            Element elementoActual = (Element) objetoActual;

//            NombresDeArchivosBusiness nombreBusiness = new NombresDeArchivosBusiness();
            List<String> listaNombresEncuestas = new ArrayList<>();
            
//            for (int i = 0; i < listaNombresEncuestas.size(); i++) {
//                encuestaBusiness.iniciar(listaNombresEncuestas.get(i));
//                Encuesta encuestaTemporal = encuestaBusiness.getEncuesta();
//                if (encuestaTemporal.getNickname().equals(elementoActual.getAttributeValue("nickname"))) {
////                    listaEncuestasCreadas.add(encuestaTemporal.get);
//                }
//            }

            Administrador adminActual = new Administrador(elementoActual.getChild("nombre").getValue(),
                    elementoActual.getAttributeValue("nickname"),
                    elementoActual.getChild("contrasenna").getValue(),
                    elementoActual.getChild("correo").getValue());

//            adminActual.setEncuestasCreadas(this.encuestaData.getNombresDeEncuestasPorAdmin(elementoActual.getAttributeValue("nickname")));
            
            Encuesta[] aux = this.encuestaData.getEncuestasPorAdmin(elementoActual.getAttributeValue("nickname"));
            
            for (int i = 0; i < aux.length; i++) {
                listaNombresEncuestas.add(aux[i].getNombreArchivo());
            }
            
//            System.out.println("lista: "+listaNombresEncuestas);
            
            adminActual.setEncuestasCreadas(listaNombresEncuestas);
            
            String primeraVez = (elementoActual.getChild("primeraVez").getValue());
            if (primeraVez.equals("true")) {
                adminActual.setPrimeraVez(true);
            } else {
                adminActual.setPrimeraVez(false);
            }
 
            administradores[contador++] = adminActual;
        }

        return administradores;

    }

    public Administrador getAdministrador(String nickname) throws IOException, JDOMException {

        Administrador[] admin = getAdministradores();

        for (int i = 0; i < admin.length; i++) {
            if (admin[i].getNickname().equals(nickname)) {
                return admin[i];
            }

        }
        return null;
    }

    public boolean eliminaAdministrador(String nickname) throws FileNotFoundException, IOException {

        List listaElementos = this.raiz.getChildren();
        for (Object objetoActual : listaElementos) {

            Element elementoActual = (Element) objetoActual;

            if (elementoActual.getAttributeValue("nickname").equals(nickname)) {
                this.raiz.removeContent(elementoActual);
                elementoActual.removeContent();
                XMLOutputter xmlOutputter = new XMLOutputter();
                xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
                return true;
            }
        }
        return false;
    }

    public String[] getNombresParaConsola() throws IOException, JDOMException {
        List<String> nombresUnicos = new ArrayList<>();

        for (int i = 0; i < getAdministradores().length; i++) {
            if (nombresUnicos.isEmpty()) {
                nombresUnicos.add(getAdministradores()[i].getNickname());
            } else {
                if (!nombresUnicos.contains(getAdministradores()[i].getNickname())) {
                    nombresUnicos.add(getAdministradores()[i].getNickname());
                }
            }
        }

        String[] nombres = new String[nombresUnicos.size()];

        for (int j = 0; j < nombresUnicos.size(); j++) {
            nombres[j] = nombresUnicos.get(j);
        }
        return nombres;
    }

    public boolean editaAdministrador(Administrador administrador) throws IOException {
        boolean eliminado = eliminaAdministrador(administrador.getNickname());
        if (eliminado) {
            insertar(administrador);
            return true;
        }
        return false;
    }
}

package data;

import business.EncuestaBusiness;
import business.NombresDeArchivosBusiness;
import domain.Administrador;
import domain.Encuesta;
import domain.Pregunta;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
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

    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public AdministradorData(String rutaArchivo) throws JDOMException, IOException {
        this.rutaArchivo = "src/files/" + rutaArchivo + ".xml";
        File archivo = new File(this.rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element("Administradores");
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
        eAdministrador.setAttribute("nickname", administrador.getNombreUsuario());
        Element eNombre = new Element("nombre");
        eNombre.addContent(administrador.getNombre());
        eAdministrador.addContent(eNombre);
        Element eContrasenna = new Element("contrasenna");
        eContrasenna.addContent(administrador.getContrasenna());
        eAdministrador.addContent(eContrasenna);
        Element eCorreo = new Element("correo");
        eCorreo.addContent(administrador.getCorreoElectronico());
        eAdministrador.addContent(eCorreo);

        List<Encuesta> listaObjetos = administrador.getEncuestasCreadas();

        Element eEncuestas = new Element("encuestas");

        for (int i = 0; i < listaObjetos.size(); i++) {

            Element eEncuesta = new Element("encuesta" + i);

            Element eTitulo = new Element("titulo");
            eTitulo.addContent(listaObjetos.get(i).getTitulo());

            eEncuesta.addContent(eTitulo);

            eEncuestas.addContent(eEncuesta);
        }
        eAdministrador.addContent(eEncuestas);
        Element ePrimeraVez = new Element("primeraVez");
        String tipo = String.valueOf(administrador.isPrimeraVez());
        ePrimeraVez.addContent(tipo);
        eAdministrador.addContent(ePrimeraVez);

        this.raiz.addContent(eAdministrador);
        guardarXML();

    }

    public Administrador[] getAdministradores() {

        int cantidadProyectos = this.raiz.getContentSize();
        Administrador[] administradores = new Administrador[cantidadProyectos];
        int contador = 0;

        List listaElementosAdmins = this.raiz.getChildren();

        for (Object objetoActual : listaElementosAdmins) {

            List<Encuesta> listaEncuestasCreadas = new ArrayList<>();


            Element elementoActual = (Element) objetoActual;

            List listaNombresEncuestasXML = elementoActual.getChild("encuestas").getContent();


            NombresDeArchivosBusiness nombreBusiness = new NombresDeArchivosBusiness();
            List<String> listaNombresEncuestas = nombreBusiness.getNombres();

            for (int i = 0; i < listaNombresEncuestas.size(); i++) {
                EncuestaBusiness encuestaBusiness = new EncuestaBusiness(listaNombresEncuestas.get(i));
                Encuesta encuestaTemporal = encuestaBusiness.getEncuesta(listaNombresEncuestas.get(i));
                if (encuestaTemporal.getCreador().equals(elementoActual.getAttributeValue("nickname"))) {
                    listaEncuestasCreadas.add(encuestaTemporal);
                }
            }

            Administrador adminActual = new Administrador(elementoActual.getChild("nombre").getValue(),
                    elementoActual.getAttributeValue("nickname"),
                    elementoActual.getChild("contrasenna").getValue(),
                    elementoActual.getChild("correo").getValue());

            adminActual.setEncuestasCreadas(listaEncuestasCreadas);

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

    public Administrador getAdministrador(String nickname) {

        Administrador[] admins = getAdministradores();

        for (int i = 0; i < admins.length; i++) {
            if (admins[i].getNombreUsuario().equals(nickname)) {

                return admins[i];
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

    public String[] getNombresAdministradores() {
        List<String> nombresUnicos = new ArrayList<>();

        for (int i = 0; i < getAdministradores().length; i++) {

            if (nombresUnicos.isEmpty()) {
                nombresUnicos.add(getAdministradores()[i].getNombreUsuario());
            } else {
                if (!nombresUnicos.contains(getAdministradores()[i].getNombreUsuario())) {
                    nombresUnicos.add(getAdministradores()[i].getNombreUsuario());
                }
            }
        }

        String[] nombres = new String[nombresUnicos.size()+1];

        nombres[0] = "Administradores";
        
        for (int j = 0; j < nombresUnicos.size(); j++) {
            nombres[j+1] = nombresUnicos.get(j);
        }

        return nombres;
    }
}

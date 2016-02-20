package data;

import domain.Administrador;
import domain.Encuesta;
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

    private EncuestaData encuestaData;
    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    /**
     * Inicializa el administrador data
     *
     * @throws IOException
     * @throws JDOMException
     *
     */
    public AdministradorData() throws JDOMException, IOException {
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

    /**
     * Inserta un administrador al sistema
     * @param administrador 
     * @throws IOException
     **/
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

        Element ePrimeraVez = new Element("primeraVez");
        String tipo = String.valueOf(administrador.isPrimeraVez());
        ePrimeraVez.addContent(tipo);
        eAdministrador.addContent(ePrimeraVez);

        this.raiz.addContent(eAdministrador);
        guardarXML();
    }

    /**
     * Le inserta una encuesta a un administrador
     * @param nombreEncuesta 
     * @param nickName 
     * @throws IOException
     * @throws JDOMException
     * @return 
     **/
    public boolean insertarEncuesta(String nombreEncuesta, String nickName) throws IOException, JDOMException {
        Administrador admin = getAdministrador(nickName);
        admin.addEncuestasCreadas(nombreEncuesta);
        return editaAdministrador(admin);
    }

    /**
     * Obtiene todos los administradores del sistema
     * @throws IOException
     * @throws JDOMException
     * @return 
     **/
    public Administrador[] getAdministradores() throws IOException, JDOMException {

        int cantidadProyectos = this.raiz.getContentSize();
        Administrador[] administradores = new Administrador[cantidadProyectos];
        int contador = 0;

        List listaElementosAdmins = this.raiz.getChildren();

        for (Object objetoActual : listaElementosAdmins) {
            Element elementoActual = (Element) objetoActual;
            List<String> listaNombresEncuestas = new ArrayList<>();

            Administrador adminActual = new Administrador(elementoActual.getChild("nombre").getValue(),
                    elementoActual.getAttributeValue("nickname"),
                    elementoActual.getChild("contrasenna").getValue(),
                    elementoActual.getChild("correo").getValue());

            this.encuestaData = new EncuestaData();
            Encuesta[] aux = this.encuestaData.getEncuestasPorAdmin(elementoActual.getAttributeValue("nickname"));

            for (int i = 0; i < aux.length; i++) {
                listaNombresEncuestas.add(aux[i].getNombreArchivo());
            }

            adminActual.setEncuestasCreadas(listaNombresEncuestas);

            String primeraVez = elementoActual.getChild("primeraVez").getValue();
            adminActual.setPrimeraVez(primeraVez);

            administradores[contador++] = adminActual;
        }

        return administradores;

    }

    /**
     * Obtiene un solo administrador del sistema por nikname
     * @param nickname 
     * @throws IOException
     * @throws JDOMException
     * @return 
     **/
    public Administrador getAdministrador(String nickname) throws IOException, JDOMException {

        Administrador[] admin = getAdministradores();

        for (int i = 0; i < admin.length; i++) {
            if (admin[i].getNickname().equals(nickname)) {
                return admin[i];
            }
        }
        return null;
    }

    /**
     * Elimina un administrador al sistema
     * @param nickname 
     * @throws IOException
     * @throws FileNotFoundException
     * @return 
     **/
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

    /**
     * Obtiene los nombres de los administradores
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
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

    /**
     * Edita un administrador del sistema
     * @param administrador 
     * @throws IOException
     * @return 
     **/
    public boolean editaAdministrador(Administrador administrador) throws IOException {
        boolean eliminado = eliminaAdministrador(administrador.getNickname());
        if (eliminado) {
            insertar(administrador);
            return true;
        }
        return false;
    }
}

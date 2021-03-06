package data;

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
import util.Strings;

/**
 *
 * @author Adrian
 */
public class EncuestaData {

    private NombresDeArchivosData nombresDeArchivosData;
    private Document documento;
    private Element raiz;
    private String rutaArchivo;
    private String nombreArchivo;
    private EncuestaRespondidaData encuestaRespondidaData;

    public EncuestaData() throws JDOMException, IOException {
        this.nombresDeArchivosData = new NombresDeArchivosData();
        this.encuestaRespondidaData = new EncuestaRespondidaData();
    }

    /**
     * Inicia la clase encuesta data con una encuesta
     *
     * @param rutaArchivo Ruta de la encuesta que se va a trabajar
     * @throws JDOMException
     * @throws IOException
     *
     */
    public void iniciar(String rutaArchivo) throws JDOMException, IOException {
        this.nombreArchivo = rutaArchivo;
        this.rutaArchivo = "src/files/" + rutaArchivo + ".xml";
        File archivo = new File(this.rutaArchivo);
        
        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element(rutaArchivo);
            this.documento = new Document(this.raiz);

            guardarXML();
        }
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }

    /**
     * Inserta una encuesta al sistema
     * @throws IOException
     * @throws JDOMException
     * @param encuesta 
     * @return 
     **/
    public boolean insertar(Encuesta encuesta) throws IOException, JDOMException {

        if (this.nombresDeArchivosData.existeArchivo(encuesta.getNombreArchivo())) {//ya existe
            return false;
        }

        Element elemCreador = new Element("creador");
        elemCreador.addContent(encuesta.getNickname());

        Element elemTitulo = new Element("titulo");
        elemTitulo.addContent(encuesta.getTitulo());

        Element elemDescripcion = new Element("descripcion");
        elemDescripcion.addContent(encuesta.getDescripcion());

        Element elemNombreArchivo = new Element("nombreArchivo");
        elemNombreArchivo.addContent(encuesta.getNombreArchivo());

        Element elemPreguntas = new Element("preguntas");

        List<Pregunta> listaPreguntas = encuesta.getPreguntas();

        for (int i = 0; i < listaPreguntas.size(); i++) {

            Element elemPregunta = new Element("pregunta");
            elemPregunta.setAttribute("tipo", listaPreguntas.get(i).getTipo());
            elemPregunta.setAttribute("enunciado", listaPreguntas.get(i).getEnunciado());

            List<String> listaRespuestas = encuesta.getPreguntas().get(i).getListaRespuestas();

            for (String respuesta : listaRespuestas) {
                Element elemRespuesta = new Element("respuesta");
                elemRespuesta.addContent(respuesta);
                elemPregunta.addContent(elemRespuesta);
            }

            elemPreguntas.addContent(elemPregunta);
        }

        this.raiz.addContent(elemCreador);
        this.raiz.addContent(elemTitulo);
        this.raiz.addContent(elemDescripcion);
        this.raiz.addContent(elemNombreArchivo);
        this.raiz.addContent(elemPreguntas);
        guardarXML();

        this.encuestaRespondidaData.insertaNuevaEncuesta(encuesta.getNombreArchivo(), 0);
        this.nombresDeArchivosData.insertarNombre(encuesta.getNombreArchivo(), encuesta.getNickname());
        return true;
    }

    /**
     * Obtiene todas las encuestas del sistema
     * @throws IOException
     * @throws JDOMException
     * @return 
     **/
    public Encuesta[] getTodasLasEncuestas() throws IOException, JDOMException {

        List<String> nombresDeArchivos = this.nombresDeArchivosData.getNombresDeEncuestas();
        Encuesta[] listaEncuestas = new Encuesta[nombresDeArchivos.size()];
        GetEncuestaPorArchivoData getEncuestaPorArchivoData = new GetEncuestaPorArchivoData();

        for (int i = 0; i < nombresDeArchivos.size(); i++) {

            String aux = "src/files/" + nombresDeArchivos.get(i) + ".xml";

            getEncuestaPorArchivoData.iniciar(aux);
            Encuesta encuesta = getEncuestaPorArchivoData.getEncuesta();

            listaEncuestas[i] = encuesta;
        }

        return listaEncuestas;
    }

    /**
     * Obtiene la encuesta actual
     * @throws IOException
     * @throws JDOMException
     * @return 
     **/
    public Encuesta getEncuesta() throws JDOMException, IOException {

        String aux = this.rutaArchivo;

        GetEncuestaPorArchivoData getEncuestaPorArchivoData = new GetEncuestaPorArchivoData();
        getEncuestaPorArchivoData.iniciar(aux);

        Encuesta encuesta = getEncuestaPorArchivoData.getEncuesta();

        return encuesta;
    }

    /**
     * Obtiene todas las preguntas de una encuesta
     * @param nombreEncuesta 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public List<String> getPreguntasPorEncuesta() throws JDOMException, IOException {

        List<String> lista = new ArrayList<>();

        Encuesta encuesta = getEncuesta();

        for (int i = 0; i < encuesta.getPreguntas().size(); i++) {
            if (encuesta.getPreguntas().get(i).getTipo().equals(Strings.TIPO_UNICA)) {
                lista.add(encuesta.getPreguntas().get(i).getEnunciado());
            }
        }

        return lista;
    }

    /**
     * Obtiene todas las encuestas de una administrador
     * @param nickname 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public Encuesta[] getEncuestasPorAdmin(String nickname) throws IOException, JDOMException {

        Encuesta[] todasLasEncuestas = getTodasLasEncuestas();
        List<Encuesta> listaEncuestas = new ArrayList<>();

        for (int i = 0; i < todasLasEncuestas.length; i++) {
            if (todasLasEncuestas[i].getNickname().equals(nickname)) {
                listaEncuestas.add(todasLasEncuestas[i]);
            }
        }

        Encuesta[] encuestasPorAdmin = new Encuesta[listaEncuestas.size()];

        for (int i = 0; i < encuestasPorAdmin.length; i++) {
            encuestasPorAdmin[i] = listaEncuestas.get(i);
        }

        return encuestasPorAdmin;
    }

    /**
     * Obtiene una encuesta del sistema 
     * @param nickname 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public List<String> getNombresDeEncuestasPorAdmin(String nickname) throws IOException, JDOMException {

        Encuesta[] todasLasEncuestas = getTodasLasEncuestas();
        List<String> listaEncuestas = new ArrayList<>();

        for (int i = 0; i < todasLasEncuestas.length; i++) {
            if (todasLasEncuestas[i].getNickname().equals(nickname)) {
                listaEncuestas.add(todasLasEncuestas[i].getNombreArchivo());
            }
        }
        return listaEncuestas;
    }

    /**
     * Borra una encuesta del sistema
     *
     * @throws IOException
     * @return 
     *
     */
    public boolean borrarEncuesta() throws IOException {
        this.raiz.removeContent();

        guardarXML();

        return this.nombresDeArchivosData.borrarNombreArchivo(this.nombreArchivo);
    }

    /**
     * Edita una encuesta del sistema
     *
     * @throws IOException
     * @throws JDOMException
     * @return 
     *
     */
    public boolean editarEncuesta(Encuesta encuesta) throws IOException, JDOMException {

        return borrarEncuesta() && insertar(encuesta);
    }

}

    /*  Hacer crud
 editar
 */
package data;

import business.GetEncuestaPorArchivoBusiness;
import business.NombresDeArchivosBusiness;
import domain.Encuesta;
import domain.Pregunta;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

/**
 *
 * @author Adrian
 */
public class EncuestaData {

    private NombresDeArchivosBusiness nombresDeArchivosBusiness;
    private Document documento;
    private Element raiz;
    private String rutaArchivo;
    private String nombreArchivo;

    /**
     * Este constructor es para los metodos de escritura como insertar, editar y borrar
     *
     * @param rutaArchivo Recibe el nombre de la encuesta
     * @throws JDOMException
     * @throws IOException
     *
     */
    public EncuestaData(String rutaArchivo) throws JDOMException, IOException {
        this.nombreArchivo = rutaArchivo;
        this.rutaArchivo = "src/files/" + rutaArchivo + ".xml";
        File archivo = new File(this.rutaArchivo);
        this.nombresDeArchivosBusiness = new NombresDeArchivosBusiness();

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

    /**
     * Este constructor es solo para los metodos de lectura como los get
     *
     */
    public EncuestaData() {
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }

    public boolean insertar(Encuesta encuesta) throws IOException {

        if (this.nombresDeArchivosBusiness.existeArchivo(this.nombreArchivo)) {//ya existe
            return false;
        }

        Element elemCreador = new Element("creador");
        elemCreador.addContent(encuesta.getCreador());

        Element elemTitulo = new Element("titulo");
        elemTitulo.addContent(encuesta.getTitulo());

        Element elemDescripcion = new Element("descripcion");
        elemDescripcion.addContent(encuesta.getDescripcion());

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
        this.raiz.addContent(elemPreguntas);
        guardarXML();

        this.nombresDeArchivosBusiness.insertarNombre(this.nombreArchivo, encuesta.getCreador());
        return true;//insertado con exito
    }

    public Encuesta[] getTodasLasEncuestas() throws IOException, JDOMException {

        List<String> nombresDeArchivos = this.nombresDeArchivosBusiness.getNombres();
        Encuesta[] listaEncuestas = new Encuesta[nombresDeArchivos.size()];

        for (int i = 0; i < nombresDeArchivos.size(); i++) {

            String aux = "src/files/" + nombresDeArchivos.get(i) + ".xml";

            GetEncuestaPorArchivoBusiness getEncuestaPorArchivoBusiness = new GetEncuestaPorArchivoBusiness(aux);

            Encuesta encuesta = getEncuestaPorArchivoBusiness.getEncuesta();

            listaEncuestas[i] = encuesta;
        }

        return listaEncuestas;
    }

    public Encuesta getEncuesta() throws JDOMException, IOException {

        String aux = this.rutaArchivo;

        GetEncuestaPorArchivoBusiness getEncuestaPorArchivoBusiness = new GetEncuestaPorArchivoBusiness(aux);

        Encuesta encuesta = getEncuestaPorArchivoBusiness.getEncuesta();

        return encuesta;
    }

    public Encuesta[] getEncuestasPorAdmin(String nickname) throws IOException, JDOMException {
        
        Encuesta[] todasLasEncuestas = getTodasLasEncuestas();
        Encuesta[] listaEncuestas = new Encuesta[todasLasEncuestas.length];
        
        for (int i = 0; i< listaEncuestas.length; i++) {
            if (todasLasEncuestas[i].getCreador().equals(nickname)) {
                listaEncuestas[i] = todasLasEncuestas[i];
            }
        }

        return listaEncuestas;
    }

    public boolean borrarEncuesta() throws IOException {
        this.raiz.removeContent();
        
            guardarXML();
        
        return this.nombresDeArchivosBusiness.borrarNombreArchivo(this.nombreArchivo);
    }

    public boolean editarEncuesta(Encuesta encuesta) throws IOException {

        return borrarEncuesta() && insertar(encuesta);
    }

}

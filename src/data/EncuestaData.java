    /*  Hacer crud
        editar, borrar
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

    public EncuestaData(String rutaArchivo) throws JDOMException, IOException {
        this.rutaArchivo = rutaArchivo+".xml";
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

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }
    
    public void insertar(Encuesta encuesta) throws IOException {

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
        
        this.nombresDeArchivosBusiness.insertarNombre(this.rutaArchivo);
    }

    public List<Encuesta> getTodasLasEncuestas() throws IOException, JDOMException {

        List<String> nombresDeArchivos = this.nombresDeArchivosBusiness.getNombres();
        List<Encuesta> listaEncuestas = new ArrayList<>();
        
        for (int i = 0; i < nombresDeArchivos.size(); i++) {
            listaEncuestas.add(getEncuesta(nombresDeArchivos.get(i)));
        }

        return listaEncuestas;
    }
    
    private Encuesta getEncuesta(String nombreEncuesta) throws JDOMException, IOException{
        
        GetEncuestaPorArchivoBusiness getEncuestaPorArchivoBusiness = new GetEncuestaPorArchivoBusiness(nombreEncuesta);
        
        Encuesta encuesta = getEncuestaPorArchivoBusiness.getEncuesta();
        
        return encuesta;
    }
    
    
}

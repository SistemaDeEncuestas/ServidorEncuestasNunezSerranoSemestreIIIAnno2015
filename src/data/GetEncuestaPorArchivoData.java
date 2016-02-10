package data;

import business.NombresDeArchivosBusiness;
import domain.Encuesta;
import domain.Pregunta;
import domain.PreguntaAbierta;
import domain.PreguntaRespuestaMultiple;
import domain.PreguntaRespuestaUnica;
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
 * @author adriansb3105
 */
public class GetEncuestaPorArchivoData {

    private NombresDeArchivosBusiness nombresDeArchivosBusiness;
    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public GetEncuestaPorArchivoData(String rutaArchivo) throws JDOMException, IOException {
        this.rutaArchivo = rutaArchivo;
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

//            guardarXML();
        }
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }
    
    public Encuesta getEncuesta(){
        
        List<Pregunta> listaPreguntas = new ArrayList<>();
        List<String> listaRespuestas = new ArrayList<>();
        
        Element elemCreador = this.raiz.getChild("creador");
        Element elemTitulo = this.raiz.getChild("titulo");
        Element elemDescripcion = this.raiz.getChild("descripcion");
        Element elemPreguntas = this.raiz.getChild("preguntas");
        
        List listaElementosPreguntas = elemPreguntas.getChildren();
        
        for (Object objetoActualPreguntas : listaElementosPreguntas) {
            Element elementoActualPreguntas = (Element) objetoActualPreguntas;
            
            Pregunta pregunta;
            
            switch (elementoActualPreguntas.getAttributeValue("tipo")) {
                case Strings.TIPO_MULTIPLE:
                    pregunta = new PreguntaRespuestaMultiple(elementoActualPreguntas.getAttributeValue("enunciado"));
                    break;
                case Strings.TIPO_UNICA:
                    pregunta = new PreguntaRespuestaUnica(elementoActualPreguntas.getAttributeValue("enunciado"));
                    break;
                default://TIPO_ABIERTA
                    pregunta = new PreguntaAbierta(elementoActualPreguntas.getAttributeValue("enunciado"));
                    break;
            }
                
            List listaElementosRespuestas = elementoActualPreguntas.getChildren();
            
            for (Object objetoActualRespuestas : listaElementosRespuestas) {
                Element elementoActualRespuestas = (Element) objetoActualRespuestas;
                listaRespuestas.add(elementoActualRespuestas.getValue());
            }
            
            pregunta.setListaRespuestas(listaRespuestas);
            
            listaPreguntas.add(pregunta);
        }
        
        Encuesta encuesta = new Encuesta(elemCreador.getValue(), elemTitulo.getValue(),
                                        elemDescripcion.getValue(), listaPreguntas);
        
        
        return encuesta;
    }
}

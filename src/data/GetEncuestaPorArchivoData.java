package data;

import domain.Encuesta;
import domain.Pregunta;
import domain.PreguntaAbierta;
import domain.PreguntaRespuestaMultiple;
import domain.PreguntaRespuestaUnica;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import util.Strings;

/**
 * @author adriansb3105
 */
public class GetEncuestaPorArchivoData {

    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public GetEncuestaPorArchivoData() throws JDOMException, IOException {
    }

     /**
     * Inicia la clase con el archivo deseado
     * @param rutaArchivo 
     * @throws JDOMException
     * @throws IOException
     **/
    public void iniciar(String rutaArchivo) throws JDOMException, IOException{
        this.rutaArchivo = rutaArchivo;
        File archivo = new File(this.rutaArchivo);
        
        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element(rutaArchivo);
            this.documento = new Document(this.raiz);

        }
    }
    
     /**
     * Obtiene una encuesta
     * @return 
     **/
    public Encuesta getEncuesta(){
        
        List<Pregunta> listaPreguntas = new ArrayList<>();
        
        Element elemCreador = this.raiz.getChild("creador");
        Element elemTitulo = this.raiz.getChild("titulo");
        Element elemDescripcion = this.raiz.getChild("descripcion");
        Element elemNombreArchivo = this.raiz.getChild("nombreArchivo");
        Element elemPreguntas = this.raiz.getChild("preguntas");
        
        List lista = elemPreguntas.getChildren();
        
        for (Object objetoActualPreguntas : lista) {
            List<String> listaRespuestas = new ArrayList<>();
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
                                        elemDescripcion.getValue(), elemNombreArchivo.getValue(), listaPreguntas);
        
        
        return encuesta;
    }
}

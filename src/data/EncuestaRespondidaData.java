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

/**
 * @author adriansb3105
 */
public class EncuestaRespondidaData {

    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public EncuestaRespondidaData() {
    }
    
    /**
     * Inicia la clase encuesta respondida
     * @param rutaArchivo 
     * @param cont 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    private void iniciar(String rutaArchivo, int cont) throws JDOMException, IOException{
        this.rutaArchivo = "src/files/respondidas/" + rutaArchivo + "_" + cont +".xml";
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
     * Inserta una encuesta que ya esta respondida al sistema
     * @param nombre  
     * @param cont 
     * @throws JDOMException
     * @throws IOException
     **/
    public void insertaNuevaEncuesta(String nombre, int cont) throws IOException, JDOMException{
        Document documento;
        Element raiz;
        String rutaArchivo = "src/files/respondidas/contadorEncuestasRespondidas.xml";
        File archivo = new File(rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            documento = saxBuilder.build(rutaArchivo);
            raiz = documento.getRootElement();
        } else {
            raiz = new Element("encuestasRespondidas");
            documento = new Document(raiz);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
        }
        
        Element elemEncuesta = new Element("encuesta");
        elemEncuesta.setAttribute("titulo", nombre);
        elemEncuesta.addContent(String.valueOf(cont));
        raiz.addContent(elemEncuesta);
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
    }
    
    /**
     * Devuelve la cantidad de encuestas que hay respondidas para casa encuesta
     * @param nombreEncuesta 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public int getNumeroPorEncuesta(String nombreEncuesta) throws JDOMException, IOException {
        Document documento;
        Element raiz;
        String rutaArchivo = "src/files/respondidas/contadorEncuestasRespondidas.xml";
        File archivo = new File(rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            documento = saxBuilder.build(rutaArchivo);
            raiz = documento.getRootElement();
        } else {
            raiz = new Element("encuestasRespondidas");
            documento = new Document(raiz);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
        }
        
        List listaElementos = raiz.getChildren();
        int cont;
        
        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            
            if (elementoActual.getAttributeValue("titulo").equals(nombreEncuesta)) {
                cont = Integer.parseInt(elementoActual.getValue());
                return cont;
            }
        }

        return 0;
    }
    
     /**
     * Setea la cantidad de encuestas que hay respondidas para casa encuesta
     * @param nombreEncuesta 
     * @param cont 
     * @throws JDOMException
     * @throws IOException
     **/
    public void setNumeroPorEncuesta(String nombreEncuesta, int cont) throws JDOMException, IOException {
        Document documento;
        Element raiz;
        String rutaArchivo = "src/files/respondidas/contadorEncuestasRespondidas.xml";
        File archivo = new File(rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            documento = saxBuilder.build(rutaArchivo);
            raiz = documento.getRootElement();
        } else {
            raiz = new Element("encuestasRespondidas");
            documento = new Document(raiz);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
        }
        
        cont = cont + 1;
        if (eliminaEncuesta(nombreEncuesta)) {
            insertaNuevaEncuesta(nombreEncuesta, cont);
        }
    }
    
     /**
     * Elimina una encuesta respondida
     * @param nombreEncuesta 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public boolean eliminaEncuesta(String nombreEncuesta) throws FileNotFoundException, IOException, JDOMException {
        Document documento;
        Element raiz;
        String rutaArchivo = "src/files/respondidas/contadorEncuestasRespondidas.xml";
        File archivo = new File(rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            documento = saxBuilder.build(rutaArchivo);
            raiz = documento.getRootElement();
        } else {
            raiz = new Element("encuestasRespondidas");
            documento = new Document(raiz);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
        }
        
        List listaElementos = raiz.getChildren();
        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;

            if (elementoActual.getAttributeValue("titulo").equals(nombreEncuesta)) {
                raiz.removeContent(elementoActual);
                elementoActual.removeContent();
                XMLOutputter xmlOutputter = new XMLOutputter();
                xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
                return true;
            }
        }
        return false;
    }
    
     /**
     * Inserta una encuesta respondida
     * @param encuesta 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public boolean insertar(Encuesta encuesta) throws IOException, JDOMException {
        int cont = getNumeroPorEncuesta(encuesta.getNombreArchivo());
        
        iniciar(encuesta.getNombreArchivo(), cont);
        
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
        
        setNumeroPorEncuesta(encuesta.getNombreArchivo(), cont);
        
        return true;
    }
    
     /**
     * Obtiene las preguntas de una encuesta
     * @param nombreEncuesta 
     * @param parte 
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public List<String> getPreguntas(String nombreEncuesta, String parte) throws JDOMException, IOException{
        GetEncuestaPorArchivoData getEncuestaPorArchivoData = new GetEncuestaPorArchivoData();
        List<String> lista = new ArrayList<>();
        int numEncuestas = getNumeroPorEncuesta(nombreEncuesta);
        List<Encuesta> listaEncuesta = new ArrayList<>();
        
        for (int i = 0; i < numEncuestas; i++) {
            getEncuestaPorArchivoData.iniciar("src/files/respondidas/"+nombreEncuesta+"_"+i+".xml");
            listaEncuesta.add(getEncuestaPorArchivoData.getEncuesta());
        }
        
        for (int i = 0; i < listaEncuesta.size(); i++) {
            for (int j = 0; j < listaEncuesta.get(i).getPreguntas().size(); j++) {
                if (listaEncuesta.get(i).getPreguntas().get(j).getEnunciado().equals(parte)) {
                    lista.add(listaEncuesta.get(i).getPreguntas().get(j).getListaRespuestas().get(0));
                }
            }
        }
        
        return lista;
    }
    
     /**
     * Obtiene los nombres de las encuestas
     * @throws JDOMException
     * @throws IOException
     * @return 
     **/
    public List<String> getNombres() throws JDOMException, IOException{
        Document documento;
        Element raiz;
        String rutaArchivo = "src/files/respondidas/contadorEncuestasRespondidas.xml";
        File archivo = new File(rutaArchivo);
        List<String> lista = new ArrayList<>();
        
        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            documento = saxBuilder.build(rutaArchivo);
            raiz = documento.getRootElement();
        } else {
            raiz = new Element("encuestasRespondidas");
            documento = new Document(raiz);
            XMLOutputter xmlOutputter = new XMLOutputter();
            xmlOutputter.output(documento, new PrintWriter(rutaArchivo));
        }
        
        List listaElementos = raiz.getChildren();
        
        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            lista.add(elementoActual.getAttributeValue("titulo"));
        }

        return lista;
    }
    
}

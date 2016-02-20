package data;

import domain.Encuesta;
import domain.Pregunta;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
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
    private String nombreArchivo;

    public EncuestaRespondidaData() {
    }
    
    private void iniciar(String rutaArchivo, int cont) throws JDOMException, IOException{
        this.nombreArchivo = rutaArchivo;
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
    
    
    
//    public Encuesta[] getTodasLasEncuestas() throws IOException, JDOMException {
//
//        List<String> nombresDeArchivos = this.nombresDeArchivosData.getNumeroPorEncuesta();
//        Encuesta[] listaEncuestas = new Encuesta[nombresDeArchivos.size()];
//        GetEncuestaPorArchivoData getEncuestaPorArchivoData = new GetEncuestaPorArchivoData();
//        
//        for (int i = 0; i < nombresDeArchivos.size(); i++) {
//
//            String aux = "src/files/" + nombresDeArchivos.get(i) + ".xml";
//
//            getEncuestaPorArchivoData.iniciar(aux);
//            Encuesta encuesta = getEncuestaPorArchivoData.getEncuesta();
//
//            listaEncuestas[i] = encuesta;
//        }
//
//        return listaEncuestas;
//    }

//    public Encuesta getEncuesta() throws JDOMException, IOException {
//
//        String aux = this.rutaArchivo;
//
//        GetEncuestaPorArchivoData getEncuestaPorArchivoData = new GetEncuestaPorArchivoData();
//        getEncuestaPorArchivoData.iniciar(aux);
//        
//        Encuesta encuesta = getEncuestaPorArchivoData.getEncuesta();
//
//        return encuesta;
//    }
    
}

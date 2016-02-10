package data;

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
public class NombresDeArchivosData {
    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public NombresDeArchivosData() throws JDOMException, IOException {
        this.rutaArchivo = Strings.RUTA_ARCHIVOS;
        File archivo = new File(this.rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element("nombresArchivos");
            this.documento = new Document(this.raiz);

            guardarXML();
        }
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }
    
//    /**
//     * <nombreArchivos>
//     *      <nombre>
//     *          nombre1
//     *      </nombre>
//     * </nombreArchivos>
//     **/
    
    public void insertarNombre(String nombreArchivo) throws IOException{
        
        Element elemNombre = new Element("nombre");
        elemNombre.addContent(nombreArchivo);
        
        this.raiz.addContent(elemNombre);
        guardarXML();
    }
    
    public List<String> getNombres(){
        
        List<String> listaNombres = new ArrayList<>();
        List listaElementos = this.raiz.getChildren();

        for (Object objetoActual : listaElementos) {
            Element elementoActual = (Element) objetoActual;
            listaNombres.add(elementoActual.getValue());
        }
        
        return listaNombres;
    }
    
    public boolean existeArchivo(String nombreArchivo){
        
        List<String> lista = getNombres();
        
        for(String elem : lista){
            if (elem.equals(nombreArchivo)) {
                return true;
            }
        }
        
        return false;
    }
}

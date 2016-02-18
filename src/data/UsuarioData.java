package data;

import domain.Encuestado;
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
public class UsuarioData {

    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public UsuarioData() throws JDOMException, IOException {
        this.rutaArchivo = "src/files/usuarios.xml";
        File archivo = new File(this.rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element("encuestados");
            this.documento = new Document(this.raiz);

            guardarXML();
        }
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }

    public void insertar(Encuestado encuestado) throws IOException {

        Element eEncuestado = new Element("encuestado");
        eEncuestado.setAttribute("nickname", encuestado.getNickname());
        Element eNombre = new Element("nombre");
        eNombre.addContent(encuestado.getNombre());
        eEncuestado.addContent(eNombre);
        Element eContrasenna = new Element("contrasenna");
        eContrasenna.addContent(encuestado.getContrasenna());
        eEncuestado.addContent(eContrasenna);
        Element eCorreo = new Element("correo");
        eCorreo.addContent(encuestado.getCorreoElectronico());
        eEncuestado.addContent(eCorreo);

        List<String> listaObjetos = encuestado.getListaEncuestas();

        Element eEncuestas = new Element("encuestas");

        for (int i = 0; i < listaObjetos.size(); i++) {
            Element eEncuesta = new Element("encuesta" + i);
            eEncuesta.addContent(listaObjetos.get(i));
            eEncuestas.addContent(eEncuesta);
        }
        eEncuestado.addContent(eEncuestas);

        this.raiz.addContent(eEncuestado);
        guardarXML();

    }

    public Encuestado[] getEncuestados() {

        int cantidadEncuestados = this.raiz.getContentSize();
        Encuestado[] encuestados = new Encuestado[cantidadEncuestados];
        int contador = 0;
        List listaElementosEncuestados = this.raiz.getChildren();

        for (Object objetoActual : listaElementosEncuestados) {

            Element elementoActual = (Element) objetoActual;
            List listaEncuestas = elementoActual.getChild("encuestas").getChildren();
            List<String> listaNombres = new ArrayList<>();

            for (Object objetoEncuesta : listaEncuestas) {
                Element encuestaActual = (Element) objetoEncuesta;
                String compActual = encuestaActual.getValue();

                listaNombres.add(compActual);
            }

            Encuestado encuestadoActual = new Encuestado(elementoActual.getChild("nombre").getValue(),
                    elementoActual.getAttributeValue("nickname"),
                    elementoActual.getChild("contrasenna").getValue(),
                    elementoActual.getChild("correo").getValue());

            encuestadoActual.setListaEncuestas(listaNombres);

            encuestados[contador++] = encuestadoActual;
        }

        return encuestados;
    }

    public Encuestado getEncuestado(String nickname) {

        Encuestado[] encuestados = getEncuestados();

        for (int i = 0; i < encuestados.length; i++) {
            if (encuestados[i].getNickname().equals(nickname)) {

                return encuestados[i];
            }

        }
        return null;
    }

    public boolean eliminaEncuestado(String nickname) throws FileNotFoundException, IOException {

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
   
    
    public boolean editaEncuestado(Encuestado encuestado) throws IOException{
        
        boolean eliminado = eliminaEncuestado(encuestado.getNickname());
        if(eliminado){
            insertar(encuestado);
            return true;
        }
        return false;
    }
    
    public List<String> getNombresEncuestados(){
        List<String> lista = new ArrayList<>();
        
        Encuestado[] encuestados = getEncuestados();
        
        for (int i = 0; i < encuestados.length; i++) {
            lista.add(encuestados[i].getNickname());
        }
        return lista;
    }
}

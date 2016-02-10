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

    private Document documento;
    private Element raiz;
    private String rutaArchivo;

    public AdministradorData(String rutaArchivo) throws JDOMException, IOException {
        this.rutaArchivo = rutaArchivo;
        File archivo = new File(this.rutaArchivo);

        if (archivo.exists()) {
            SAXBuilder saxBuilder = new SAXBuilder();
            saxBuilder.setIgnoringElementContentWhitespace(true);
            this.documento = saxBuilder.build(this.rutaArchivo);
            this.raiz = this.documento.getRootElement();
        } else {
            this.raiz = new Element("Administradores");
            this.documento = new Document(this.raiz);

            guardarXML();
        }
    }

    public void guardarXML() throws FileNotFoundException, IOException {
        XMLOutputter xmlOutputter = new XMLOutputter();
        xmlOutputter.output(this.documento, new PrintWriter(this.rutaArchivo));
    }

    public void insertar(Administrador administrador) throws IOException {

        Element eAdministrador = new Element("administrador");
        eAdministrador.setAttribute("nickname", administrador.getNombreUsuario());
        Element eNombre = new Element("nombre");
        eNombre.addContent(administrador.getNombre());
        eAdministrador.addContent(eNombre);
        Element eContrasenna = new Element("contrasenna");
        eContrasenna.addContent(administrador.getContrasenna());
        eAdministrador.addContent(eContrasenna);
        Element eCorreo = new Element("correo");
        eCorreo.addContent(administrador.getCorreoElectronico());
        eAdministrador.addContent(eCorreo);

        List<Encuesta> listaObjetos = administrador.getEncuestasCreadas();
        
        for (int i = 0; i < listaObjetos.size(); i++) {
            
            Element eEncuesta = new Element("Encuesta" + i);
            
            Element eTitulo = new Element("titulo");
            eTitulo.addContent(listaObjetos.get(i).getTitulo());
            
            eEncuesta.addContent(eTitulo);
            
            eAdministrador.addContent(eEncuesta);

        }
        this.raiz.addContent(eAdministrador);
        guardarXML();

    }
    
     public Administrador[] getAdministradores() {

        int cantidadProyectos = this.raiz.getContentSize();
        Administrador[] administradores = new Administrador[cantidadProyectos];

        int contador = 0;

        List listaElementosProyecto = this.raiz.getChildren();

        for (Object objetoActual : listaElementosProyecto) {
            
            List<Encuesta> lista = new ArrayList<>();
            Element elementoActual = (Element) objetoActual;

            List listaComponentes = elementoActual.getChildren();

            //TODO recuperar las encuestas    
            
            Administrador adminActual = new Administrador(elementoActual.getChild("nombre").getValue(),
                    elementoActual.getAttributeValue("nickname"),
                    elementoActual.getChild("contrasenna").getValue(),
                    elementoActual.getChild("correo").getValue());

            administradores[contador++] = adminActual;
        }

        return administradores;

    }
    
    

}

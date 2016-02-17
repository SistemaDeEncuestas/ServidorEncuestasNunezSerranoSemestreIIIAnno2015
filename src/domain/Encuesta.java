package domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Encuesta implements Serializable{
    private String nickname;
    private String titulo;
    private String descripcion;
    private List<Pregunta> preguntas;
    private String nombreArchivo;

    public Encuesta(String nickname, String titulo, String descripcion, String nombreArchivo, List<Pregunta> preguntas) {
        this.nickname = nickname;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.nombreArchivo = nombreArchivo;
        this.preguntas = preguntas;
    }

    public Encuesta() {
        this.nickname = "";
        this.titulo = "";
        this.descripcion = "";
        this.preguntas = new ArrayList<>();
    }
    

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String creador) {
        this.nickname = creador;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
    
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }

    @Override
    public String toString() {
//        return "Encuesta{" + "creador=" + nickname + ", titulo=" + titulo + ", descripcion=" + descripcion + ", preguntas=" + preguntas + '}';
        return "Encuesta{" + "creador=" + nickname + ", descripcion=" + descripcion + ", preguntas=" + preguntas + '}';
    }
    
}

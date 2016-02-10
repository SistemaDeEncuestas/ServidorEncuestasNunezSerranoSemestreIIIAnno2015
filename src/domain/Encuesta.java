package domain;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Daniel
 */
public class Encuesta {
    private String creador;
    private String titulo;
    private String descripcion;
    private List<Pregunta> preguntas;

    public Encuesta(String creador, String titulo, String descripcion, List<Pregunta> preguntas) {
        this.creador = creador;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.preguntas = preguntas;
    }

    public Encuesta() {
        this.creador = "";
        this.titulo = "";
        this.descripcion = "";
        this.preguntas = new ArrayList<>();
    }
    

    public String getCreador() {
        return creador;
    }

    public void setCreador(String creador) {
        this.creador = creador;
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
        return "Encuesta{" + "creador=" + creador + ", titulo=" + titulo + ", descripcion=" + descripcion + ", preguntas=" + preguntas + '}';
    }
    
}

package utfpr.edu.br.api_avaliacao.model;


import jakarta.persistence.*;

@Entity
public class Avaliacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avaliacao_seq")
    @SequenceGenerator(name = "avaliacao_seq", sequenceName = "avaliacao_seq", allocationSize = 1)
    long id;
    private String titulo;
    private String comentario;
    private Double nota;

    public Avaliacao() {}
    public Avaliacao(long id,String titulo, String comentario, Double nota) {
        this.id = id;
        this.titulo = titulo;
        this.comentario = comentario;
        this.nota = nota;
    }

    public String getTitulo() {return titulo;}

    public void setTitulo(String titulo) {this.titulo = titulo;}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Double getNota() {
        return nota;
    }

    public void setNota(Double nota) {
        this.nota = nota;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
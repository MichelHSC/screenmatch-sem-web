package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ConsultaGpt;

import java.util.OptionalDouble;

public class Serie {
    
    private String titulo;

    private Integer totalTemporadas;

    private Double avaliacoes;

    private Categoria genero;

    private String atores;

    private String poste;

    private String sinopse;

    public Serie(DadosSerie dadosSerie) {
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacoes = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacoes())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.poste = dadosSerie.poste();
        this.sinopse = ConsultaGpt.obterTraducao(dadosSerie.sinopse()).trim();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacoes() {
        return avaliacoes;
    }

    public void setAvaliacoes(Double avaliacoes) {
        this.avaliacoes = avaliacoes;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {
        return
                ", genero= " + genero + '\'' +
                " titulo= '" + titulo + '\'' +
                ", totalTemporadas= " + totalTemporadas +
                ", avaliacoes= " + avaliacoes +
                ", atores= '" + atores + '\'' +
                ", poste= '" + poste + '\'' +
                ", sinopse= '" + sinopse + '\'';
    }
}

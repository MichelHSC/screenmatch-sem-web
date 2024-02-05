package br.com.alura.screenmatch.model;

public enum Categoria {

    ACAO("Action", "Ação", "Acao"),
    ROMANCE("Romance", "Romance", "Romance"),
    COMEDIA("Comedy", "Comédia", "Comedia"),
    DRAMA("Drama", "Drama", "Drama"),
    CRIME("Crime", "Crime", "Crime");


    private String categoriaBrComAcento;
    private String categoriaBrSemAcento;
    private String categoriaOmdb;

    Categoria(String categoriaOmdb, String categoriaBrComAcento, String categoriaBrSemAcento) {

        this.categoriaOmdb = categoriaOmdb;
        this.categoriaBrComAcento = categoriaBrComAcento;
        this.categoriaBrSemAcento = categoriaBrSemAcento;
    }

    public static Categoria fromString (String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaOmdb.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenuma categoria encontrada Para esta serie");
    }

    public static Categoria fromPortugues(String text){
        for (Categoria categoria : Categoria.values()){
            if (categoria.categoriaBrComAcento.equalsIgnoreCase(text) || categoria.categoriaBrSemAcento.equalsIgnoreCase(text)){
                return categoria;
            }
        }
        throw new IllegalArgumentException("Nenuma Serie encontrada Para esta categoria");
    }

}

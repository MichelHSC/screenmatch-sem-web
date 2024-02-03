package br.com.alura.scrrenmatch.principal;

import br.com.alura.scrrenmatch.model.DadosEpisodio;
import br.com.alura.scrrenmatch.model.DadosSerie;
import br.com.alura.scrrenmatch.model.DadosTemporada;
import br.com.alura.scrrenmatch.model.Episodio;
import br.com.alura.scrrenmatch.service.ConsumoApi;
import br.com.alura.scrrenmatch.service.ConverterDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private Scanner ler = new Scanner(System.in);
    private final String ENDERECO ="https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d343fc07";

    private ConsumoApi consumo = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();

    public void exibeMenu(){

        System.out.println("********************************************************");
        System.out.println("Digite o nome da serie: ");
        var nomeSerie = ler.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ","+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
        System.out.println("********************************************************");

        List<DadosTemporada> temporadas = new ArrayList<>();

        for(int i =1 ; i<= dados.totalTemporadas(); i++){
            json = consumo.obterDados( ENDERECO + nomeSerie.replace(" ","+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);
        System.out.println("********************************************************");

//        for(int i = 0; i < dados.totalTemporadas();i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            System.out.println("Temp: "+ i+ " ");
//            for(int j =0 ;j< episodiosTemporada.size(); j++){
//                System.out.println("Ep"+ j +" " + episodiosTemporada.get(j).titulo());
//            }
//        }

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println("Titulo: " + e.titulo())));

//        List<String> nomes = Arrays.asList("Michel", "Anne","Isaac");
//        nomes.stream()
//                .sorted().limit(2).filter(n -> n.startsWith("I")).map(n -> n.toUpperCase())
//                .forEach(System.out::println);

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());

        System.out.println("Top 5 episodios: ");
        dadosEpisodios.stream()
                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .limit(5)
                .forEach(System.out::println);

        System.out.println("********************************************************");


        List<Episodio> episodios =  temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.temporada(), d))
                ).collect(Collectors.toList()); ;

        episodios.forEach(System.out::println);

    }
}
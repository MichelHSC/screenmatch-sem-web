package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SerieRepository;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverterDados;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    private Scanner ler = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverterDados conversor = new ConverterDados();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=d343fc07";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    private SerieRepository repositorio;

    private List<Serie> series = new ArrayList<>();

    private  Optional<Serie> serieBuscada;

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1  - Buscar séries
                    2  - Buscar episódios
                    3  - Listar Series buscadas 
                    4  - Busca serie por Titulo 
                    5  - Buscar Serie por Ator  
                    6  - Top 5 Series     
                    7  - Buscar Series por Categoria 
                    8  - Filtrar por categoria e avaliacao    
                    9  - Buscar nome do Ep
                    10 - Top 5 melhores Ep
                    11 - Buscar ep a partir de uma data
                      
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = ler.nextInt();
            ler.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriePorGenero();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPeloTrecho();
                    break;
                case 10:
                    topEpPorSerie();
                    break;
                case 11:
                    buscarEpDepoisDaData();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }


    private void listarSeriesBuscadas() {

        series = repositorio.findAll();
        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                        .forEach(System.out::println);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
//        dadosSeries.add(dados);
        repositorio.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = ler.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie(){

        listarSeriesBuscadas();
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = ler.nextLine();

        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serie.isPresent()) {

            var serieEncontrada = serie.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repositorio.save(serieEncontrada);
        }else {
            System.out.println("Serie não encontrada");
        }
    }


    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma serie pelo nome: ");
        var nomeSerie = ler.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if(serieBuscada.isPresent()){
            System.out.println("Dados da serie: " + serieBuscada.get());
        }else {
            System.out.println("Sereie nâo encontrada!");
        }
    }


    private void buscarSeriePorAtor() {
        System.out.println("Qual nome do ator: ");
        var nomeAtor = ler.nextLine();
        List<Serie> seriesEncontradas = repositorio.findByAtoresContainingIgnoreCase(nomeAtor);
        System.out.println("Series em q o "+nomeAtor+ " trabalhou: ");
        seriesEncontradas.forEach(s -> System.out.println(s.getTitulo() + " Avaliação: "+s.getAvaliacoes()));


    }

    private void buscarTop5Series() {
        List<Serie> serieTop = repositorio.findTop5ByOrderByAvaliacoesDesc();
        serieTop.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacoes()));
    }

    private void buscarSeriePorGenero() {
        System.out.println("Qual categoria: ");
        var nomeGenero = ler.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repositorio.findByGenero(categoria);
        System.out.println("Series da categoria: "+ nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaEAvaliacao() {
        System.out.println("Filtrar series ate quantas temporadas? ");
        var totalTemporadas = ler.nextInt();
        System.out.println("Com Avaliação a partir de que valor?");
        var avaliacoes = ler.nextDouble();
        ler.nextLine();
        List<Serie> filtroSeries = repositorio.seriePorTemporadaEAvaliacao(totalTemporadas,avaliacoes);
        System.out.println("*** Series Filtradas ***");
        filtroSeries.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: "+ s.getAvaliacoes()));
    }

    private void buscarEpisodioPeloTrecho() {
        System.out.println("Qual nome do episodio para busca: ");
        var trechoEpisodio = ler.nextLine();
        List<Episodio> episodiosEncontrados = repositorio.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(), e.getTemporada(),
                        e.getNumeroEp(), e.getTitulo()));
    }

    private void topEpPorSerie() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            List<Episodio> topEp = repositorio.topEpPorSerie(serie);
            topEp.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSerie().getTitulo(), e.getTemporada(),
                            e.getNumeroEp(), e.getTitulo(), e.getAvaliacao()));

        }

    }
    private void buscarEpDepoisDaData() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()){
            Serie serie = serieBuscada.get();
            System.out.println("Digite o ano limite de lancamento");
            var anoLancamento = ler.nextInt();
            ler.nextLine();
            List<Episodio>episodiosAno = repositorio.episodioPorSerieEAno(serie, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }




}
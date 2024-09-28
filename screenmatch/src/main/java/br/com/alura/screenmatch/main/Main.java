package br.com.alura.screenmatch.main;

import br.com.alura.screenmatch.model.*;
import br.com.alura.screenmatch.repository.SeriesRepository;
import br.com.alura.screenmatch.service.APIConsumer;
import br.com.alura.screenmatch.service.DataConverter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {


    private Scanner scanner = new Scanner(System.in);
    private APIConsumer consumer = new APIConsumer();
    private DataConverter converter = new DataConverter();
    private final String URL = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private List<SeriesData> searchedSeries = new ArrayList<>();

    private SeriesRepository repository;
    private List<Series> series = new ArrayList<>();
    private Optional<Series> seriesSearch;

    public Main(SeriesRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {
        var opcao = -1;
        while(opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por título
                    5 - Buscar séries por ator
                    6 - Top 5 Séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódios por trecho
                    10 - Top 5 episódios por série
                    11 - Buscar episódios a partir de uma data 
                                    
                    0 - Sair                                 
                    """;

            System.out.println(menu);
            opcao = scanner.nextInt();
            scanner.nextLine();

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
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    buscarTop5Series();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    buscarEpisodioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 11:
                    buscarEpisodiosDepoisDeUmaData();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        }
    }

    private void searchByTitle() {
        SeriesData seriesData = getSeasonData();
        Series series = new Series(seriesData);
//        searchedSeries.add(seriesData);
        repository.save(series);
        System.out.println("Output:");
        System.out.println(seriesData);
    }

    private SeriesData getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = scanner.nextLine();
        var json = consumer.consume(URL + nomeSerie.replace(" ", "+") + API_KEY);
        SeriesData dados = converter.obterDados(json, SeriesData.class);
        return dados;
    }

    private void searchBySeason() {
        listSearchedTitles();
        var seriesName = readTitle();

//        Old method using streams
//        Optional<Series> selectedSeries = series.stream()
//                .filter(s -> s.getTitle().toLowerCase().contains(seriesName.toLowerCase()))
//                .findFirst();

//        New method using the JPA repository
        Optional<Series> selectedSeries = repository.findByTitleContainsIgnoreCase(seriesName);

        if (selectedSeries.isPresent()) {
            //SeriesData seriesData = getSeasonData();
            Series foundSeries = selectedSeries.get();
            List<SeasonData> seasons = new ArrayList<>();

            for (int i = 1; i <= foundSeries.getSeasons(); i++) {
                String json = consumer.consume(foundSeries.getTitle() + "&season=" + i);
                SeasonData seasonData = converter.getData(json, SeasonData.class);
                seasons.add(seasonData);
            }

            seasons.forEach(System.out::println);

            List<Episode> episodes = seasons.stream()
                    .flatMap(d -> d.episodeData().stream()
                            .map(e -> new Episode(d.seasonIdx(), e)))
                    .collect(Collectors.toList());

            foundSeries.setEpisodes(episodes);
            repository.save(foundSeries);
        } else {
            System.out.println("Series not found!");
        }
    }

    private void listarSeriesBuscadas(){
        series = repository.findAll();
        series.stream()
                .sorted(Comparator.comparing(Series::getGenre))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha um série pelo nome: ");
        var nomeSerie = scanner.nextLine();
        seriesSearch = repository.findByTitleContainingIgnoreCase(nomeSerie);

        if (seriesSearch.isPresent()) {
            System.out.println("Dados da série: " + seriesSearch.get());

        } else {
            System.out.println("Série não encontrada!");
        }

    }

    private void buscarSeriesPorAtor() {
        System.out.println("Qual o nome para busca?");
        var nomeAtor = scanner.nextLine();
        System.out.println("Avaliações a partir de que valor? ");
        var avaliacao = scanner.nextDouble();
        List<Series> seriesEncontradas = repository.findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitle() + " avaliação: " + s.getRating()));
    }

    private void buscarTop5Series() {
        List<Series> seriesTop = repository.findTop5ByOrderByRatingDesc();
        seriesTop.forEach(s ->
                System.out.println(s.getTitle() + " avaliação: " + s.getRating()));
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Deseja buscar séries de que categoria/gênero? ");
        var nomeGenero = scanner.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Series> seriesPorCategoria = repository.findByGenre(categoria);
        System.out.println("Séries da categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar séries até quantas temporadas? ");
        var totalTemporadas = scanner.nextInt();
        scanner.nextLine();
        System.out.println("Com avaliação a partir de que valor? ");
        var avaliacao = scanner.nextDouble();
        scanner.nextLine();
        List<Series> filtroSeries = repository.seriesBySeasonAndRating(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitle() + "  - avaliação: " + s.getRating()));
    }

    private void buscarEpisodioPorTrecho(){
        System.out.println("Qual o nome do episódio para busca?");
        var trechoEpisodio = scanner.nextLine();
        List<Episode> episodiosEncontrados = repository.episodeBySnippet(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s Temporada %s - Episódio %s - %s\n",
                        e.getSeries().getTitle(), e.getSeason(),
                        e.getNumeroEpisodio(), e.getTitle()));
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if(seriesSearch.isPresent()){
            Series series = seriesSearch.get();
            List<Episode> topEpisodes = repository.topEpisodesBySeries(series);
            topEpisodes.forEach(e ->
                    System.out.printf("Série: %s Temporada %s - Episódio %s - %s Avaliação %s\n",
                            e.getSeries().getTitle(), e.getSeason(),
                            e.getNumeroEpisodio(), e.getTitle(), e.getRating()));
        }
    }
    private void buscarEpisodiosDepoisDeUmaData(){
        buscarSeriePorTitulo();
        if(seriesSearch.isPresent()){
            Series series = seriesSearch.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoLancamento = scanner.nextInt();
            scanner.nextLine();

            List<Episode> episodiosAno = repository.episodesBySeriesAndYear(series, anoLancamento);
            episodiosAno.forEach(System.out::println);
        }
    }
}
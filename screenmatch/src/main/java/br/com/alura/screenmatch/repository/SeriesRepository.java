package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Series> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, double avaliacao);

    List<Series> findTop5ByOrderByAvaliacaoDesc();

    List<Series> findByGenero(Categoria categoria);

    List<Series> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);
    @Query("select s from Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Series> seriesPorTemporadaEAValiacao(int totalTemporadas, double avaliacao);
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episode> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episode> topEpisodiosPorSerie(Series series);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episode> episodiosPorSerieEAno(Series series, int anoLancamento);
}

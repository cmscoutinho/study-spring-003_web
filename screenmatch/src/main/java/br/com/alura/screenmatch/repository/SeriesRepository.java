package br.com.alura.screenmatch.repository;

import br.com.alura.screenmatch.model.Categoria;
import br.com.alura.screenmatch.model.Episode;
import br.com.alura.screenmatch.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {
    Optional<Series> findByTitleContainingIgnoreCase(String seriesName);

    List<Series> findByActorsContainingIgnoreCaseAndRatingGreaterThanEqual(String actorName, double rating);

    List<Series> findTop5ByOrderByRatingDesc();

    List<Series> findByGenre(Categoria categoria);

    List<Series> findBySeasonsLessThanEqualAndRatingGreaterThanEqual(int seasons, double rating);

    @Query("select s from Series s WHERE s.seasons <= :seasons AND s.rating >= :rating")
    List<Series> seriesBySeasonAndRating(int seasons, double rating);

    @Query("SELECT e FROM Series s JOIN s.episode e WHERE e.title ILIKE %:episodeSnippet%")
    List<Episode> episodeBySnippet(String episodeSnippet);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episode> topEpisodiosPorSerie(Series series);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episode> episodiosPorSerieEAno(Series series, int anoLancamento);
}

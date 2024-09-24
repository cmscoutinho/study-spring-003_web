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

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series ORDER BY e.rating DESC LIMIT 5")
    List<Episode> topEpisodesBySeries(Series series);

    @Query("SELECT e FROM Series s JOIN s.episodes e WHERE s = :series AND YEAR(e.releaseDate) >= :releaseYear")
    List<Episode> episodesBySeriesAndYear(Series series, int releaseYear);
}

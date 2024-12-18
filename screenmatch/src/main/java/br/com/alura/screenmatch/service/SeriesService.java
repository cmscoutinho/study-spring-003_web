package br.com.alura.screenmatch.service;

import br.com.alura.screenmatch.dto.EpisodeDTO;
import br.com.alura.screenmatch.dto.SeriesDTO;
import br.com.alura.screenmatch.model.Category;
import br.com.alura.screenmatch.model.Series;
import br.com.alura.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeriesService {

    @Autowired
    private SeriesRepository repository;

    private List<SeriesDTO> convertData(List<Series> series) {
        return series.stream()
                .map(s -> new SeriesDTO(s.getId(), s.getTitle(), s.getSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot()))
                .collect(Collectors.toList());
    }
    
    public List<SeriesDTO> getAllSeries() {
        return convertData(repository.findAll());
    }

    public List<SeriesDTO> getTop5Series() {
        return convertData(repository.findTop5ByOrderByRatingDesc());
    }

    public List<SeriesDTO> getReleases() {
        //return convertData(repository.findTop5ByOrderByEpisodesReleaseDateDesc());
        return convertData(repository.mostRecentEpisodes());
    }

    public SeriesDTO getById(Long id) {
        Optional<Series> series = repository.findById(id);
        if (series.isPresent()) {
            Series s = series.get();
            return new SeriesDTO(s.getId(), s.getTitle(), s.getSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot());
        }
        return null;
    }

    public List<EpisodeDTO> getAllSeasons(Long id) {
        Optional<Series> series = repository.findById(id);
        if (series.isPresent()) {
            Series s = series.get();
            return s.getEpisodes().stream()
                    .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeIdx(), e.getTitle()))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<EpisodeDTO> getSeasonByNumber(Long id, Long epId) {
        return repository.getEpisodesBySeason(id, epId).stream()
                .map(e -> new EpisodeDTO(e.getSeason(), e.getEpisodeIdx(), e.getTitle()))
                .collect(Collectors.toList());
    }

    public List<SeriesDTO> getSeriesByCategory(String categoryAlias) {
        Category category = Category.fromString(categoryAlias);
        return convertData(repository.findByGenre(category));
    }
}

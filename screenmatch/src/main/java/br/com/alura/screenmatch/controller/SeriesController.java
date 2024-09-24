package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.dto.SeriesDTO;
import br.com.alura.screenmatch.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SeriesController {

    @Autowired
    SeriesRepository repository;

    @GetMapping("/series")
    public List<SeriesDTO> getSeries() {
        return repository.findAll()
                .stream()
                .map(s -> new SeriesDTO(s.getId(), s.getTitle(), s.getSeasons(), s.getRating(), s.getGenre(), s.getActors(), s.getPoster(), s.getPlot()))
                .collect(Collectors.toList());
    }
}

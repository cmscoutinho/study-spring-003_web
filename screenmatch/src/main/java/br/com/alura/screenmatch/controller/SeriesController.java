package br.com.alura.screenmatch.controller;

import br.com.alura.screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SeriesController {

    @Autowired
    SerieRepository repository;

    @GetMapping("/series")
    public String getSeries() {
        return "Series will be listed here.";
    }
}

package br.com.alura.screenmatch.dto;

import br.com.alura.screenmatch.model.Category;

public record SeriesDTO(
        Long id,
        String title,
        Integer seasons,
        Double rating,
        Category genre,
        String actors,
        String poster,
        String plot) {
}

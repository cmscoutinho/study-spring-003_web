package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EpisodeData(@JsonAlias("Episode") Integer episodeIdx,
                          @JsonAlias("Title") String title,
                          @JsonAlias("Released") String releaseDate,
                          @JsonAlias("imdbRating") String rating) {}
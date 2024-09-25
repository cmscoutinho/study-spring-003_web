package br.com.alura.screenmatch.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SeasonData(@JsonAlias("Title") String title,
                         @JsonAlias("Season") Integer seasonIdx,
                         @JsonAlias("totalSeasons") String totalSeasons,
                         @JsonAlias("Episodes") List<EpisodeData> episodeData) {
}

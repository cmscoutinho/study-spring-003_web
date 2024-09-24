package br.com.alura.screenmatch.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Entity
@Table(name = "episodes")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer season;
    private String title;
    private Integer numeroEpisodio;
    private Double rating;
    private LocalDate releaseDate;
    @ManyToOne
    private Series series;

    public Episode(){}

    public Episode(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.season = numeroTemporada;
        this.title = dadosEpisodio.titulo();
        this.numeroEpisodio = dadosEpisodio.numero();

        try {
            this.rating = Double.valueOf(dadosEpisodio.avaliacao());
        } catch (NumberFormatException ex) {
            this.rating = 0.0;
        }

        try {
            this.releaseDate = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (DateTimeParseException ex) {
            this.releaseDate = null;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Series getSeries() {
        return series;
    }

    public void setSeries(Series series) {
        this.series = series;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    @Override
    public String toString() {
        return "temporada=" + season +
                ", titulo='" + title + '\'' +
                ", numeroEpisodio=" + numeroEpisodio +
                ", avaliacao=" + rating +
                ", dataLancamento=" + releaseDate;
    }
}

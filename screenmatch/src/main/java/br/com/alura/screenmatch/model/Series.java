package br.com.alura.screenmatch.model;

import br.com.alura.screenmatch.service.ChatGPTQuery;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String title;
    @Enumerated(EnumType.STRING)
    private Category genre;
    private Integer seasons;
    private String actors;
    private String releaseDate;
    private Double rating;
    private String poster;
    private String plot;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episode> episodes = new ArrayList<>();

    public Series() {}

    public Series(SeriesData seriesData){
        this.title = seriesData.title();
        this.genre = Category.fromString(seriesData.genre().split(",")[0].trim());
        this.seasons = seriesData.seasons();
        this.actors = seriesData.actors();
        this.releaseDate = seriesData.releaseDate();
        this.rating = OptionalDouble.of(Double.valueOf(seriesData.rating())).orElse(0);
        this.poster = seriesData.poster();
        this.plot = ChatGPTQuery.getTranslation(seriesData.plot()).trim();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Episode> getEpisodes() {
        return episodes;
    }

    public void setEpisodes(List<Episode> episodes) {
        episodes.forEach(e -> e.setSeries(this));
        this.episodes = episodes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeasons() {
        return seasons;
    }

    public void setSeasons(Integer seasons) {
        this.seasons = seasons;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Category getGenre() {
        return genre;
    }

    public void setGenre(Category genre) {
        this.genre = genre;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    @Override
    public String toString() {
        return
                "genero=" + genre +
                        ", titulo='" + title + '\'' +
                        ", totalTemporadas=" + seasons +
                        ", avaliacao=" + rating +
                        ", atores='" + actors + '\'' +
                        ", poster='" + poster + '\'' +
                        ", sinopse='" + plot + '\'' +
                        ", episodios='" + episodes + '\'';
    }
}

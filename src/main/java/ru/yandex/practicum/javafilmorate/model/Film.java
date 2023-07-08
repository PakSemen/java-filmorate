package ru.yandex.practicum.javafilmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.javafilmorate.validation.FilmReleaseDate;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.LinkedHashSet;

@Builder
@RequiredArgsConstructor
@Data
public class Film {

    private Integer id;
    @NotBlank(message = "Film name can't be blank")
    private String name;
    @Size(max = 200, message = "Film description has to be less than 200 symbols")
    @NotBlank(message = "Film description can't be blank")
    private String description;
    @FilmReleaseDate(message = "The release date has to be before today")
    private LocalDate releaseDate;
    @NotNull(message = "The film duration can't be empty")
    @Positive(message = "The film duration can't be positive")
    private Integer duration;

    @Valid
    @NotNull
    private Mpa mpa;
    private LinkedHashSet<Genre> genres;

    public Film(String name, String description, LocalDate releaseDate, int duration,
                Mpa mpa, LinkedHashSet<Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film(Integer id, String name, String description, LocalDate releaseDate, Integer duration,
                Mpa mpa, LinkedHashSet<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

    public void addGenre(Genre genre) {
        if (genres == null) {
            genres = new LinkedHashSet<>();
        }
        genres.add(genre);
    }
}
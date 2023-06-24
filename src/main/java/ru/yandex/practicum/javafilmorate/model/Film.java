package ru.yandex.practicum.javafilmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.javafilmorate.validation.FilmReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Builder
@Data
public class Film {

    private int id;
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
    @JsonIgnore
    final Set<Integer> likes = new HashSet<>();

}

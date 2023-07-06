package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    void createFilmGenre(Film film);

    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    void updateFilmGenre(Film film);

    void isGenreExisted(int id);
}

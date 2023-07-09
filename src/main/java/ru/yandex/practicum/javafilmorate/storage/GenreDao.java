package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.model.Genre;

import java.util.List;

public interface GenreDao {

    Genre getGenreById(int id);

    List<Genre> getAllGenres();

    void loadGenres(List<Film> films);
}

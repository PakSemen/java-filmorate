package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;


import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film createFilm(Film film);

    Optional<Film> getFilmById(Integer id);

    List<Film> getAllFilms();

    Film updateFilm(Film film);
}

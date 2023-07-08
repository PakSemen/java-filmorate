package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;


import java.util.List;

public interface FilmDao {

    Film createFilm(Film film);

    Film getFilmById(int id);

    List<Film> getAllFilms();

    Film updateFilm(Film film);

    List<Film> getFavoritesFilms(int id);

}

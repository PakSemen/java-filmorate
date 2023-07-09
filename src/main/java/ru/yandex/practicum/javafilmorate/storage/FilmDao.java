package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;


import java.util.List;

public interface FilmDao {

    void save(Film film);

    Film getFilmById(int id);

    List<Film> getAllFilms();

    void updateFilm(Film film);

    List<Film> getFavoritesFilms(int id);

}

package ru.yandex.practicum.javafilmorate.storage;

import ru.yandex.practicum.javafilmorate.model.Film;


import java.util.List;
import java.util.Optional;

public interface FilmDao {

    Film createFilm(Film film);

    Film getFilmById(int id);

    List<Film> getAllFilms();

    Film updateFilm(Film film);


    void createLike(int id, int userId);

    void deleteLike(int id, int userId);

    void isFilmExisted(int id);

}

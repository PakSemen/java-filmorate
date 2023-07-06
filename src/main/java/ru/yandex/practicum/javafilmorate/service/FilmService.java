package ru.yandex.practicum.javafilmorate.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.FilmDao;
import ru.yandex.practicum.javafilmorate.storage.GenreDao;
import ru.yandex.practicum.javafilmorate.storage.MpaDao;
import ru.yandex.practicum.javafilmorate.storage.UserDao;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmDao filmStorage;
    private final UserDao userStorage;
    private final MpaDao daoStorage;
    private final GenreDao genreStorage;

    public Film createFilm(Film film) {
        daoStorage.isMpaExisted(film.getMpa().getId());
        filmStorage.createFilm(film);
        genreStorage.createFilmGenre(film);
        log.info("Create a film with id = {} ", film.getId());
        return film;
    }

    public Film updateFilm(Film film) {
        filmStorage.isFilmExisted(film.getId());
        genreStorage.updateFilmGenre(film);
        daoStorage.isMpaExisted(film.getMpa().getId());
        filmStorage.updateFilm(film);
        log.info("Update the film with id = {} ", film.getId());
        return film;
    }

    public List<Film> getAllFilms() {
        log.info("GET {} films", filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        filmStorage.isFilmExisted(id);
        return filmStorage.getFilmById(id);
    }

    public void addLikes(int filmId, int userId) {
        filmStorage.isFilmExisted(filmId);
        userStorage.isUserExisted(userId);
        filmStorage.createLike(filmId, userId);
        log.info("Film id: {} like from user: {} ", filmId, userId);
    }

    public Film removeLikes(int filmId, int userID) {
        if (filmId < 0 || userID < 0) {
            throw new NotFoundException("Negative value is not allowed");
        }
        Film film = getFilmById(filmId);
        film.getLikes().remove(userID);
        log.info("The user with id = {} remove a like from the film id = {}", userID, filmId);
        return film;
    }

    public List<Film> favoritesFilms(Integer number) {
        return filmStorage.getAllFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(film -> film.getLikes().size())))
                .limit(number)
                .collect(Collectors.toList());
    }
}

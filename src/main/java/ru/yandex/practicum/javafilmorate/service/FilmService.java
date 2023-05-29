package ru.yandex.practicum.javafilmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.javafilmorate.exception.NotFoundException;
import ru.yandex.practicum.javafilmorate.exception.UserAlreadyExistException;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        log.info("Create a film with id = {} ", film.getId());
        return filmStorage.createFilm(film);
    }

    public Film updateFilm(Film film) {
        log.info("Update the film with id = {} ", film.getId());
        return filmStorage.updateFilm(film);
    }

    public List<Film> getAllFilms() {
        log.info("GET {} films", filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int filmId) {
        Optional<Film> film = filmStorage.getFilmById(filmId);
        if (film.isPresent()) {
            log.info("Get the film with id = {} ", filmId);
            return film.get();
        } else {
            throw new NotFoundException("The film with id =" + filmId + " not found");
        }
    }

    public Film addLikes(int filmId, int userId) {
        Film film = getFilmById(filmId);
        if (!film.getLikes().contains(userId)) {
            film.getLikes().add(userId);
            log.info("A like added to the film with id = {} ", filmId);
            return film;
        } else {
            log.debug("The user doesn't found");
            throw new UserAlreadyExistException("The user doesn't found");
        }
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






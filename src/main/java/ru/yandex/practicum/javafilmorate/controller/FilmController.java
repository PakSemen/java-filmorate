package ru.yandex.practicum.javafilmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.javafilmorate.model.Film;
import ru.yandex.practicum.javafilmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
@Validated
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film addFilm(@Valid @RequestBody @NotNull Film film) {
        log.info("Create a film with id = {} ", film.getId());
        return filmService.createFilm(film);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("GET {} films", filmService.getAllFilms().size());
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("Get a film by id = {} ", id);
        return filmService.getFilmById(id);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("The film with id = {}{}", film.getId(), " has been updated");
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("The user with id = {} {} {} ", userId, " added like for the film with id = ", id);
        filmService.addLikes(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikes(@PathVariable int id, @PathVariable int userId) {
        log.info("The user with id = {} {} {} ", userId, " has been removed like for the film with id = ", id);
        filmService.removeLikes(id, userId);
    }


    @GetMapping("/popular")
    public List<Film> getTopFilms(@Positive @RequestParam(value = "count", defaultValue = "10") int count) {
        log.debug("Get favorite films");
        return filmService.favoritesFilms(count);
    }
}
